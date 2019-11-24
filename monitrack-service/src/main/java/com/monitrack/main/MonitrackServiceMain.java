package com.monitrack.main;

import com.monitrack.socket.server.Server;

/**
 * This main class initiates the the server
 */
public class MonitrackServiceMain {

	public static void main(String [] args) 
	{		
		Server server = new Server();
		// Starts the server, creates the connection pool and enables client connections
		server.start();		
	}
}
