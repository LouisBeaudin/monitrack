package com.monitrack.connection.pool.implementation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.Vector;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.monitrack.connection.pool.abstracts.IJDBCConnectionPool;
import com.monitrack.shared.MonitrackServiceUtil;
import com.monitrack.util.Util;

public class JDBCConnectionPool implements IJDBCConnectionPool {
	
    private Vector<Connection> connections;
    private static final Logger log = LoggerFactory.getLogger(JDBCConnectionPool.class);
    private final String URL             =  Util.getPropertyValueFromPropertiesFile("url_dev");
    private final String USER            =  Util.getPropertyValueFromPropertiesFile("username");
    private final String PSWD            =  Util.getPropertyValueFromPropertiesFile("password");
    private int numberOfConnections;
    private int numberOfConnectionsCreated;
    
	
	public JDBCConnectionPool() {
		connections = new Vector<Connection>();
		numberOfConnectionsCreated = 0;
		log.info("Database URL :\n" + URL);
		numberOfConnections = askPoolConnectionsNumber();

		log.info(numberOfConnections + " connection(s) should be put inside the connection pool.");
	} 
	

	public void fillConnectionsList() throws SQLException {		
				
        for (int i = 0; i < numberOfConnections; i++ )
        {
        	Connection createdConnection = this.createConnection(); 
        	if(createdConnection != null) {
                connections.addElement(createdConnection);
                log.info("A connection has been created and is being added to the pool. (" + ( (i+1) + "/" + numberOfConnections) + ")" );
        	}
        	else {
        		log.error("An error occurs during the creation of a connection because the connection equals to null !");
        		/*
        		 * An error occured, so we will stop the creation of the connection 
        		 * in order to avoid other problems
        		 */
        		throw new SQLException("A connection is equal to null !");
        	}
        }
        displayConnectionPoolState();
	}

	public Connection getConnection() throws Exception {
		if(!connections.isEmpty())
		{
			Connection connection = connections.lastElement();
	        connections.removeElement(connection);
			//log.info("A connection is being retrieved from the connection pool.");
			displayConnectionPoolState();
	        return connection; 
		}
		else
		{
			throw new Exception("There are no connections left in the connection pool ! Please try later.");
		}     
	}

	public void putConnection(Connection connection) {
		if(connection != null)
		{
			connections.addElement(connection);	
			//log.info("A connection is being added to the connection pool.");			
		}
		displayConnectionPoolState();
		
	}

	public void closeAllConnections() {
		for(Connection connection : connections)
		{
			try {
				if(!connection.isClosed())
				{
					connection.close();	
					log.info("A connection has been closed.");
				}
			} catch (SQLException e) {
	            log.error("An error occurs during the closing of the connection :\n" + e.getMessage());
			}
		}
	}
		
	
    public Vector<Connection> getConnections() {
		return connections;
	}


	private Connection createConnection(){
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PSWD);
            numberOfConnectionsCreated++;
        } catch (SQLException e) {
            log.error("A SQL Exception has been raised during the creation of a connection :\n" + e.getMessage());
        }
        return connection;
    }  

	public synchronized int getRemaningNumberOfConnections(){
		return connections.size();
	}
	
	
	private void displayConnectionPoolState()
	{
		String state = "Connection(s) in the pool : " + getRemaningNumberOfConnections() + "/" + numberOfConnectionsCreated;
		log.info(state);
	}
	
	/*private void displayConnectionPoolState()
	{
		String legend  = FREE_CREATED_ASCII;
		String numbers = convertIntegerToAsciiCharacter(getRemaningNumberOfConnections());
		String end 		 = "--------------------------------------------\n";
		log.info("Connection pool state :\n" + legend + numbers + end);
	}
	
	private String convertIntegerToAsciiCharacter(Integer number)
	{
		if(number >= numberOfAsciiCharacters)
			return number.toString() + " / " + numberOfConnectionsCreated + "\n";
		return asciiCharacters[number];
		
	}*/
	
	private int askPoolConnectionsNumber() {
		int littleInfrastructure = Integer.parseInt(Util.getPropertyValueFromPropertiesFile("number_of_connections_little_infrastructure"));
		int bigInfrastructure = Integer.parseInt(Util.getPropertyValueFromPropertiesFile("number_of_connections_big_infrastructure"));
		System.out.println(MonitrackServiceUtil.getASCII("infrastructure_choice.txt"));
		System.out.print("Please enter your choice depending on your infrastructure [By default 1] : ");
		Scanner sc = new Scanner(System.in);
		String choice = sc.nextLine();
		sc.close();
		int choiceNumber = NumberUtils.toInt(choice, 1);
		return (choiceNumber == 2) ? bigInfrastructure : littleInfrastructure;	
	}
	

}
