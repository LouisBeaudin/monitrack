package com.monitrack.util;

import java.io.IOException;
import java.util.List;

import javax.management.InvalidAttributeValueException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.monitrack.enumeration.JSONField;
import com.monitrack.enumeration.RequestSender;
import com.monitrack.enumeration.RequestType;

public class JsonUtil {

	private static final Logger log = LoggerFactory.getLogger(JsonUtil.class);

	/**
	 * Converts a JAVA Object into a JSON format String
	 * 
	 * @param object :
	 * 			object to convert into JSON. It can be a simple entity or a list of entity
	 * @param objectClass :
	 * 			the class of the object to serialize. We can not use the object parameter to determine
	 * 			this parameter because the object parameter can be null.
	 * @return
	 * 			the JSON String representing the JAVA Object
	 * @throws InvalidAttributeValueException
	 */
	@SuppressWarnings("rawtypes")
	public static String serializeObject(Object object, Class objectClass, String message)
	{		
		String objectToJSON = null;

		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		message = (message == null) ? "" : message;
		node.put(JSONField.ERROR_MESSAGE.getLabel(), message);		

		try {

			if(object != null && objectClass != null)
			{
				/*
				 * The object can be a list when we are retrieving all of the datas of a table.
				 * This usually happens because of the 'findAll()' method
				 */
				if(object instanceof List)
				{
					node.put(JSONField.IS_LIST.getLabel(), true);				
				}
				else
				{
					node.put(JSONField.IS_LIST.getLabel(), false);
				}

				String className = objectClass.getName();
				/*
				 * We add the entity name in order to make the deserialization easier
				 * because we will have one deserialization process for all of the entities
				 */
				node.put(JSONField.ENTITY.getLabel(), className);
				node.putPOJO(JSONField.DATAS.getLabel(), object);
				log.info("Serialization into JSON succedeed");
			}
			else
			{
				log.error("The object you want to serialize is null. Consequently, the serialization can not happen !");				
			}

			objectToJSON = mapper.writeValueAsString(node);
		} 
		catch (Exception e) 
		{
			log.error("Serialization into JSON failed : " + e.getMessage());
		}			

		return objectToJSON;
	}

	/**
	 * Converts a JSON String into a JAVA Object
	 * @param objectInJSONString :
	 * 			JSON String representing the object to convert
	 * @return
	 * 			JAVA Object converted from the JSON String
	 */
	public static Object deserializeObject(String objectInJSONString) {

		Object jsonConvertedToObject = null;
		ObjectMapper mapper = new ObjectMapper();

		try {

			if(objectInJSONString == null || objectInJSONString.trim().length() == 0)
				throw new Exception("There is no object to deserialize !");

			// Converts the String into a JSON Node
			JsonNode objectFromStringNode = mapper.readTree(objectInJSONString);
			// JSON Node containing the name of the entity
			JsonNode entityNode = objectFromStringNode.get(JSONField.ENTITY.getLabel());
			//JSON Node containing the datas of the entity
			JsonNode datasNode = objectFromStringNode.get(JSONField.DATAS.getLabel());
			// Node which allows us to know if we have a list of entities (because of the method 'findAll()') or only one
			JsonNode isListNode = objectFromStringNode.get(JSONField.IS_LIST.getLabel());

			// Gets the name of the entity we want to deserialize
			String className = entityNode.textValue();

			Class<?> objectClass = Class.forName(className);

			boolean isListOfEntities = isListNode.booleanValue();			

			if(isListOfEntities)
				jsonConvertedToObject = mapper.readValue(datasNode.toString(), mapper.getTypeFactory().constructCollectionType(List.class, objectClass));
			else
				jsonConvertedToObject = mapper.readValue(datasNode.toString(), objectClass);

			//log.info("Deserialization into Java Object succedeed");

		} catch (Exception e) {
			log.error("Deserialization into Java Object failed : " + e.getMessage());
		} 

		return jsonConvertedToObject;		
	}

