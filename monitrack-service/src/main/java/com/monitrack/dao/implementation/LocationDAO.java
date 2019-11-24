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
import com.monitrack.entity.Location;

public class LocationDAO extends DAO<Location> {

	private static final Logger log = LoggerFactory.getLogger(LocationDAO.class);	

	public LocationDAO(Connection connection) 
	{
		super(connection, "LOCATION");
	}

	public Location create(Location location) {

		synchronized (lock) {
			// Checks if the connection is not null before using it
			if (connection != null) {
				try {
					PreparedStatement preparedStatement = connection
							.prepareStatement("INSERT INTO LOCATION (NAME, CENTER, ID_SENSOR, CREATION_DATE, FLOOR, WING, AREA)"
									+ " VALUES (? , ? , ? , ? , ? , ? , ?)", Statement.RETURN_GENERATED_KEYS);
					preparedStatement.setString(1, location.getNameLocation());
					preparedStatement.setString(2, location.getCenter());
					preparedStatement.setInt(3, location.getIdSensor());
					preparedStatement.setTimestamp(4, location.getCreationDate());
					preparedStatement.setInt(5, location.getFloor());
					preparedStatement.setString(6, location.getWing());
					preparedStatement.setInt(7, location.getArea());
					preparedStatement.execute();
					ResultSet rs = preparedStatement.getGeneratedKeys();
					int lastCreatedId = 0;
					if (rs.next()) {
						lastCreatedId = rs.getInt(1);
						location.setIdLocation(lastCreatedId);
					}
				} catch (Exception e) {
					log.error("An error occurred during the creation of a location : " + e.getMessage());
					e.printStackTrace();
				}
			}
			return location;
		}

	}

	@Override
	public void delete(Location obj){

		synchronized (lock) {
			// Checks if the connection is not null before using it
			if (connection != null) {
				try {
					PreparedStatement preparedStatement = null;
					preparedStatement = connection.prepareStatement("DELETE FROM LOCATION where ID_LOCATION=(?)");
					preparedStatement.setInt(1, obj.getIdLocation());					
					preparedStatement.execute();
				} catch (Exception e) {
					log.error("An error occurred during the delete of a location : " + e.getMessage());
					e.printStackTrace();
				}
			}
		}		
	}

	@Override
	public void update(Location location) {
		synchronized (lock) {
			// Checks if the connection is not null before using it
			if (connection != null) {
				try {
					PreparedStatement preparedStatement = connection.prepareStatement(
							"UPDATE LOCATION SET NAME = ?, CENTER = ?, FLOOR = ?, WING = ?, AREA = ?"
							+ " WHERE ID_LOCATION = ?");
					preparedStatement.setString(1, location.getNameLocation());
					preparedStatement.setString(2, location.getCenter());
					preparedStatement.setInt(3, location.getFloor());
					preparedStatement.setString(4, location.getWing());
					preparedStatement.setInt(5, location.getArea());
					preparedStatement.setInt(6, location.getIdLocation());
					preparedStatement.execute();
				} catch (Exception e) {
					log.error("An error occurred during the update of a location : " + e.getMessage());
					e.printStackTrace();
				}
			}
		}

	}

	@SuppressWarnings("finally")
	@Override
	protected Location getSingleValueFromResultSet(ResultSet rs) {
		Location location = null;
		try {
			location = new Location(rs.getInt("ID_LOCATION"), rs.getString("NAME"), rs.getString("CENTER")
					, rs.getTimestamp("CREATION_DATE"), rs.getInt("ID_SENSOR"), rs.getInt("FLOOR"), rs.getString("WING"), rs.getInt("AREA"));
		} catch (SQLException e) {
			log.error("An error occurred when getting one Person from the resultSet : " + e.getMessage());
		}
		finally {
			return location;
		}
	}

}
