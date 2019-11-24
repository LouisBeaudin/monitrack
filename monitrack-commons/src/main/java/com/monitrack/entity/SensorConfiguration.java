package com.monitrack.entity;
import java.sql.Time;
import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.monitrack.enumeration.SensorActivity;
import com.monitrack.enumeration.SensorType;
import com.monitrack.util.Util;
@JsonIgnoreProperties(ignoreUnknown = true)
public class SensorConfiguration extends Sensor {
	
	@JsonProperty("sensor_configuration_id")
	private Integer sensorConfigurationId;
	@JsonProperty("sensor_activity")
	private SensorActivity sensorActivity;
	@JsonProperty("location_id")	
	private Integer locationId;
	@JsonProperty("ip_address")
	private String ipAddress;
	@JsonProperty("creation_date")
	private Timestamp creationDate;
	@JsonProperty("date_of_last_message")
	private Timestamp lastMessageDate;
	@JsonProperty("last_configuration_date")
	private Timestamp lastConfigurationDate;
	@JsonProperty("time_of_begin_activity")
	private Time beginTime;
	@JsonProperty("time_of_end_activity")
	private Time endTime;
	//In milliseconds
	@JsonProperty("check_frequency")
	private Float checkFrequency;
	@JsonProperty("measurement_unit")
	private String measurementUnit;	
	@JsonProperty("current_threshold")
	private Float currentThreshold;
	@JsonProperty("min_danger_threshold")
	private Float minDangerThreshold;
	@JsonProperty("max_danger_threshold")
	private Float maxDangerThreshold;
	@JsonProperty("position_x")
	private Float positionX;
	@JsonProperty("position_y")
	private Float positionY;
	@JsonProperty("location")
	private Location location;	

	@JsonIgnore
	private Timestamp dangerStartDate;

	public SensorConfiguration(Integer sensorConfigurationId, Integer sensorId, SensorActivity sensorActivity, SensorType sensorType, Integer locationId,
			String ipAddress, String macAddress, String serialNumber, Float hardwareVersion, Float softwareVersion,
			Timestamp creationDate, Timestamp lastMessageDate, Timestamp lastConfigurationDate, Time beginTime,
			Time endTime, Float checkFrequency, String measurementUnit, Float currentThreshold, Float minDangerThreshold,
			Float maxDangerThreshold, Float positionX,
			Float positionY) {
		super(sensorId, sensorType, macAddress, serialNumber, hardwareVersion, softwareVersion);
		this.sensorConfigurationId = sensorConfigurationId;
		this.sensorActivity = sensorActivity;
		this.locationId = locationId;
		this.ipAddress = ipAddress;
		this.creationDate = (creationDate != null) ? creationDate : Util.getCurrentTimestamp();
		this.lastMessageDate = lastMessageDate;
		this.lastConfigurationDate = lastConfigurationDate;
		this.beginTime = beginTime;
		this.endTime = endTime;
		this.checkFrequency = checkFrequency;
		this.measurementUnit = measurementUnit;
		this.currentThreshold = currentThreshold;
		this.minDangerThreshold = minDangerThreshold;
		this.maxDangerThreshold = maxDangerThreshold;
		this.positionX = positionX;
		this.positionY = positionY;
	}
	
	public SensorConfiguration() { }
	
	public Integer getSensorConfigurationId() {
		return sensorConfigurationId;
	}

	public void setSensorConfigurationId(Integer sensorConfigurationId) {
		this.sensorConfigurationId = sensorConfigurationId;
	}

	public SensorActivity getSensorActivity() {
		return sensorActivity;
	}

	public void setSensorActivity(SensorActivity sensorActivity) {
		this.sensorActivity = sensorActivity;
	}

	public Integer getLocationId() {
		return locationId;
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public Timestamp getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}

	public Timestamp getLastMessageDate() {
		return lastMessageDate;
	}

	public void setLastMessageDate(Timestamp lastMessageDate) {
		this.lastMessageDate = lastMessageDate;
	}

	public Timestamp getLastConfigurationDate() {
		return lastConfigurationDate;
	}

	public void setLastConfigurationDate(Timestamp lastConfigurationDate) {
		this.lastConfigurationDate = lastConfigurationDate;
	}

