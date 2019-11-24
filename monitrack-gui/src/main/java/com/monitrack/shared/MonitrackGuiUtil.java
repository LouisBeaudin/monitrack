package com.monitrack.shared;

import java.io.IOException;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.monitrack.enumeration.ConnectionState;
import com.monitrack.enumeration.Images;
import com.monitrack.enumeration.JSONField;
import com.monitrack.exception.DeprecatedVersionException;
import com.monitrack.exception.NoAvailableConnectionException;
import com.monitrack.socket.client.ClientSocket;
import com.monitrack.util.JsonUtil;
import com.monitrack.util.Util;

public class MonitrackGuiUtil {

	private static final Logger log = LoggerFactory.getLogger(MonitrackGuiUtil.class);
	
	private static final String APPLICATION_VERSION = Util.getPropertyValueFromPropertiesFile("version");
	private static String serverVersion = "";
	
	public static void showComingSoonMesage() {
		JOptionPane.showMessageDialog(null, "Cette fonctionnalité sera bientôt disponible.", "Bientôt disponible", JOptionPane.INFORMATION_MESSAGE, Images.COMING_SOON.getIcon());
	}
			
	public static void showNoConnectionMessage() {
		JOptionPane.showMessageDialog(null, ConnectionState.NO_CONNECTION.getFrenchLabel(), "Erreur", JOptionPane.ERROR_MESSAGE, Images.NO_CONNECTION.getIcon());
	}
	
	public static String sendRequest(String jsonRequest) throws NoAvailableConnectionException, IOException, DeprecatedVersionException
	{
		String response = "";
		ClientSocket clientSocket = new ClientSocket();
		ConnectionState state = clientSocket.start();
		
		
		if(state == ConnectionState.SUCCESS || state == ConnectionState.DEPRECATED_VERSION)
		{
			if(state == ConnectionState.DEPRECATED_VERSION)
			{
				String message = ConnectionState.DEPRECATED_VERSION.getFrenchLabel() + "\n";
				message += "Vous devez utiliser la version " + serverVersion + " de l'application car certaines fonctionnalités risquent de ne pas fonctionner.";
				JOptionPane.showMessageDialog(null, message, "Version obselète", JOptionPane.INFORMATION_MESSAGE);
			}
			
			response = clientSocket.sendRequestToServer(jsonRequest);
			
			//Checks if we reserved a connection, because the response is not in json format
			if(!jsonRequest.trim().equals(ConnectionState.RESERVED_CONNECTION.getCode().toString()))
			{
				String error = JsonUtil.getJsonNodeValue(JSONField.ERROR_MESSAGE, response).trim();
				
				if(!error.equals(""))
				{
					JOptionPane.showMessageDialog(null, error, "Erreur", JOptionPane.ERROR_MESSAGE);
					throw new NoAvailableConnectionException();
				}
				
				log.info("Response from the server :\n" + JsonUtil.indentJsonOutput(response));
			}
			
			return response;
		}
		else
		{	
			showNoConnectionMessage();
			throw new NoAvailableConnectionException();
		}
	}

	/**
	 * @return the applicationVersion
	 */
	public static String getApplicationVersion() {
		return APPLICATION_VERSION;
	}

	/**
	 * @param serverVersion the serverVersion to set
	 */
	public static void setServerVersion(String serverVersion) {
		MonitrackGuiUtil.serverVersion = serverVersion;
	}
	
	

}
