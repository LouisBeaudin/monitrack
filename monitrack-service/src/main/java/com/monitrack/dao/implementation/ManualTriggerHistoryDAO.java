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
import com.monitrack.entity.ManualTriggerHistory;

public class ManualTriggerHistoryDAO extends DAO<ManualTriggerHistory> {

	private static final Logger log = LoggerFactory.getLogger(ManualTriggerHistoryDAO.class);
	
	public ManualTriggerHistoryDAO(Connection connection) {
		super(connection, "MANUAL_TRIGGER_HISTORY");
	}

	@Override
	public ManualTriggerHistory create(ManualTriggerHistory obj) {
		synchronized (lock) {
			// Checks if the connection is not null before using it
			if (connection != null) {
				try {
					PreparedStatement preparedStatement = connection
							.prepareStatement("INSERT INTO " + tableName + " (ID_SENSOR, ID_LOCATION, CODE_ENTERED, TRIGGERING_DATE, ACCESS_GRANTED)"
									+ " VALUES (? , ? , ? , ? , ? , ? , ?)", Statement.RETURN_GENERATED_KEYS);
					preparedStatement.setInt(1, obj.getSensorId());
					preparedStatement.setFloat(2, obj.getLocationId());
					preparedStatement.setString(3, obj.getCodeEntered());
					preparedStatement.setTimestamp(4, obj.getTriggeringDate());
					preparedStatement.setInt(6, obj.accessGranted());
					preparedStatement.execute();
					ResultSet rs = preparedStatement.getGeneratedKeys();
					int lastCreatedId = 0;
					if (rs.next()) {
						lastCreatedId = rs.getInt(1);
						obj.setHistoryId(lastCreatedId);
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
	public void update(ManualTriggerHistory obj) {
		try {
			throw new Exception("A history from the manual trigger can not be updated !");
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		
	}

	@Override
	public void delete(ManualTriggerHistory obj) {
		synchronized (lock) {
			// Checks if the connection is not null before using it
			if (connection != null) {
				try {
					PreparedStatement preparedStatement = null;
					preparedStatement = connection.prepareStatement("DELETE FROM " + tableName + " where ID_HISTORY=?");
					preparedStatement.setInt(1, obj.getHistoryId());					
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
	protected ManualTriggerHistory getSingleValueFromResultSet(ResultSet rs) {
		ManualTriggerHistory manualTriggerHistory = null;
		try {
			manualTriggerHistory = new ManualTriggerHistory(rs.getInt("ID_HISTORY"), rs.getInt("ID_SENSOR"), rs.getInt("ID_LOCATION"),
					rs.getString("CODE_ENTERED"), rs.getTimestamp("TRIGGERING_DATE"), rs.getInt("ACCESS_GRANTED"));


		} catch (SQLException e) {
			log.error("An error occurred when getting one Flow Sensor from the resultSet : " + e.getMessage());
		}
		finally {
			return manualTriggerHistory;
		}
	}

}