	public Time getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Time beginTime) {
		this.beginTime = beginTime;
	}

	public Time getEndTime() {
		return endTime;
	}

	public void setEndTime(Time endTime) {
		this.endTime = endTime;
	}

	public Float getCheckFrequency() {
		return checkFrequency;
	}

	public void setCheckFrequency(Float checkFrequency) {
		this.checkFrequency = checkFrequency;
	}

	public String getMeasurementUnit() {
		return measurementUnit;
	}

	public void setMeasurementUnit(String measurementUnit) {
		this.measurementUnit = measurementUnit;
	}

	public Float getCurrentThreshold() {
		return currentThreshold;
	}

	public void setCurrentThreshold(Float currentThreshold) {
		this.currentThreshold = currentThreshold;
	}

	public Float getMinDangerThreshold() {
		return minDangerThreshold;
	}

	public void setMinDangerThreshold(Float minDangerThreshold) {
		this.minDangerThreshold = minDangerThreshold;
	}

	public Float getMaxDangerThreshold() {
		return maxDangerThreshold;
	}

	public void setMaxDangerThreshold(Float maxDangerThreshold) {
		this.maxDangerThreshold = maxDangerThreshold;
	}

	public Float getPositionX() {
		return positionX;
	}

	public void setPositionX(Float positionX) {
		this.positionX = positionX;
	}

	public Float getPositionY() {
		return positionY;
	}

	public void setPositionY(Float positionY) {
		this.positionY = positionY;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public void setLastMessageDate() {
		this.setLastMessageDate(Util.getCurrentTimestamp());
	}
	
	@JsonIgnore
	public Timestamp getDangerStartDate() {
		return dangerStartDate;
	}

	@JsonIgnore
	public void setDangerStartDate(Timestamp dangerStartDate) {
		this.dangerStartDate = dangerStartDate;
	}



	@Override
	public String toString() {
		return "Sensor [id=" + sensorConfigurationId + ", sensorActivity=" + sensorActivity + ", sensorType=" + sensorType
				+ ", locationId=" + locationId + ", ipAddress=" + ipAddress + ", macAddress=" + macAddress
				+ ", serialNumber=" + serialNumber + ", hardwareVersion=" + hardwareVersion + ", softwareVersion="
				+ softwareVersion + ", creationDate=" + creationDate + ", lastMessageDate=" + lastMessageDate
				+ ", lastConfigurationDate=" + lastConfigurationDate + ", beginTime=" + beginTime + ", endTime="
				+ endTime + ", checkFrequency=" + checkFrequency + ", measurementUnit=" + measurementUnit
				+ ", maxDangerThreshold=" + maxDangerThreshold + ", positionX=" + positionX + ", positionY=" + positionY
				+ "]";
	}
	
	

	@JsonIgnore
	public String getStateInfo() throws Exception {
		String locationName = (location != null) ? location.getNameLocation() : "Location undefined";
		String template = "in \u00AB" + locationName + "\u00BB is equal to " + currentThreshold + "/" + maxDangerThreshold + " " + measurementUnit;
		
		switch(sensorType) {
		case FLOW:
			return "There is (are) currently " + currentThreshold.intValue() + " person(s) in \u00AB" + locationName + "\u00BB";
		case SMOKE:	
			return "The smoke rate " + template;
		case DOOR:	
			return "FIXME"; //FIXME
		case TEMPERATURE:	
			return "The temperature in \u00AB" + locationName + "\u00BB is equal to " + currentThreshold 
					+ "(min:" + minDangerThreshold + measurementUnit +" - max: " + maxDangerThreshold + measurementUnit + ")";
		case WINDOW:	
			return "FIXME"; //FIXME
		case HUMIDITY:	
			return "The humidity level " + template;
		case LIGHT:	
			return "The light level " + template;
		case GAS:	
			return "The rate of carbone monoxyde in \u00AB" + locationName + "\u00BB is equal to " + currentThreshold 
					+ "/" + maxDangerThreshold + " " + measurementUnit;
		case GLASS_BREAKAGE:	
			return "The glass damage on the window located in \u00AB" + locationName + "\u00BB is equal to " + currentThreshold + "%";
		case ACOUSTIC:	
			return "The sound " + template;
		case MANUAL_TRIGGER:	
			return "A manual trigger has been triggered in \u00AB" + locationName + "\u00BB";
		case ACCESS_CONTROL:	
			return "Someone is trying to access to the \u00AB" + locationName + "\u00BB";
		case FLOOD:	
			return "The water level " + template;
		default:
			throw new Exception("The type of the sensor was not found");
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sensorConfigurationId == null) ? 0 : sensorConfigurationId.hashCode());
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
		SensorConfiguration other = (SensorConfiguration) obj;
		if (sensorConfigurationId == null) {
			if (other.sensorConfigurationId != null) {
				return false;
			}
		} else if (!sensorConfigurationId.equals(other.sensorConfigurationId)) {
			return false;
		}
		return true;
	}
}
