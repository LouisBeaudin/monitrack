package com.monitrack.enumeration;

public enum RequestType {
	SELECT,
	INSERT,
	UPDATE,
	DELETE;
	
	public static RequestType getRequestType(String requestType)
	{
		RequestType[] values = RequestType.values();
		for(RequestType value : values)
		{
			if(value.toString().equalsIgnoreCase(requestType))
				return value;
		}
		return null;
	}
}
