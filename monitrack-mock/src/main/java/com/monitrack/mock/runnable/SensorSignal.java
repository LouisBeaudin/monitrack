package com.monitrack.mock.runnable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.monitrack.entity.Message;
import com.monitrack.entity.SensorConfiguration;
import com.monitrack.enumeration.SensorActivity;
import com.monitrack.enumeration.SensorState;
import com.monitrack.mock.util.MockUtil;

public class SensorSignal implements Runnable {
	
	private static final Logger log = LoggerFactory.getLogger(SensorSignal.class);
	private SensorConfiguration sensorConfiguration;
	private boolean isInSendingWarningMessageMode;
	private boolean sendMessage;

	public SensorSignal(SensorConfiguration sensorConfiguration) {
		this.sensorConfiguration = sensorConfiguration;	
		isInSendingWarningMessageMode = false;
		sendMessage = false;
	}

	@Override
	public void run() {
		try {
			while(true) {
				sendSignal();				
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		
	}
	
	public void sendSignal() throws Exception {
		if(sendMessage) {
			if(sensorConfiguration.getSensorActivity() == SensorActivity.ENABLED) {
				boolean messageSent = MockUtil.sendMessage(new Message(sensorConfiguration));
				if(!messageSent)
					throw new Exception("The server can not be reached at this time");
				Thread.sleep(sensorConfiguration.getCheckFrequency().longValue() - 100);
			}					
		}
	}
	
	public void setSendMessage(boolean sendMessage) {
		this.sendMessage = sendMessage;
	}

	public SensorConfiguration getSensorConfiguration() {
		return sensorConfiguration;
	}
	
	

}
