package com.monitrack.socket.client;

import static org.junit.Assert.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.monitrack.entity.Location;
import com.monitrack.enumeration.ConnectionState;
import com.monitrack.enumeration.RequestSender;
import com.monitrack.enumeration.RequestType;
import com.monitrack.socket.client.ClientSocket;
import com.monitrack.util.JsonUtil;
import com.monitrack.util.Util;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ClientSocketTest {

	private static final Logger log = LoggerFactory.getLogger(ClientSocketTest.class);
	private static Location location;
	private static int id = 0;

	@Before
	public void initialize() {
		location = new Location("popfd52df", "", 1, "NORD", 141);
	}

	/**
	 * Insert a random location to the server
	 * @throws InterruptedException 
	 */
	@Test
	public void testSendInsertRequestToServer() throws InterruptedException {
		try 
		{
			log.info("============ testSendInsertRequestToServer ==============");
			ClientSocket clientSocket = new ClientSocket();
			ConnectionState connectionState = clientSocket.start();
			if(connectionState == ConnectionState.SUCCESS)
			{
				String serializedObject = JsonUtil.serializeObject(location, location.getClass(), "");				
				String jsonRequest = JsonUtil.serializeRequest(RequestType.INSERT, Location.class, serializedObject, null, null, RequestSender.CLIENT);
				String response = clientSocket.sendRequestToServer(jsonRequest);
				Location location2 = (Location) JsonUtil.deserializeObject(response);
				assertEquals(location.getNameLocation(), location2.getNameLocation());
				location.setIdLocation(location2.getIdLocation());
				id = location2.getIdLocation();
			}
			else
				fail("Error when sending the request to the server");

		}  
		catch (IOException e) 
		{
			log.error("The message was not sent to the server :\n" + e.getMessage());
		}		
		
		//We sleep the Thread in order to have time to see the results on the database
		Thread.sleep(4000);
	}

	/**
	 * Check if the location was inserted successfully by trying to retrieving it
	 * @throws InterruptedException 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testSendSelectWithFiltersAfterInsertRequestToServer() throws InterruptedException {
		try 
		{
			log.info("============ testSendSelectWithFiltersAfterInsertRequestToServer ==============");
			ClientSocket clientSocket = new ClientSocket();
			ConnectionState connectionState = clientSocket.start();
			if(connectionState == ConnectionState.SUCCESS)
			{				
				List<String> fields = new ArrayList<>();
				List<String> values = new ArrayList<>();
				fields.add("ID_LOCATION");
				fields.add("NAME");
				fields.add("FLOOR");
				fields.add("AREA");
				values.add(String.valueOf(id));
				values.add(location.getNameLocation());
				values.add(String.valueOf(location.getFloor()));
				values.add(String.valueOf(location.getArea()));
				String jsonRequest = JsonUtil.serializeRequest(RequestType.SELECT, Location.class, null, fields, values, RequestSender.CLIENT);
				log.info("Request : " + JsonUtil.indentJsonOutput(jsonRequest));
				String response = clientSocket.sendRequestToServer(jsonRequest);
				Location locationRetrived = ((List<Location>)JsonUtil.deserializeObject(response)).get(0);
				
				//We set the creationDate to null, because of some delay the milliseconds will be different
				location.setCreationDate(null);
				locationRetrived.setCreationDate(null);
				
				location.setIdLocation(id);
				assertEquals(location, locationRetrived);
			}
			else
				fail("Error when sending the request to the server");

		}  
		catch (IOException e) 
		{
			log.error("The message was not sent to the server :\n" + e.getMessage());
		}
		
		//We sleep the Thread in order to have time to see the results on the database
		Thread.sleep(4000);
	}

	/**
	 * Delete the inserted location. We add a 'T'on the method name bacause, the methods will be run on ascending order
	 * However, we want this method to run after the method before
	 * @throws InterruptedException 
	 */
	@Test
	public void testSendTDeleteRequestToServer() throws InterruptedException {
		try 
		{
			log.info("============ testSendTDeleteRequestToServer ==============");
			ClientSocket clientSocket = new ClientSocket();
			ConnectionState connectionState = clientSocket.start();
			if(connectionState == ConnectionState.SUCCESS)
			{				
				location.setIdLocation(id);
				String serializedObject = JsonUtil.serializeObject(location, location.getClass(), "");
				String jsonRequest = JsonUtil.serializeRequest(RequestType.DELETE, Location.class, serializedObject, null, null, RequestSender.CLIENT);
				clientSocket.sendRequestToServer(jsonRequest);
			}
			else
				fail("Error when sending the request to the server");

		}  
		catch (IOException e) 
		{
			log.error("The message was not sent to the server :\n" + e.getMessage());
		}
		
		//We sleep the Thread in order to have time to see the results on the database
		Thread.sleep(4000);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testSendTSelectAfterDeleteRequestToServer() throws InterruptedException {
		try 
		{
			log.info("============ testSendTSelectAfterDeleteRequestToServer ==============");
			ClientSocket clientSocket = new ClientSocket();
			ConnectionState connectionState = clientSocket.start();
			if(connectionState == ConnectionState.SUCCESS)
			{				
				List<String> fields = new ArrayList<>();
				List<String> values = new ArrayList<>();
				fields.add("ID_LOCATION");
				fields.add("NAME");
				fields.add("FLOOR");
				fields.add("AREA");
				values.add(String.valueOf(id));
				values.add(location.getNameLocation());
				values.add(String.valueOf(location.getFloor()));
				values.add(String.valueOf(location.getArea()));
				String jsonRequest = JsonUtil.serializeRequest(RequestType.SELECT, Location.class, null, fields, values, RequestSender.CLIENT);
				log.info("Request : " + JsonUtil.indentJsonOutput(jsonRequest));
				String response = clientSocket.sendRequestToServer(jsonRequest);
				List<Location> locationsRetrived = ((List<Location>)JsonUtil.deserializeObject(response));
				assertEquals(0, locationsRetrived.size());
			}
			else
				fail("Error when sending the request to the server");

		}  
		catch (IOException e) 
		{
			log.error("The message was not sent to the server :\n" + e.getMessage());
		}
		
		//We sleep the Thread in order to have time to see the results on the database
		Thread.sleep(4000);
	}

	@Test
	public void testToReserveConnectionsFromThePool()
	{		
		log.info("============ testToReserveConnectionsFromThePool ==============");
		String numConnectionsString = Util.getPropertyValueFromPropertiesFile("number_of_connections");

		int numberOfConnections = NumberUtils.toInt(numConnectionsString, 2);

		//In this loop, we create as much client's thread as connections in the pool so that the pool can be empty
		for(int i = 0; i < numberOfConnections; i++)
		{
			ClientSocket clientSocketReserver = new ClientSocket();
			Thread clientThread = new Thread(new Runnable() {
				@Override
				public void run() {
					try 				{
						ConnectionState reservedState = clientSocketReserver.start();
						clientSocketReserver.sendRequestToServer(ConnectionState.RESERVED_CONNECTION.getCode().toString());
						assertEquals(ConnectionState.SUCCESS, reservedState);
					} 
					catch (IOException e) {
						log.error(e.getMessage());
					}
				}
			});
			clientThread.start();
		}

		// The next client will fail because we retrived all connections of the pool
		ClientSocket clientSocketFail = new ClientSocket();
		Thread clientFailThread = new Thread(new Runnable() {

			@Override
			public void run() {
				ConnectionState reservedState = clientSocketFail.start();
				assertEquals(ConnectionState.NO_CONNECTION, reservedState);
			}
		});
		clientFailThread.start();
		
	}

	/*private <T> void displayListElements(List<T> elements)
	{
		if(elements != null && elements.size() > 0)
		{
			System.out.println("Displaying the elements of the list :\n");
			for(T element : elements)
			{
				System.out.println("===> " + element);
			}
		}
		else
		{
			System.out.println("The list is empty !");
		}
	}*/

}
