package com.monitrack.enumeration;

public enum RequestSender {
	
	CLIENT,
	
	CLIENT_FOR_SENSOR_UPDATE,
	
	SENSOR;

	public static RequestSender getValueOf(String sender) {
		RequestSender[] values = RequestSender.values();
		
		for(RequestSender value : values) {
			if(value.toString().equalsIgnoreCase(sender))
				return value;
		}
		
		return null;
	}
}