	/**
	 * 
	 * @param serializedObject
	 * @param entityClass : allows us to know which table is concerned
	 * @param requestedFields: especially used for select queries
	 * @param requestedValues: especially used for select queries
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("rawtypes")
	public static String serializeRequest(RequestType requestType, Class entityClass,String serializedObject, 
			List<String> requestedFields, List<String> requiredValues, RequestSender requestSender)
	{
		String objectToJSON = null;
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode rootNode = mapper.createObjectNode();

		try {
			if(requestSender != RequestSender.CLIENT_FOR_SENSOR_UPDATE) {

				if(requestType == null || entityClass == null)
					throw new IOException("The request type and the entity class cannot be null !");

				objectToJSON = null;

				ObjectNode requestNode = mapper.createObjectNode();

				if(serializedObject == null)
					serializedObject = "";
				JsonNode serializedObjectNode = mapper.readTree(serializedObject);

				requestNode.put(JSONField.REQUEST_TYPE.getLabel(), requestType.toString());		
				requestNode.put(JSONField.REQUESTED_ENTITY.getLabel(), entityClass.getName());	

				if(requestedFields != null && requiredValues != null)
				{
					if((requestedFields.size() != requiredValues.size()))
					{
						requestedFields = null;
						requiredValues = null;
						log.error("An error occured during the request serialization : the sizes of the fields list and the required values list are different ! Consequently, they will be set to null.");
					}
				}


				/**
				 * Those list must be added to the final json String, in order to avoid some deserialization issue on the server's side
				 */
				requestNode.putPOJO(JSONField.REQUESTED_FIELDS.getLabel(), requestedFields);
				requestNode.putPOJO(JSONField.REQUIRED_VALUES.getLabel(), requiredValues);


				rootNode.putPOJO(JSONField.REQUEST_INFO.getLabel(), requestNode);
				rootNode.putPOJO(JSONField.SERIALIZED_OBJECT.getLabel(), serializedObjectNode);

			}
			
			requestSender = (requestSender == null) ? RequestSender.CLIENT : requestSender;
			rootNode.putPOJO(JSONField.REQUEST_SENDER.getLabel(), requestSender);
			objectToJSON = mapper.writeValueAsString(rootNode);
		} catch (IOException e) {
			log.error("An error occurred during the serialization of the request :\n" + e.getMessage());
		}

		return objectToJSON;
	}	

	public static String serializeSensorsUpdateRequest() {
		return serializeRequest(null, null,null, null,null, RequestSender.CLIENT_FOR_SENSOR_UPDATE);
	}

	/**
	 * Indents a jsonString in order to be more readable
	 * @param mapper
	 * @param jsonString
	 * @return the jsonString indented
	 */
	public static String indentJsonOutput(String jsonString)
	{
		ObjectMapper mapper = new ObjectMapper();
		// Allows us to have an indented JSON on the output and not a single line
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		try 
		{
			JsonNode jsonNode = mapper.readTree(jsonString);
			return mapper.writeValueAsString(jsonNode);
		} 
		catch (IOException e) 
		{
			log.error(e.getMessage());
			return "";
		}
	}

	@SuppressWarnings("finally")
	public static String getJsonNodeValue(JSONField field, String json)
	{
		String result = "";		

		try 
		{
			ObjectMapper mapper = new ObjectMapper();
			JsonNode objectFromStringNode = mapper.readTree(json);
			JsonNode dataNode = objectFromStringNode.get(field.getLabel());
			result = dataNode.textValue();			
		} 
		catch (IOException e) 
		{
			log.error(e.getMessage());
		}
		finally
		{
			return result;
		}
	}

	public static boolean hasSerializedObjectError(String json)
	{
		return !JsonUtil.getJsonNodeValue(JSONField.ERROR_MESSAGE, json).trim().equals("");
	}

}
