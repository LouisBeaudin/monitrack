package com.monitrack.dao.abstracts;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.monitrack.dao.implementation.SensorConfigurationDAO;

public abstract class DAO<T> {
	
	private static final Logger log = LoggerFactory.getLogger(DAO.class);
	protected static String tableName;
	protected final Object lock = new Object();
	protected Connection connection;	

	public DAO(Connection connection, String tableName) {
		this.connection = connection;
		DAO.tableName = tableName;
	}
	
	/**
	 * Creates the object in the database
	 * @param obj
	 * @return the object with its id retrieved from the database
	 */
	public abstract T create(T obj);
	
	/**
	 * Updates an object
	 * @param obj
	 */
	public abstract void update(T obj);
	
	/**
	 * Deletes an object
	 * @param obj
	 */
	public abstract void delete(T obj);	
	
	/**
	 * Finds the objects in the database
	 * @param fields : the fields we want to filter
	 * @param values : the values required for the fields
	 * @return
	 */
	public synchronized List<T> find(List<String> fields, List<String> values){
		List<T> elements = new ArrayList<T>();
		if (connection != null) {
			try {
				String sql = "SELECT * FROM " + tableName;
				if(tableName.equalsIgnoreCase(SensorConfigurationDAO.tableName))
				{
					sql += " table1 inner join SENSOR table2 on table1.ID_SENSOR = table2.ID_SENSOR";
				}
				sql += getRequestFilters(fields, values);
				
				PreparedStatement preparedStatement = connection.prepareStatement(sql);
				ResultSet rs = preparedStatement.executeQuery();
				T element;
				while (rs.next()) {
					element = getSingleValueFromResultSet(rs);
					if (element != null) {
						elements.add(element);
					}
				}
			} catch (Exception e) {
				log.error("An error occurred when finding all of the persons : " + e.getMessage());
				e.printStackTrace();
			}
		}
		return elements;
	}
	
	/**
	 * Allows to add quote if needeed in the sql request
	 * @param value
	 * @return the value with quote if it is not a number
	 */
	protected String addNecessaryQuotes(String value)
	{
		if(!NumberUtils.isParsable(value))
			value = "'" + value + "'";
		return value;
	}
	
	
	/**
	 * Allows to specified the constraints in the request
	 * @param fields
	 * @param values
	 * @return
	 */
	protected String getRequestFilters(List<String> fields, List<String> values)
	{
		String sql = "";
		
		if(fields != null && values != null)
		{
			int fieldsListSize = fields.size();
			if(fieldsListSize >= 1 && fieldsListSize == values.size())
			{
				sql += " WHERE ";
				int i;
				
				for(i = 0; i < fieldsListSize - 1; i++)
					sql += fields.get(i) + "=" + addNecessaryQuotes(values.get(i)) + " AND ";
				
				sql += fields.get(i) + "= " + addNecessaryQuotes(values.get(i));
			}
			
		}
		
		return sql;
	}
	
	protected abstract T getSingleValueFromResultSet(ResultSet rs);

	public String getTableName() {
		return tableName;
	}

}
