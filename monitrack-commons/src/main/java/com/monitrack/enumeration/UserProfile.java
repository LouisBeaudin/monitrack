package com.monitrack.enumeration;

public enum UserProfile {
	RESIDENT,
	STAFF,
	SERVICE_AGENT,
	DIRECTION;
	
	public static UserProfile getUserProfile(String userProfil)
	{
		UserProfile[] values = UserProfile.values();
		for(UserProfile value : values)
		{
			if(value.toString().equalsIgnoreCase(userProfil))
				return value;
		}
		return null;
	}
}
