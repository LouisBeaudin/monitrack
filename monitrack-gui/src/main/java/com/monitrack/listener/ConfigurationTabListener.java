package com.monitrack.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.monitrack.entity.SensorConfiguration;
import com.monitrack.enumeration.Images;
import com.monitrack.enumeration.RequestSender;
import com.monitrack.enumeration.RequestType;
import com.monitrack.enumeration.SensorActivity;
import com.monitrack.exception.DeprecatedVersionException;
import com.monitrack.exception.NoAvailableConnectionException;
import com.monitrack.gui.panel.ConfigurationTab;
import com.monitrack.shared.MonitrackGuiUtil;
import com.monitrack.util.JsonUtil;

public class ConfigurationTabListener implements ActionListener {

	private static final Logger log = LoggerFactory.getLogger(ConfigurationTabListener.class);
	
	private ConfigurationTab configurationTab;
	private List<SensorConfiguration> sensors;
	private SensorConfiguration sensor;
	private List<String> fields;
	private List<String> values;

	public ConfigurationTabListener(ConfigurationTab configurationTab)
	{
		this.configurationTab = configurationTab;
		sensors = new ArrayList<>();
		fields = new ArrayList<String>();
		values = new ArrayList<String>();	
	}

	@SuppressWarnings("rawtypes")
	public void actionPerformed(ActionEvent e)
	{
		try
		{
			if(e.getSource() instanceof JComboBox)
			{
				JComboBox combo = (JComboBox)e.getSource();
				if(combo == configurationTab.getActionsCombobox())
				{
					configurationTab.getNorthPanel().removeAll();
					configurationTab.getNorthPanel().add(configurationTab.getNorthPanelActionsChoice());
					
					if(combo.getSelectedItem().equals("Visualiser les capteurs"))
					{
						
						configurationTab.getNorthPanel().add(configurationTab.getNorthPanelForShow());
						
					} else if(combo.getSelectedItem().equals("Configurer un capteur"))
					
					{	
//						setComboboxWithSensors(configurationTab.getConfigureSensorsCombobox());
						configurationTab.getNorthPanel().add(configurationTab.getNorthPanelForConfigure());				
					}
				}
			}
			else if(e.getSource() instanceof JButton)
			{
				JButton clickedButton = (JButton)e.getSource();
				if(clickedButton == configurationTab.getShowButton())
				{
					displaySensors();
				}
				else if(clickedButton == configurationTab.getConfigureButton()){
					displaySensorConfigured();	
				}
			}
		}
		catch(Exception e1)
		{
			log.error(e1.getClass().getSimpleName() + " : " + e1.getMessage());
		}

		//Updates the Panel
		configurationTab.revalidate();
		configurationTab.repaint();
	}

	
	private void configureSensor() throws NoAvailableConnectionException, IOException, DeprecatedVersionException
	{
	
		SensorConfiguration sensorToConfigure = this.sensor;

		configurationTab.getOldMaxDangerThresholdTextField().setText(String.valueOf(sensorToConfigure.getMaxDangerThreshold()));
		configurationTab.getOldMinDangerThresholdTextField().setText(String.valueOf(sensorToConfigure.getMinDangerThreshold()));
		configurationTab.getOldActivityTextField().setText(String.valueOf(sensorToConfigure.getSensorActivity()));	
		configurationTab.getOldVersionTextField().setText(String.valueOf(sensorToConfigure.getSoftwareVersion()));
		configurationTab.getOldFrequencyTextField().setText(String.valueOf(sensorToConfigure.getCheckFrequency()));
		configurationTab.getOldBeginTimeTextField().setText(String.valueOf(sensorToConfigure.getBeginTime()));
		configurationTab.getOldEndTimeTextField().setText(String.valueOf(sensorToConfigure.getEndTime()));
		
		
		int choice = 0;
		String errorMessage = "";

		errorMessage = "";
		choice = JOptionPane.showConfirmDialog(configurationTab, configurationTab.getConfigureCaptorPopupPanel(), 
				"Configurer un capteur", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, Images.CONFIGURER.getIcon());
		
		
		String maxDangerThreshold = configurationTab.getModifiedMaxDangerThresholdTextField().getText().trim();
		if (maxDangerThreshold.length() <= 0) {
			maxDangerThreshold = configurationTab.getOldMaxDangerThresholdTextField().getText().trim();
		}
		float maxDanger = Float.parseFloat(maxDangerThreshold);
		
		
		
		String minDangerThreshold = configurationTab.getModifiedMinDangerThresholdTextField().getText().trim();
		if (minDangerThreshold.length() <= 0) {
			minDangerThreshold = configurationTab.getOldMinDangerThresholdTextField().getText().trim();
		}
		float minDanger = Float.parseFloat(minDangerThreshold);
		
		
		
		String sensorActivity = configurationTab.getModifiedCaptorStatusCombobox().getSelectedItem().toString();
		SensorActivity sensorAct = null ;
		if (sensorActivity.equals("ACTIVÉ")) {
			sensorAct = SensorActivity.ENABLED;
		}else if (sensorActivity.equals("DESACTIVÉ")) {
			sensorAct = SensorActivity.DISABLED;
			sensorActivity = "DISABLED";
		}else if (sensorActivity.equals("PAS_CONFIGURÉ")) {
			sensorAct = SensorActivity.NOT_CONFIGURED;
		}
		if(sensorAct == null) {
			errorMessage+="Erreur lors de l'activité du capteur";
		}
		
		
		
		String softwareVersion = configurationTab.getModifiedVersionTextField().getText().trim();
		if (softwareVersion.length() <= 0) {
			softwareVersion = configurationTab.getOldVersionTextField().getText().trim();
		}
		float softVer = Float.parseFloat(softwareVersion);
		if (softVer>50 || softVer <=0) {
			errorMessage += "Cette version n''existe pas";
		}
		
		
		
		String checkFrequency = configurationTab.getModifiedFrequencyTextField().getText().trim();
		if (checkFrequency.length() <= 0) {
			checkFrequency = configurationTab.getOldFrequencyTextField().getText().trim();
		}
		float frequency = Float.parseFloat(checkFrequency);
		
		
		
		String beginTime = configurationTab.getModifiedBeginTimeTextField().getText().trim();
		if (beginTime.length() <= 0) {
			beginTime = configurationTab.getOldBeginTimeTextField().getText().trim();
		}
		Time beginT = null;
		//if() {
			beginT = Time.valueOf(beginTime);
		//}else {
		//	errorMessage+="L'heure de début d'activité ne respecte pas le format de temps";
		//}
				
		
		
		
		
		String endTime = configurationTab.getModifiedEndTimeTextField().getText().trim();
		if (endTime.length() <= 0) {
			endTime = configurationTab.getOldEndTimeTextField().getText().trim();
		}
		Time endT =null;
		//if (endTime) {
		endT = Time.valueOf(endTime);
		//}else {
		//	errorMessage+="La date de fin d'activité ne respecte pas le format de temps";
		//}
		
		
		
		
			if(choice == 0)
			{
				// Upadates the location which needs to be updated
				sensorToConfigure.setMaxDangerThreshold(maxDanger);
				sensorToConfigure.setMinDangerThreshold(minDanger);
				sensorToConfigure.setSensorActivity(sensorAct);
				sensorToConfigure.setSoftwareVersion(softVer);
				sensorToConfigure.setCheckFrequency(frequency);
				sensorToConfigure.setBeginTime(beginT);
				sensorToConfigure.setEndTime(endT);
			}


			//Send the request if all fields are correct and the user clicked on OK Button
			if(choice == 0)
			{		
				String serializedObject = JsonUtil.serializeObject(sensorToConfigure, SensorConfiguration.class, "");	
				String jsonRequest = JsonUtil.serializeRequest(RequestType.UPDATE, SensorConfiguration.class, serializedObject, null, null, RequestSender.CLIENT);
				MonitrackGuiUtil.sendRequest(jsonRequest);
				JOptionPane.showMessageDialog(configurationTab, "Votre capteur a bien été mis à jour", "Mise à jour réussie", JOptionPane.INFORMATION_MESSAGE);
			}

			if(errorMessage.trim().length() > 0 && choice == 0)
			{
				String message = "Pour certains champs la valeur restera inchangée suite à des erreurs :\n" + errorMessage;
				JOptionPane.showMessageDialog(configurationTab, message, "Erreur", JOptionPane.INFORMATION_MESSAGE);		
			}
			// Clear the fields
			configurationTab.getModifiedMaxDangerThresholdTextField().setText("");
			configurationTab.getModifiedMinDangerThresholdTextField().setText("");
			configurationTab.getModifiedVersionTextField().setText("");
			configurationTab.getModifiedFrequencyTextField().setText("");
			configurationTab.getModifiedBeginTimeTextField().setText("");
			configurationTab.getModifiedBeginTimeTextField().setText("");
		
	}
	
