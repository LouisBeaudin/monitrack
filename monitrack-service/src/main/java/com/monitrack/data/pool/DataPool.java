package com.monitrack.data.pool;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.monitrack.connection.pool.implementation.DataSource;
import com.monitrack.dao.implementation.DAOFactory;
import com.monitrack.dao.implementation.SensorConfigurationDAO;
import com.monitrack.entity.Message;
import com.monitrack.entity.SensorConfiguration;
import com.monitrack.entity.SensorConfigurationHistory;
import com.monitrack.enumeration.RequestType;
import com.monitrack.enumeration.SensorAction;
import com.monitrack.enumeration.SensorActivity;
import com.monitrack.enumeration.SensorState;
import com.monitrack.enumeration.SensorType;
import com.monitrack.shared.MonitrackServiceUtil;
import com.monitrack.util.Util;

public class DataPool {

	private final Object lock = new Object();

	private static final Logger log = LoggerFactory.getLogger(DataPool.class);
	private final long updateListFrequency = NumberUtils.toLong(Util.getPropertyValueFromPropertiesFile("update_time_ms"));
	private final long sleepTime = NumberUtils.toLong(Util.getPropertyValueFromPropertiesFile("sleep_time_ms"));
	private final List<String> fieldsForActiveSensors = Arrays.asList("ACTIVITY");
	private final List<String> valuesForActiveSensors = Arrays.asList(SensorActivity.ENABLED.toString());
	private final Long loopSleep = MonitrackServiceUtil.getDataPoolLoopSleep();

	private Map<SensorConfiguration, SensorState> dataPoolCache;
	private Map<Integer, Integer> dangerAlertCountBySensors;
	private final int maxDangerMessage = NumberUtils.toInt(Util.getPropertyValueFromPropertiesFile("max_danger_message"));
	private List<SensorConfiguration> activeSensors;	
	private Timestamp currentTime;
	private Thread listUpdaterThread;
	private long counter;
	private Connection connection;

	public DataPool() {		
		dataPoolCache = Collections.synchronizedMap(new HashMap<SensorConfiguration, SensorState>());
		dangerAlertCountBySensors = Collections.synchronizedMap(new HashMap<Integer, Integer>());
		activeSensors = Collections.synchronizedList(new ArrayList<SensorConfiguration>());
		counter = updateListFrequency;
	}

	/**
	 * Starts the update list thread to that the data can be refresh every 10 seconds
	 */
	public synchronized void startListUpdaterThread() {
		listUpdaterThread = new Thread(new Runnable() {

			@Override
			public void run() {
				while(true) {
					if(counter <= 0) {
						//FIXME updateActiveSensorsList();
					}else {
						try {
							Thread.sleep(sleepTime);
							counter -= sleepTime;
						} catch (InterruptedException e) {
							log.error(e.getMessage());
						}
					}
				}
			}
		});
		//updateActiveSensorsList();
		listUpdaterThread.start();
	}

	public synchronized List<SensorConfiguration> getCacheSensorsByState(SensorState sensorState) {
		List<SensorConfiguration> results = new ArrayList<SensorConfiguration>();		
		dataPoolCache.containsKey(new SensorConfiguration());
		for(Map.Entry<SensorConfiguration, SensorState > mapEntry : dataPoolCache.entrySet()) {
			if(mapEntry.getValue() == sensorState) {
				results.add(mapEntry.getKey());
			}
		}

		return results;
	}

	public synchronized void processMessage(Message receivedMessage) {
		try {
			log.info("Processing sensor message");
			currentTime = Util.getCurrentTimestamp();
			boolean isSensorWithCorrectIntervalInCache = false;
			boolean isInCache = false;
			SensorConfiguration sensorFromCache = null;
			SensorState sensorStateFromCache = null;
			
			SensorConfiguration sensorFromMessage = receivedMessage.getSensor();
			SensorState sensorState = checkSensorState(sensorFromMessage);
			
			dataPoolCache.getOrDefault(sensorFromMessage, null);
			log.info(sensorFromMessage.getStateInfo());
			int sensorId = sensorFromMessage.getSensorConfigurationId();

			
			for (SensorConfiguration cacheSensor : dataPoolCache.keySet()) {
				if (cacheSensor.equals(sensorFromMessage)) {
					isInCache = true;
					sensorFromCache = cacheSensor;
					sensorStateFromCache = dataPoolCache.get(cacheSensor);
					
					Long cacheTime = sensorFromCache.getLastMessageDate().getTime();  
					isSensorWithCorrectIntervalInCache = (cacheTime != null) && (currentTime.getTime() - cacheTime) <= sensorFromCache.getCheckFrequency();
					
					if(isSensorWithCorrectIntervalInCache) {
						log.info("The sensor n°" + sensorId + " has been found in the cache");
						log.info("Old value : " + sensorFromCache.getStateInfo());
						/* Checks it is was a danger before and according to the number of danger Message
						 * it will either be a reparation that has been made or a false alert
						 */
						int numberOfDangerMessage = dangerAlertCountBySensors.get(sensorId);
						if(sensorState == SensorState.NORMAL) {
							// Case : the reparators have done their job
							if(numberOfDangerMessage >= maxDangerMessage) {
								dangerAlertCountBySensors.replace(sensorId, 0);
								System.err.println("======>  /!\\ The sensor n°" + sensorId + " has been repaired /!\\" );
								saveSensorConfigurationHistory(sensorFromCache, SensorAction.STOP_DANGER_ALERT, "");
							}
							// Case : Fake Alert
							else if(numberOfDangerMessage > 0) {
								dangerAlertCountBySensors.replace(sensorId, 0);
								saveSensorConfigurationHistory(sensorFromCache, SensorAction.FAKE_ALERT, "");
							}
						}
						else if(sensorState == SensorState.DANGER) {
							int newNumberOfDangerMessage = numberOfDangerMessage + 1;
							dangerAlertCountBySensors.replace(sensorId, newNumberOfDangerMessage);
							if(newNumberOfDangerMessage == 1) 
							{
								sensorFromMessage.setDangerStartDate(Util.getCurrentTimestamp());
							} 
							else if(newNumberOfDangerMessage >= maxDangerMessage) 
							{
								System.err.println(" /!\\ The sensor n°" + sensorId + " is in real DANGER /!\\" );
							}
						}						
					}	
					break;					
				}
			}
			if (!isSensorWithCorrectIntervalInCache) {
				log.info("The sensor n°" + sensorId + " has not been found in the cache (or was old). It will be added (or updated) to it !");				
			}

			if(isInCache) {
				dataPoolCache.remove(sensorFromMessage);
			}
			else {
				int dangerMessage = (sensorState == SensorState.DANGER) ? 1 : 0; 
				if(dangerMessage == 1)
					sensorFromMessage.setDangerStartDate(Util.getCurrentTimestamp());
				dangerAlertCountBySensors.put(sensorId, dangerMessage);
			}
			
			sensorFromMessage.setLastMessageDate(currentTime);
			dataPoolCache.put(sensorFromMessage, sensorState);
			
			//FIXME Display info
			
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}

		//updateActiveSensorsList();
		//FIXME Récupérer tous les capteurs d'une même salle*/

	}
	
