package com.monitrack.connectionpool.implementation;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;
import org.junit.Before;
import org.junit.Test;
import com.monitrack.connection.pool.implementation.JDBCConnectionPool;
import com.monitrack.util.Util;
public class JDBCConnectionPoolTest {

	private JDBCConnectionPool pool = new JDBCConnectionPool();
	
	@Before
	public void initialize() throws Exception {
		pool.fillConnectionsList();
	}
// Si on crée une nouvelle connnexion et qu'elle ne soit pas null 
	
	@Test
	public void poolConnectionsNull() {
		assertNotNull("The connections pool is not null", pool);
	}
	
	@Test
	public void correctNumberConnexion(){
		Vector<Connection> connections = pool.getConnections();
		try {
			assertEquals(Integer.parseInt(Util.getPropertyValueFromPropertiesFile("number_of_connections")),connections.size());
		}catch (Exception e){ 
			fail();
		}
	}
	
	@Test
	public void connectionFromPoolNotNull() {
		try {
			assertNotNull("The connection from the pool is not null", pool.getConnection());
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void closeAllPoolConnection() {
		pool.closeAllConnections();
		Vector<Connection> connections = pool.getConnections();
		for (Connection connection : connections ){
			try {
				if (!connection.isClosed()){
					fail("One connection has not been closed");
				}
			} catch (SQLException e) {
				fail("An exception has occured when verifying if all connections has been closed ");
			}
		}
		assertTrue(true);
	}
	
}
