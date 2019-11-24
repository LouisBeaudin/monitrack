package com.monitrack.mock.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.monitrack.entity.Message;
import com.monitrack.enumeration.ConnectionState;
import com.monitrack.enumeration.RequestSender;
import com.monitrack.enumeration.RequestType;
import com.monitrack.enumeration.SensorActivity;
import com.monitrack.socket.client.ClientSocket;
import com.monitrack.util.JsonUtil;

public class MockUtil {
	
	private static final Logger log = LoggerFactory.getLogger(MockUtil.class);
	
	public static boolean sendMessage(Message message) {
		try {
			ClientSocket clientSocket = new ClientSocket();
			ConnectionState connectionState = clientSocket.start();
			if(connectionState == ConnectionState.SUCCESS) {
				String serializedObject = JsonUtil.serializeObject(message, message.getClass(), "");
				String jsonRequest = JsonUtil.serializeRequest(RequestType.INSERT, message.getClass(), serializedObject, null, null, RequestSender.SENSOR);
				clientSocket.sendRequestToServer(jsonRequest);		
				return true;
			}
			else {
				log.error("An error occurred during the connection with the server. Perhaps the server is off.");
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return false;
		
	}

}