	private void displaySensorInfo() {
		String alignFormat = "| %-4d |";
		System.out.format("+-----------+");
		System.out.format("|  ID  |    STATE    |  ");
		
	}
	
	private SensorState checkSensorState(SensorConfiguration sensorFromMessage) {
		Float maxThreshold = sensorFromMessage.getMaxDangerThreshold();
		Float minThreshold = sensorFromMessage.getMinDangerThreshold();
		Float thresholdFromMessage = sensorFromMessage.getCurrentThreshold();
		
		if (thresholdFromMessage >= maxThreshold || thresholdFromMessage < minThreshold) {
			return SensorState.DANGER;
		}						
		return SensorState.NORMAL;
	}
	
	private void saveSensorConfigurationHistory(SensorConfiguration sensor, SensorAction action, String message) {
		int id = sensor.getSensorConfigurationId();
		try {
			SensorConfigurationHistory sensorHistory = new SensorConfigurationHistory(sensor, message, action);
			DAOFactory.execute(connection, sensorHistory.getClass(), RequestType.INSERT, sensorHistory, null, null);
			dangerAlertCountBySensors.replace(id, 0);
		} catch (Exception e) {
			log.error(e.getMessage());
		}	
	}

	//FIXME used this fonction
	@SuppressWarnings("unchecked")
	private synchronized void updateActiveSensorsList() {
		try {			
			connection = DataSource.getConnection();
			activeSensors.clear();
			activeSensors.addAll((List<SensorConfiguration>)DAOFactory.execute(connection, SensorConfiguration.class, RequestType.SELECT, null, fieldsForActiveSensors, valuesForActiveSensors));
			DataSource.putConnection(connection);
			connection = null;
			log.info("active sensors list updated");
			checkAllSensorsActivity();
		} catch (Exception e) {
			log.error("An error occured during the update of the active sensors list : " + e.getMessage());
		}
		counter = updateListFrequency;
	}

	/**
	 * Checks if all sensors work correctly = If they all send message
	 */
	public void checkAllSensorsActivity() {
		synchronized (lock) {
			System.out.println("===> size : " + activeSensors.size());
			for(SensorConfiguration sensorConfiguration : activeSensors) {
				for(Map.Entry<SensorConfiguration, SensorState> mapEntry : dataPoolCache.entrySet()) {
					SensorConfiguration cacheSensor = mapEntry.getKey();
					if(sensorConfiguration.getSensorConfigurationId() == cacheSensor.getSensorConfigurationId())
						System.out.println("The sensor with the id n°" + sensorConfiguration.getSensorConfigurationId() + " is in the cache");
					else
						System.err.println("The sensor with the id n°" + sensorConfiguration.getSensorConfigurationId() + " is not in the cache");	

				}
			}
		}
	}

	public synchronized void displayUpdatedSensors() {
		for(SensorConfiguration sensorConfiguration : activeSensors)
			System.out.println(sensorConfiguration);
	}
	
	public void setConnection(Connection connection) {
		this.connection = connection;
		//Sensor sensor = (Sensor) new SensorDAO(connection).find(Arrays.asList("ID_SENSOR"), Arrays.asList("8")).get(0);
		/*SensorConfiguration sensorConfiguration = new SensorConfiguration(8, 0, SensorActivity.ENABLED, SensorType.FLOW, 1, "192.168.20.15", "dsfsd", "dsfsdf", 
				1.0f, 2.0f, null, null, null, null, null, 2500f, "Decibel", 4.0f, 0.0f, 5.0f, 6.23f, 4.94f);
		sensorConfiguration.setLastMessageDate();
		for(Integer i = 29; i < 36; i++) {
			SensorConfiguration sensor2 = (SensorConfiguration) new SensorConfigurationDAO(connection).find(Arrays.asList("ID_SENSOR"), Arrays.asList(i.toString())).get(0);
			dataPoolCache.put(sensor2, SensorState.DANGER);
		}
		
		dataPoolCache.put(sensorConfiguration, SensorState.DANGER);*/
	}

}
