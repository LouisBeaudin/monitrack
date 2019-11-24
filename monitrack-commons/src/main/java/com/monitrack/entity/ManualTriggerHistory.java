package com.monitrack.entity;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ManualTriggerHistory {
	
	@JsonProperty("history_id")
	private int historyId;
	@JsonProperty("sensor_id")
	private int sensorId;
	@JsonProperty("location_id")
	private int locationId;
	@JsonProperty("code_entered")
	private String codeEntered;
	@JsonProperty("triggering_date")
	private Timestamp triggeringDate;
	@JsonProperty("access_granted")
	private boolean isAccessGranted;
	
	
	public ManualTriggerHistory(int historyId, int sensorId, int locationId, String codeEntered,
			Timestamp triggeringDate, boolean isAccessGranted) {
		this.historyId = historyId;
		this.sensorId = sensorId;
		this.locationId = locationId;
		this.codeEntered = codeEntered;
		this.triggeringDate = triggeringDate;
		this.isAccessGranted = isAccessGranted;
	}
	
	public ManualTriggerHistory(int historyId, int sensorId, int locationId, String codeEntered,
			Timestamp triggeringDate, int accessGranted) {
		this(historyId, sensorId, locationId, codeEntered, triggeringDate, accessGranted == 1);
	}
	
	public ManualTriggerHistory() {}

	public int getHistoryId() {
		return historyId;
	}

	public void setHistoryId(int historyId) {
		this.historyId = historyId;
	}

	public int getSensorId() {
		return sensorId;
	}

	public void setSensorId(int sensorId) {
		this.sensorId = sensorId;
	}

	public int getLocationId() {
		return locationId;
	}

	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}

	public String getCodeEntered() {
		return codeEntered;
	}

	public void setCodeEntered(String codeEntered) {
		this.codeEntered = codeEntered;
	}

	public Timestamp getTriggeringDate() {
		return triggeringDate;
	}

	public void setTriggeringDate(Timestamp triggeringDate) {
		this.triggeringDate = triggeringDate;
	}

	public boolean isAccessGranted() {
		return isAccessGranted;
	}

	public void setIsAccessGranted(boolean isAccessGranted) {
		this.isAccessGranted = isAccessGranted;
	}
	
	@JsonIgnore
	public int accessGranted() {
		return (isAccessGranted) ? 1 : 0;
	}
	
}