	@SuppressWarnings("unchecked")
	private void displaySensorConfigured() throws NoAvailableConnectionException, IOException, DeprecatedVersionException
	{
		fields.clear();
		values.clear();
		this.sensor = null;
		
		
		String field = "ID_SENSOR_CONFIGURATION";
		
		String value = configurationTab.getFilter3TextField().getText().trim();
		
		if(value.length() > 0)
		{
			fields.add(field);
			values.add(value);
		
		
			String jsonRequest = JsonUtil.serializeRequest(RequestType.SELECT, SensorConfiguration.class, null, fields, values, RequestSender.CLIENT);
			String response = MonitrackGuiUtil.sendRequest(jsonRequest);
			List<SensorConfiguration> sensorToDisplays = (List<SensorConfiguration>)JsonUtil.deserializeObject(response);

			for(SensorConfiguration sensorToDisplay: sensorToDisplays) {
				this.sensor=sensorToDisplay;
			}
			
			configureSensor();
		} else {
			JOptionPane.showMessageDialog(configurationTab, "Veuillez saisir un id pour le capteur"
					, "Pas de données"
					, JOptionPane.INFORMATION_MESSAGE);
		}
	}
	

	
		@SuppressWarnings("unchecked")
		private void displaySensors() throws NoAvailableConnectionException, IOException, DeprecatedVersionException
		{
			fields.clear();
			values.clear();

			//Filters
			String filter1 = configurationTab.getFilter1ForShowCombobox().getSelectedItem().toString();
			String filter2 = configurationTab.getFilter2ForShowCombobox().getSelectedItem().toString();

			if(!filter1.equals("-"))
			{

				String field = "TYPE";
				
				if(filter1.equalsIgnoreCase("id"))
				{
					field = "ID_SENSOR_CONFIGURATION";
				} else if (filter1.equalsIgnoreCase("activity")) {
					field = "ACTIVITY";
				}
				
				String value = configurationTab.getFilter1TextField().getText().trim();
				
				if(value.length() > 0)
				{
					fields.add(field);
					values.add(value);
				}
			}

			if(!filter2.equals("-")) 
			{
				String field = "ACTIVITY";
				if(filter2.equalsIgnoreCase("Adresse IP"))
				{
					field = "IP_ADDRESS";
				}
				
				String value2 = configurationTab.getFilter2TextField().getText().trim();
				
				if(value2.length() > 0)
				{
					fields.add(field);
					values.add(value2);
				}

			}



			String jsonRequest = JsonUtil.serializeRequest(RequestType.SELECT, SensorConfiguration.class, null, fields, values, RequestSender.CLIENT);
			String response = MonitrackGuiUtil.sendRequest(jsonRequest);
			List<SensorConfiguration> sensorToDisplay = (List<SensorConfiguration>)JsonUtil.deserializeObject(response);

			String configurationText = "";
			for(SensorConfiguration sensor : sensorToDisplay)
			{
				configurationText += sensor.toString() + "\n";
			}

			configurationTab.getTextArea().setText(configurationText);
			
			if(configurationText.equals(""))
				JOptionPane.showMessageDialog(configurationTab, "Aucune donnée ne correpsond à vos critères", "Pas de données", JOptionPane.INFORMATION_MESSAGE);

		}
		
	

}
