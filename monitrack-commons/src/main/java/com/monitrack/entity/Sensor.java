package com.monitrack.entity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.monitrack.enumeration.SensorType;

@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class Sensor {

	@JsonProperty("sensor_id")
	protected Integer sensorId;
	@JsonProperty("sensor_type")
	protected SensorType sensorType;
	@JsonProperty("mac_address")
	protected String macAddress;
	@JsonProperty("serial_number")
	protected String serialNumber;
	@JsonProperty("hardware_version")
	protected Float hardwareVersion;
	@JsonProperty("software_version")
	protected Float softwareVersion;


	public Sensor(Integer sensorId, SensorType sensorType, String macAddress, String serialNumber, Float hardwareVersion,
			Float softwareVersion) {
		this.sensorId = sensorId;
		this.sensorType = sensorType;
		this.macAddress = macAddress;
		this.serialNumber = serialNumber;
		this.hardwareVersion = hardwareVersion;
		this.softwareVersion = softwareVersion;
	}
	
	public Sensor() {}

	public Integer getSensorId() {
		return sensorId;
	}

	public void setSensorId(Integer sensorId) {
		this.sensorId = sensorId;
	}

	public SensorType getSensorType() {
		return sensorType;
	}

	public void setSensorType(SensorType sensorType) {
		this.sensorType = sensorType;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Float getHardwareVersion() {
		return hardwareVersion;
	}

	public void setHardwareVersion(Float hardwareVersion) {
		this.hardwareVersion = hardwareVersion;
	}

	public Float getSoftwareVersion() {
		return softwareVersion;
	}

	
	public void setSoftwareVersion(Float softwareVersion) {
		this.softwareVersion = softwareVersion;
	}

}
