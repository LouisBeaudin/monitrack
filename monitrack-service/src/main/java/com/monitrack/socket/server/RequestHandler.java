package com.monitrack.socket.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.Connection;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monitrack.connection.pool.implementation.DataSource;
import com.monitrack.dao.implementation.DAOFactory;
import com.monitrack.data.pool.DataPool;
import com.monitrack.entity.Message;
import com.monitrack.entity.SensorConfiguration;
import com.monitrack.enumeration.ConnectionState;
import com.monitrack.enumeration.JSONField;
import com.monitrack.enumeration.RequestSender;
import com.monitrack.enumeration.RequestType;
import com.monitrack.enumeration.SensorState;
import com.monitrack.shared.MonitrackServiceUtil;
import com.monitrack.util.JsonUtil;
import com.monitrack.util.Util;

/**
 * This class will execute the client request
 */
public class RequestHandler implements Runnable {

	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

	//This reader will allow us to read the message received and so sent by the client
	private BufferedReader readFromClient;

	//This writer will allow us to send a response to the client
	private PrintWriter writeToClient;

	private Socket socket;
	private Connection connection;
	//For the JSON
	private ObjectMapper mapper;
	private DataPool dataPool;

	public RequestHandler(Socket socket, Connection connection, DataPool dataPool) {
		this.socket = socket;
		this.connection = connection;
		this.dataPool = dataPool;
		this.dataPool.setConnection(connection);
		mapper = new ObjectMapper();
	}

	@Override
	public void run() {	

		try 
		{		
			InetSocketAddress socketAddress = (InetSocketAddress)socket.getRemoteSocketAddress();
			log.info("Client connected with the IP " + socketAddress.getAddress());
			readFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			writeToClient = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
			
			//Check client application version
			String clientVersion = readFromClient.readLine();
			String serverVersion = MonitrackServiceUtil.getApplicationVersion();
			
			//Check if the client has the same version than the server
			if(!clientVersion.equalsIgnoreCase("v" + serverVersion))
			{
				writeToClient.println(ConnectionState.DEPRECATED_VERSION.getCode() + "v" + serverVersion);
			}
			else
			{
				writeToClient.println("");
			}
			
			//Handle client request if he has the good version of the application
			String requestOfClient = readFromClient.readLine();
			String reservedConnectionCode = ConnectionState.RESERVED_CONNECTION.getCode().toString();
			
			//Checks if the client (= super user) wants to reserve the connection
			if(requestOfClient.trim().equalsIgnoreCase(reservedConnectionCode))
			{
				int reservedTimeInMilliseconds = NumberUtils.toInt(Util.getPropertyValueFromPropertiesFile("reserved_time_ms"), 17000);
				String reservedTime = (new Integer(reservedTimeInMilliseconds / 1000)).toString();
				log.info("A client has reserved a connection for " + reservedTime + " sec\n");
				String message = "Votre connexion ne sera pas utilisable par les autres durant " + reservedTime + " sec-" + reservedTime;
				writeToClient.println(message);
				//Sleeps the Thread in order to make the connection no accessible by another person
				Thread.sleep(reservedTimeInMilliseconds);
				log.info("A client has release its reserved connection !");
			}
			else
			{
				JsonNode json = mapper.readTree(requestOfClient);
				RequestSender requestSender = RequestSender.getValueOf(JsonUtil.getJsonNodeValue(JSONField.REQUEST_SENDER, requestOfClient));

				if(requestSender == RequestSender.CLIENT) {
					log.info("Request received from the client :\n" + JsonUtil.indentJsonOutput(requestOfClient) + "\n");
					String responseToClient = executeClientRequest(json);
					log.info("Response to the client :\n" + JsonUtil.indentJsonOutput(responseToClient) + "\n");
					writeToClient.println(responseToClient);						
				}
				else if(requestSender == RequestSender.SENSOR) {
					Message message = (Message)getObjectFromJson(json);
					dataPool.processMessage(message);
					writeToClient.println("");
				} 
				else if(requestSender == RequestSender.CLIENT_FOR_SENSOR_UPDATE) {
					List<SensorConfiguration> sensorConfigurations = dataPool.getCacheSensorsByState(SensorState.DANGER);
					String serializedObjects = JsonUtil.serializeObject(sensorConfigurations, SensorConfiguration.class, "");
					writeToClient.println(serializedObjects);					
				}
							
			}

		}
		catch (Exception e) 
		{
			log.error("Exception : The client is disconnected");
		}
		finally 
		{			
			exit();			
		}
	}

	/**
	 * 
	 * @param json
	 * @return
	 */
	@SuppressWarnings("finally")
	public String executeClientRequest(JsonNode json) 
	{		
		String result = "";

		try 
		{			
			// JSON Node containing the request info
			JsonNode requestNode = json.get(JSONField.REQUEST_INFO.getLabel());	
			String requestEntity = requestNode.get(JSONField.REQUESTED_ENTITY.getLabel()).textValue();
			Class<?> entityClass = Class.forName(requestEntity);
			
			// The fields we wants to filter
			String fieldsStringFromJson = requestNode.get(JSONField.REQUESTED_FIELDS.getLabel()).toString();
			// The values of the filters we want to filter
			String valuesStringFromJson = requestNode.get(JSONField.REQUIRED_VALUES.getLabel()).toString();

			List<String> fields = null;
			List<String> requiredValues = null;

			if(fieldsStringFromJson != null && valuesStringFromJson != null)
			{
				fields = mapper.readValue(fieldsStringFromJson, mapper.getTypeFactory().constructCollectionType(List.class, String.class));
				requiredValues = mapper.readValue(valuesStringFromJson, mapper.getTypeFactory().constructCollectionType(List.class, String.class));		
			}	
			
			Object deserializedObject = getObjectFromJson(json);
			
			RequestType requestType = RequestType.getRequestType(requestNode.get(JSONField.REQUEST_TYPE.getLabel()).textValue());
			
			Object objectResult = DAOFactory.execute(connection, entityClass, requestType, deserializedObject, fields, requiredValues); 
			
			result = JsonUtil.serializeObject(objectResult, entityClass, "");

		} 
		catch (Exception e) 
		{
			log.error("An error occured during the execution of the client request :\n" + e.getMessage());
		}
		finally
		{
			return result;
		}

	}
	
	private Object getObjectFromJson(JsonNode json)
	{
		JsonNode serializedObjectNode = json.get(JSONField.SERIALIZED_OBJECT.getLabel());
		if(serializedObjectNode != null)
			return JsonUtil.deserializeObject(serializedObjectNode.toString());
		return null;
	}

	/**
	 * This method will give back the connection to the pool and close the socket
	 */
	private void exit()
	{
		try 
		{
			//Give back to connection to the pool
			DataSource.putConnection(connection);
			this.connection = null;
			socket.close();
			readFromClient.close();
			writeToClient.close();
		} 
		catch (IOException e) 
		{
			log.error("An error occured during the closure of a socket : " + e.getMessage());
		}
	}
}
