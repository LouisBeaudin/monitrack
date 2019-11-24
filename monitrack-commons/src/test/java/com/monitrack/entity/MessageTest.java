package com.monitrack.entity;

import static org.junit.Assert.*;

import org.junit.Test;

import com.monitrack.enumeration.SensorActivity;
import com.monitrack.enumeration.SensorState;
import com.monitrack.enumeration.SensorType;
import com.monitrack.util.JsonUtil;

public class MessageTest {

	@Test
	public void test() {
		SensorConfiguration sensorConfiguration = new SensorConfiguration(0, 0, SensorActivity.DISABLED, SensorType.FLOW, 1, "192.168.20.15", "dsfsd", "dsfsdf", 
				1.0f, 2.0f, null, null, null, null, null, 0f, "Decibel", 4.0f,0.0f, 5.0f, 6.23f, 4.94f);
		Message message = new Message(sensorConfiguration);
		String serializedObject = JsonUtil.serializeObject(message, message.getClass(), "");
		Message deserializedMessage = (Message)JsonUtil.deserializeObject(serializedObject);
		assertEquals(message, deserializedMessage);
	}

}
