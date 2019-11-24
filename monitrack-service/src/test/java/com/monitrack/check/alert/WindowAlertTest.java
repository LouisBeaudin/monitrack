package com.monitrack.check.alert;

import static org.junit.Assert.*;

import java.sql.Connection;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.monitrack.connection.pool.implementation.DataSource;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WindowAlertTest {
	
	private Connection connection;

	@Before
	public void init() {
		DataSource.startConnectionPool();
		connection = DataSource.getConnection();		
	}
	
	@Test
	public void testConnectionNotNull() {
		assertNotNull(connection);
	}

	@Test
	public void test() {
		
	}

}
