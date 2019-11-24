package com.monitrack.exception;

import com.monitrack.enumeration.ConnectionState;

@SuppressWarnings("serial")
public class NoAvailableConnectionException extends Exception {

	public NoAvailableConnectionException() {
		super(ConnectionState.NO_CONNECTION.getEnglishLabel());
	}

}
