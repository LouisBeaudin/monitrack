package com.monitrack.entity;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.monitrack.enumeration.SensorState;

public class Message {
	
	@JsonProperty("sensor")
	private SensorConfiguration sensorConfiguration;
	@JsonProperty("message_creation_date")
	private Timestamp creationDate;

	public Message(SensorConfiguration sensorConfiguration) {
		this.sensorConfiguration = sensorConfiguration;
		this.creationDate = new Timestamp(System.currentTimeMillis());
	}
	
	public Message() {
		
	}

	public SensorConfiguration getSensor() {
		return sensorConfiguration;
	}

	public Timestamp getCreationDate() {
		return creationDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((creationDate == null) ? 0 : creationDate.hashCode());
		result = prime * result + ((sensorConfiguration == null) ? 0 : sensorConfiguration.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Message other = (Message) obj;
		if (creationDate == null) {
			if (other.creationDate != null)
				return false;
		} else if (!creationDate.equals(other.creationDate))
			return false;
		if (sensorConfiguration == null) {
			if (other.sensorConfiguration != null)
				return false;
		} else if (!sensorConfiguration.equals(other.sensorConfiguration))
			return false;
		return true;
	}
	
	

}
