package com.monitrack.dao.implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.monitrack.dao.abstracts.DAO;
import com.monitrack.entity.SensorConfigurationHistory;
import com.monitrack.enumeration.SensorAction;

public class SensorConfigurationHistoryDAO extends DAO<SensorConfigurationHistory> {
	
	private static final Logger log = LoggerFactory.getLogger(SensorConfigurationHistoryDAO.class);

	public SensorConfigurationHistoryDAO(Connection connection) {
		super(connection, "SENSOR_CONFIGURATION_HISTORY");
	}

	@Override
	public SensorConfigurationHistory create(SensorConfigurationHistory obj) {
		synchronized (lock) {
			// Checks if the connection is not null before using it
			if (connection != null) {
				try {
					PreparedStatement preparedStatement = connection
							.prepareStatement("INSERT INTO "+ tableName +" (ID_SENSOR_CONFIGURATION, MEASURED_THRESHOLD, MIN_DANGER_THRESHOLD, MAX_DANGER_THRESHOLD, MEASUREMENT_DATE, END_ALERT_DATE, DESCRIPTION, ACTION_DONE)"
									+ " VALUES (? , ? , ? , ? , ? , ? , ? , ?)", Statement.RETURN_GENERATED_KEYS);
					preparedStatement.setInt(1, obj.getIdSensorSource());
					preparedStatement.setFloat(2, obj.getMeasuredThreshold());
					preparedStatement.setFloat(3, obj.getMinDangerThreshlod());
					preparedStatement.setFloat(4, obj.getMaxDangerThreshlod());
					preparedStatement.setTimestamp(5, obj.getDate());
					preparedStatement.setTimestamp(6, obj.getEndAlertDate());
					preparedStatement.setString(7, obj.getDescription());
					preparedStatement.setString(8, obj.getActionDone().name());
					preparedStatement.execute();
					ResultSet rs = preparedStatement.getGeneratedKeys();
					int lastCreatedId = 0;
					if (rs.next()) {
						lastCreatedId = rs.getInt(1);
						obj.setIdHistory(lastCreatedId);
					}
				} catch (Exception e) {
					log.error("An error occurred during the creation of a location : " + e.getMessage());
					e.printStackTrace();
				}
			}
			return obj;
		}
	}

	@Override
	public void update(SensorConfigurationHistory obj) {
		try {
			throw new Exception("A history from the sensor can not be updated !");
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	@Override
	public void delete(SensorConfigurationHistory obj) {
		synchronized (lock) {
			// Checks if the connection is not null before using it
			if (connection != null) {
				try {
					PreparedStatement preparedStatement = null;
					preparedStatement = connection.prepareStatement("DELETE FROM "+ tableName +" where ID_HISTORY=(?)");
					preparedStatement.setInt(1, obj.getIdHistory());					
					preparedStatement.execute();
				} catch (Exception e) {
					log.error("An error occurred during the delete of a location : " + e.getMessage());
					e.printStackTrace();
				}
			}
		}	
		
	}

	@SuppressWarnings("finally")
	@Override
	protected SensorConfigurationHistory getSingleValueFromResultSet(ResultSet rs) {
		SensorConfigurationHistory sensorConfigurationHistory = null;
		try {
			sensorConfigurationHistory = new SensorConfigurationHistory(rs.getInt("ID_HISTORY"), rs.getInt("ID_SENSOR"), rs.getFloat("MEASURED_THRESHOLD"), rs.getFloat("MIN_DANGER_THRESHOLD"),rs.getFloat("MAX_DANGER_THRESHOLD"),
					rs.getTimestamp("MEASUREMENT_DATE"), rs.getTimestamp("END_ALERT_DATE"), rs.getString("DESCRIPTION"), SensorAction.valueOf(rs.getString("ACTION_DONE")));


		} catch (SQLException e) {
			log.error("An error occurred when getting one Flow Sensor from the resultSet : " + e.getMessage());
		}
		finally {
			return sensorConfigurationHistory;
		}
	}

}
