package com.monitrack.exception;

public class DeprecatedVersionException extends Exception {
	
	private static final long serialVersionUID = 1L;

	/**
	 * @param minimumVersion - the version of the server
	 */
	public DeprecatedVersionException(String minimumVersion) 
	{
		super("The client application is a deprecated version. The version of the application must at least be the " + minimumVersion + " ! The server will be accessible but some functionnalities will not work.");
	}

}
