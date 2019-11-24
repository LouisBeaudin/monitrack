package com.monitrack.check.alert;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import com.monitrack.connection.pool.implementation.DataSource;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SmokeAlertTest {

	/*private Connection connection;
	private static String state = "NORMAL";
	private final boolean isItRealSmoke = true;
	private final int idSensor = 1;

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
	public void SendAlert() {

	}

	@Test
	public void testSmokeAlertCheck() {
		Thread smokeSensor = new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					select();
				}
			}
		});
		smokeSensor.start();
		sendSensorMessage("NORMAL");
		sendSensorMessage("NORMAL");
		sendSensorMessage("NORMAL");
		sendSensorMessage("ALERT");
		sendSensorMessage("ALERT");
		sendSensorMessage("NORMAL");
		sendSensorMessage("ALERT");
		sendSensorMessage("NORMAL");
		sendSensorMessage("ALERT");
		sendSensorMessage("ALERT");
		sendSensorMessage("ALERT");
		sendSensorMessage("ALERT");
		System.err.println("End send message");
		try {
			Thread.sleep(1500);
			sendSensorReparator();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		while (true) {
		}
	}

	public void sendSensorMessage(String message) {

		try {

			PreparedStatement preparedStatement = connection.prepareStatement(
					"update SMOKE_SENSOR_TEST set " + "LAST_MESSAGE_RECEIVED = ? where ID_SENSOR = ?",
					Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, message);
			preparedStatement.setInt(2, idSensor);
			preparedStatement.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void select() {
		try {
			String sql = "SELECT * FROM SMOKE_SENSOR_TEST where ID_SENSOR = ?";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, idSensor);
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.first()) {
				System.out.println("Etat du capteur : " + rs.getString("STATE"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void sendSensorReparator() {
		try {

			PreparedStatement preparedStatement = connection.prepareStatement(
					"update ALERT_TEST set " + "STATE = ? where ID_SENSOR = ?", Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, "FIXED");
			preparedStatement.setInt(2, idSensor);
			preparedStatement.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.err.println("The reparator have finished !");

	}*/

}
