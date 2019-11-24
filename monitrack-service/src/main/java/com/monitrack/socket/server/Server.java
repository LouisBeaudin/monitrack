package com.monitrack.socket.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.monitrack.connection.pool.implementation.DataSource;
import com.monitrack.data.pool.DataPool;
import com.monitrack.shared.MonitrackServiceUtil;
import com.monitrack.util.Util;

public class Server {

	private static final Logger log = LoggerFactory.getLogger(Server.class);
	private Connection connection;

	private ServerSocket serverSocket;
	private static final int PORT_NUMBER = Integer.parseInt(Util.getPropertyValueFromPropertiesFile("server_port_number"));
	private DataPool dataPool;

	public Server() {
		connection = null;
	}

	/**
	 * Starts the connection pool and enables client connections
	 */
	public void start()
	{
		log.info("Launching of the server version " + MonitrackServiceUtil.getApplicationVersion() + "...");
		//Displays Monitrack Server on the console
		System.out.println(MonitrackServiceUtil.getASCII("title.txt"));
		DataSource.startConnectionPool();
		dataPool = new DataPool();
		dataPool.startListUpdaterThread();

		try{
			serverSocket = new ServerSocket(PORT_NUMBER);
			while(true)
			{				
				
				if(DataSource.getRemaningConnections() > 0)
				{
					//log.info("Waiting for a client's request...");
					/*
					 * The socket is used to communicate with the client. Many clients will use the same port 
					 * but different instance of socket.
					 * While no client is not connected to this socket, the accept method of method will pause the program.
					 */	
					Socket socket = serverSocket.accept();
					connection = DataSource.getConnection();
					/*
					 * After a connection from a client to a server, the client will be handle on his own Thread
					 */
					RequestHandler requestHandler  = new RequestHandler(socket, connection, dataPool);
					Thread clientThread = new Thread(requestHandler);
					clientThread.start();
				}

			}
		}
		catch(Exception e) {
			log.error("Server exception : " + e.getMessage());
			DataSource.closeConnectionPool();
		}
	}

}
