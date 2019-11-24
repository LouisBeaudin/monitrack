package com.monitrack.util;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monitrack.entity.Person;
import com.monitrack.enumeration.JSONField;
import com.monitrack.enumeration.RequestType;

public class JsonUtilTest {

	private static final Logger log = LoggerFactory.getLogger(JsonUtilTest.class);
	private ObjectMapper mapper = new ObjectMapper();
	/*
	@Test
	public void serializeRequest()
	{
		
		try {
			Person p = new Person("Request");
			String objectSerialized = JsonUtil.serializeObject(p, p.getClass(), "");
			List<String> fields = new ArrayList<>();
			fields.add("id");
			fields.add("name");	
			List<String> values = new ArrayList<>();
			values.add("1");
			values.add("Cheikna");			
			String jsonRequest = JsonUtil.serializeRequest(RequestType.SELECT, p.getClass(), objectSerialized, fields, values);
			System.out.println("JSON Indent 1 \n" + JsonUtil.indentJsonOutput(jsonRequest));
			// Converts the String into a JSON Node
			JsonNode jsonNode = mapper.readTree(jsonRequest);
			assertEquals("SELECT", jsonNode.get(JSONField.REQUEST_INFO.getLabel()).get(JSONField.REQUEST_TYPE.getLabel()).textValue());
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
	
	@Test
	public void testRequestTypeNode() throws IOException
	{
		String jsonFormattedRequest = "{\"request_info\":{\"request_type\":\"SELECT\",\"requested_entity\":\"PERSON\"},\"serialized_object\":null}";
		
		JsonNode json = mapper.readTree(jsonFormattedRequest);
		// JSON Node containing the request info
		JsonNode requestNode = json.get(JSONField.REQUEST_INFO.getLabel());	
		
		
		RequestType requestType = RequestType.getRequestType(requestNode.get(JSONField.REQUEST_TYPE.getLabel()).textValue());
		assertEquals(requestType, RequestType.SELECT);		
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testListOfEntitiesDeserialization()
	{
		Person p1 = new Person("climg");
		Person p2 = new Person("monitrack");
		Person p3 = new Person("smartHomeService");
		
		List<Person> list1 = new ArrayList<Person>();
		list1.add(p1);
		list1.add(p2);
		list1.add(p3);
		
		List<Person> list2 = new ArrayList<Person>();
		list2.add(p1);
		list2.add(p2);
		list2.add(p3);
		
		String list1JSON = JsonUtil.serializeObject(list1, Person.class, "");
		String list2JSON = JsonUtil.serializeObject(list2, Person.class, "");
		
		log.info("List 1 in Json :\n" + JsonUtil.indentJsonOutput(list1JSON));
		log.info("List 2 in Json :\n" + JsonUtil.indentJsonOutput(list2JSON));
		
		List<Person> persons1 = (List<Person>) JsonUtil.deserializeObject(list1JSON);
		List<Person> persons2 = (List<Person>) JsonUtil.deserializeObject(list2JSON);
		
		assertEquals(true, equalsList(persons1, persons2));
	}

	@Test
	public void testEntityDeserialization()
	{
		log.info("entity deserialization :");
		Person p = new Person("climg");
		String json = JsonUtil.serializeObject(p, p.getClass(), "");
		Person p2 = (Person)JsonUtil.deserializeObject(json);
		log.info("Json string : " + json);
		assertEquals(p, p2);
	}

	@Test
	public void testEntityDeserializationFromRequest() throws IOException
	{
		ObjectMapper mapper = new ObjectMapper();

		Person p =new Person("climg");
		String objectSerialized = JsonUtil.serializeObject(p, p.getClass(), null);
		String jsonRequest = JsonUtil.serializeRequest(RequestType.SELECT, p.getClass(), objectSerialized, null, null);
		
		JsonNode rootNode = mapper.readTree(jsonRequest);
		Person pers = (Person) JsonUtil.deserializeObject(rootNode.get(JSONField.SERIALIZED_OBJECT.getLabel()).toString());
		assertEquals(p, pers);
		
	}
	
	
	public <T> boolean equalsList(List<T> list1, List<T> list2)
	{
		if(list1 == null && list2 == null)
			return true;
		
		if(list1 == null || list2 == null)
			return false;
		
		int list1Size = list1.size();
		int list2Size = list2.size();
		
		if(list1Size != list2Size)
			return false;
		
		for(int i = 0; i < list1Size; i++)
		{
			T t1 = list1.get(i);
			T t2 = list2.get(i);
			if(!t1.equals(t2))
			{
				log.error("Here are the first two differences:\n" + t1.toString() + "\n" + t2.toString());
				return false;
			}
		}		
		
		return true;
	}
	
	@Test
	public void testGetJsonNodeValue()
	{
		String message = "my own message";
		String json = JsonUtil.serializeObject(null	, null, message);
		assertEquals(message, JsonUtil.getJsonNodeValue(JSONField.ERROR_MESSAGE, json));
	}	
	*/

}
