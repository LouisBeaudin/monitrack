package com.monitrack.socket.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.monitrack.enumeration.ConnectionState;
import com.monitrack.exception.DeprecatedVersionException;
import com.monitrack.shared.MonitrackGuiUtil;
import com.monitrack.util.JsonUtil;
import com.monitrack.util.Util;

public class ClientSocket {

	private static final Logger log = LoggerFactory.getLogger(ClientSocket.class);
	private final String SERVER_IP = Util.getPropertyValueFromPropertiesFile("server_ip");
	private final int PORT_NUMBER = Integer.parseInt(Util.getPropertyValueFromPropertiesFile("server_port_number"));
	private BufferedReader readFromServer;
	private PrintWriter writeToServer;

	/**
	 * Maximum delay of response from the server in milliseconds.
	 * If the server does not response within this delay, we consider this server as not available
	 */
	private final int TIMEOUT = 5000;

	private Socket socket;

	public ClientSocket() {

	}

	@SuppressWarnings("finally")
	public ConnectionState start()
	{
		ConnectionState connectionState = ConnectionState.NO_CONNECTION;
		
		try 
		{
			log.info("Connection to the server " + SERVER_IP + ":" + PORT_NUMBER + "...");

			// Connection to a socket
			socket = new Socket(SERVER_IP, PORT_NUMBER);

			/*
			 * The following method throws a "SocketTimeoutException" when the timeout is exceed.
			 * This will prevent the client from waiting a very long time 
			 * whereas the server is not accessible because of a problem.
			 */
			socket.setSoTimeout(TIMEOUT);
			
			readFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			writeToServer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);	
			
			//Send the client application version to the server. The letter 'v' will indicate that it is the version that we are sending
			writeToServer.println("v" + MonitrackGuiUtil.getApplicationVersion());
			
			//Check if we are using the good version of the application
			String[] serverCheck = readFromServer.readLine().split("v");
			if(serverCheck.length >= 2)
			{
				String code = serverCheck[0];
				String serverVersion = serverCheck[1];
				if(code.equalsIgnoreCase(ConnectionState.DEPRECATED_VERSION.getCode().toString()))
				{
					MonitrackGuiUtil.setServerVersion(serverVersion);
					throw new DeprecatedVersionException(serverVersion);
				}
				
			}
			
			log.info("You are connected to the server for your next request");

			connectionState = ConnectionState.SUCCESS;
		}
		catch(DeprecatedVersionException e)
		{
			log.error(e.getMessage());
			connectionState = ConnectionState.DEPRECATED_VERSION;
		}
		catch (SocketTimeoutException e) 
		{			
			log.error("The socket timed out : " + e.getMessage() + ".\nThe server cannot  be reach and cannot response to your last request !");
			exit();
		}
		catch (Exception e) 
		{
			log.error("Disconnected from server - Client Error - " + e.getMessage());
			exit();
		}
		finally
		{
			return connectionState;
		}
	}

	/**
	 * 
	 * @param requestToSendToServer : the request to send to the server
	 * @return the response from the server
	 * @throws IOException 
	 */
	public String sendRequestToServer(String requestToSendToServer) throws IOException 
	{
		String responseFromServer = "";

		if(requestToSendToServer.trim().equals(ConnectionState.RESERVED_CONNECTION.getCode().toString()))
			log.info("You are trying to reserve a connection");
		else {
			//log.info("Request sent to the server :\n" + JsonUtil.indentJsonOutput(requestToSendToServer));
			log.info("Request sent to the server :\n" + requestToSendToServer);
			
		}

		// Sends the request to the server
		writeToServer.println(requestToSendToServer);

		// Receives the response from the server
		responseFromServer = readFromServer.readLine();
		exit();
		return responseFromServer;
	}

	private void exit()
	{
		try {
			if(readFromServer != null)
				readFromServer.close();
			if(writeToServer != null)
				writeToServer.close();
			if(socket != null)
			{
				socket.close();
				log.info("The communication with the server is closed");
			}
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

}
