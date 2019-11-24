package com.monitrack.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.monitrack.entity.Location;
import com.monitrack.enumeration.Images;
import com.monitrack.enumeration.RequestSender;
import com.monitrack.enumeration.RequestType;
import com.monitrack.exception.DeprecatedVersionException;
import com.monitrack.exception.NoAvailableConnectionException;
import com.monitrack.gui.panel.LocationsTab;
import com.monitrack.shared.MonitrackGuiUtil;
import com.monitrack.util.JsonUtil;

public class LocationsTabListener implements ActionListener {

	private static final Logger log = LoggerFactory.getLogger(LocationsTabListener.class);

	private LocationsTab locationsTab;
	private List<Location> locations;
	private List<String> fields;
	private List<String> values;

	public LocationsTabListener(LocationsTab locationsTab)
	{
		this.locationsTab = locationsTab;
		locations = new ArrayList<>();
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
				if(combo == locationsTab.getActionsCombobox())
				{
					locationsTab.getNorthPanel().removeAll();
					locationsTab.getNorthPanel().add(locationsTab.getNorthPanelActionsChoice());
					if(combo.getSelectedItem().equals("Ajouter"))
					{
						locationsTab.getNorthPanel().add(locationsTab.getNorthPanelForCreate());			
					}
					else if(combo.getSelectedItem().equals("Modifier"))
					{	
						setComboboxWithLocations(locationsTab.getModifyLocationsCombobox());
						locationsTab.getNorthPanel().add(locationsTab.getNorthPanelForModify());				
					}
					else if(combo.getSelectedItem().equals("Supprimer"))
					{
						setComboboxWithLocations(locationsTab.getDeleteLocationsCombobox());
						locationsTab.getNorthPanel().add(locationsTab.getNorthPanelForDelete());		
					}
					else if(combo.getSelectedItem().equals("Visualiser"))
					{
						locationsTab.getNorthPanel().add(locationsTab.getNorthPanelForShow());
					}
				}
			}
			else if(e.getSource() instanceof JButton)
			{
				JButton clickedButton = (JButton)e.getSource();
				if(clickedButton == locationsTab.getCreateButton())
				{
					createLocation();
				}
				else if(clickedButton == locationsTab.getDeleteButton())
				{
					deleteLocation();
				}
				else if(clickedButton == locationsTab.getModifyButton())
				{
					updateLocation();					
				}
				else if(clickedButton == locationsTab.getShowButton())
				{
					displayLocations();
				}
			}
		}
		catch(Exception e1)
		{
			log.error(e1.getClass().getSimpleName() + " : " + e1.getMessage());
		}

		//Updates the Panel
		locationsTab.revalidate();
		locationsTab.repaint();
	}

	@SuppressWarnings("unchecked")
	private void setComboboxWithLocations(JComboBox<String> combobox)
	{
		String jsonRequest = JsonUtil.serializeRequest(RequestType.SELECT, Location.class, null, null, null, RequestSender.CLIENT);
		try 
		{
			String response = MonitrackGuiUtil.sendRequest(jsonRequest);
			locations = (List<Location>)JsonUtil.deserializeObject(response);
			combobox.removeAllItems();
			for(Location location : locations)
			{
				combobox.addItem(location.toString());
			}
		} 
		catch (Exception e) 
		{
			log.error(e.getMessage());
		}
	}

	private int showCreateLocationPopup()
	{
		return JOptionPane.showConfirmDialog(locationsTab, locationsTab.getCreateLocationPopupPanel(), 
				"Créer un emplacement", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, Images.ADD_ICON.getIcon());
	}

	private void createLocation() throws NoAvailableConnectionException, IOException, DeprecatedVersionException
	{
		int choice = 0;
		String errorMessage = "";
		boolean isFormValid = false;

		Location location = new Location();

		do {
			choice = showCreateLocationPopup();
			errorMessage = "";

			String name = locationsTab.getNewLocationNameTextField().getText().trim();
			if(name.length() <= 0) {
				errorMessage += "Le nom ne peut pas être vide\n";
			}

			int floor = NumberUtils.toInt(locationsTab.getNewFloorTextField().getText().trim(), -10);
			if(floor == -10 || floor < -1 || floor > 3) {
				errorMessage += "L'étage doit être un nombre compris entre -1 et 3\n";
			}						

			int area = NumberUtils.toInt(locationsTab.getNewLocationSizeTextField().getText().trim(), 0);
			if(area < 40 || area > 200) {
				errorMessage += "La superficie doit être comprise entre 40m² et 200m² !";
			}						

			if(errorMessage.trim().length() > 0 && choice == 0)
			{
				isFormValid = false;
				JOptionPane.showMessageDialog(locationsTab, errorMessage, "Erreur", JOptionPane.ERROR_MESSAGE);							
			}
			else if(choice == 0)
			{
				String wing = locationsTab.getNewBuildingWingCombobox().getSelectedItem().toString();
				location = new Location(name, "", floor, wing, area);

				//Clear the fields
				locationsTab.getNewLocationNameTextField().setText("");
				locationsTab.getNewFloorTextField().setText("");
				locationsTab.getNewLocationSizeTextField().setText("");

				isFormValid = true;
			}

		} while(!isFormValid && choice == 0);

		if(isFormValid && choice == 0)
		{						
			String serializedObject = JsonUtil.serializeObject(location, Location.class, null);
			String jsonRequest = JsonUtil.serializeRequest(RequestType.INSERT, location.getClass(), serializedObject, null, null, RequestSender.CLIENT);
			String response = MonitrackGuiUtil.sendRequest(jsonRequest); 

			Location locationCreated = (Location)JsonUtil.deserializeObject(response);
			String title = "Emplacement n°" + locationCreated.getIdLocation() + " créé";
			String message = "Un nouvel emplacement a été crée. Voulez-vous y ajouter des capteurs ?";
			int choice2 = JOptionPane.showConfirmDialog(locationsTab, message, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);

			if(choice2 == 0)
			{
				MonitrackGuiUtil.showComingSoonMesage();
			}

		}
	}

	private void updateLocation() throws NoAvailableConnectionException, IOException, DeprecatedVersionException
	{
		int selectedLocationIndex = locationsTab.getModifyLocationsCombobox().getSelectedIndex();
		if(selectedLocationIndex >= 0 && selectedLocationIndex < locations.size())
		{
			Location locationToUpdate = locations.get(selectedLocationIndex);

			locationsTab.getOldNameTextField().setText(locationToUpdate.getNameLocation());
			locationsTab.getOldFloorTextField().setText(String.valueOf(locationToUpdate.getFloor()));
			locationsTab.getOldBuildingWingTextField().setText(locationToUpdate.getWing());	
			locationsTab.getOldLocationSizeTextField().setText(String.valueOf(locationToUpdate.getArea()));

			int choice = 0;
			String errorMessage = "";

			errorMessage = "";
			choice = JOptionPane.showConfirmDialog(locationsTab, locationsTab.getModifyLocationPopupPanel(), 
					"Modifier un emplacement", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, Images.MODIFY_ICON.getIcon());

			String name = locationsTab.getModifiedNameTextField().getText().trim();
			if(name.length() <= 0) {
				name = locationsTab.getOldNameTextField().getText();
			}

			String floorText = locationsTab.getModifiedFloorTextField().getText().trim();
			int floor = NumberUtils.toInt(floorText, -10);
			if(floor == -10 || floor < -1 || floor > 3) {
				//We display this error only if the user entered something in the field
				if(floorText.length() > 0)
					errorMessage += "L'étage doit être un nombre compris entre -1 et 3\n";
				floor = locationToUpdate.getFloor();
			}						

			String areaText = locationsTab.getModifiedLocationSizeTextField().getText().trim();
			int area = NumberUtils.toInt(areaText, 0);
			if(area < 40 || area > 200) {
				//We display this error only if the user entered something in the field
				if(areaText.length() > 0)
					errorMessage += "La superficie doit être comprise entre 40m² et 200m² !";
				area = locationToUpdate.getArea();
			}	

			String wing = locationsTab.getModifiedBuildingWingCombobox().getSelectedItem().toString();

			if(errorMessage.trim().length() > 0 && choice == 0)
			{
				String message = "Pour certains champs la valeur restera inchangée suite à des erreurs :\n" + errorMessage;
				JOptionPane.showMessageDialog(locationsTab, message, "Erreur", JOptionPane.INFORMATION_MESSAGE);		
			}

			if(choice == 0)
			{
				// Upadates the location which needs to be updated
				locationToUpdate.setNameLocation(name);
				locationToUpdate.setFloor(floor);
				locationToUpdate.setWing(wing);
				locationToUpdate.setArea(area);	
			}


			//Send the request if all fields are correct and the user clicked on OK Button
			if(choice == 0)
			{		
				String serializedObject = JsonUtil.serializeObject(locationToUpdate, Location.class, "");	
				String jsonRequest = JsonUtil.serializeRequest(RequestType.UPDATE, Location.class, serializedObject, null, null, RequestSender.CLIENT);
				MonitrackGuiUtil.sendRequest(jsonRequest);
				JOptionPane.showMessageDialog(locationsTab, "Votre emplacement a bien été mis à jour", "Mise à jour réussie", JOptionPane.INFORMATION_MESSAGE);
				setComboboxWithLocations(locationsTab.getModifyLocationsCombobox());
			}

			// Clear the fields
			locationsTab.getOldNameTextField().setText("");
			locationsTab.getOldFloorTextField().setText("");
			locationsTab.getOldBuildingWingTextField().setText("");	
			locationsTab.getOldLocationSizeTextField().setText("");


		}
	}

	private void deleteLocation() throws NoAvailableConnectionException, IOException, DeprecatedVersionException
	{
		int selectedLocationIndex = locationsTab.getDeleteLocationsCombobox().getSelectedIndex();
		if(selectedLocationIndex >= 0 && selectedLocationIndex < locations.size())
		{
			Location locationToDelete = locations.get(selectedLocationIndex);
			String serializedObject = JsonUtil.serializeObject(locationToDelete, Location.class, "");
			String jsonRequest = JsonUtil.serializeRequest(RequestType.DELETE, Location.class, serializedObject, null, null, RequestSender.CLIENT);
			MonitrackGuiUtil.sendRequest(jsonRequest);
			JOptionPane.showMessageDialog(locationsTab, "L'emplacement selectionné a été supprimé", "Emplacement supprimé", JOptionPane.INFORMATION_MESSAGE);			
			setComboboxWithLocations(locationsTab.getDeleteLocationsCombobox());
		}

	}

	@SuppressWarnings("unchecked")
	private void displayLocations() throws NoAvailableConnectionException, IOException, DeprecatedVersionException
	{
		fields.clear();
		values.clear();

		//Filters
		String filter1 = locationsTab.getFilter1ForShowCombobox().getSelectedItem().toString();
		String filter2 = locationsTab.getFilter2ForShowCombobox().getSelectedItem().toString();

		if(!filter1.equals("-"))
		{

			String field = "WING";
			
			if(filter1.equalsIgnoreCase("nom"))
			{
				field = "NAME";
			}
			
			String value = locationsTab.getFilter1TextField().getText().trim();
			
			if(value.length() > 0)
			{
				fields.add(field);
				values.add(value);
			}
		}

		if(!filter2.equals("-")) 
		{
			String field = "AREA";
			if(filter2.equalsIgnoreCase("etage"))
			{
				field = "FLOOR";
			}
			
			int value = NumberUtils.toInt(locationsTab.getFilter2TextField().getText(), -2);
			if(value != -2)
			{
				fields.add(field);
				values.add(String.valueOf(value));
			}

		}



		String jsonRequest = JsonUtil.serializeRequest(RequestType.SELECT, Location.class, null, fields, values, RequestSender.CLIENT);
		String response = MonitrackGuiUtil.sendRequest(jsonRequest);
		List<Location> locationToDisplay = (List<Location>)JsonUtil.deserializeObject(response);

		String locationsText = "";
		for(Location location : locationToDisplay)
		{
			locationsText += location.toStringFull() + "\n";
		}

		locationsTab.getTextArea().setText(locationsText);
		
		if(locationsText.equals(""))
			JOptionPane.showMessageDialog(locationsTab, "Aucune donnée ne correpsond à vos critères", "Pas de données", JOptionPane.INFORMATION_MESSAGE);

	}

}
