package com.monitrack.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.monitrack.entity.Person;
import com.monitrack.enumeration.Images;
import com.monitrack.enumeration.RequestType;
import com.monitrack.gui.panel.PersonsTab;
import com.monitrack.shared.MonitrackGuiUtil;
import com.monitrack.util.JsonUtil;

public abstract class PersonsTabListener implements ActionListener {

	private static final Logger log = LoggerFactory.getLogger(PersonsTabListener.class);
	private PersonsTab personsTab;
	private List<Person> persons;
	private List<String> fields;
	private List<String> values;	
	private String personText = "";
	
/*
	public PersonsTabListener(PersonsTab personsTab) {
		this.personsTab = personsTab;
		fields = new ArrayList<String>();
		values = new ArrayList<String>();	
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void actionPerformed(ActionEvent e) {

		try
		{
			personsTab.getTextArea().setText("");
			//Case when the user chooses the CRUD Operation he wants to do
			if(e.getSource() instanceof JComboBox)
			{
				JComboBox combo = (JComboBox)e.getSource();
				if(combo == personsTab.getActionsCombobox())
				{
					personsTab.getNorthPanel().removeAll();
					personsTab.getNorthPanel().add(personsTab.getNorthPanelActionsChoice());
					if(combo.getSelectedItem().equals("Ajouter"))
					{
						personsTab.getNorthPanel().add(personsTab.getNorthPanelForCreate());			
					}
					else if(combo.getSelectedItem().equals("Modifier"))
					{	
						setComboboxWithPersons(personsTab.getModifyPersonsCombobox());
						personsTab.getNorthPanel().add(personsTab.getNorthPanelForModify());				
					}
					else if(combo.getSelectedItem().equals("Supprimer"))
					{
						setComboboxWithPersons(personsTab.getDeletePersonsCombobox());
						personsTab.getNorthPanel().add(personsTab.getNorthPanelForDelete());		
					}
					else if(combo.getSelectedItem().equals("Visualiser"))
					{
						personsTab.getNorthPanel().add(personsTab.getNorthPanelForShow());
					}
				}
				
			}
			// Case when the user clicks on a confirm Button
			else if(e.getSource() instanceof JButton)
			{
				JButton clickedButton = (JButton)e.getSource();
				if(clickedButton == personsTab.getCreateButton())
				{
					String name = personsTab.getNewNameTextField().getText().trim();
					if(name.length() <= 0)
					{
						JOptionPane.showMessageDialog(personsTab, "Le nom ne peut pas être vide", "Nom incorect", JOptionPane.ERROR_MESSAGE);
					}
					else
					{
						Person newPerson = new Person(name, userName, password, );
						String serializedObject = JsonUtil.serializeObject(newPerson, Person.class, "");
						String jsonRequest = JsonUtil.serializeRequest(RequestType.INSERT, Person.class, serializedObject, null, null);
						String response = MonitrackGuiUtil.sendRequest(jsonRequest);
						newPerson = (Person)JsonUtil.deserializeObject(response);
						JOptionPane.showMessageDialog(personsTab, "Nouvelle personne créée avec l'id n°" + newPerson.getIdPerson(), "Succes", JOptionPane.INFORMATION_MESSAGE, Images.SUCCESS.getIcon());
						
					}
				}
				else if(clickedButton == personsTab.getModifyButton())
				{
					int comboboxIndex = personsTab.getModifyPersonsCombobox().getSelectedIndex();
					if(comboboxIndex >= 0 && comboboxIndex < persons.size())
					{
						Person personToUpadte = persons.get(comboboxIndex);

						// Set the id for the dalog panel
						personsTab.getIdLabel().setText("ID : " + personToUpadte.getIdPerson());
						//Set the name of the person in the texfield for the update
						personsTab.getOldNameTextField().setText(personToUpadte.getNamePerson());
						//Remove old text of the new Name text field
						personsTab.getModifiedNameTextField().setText("");
						
						int choice = JOptionPane.showConfirmDialog(personsTab, personsTab.getModifyPersonPopupPanel(), 
								"Modifier", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, Images.EDIT_PERSON.getIcon());
						
						//If the user clicks on OK Button
						if(choice == 0)
						{
							String newName = personsTab.getModifiedNameTextField().getText().trim();
							if(newName.length() <= 0)
							{
								JOptionPane.showMessageDialog(personsTab, "Le nouveau nom ne peut pas être vide", "Erreur", JOptionPane.ERROR_MESSAGE);
							}
							else
							{
								personToUpadte.setNamePerson(newName);
								String serializedObject = JsonUtil.serializeObject(personToUpadte, Person.class, "");
								String jsonRequest = JsonUtil.serializeRequest(RequestType.UPDATE, Person.class, serializedObject, null, null);
								MonitrackGuiUtil.sendRequest(jsonRequest);
								setComboboxWithPersons(personsTab.getModifyPersonsCombobox());
							}
						}
					}
				}
				else if(clickedButton == personsTab.getDeleteButton())
				{
					int comboboxIndex = personsTab.getDeletePersonsCombobox().getSelectedIndex();
					if(comboboxIndex >= 0 && comboboxIndex < persons.size())
					{
						Person personToDelete = persons.get(comboboxIndex);
						String serializedObject = JsonUtil.serializeObject(personToDelete, personToDelete.getClass(), "");
						String jsonRequest = JsonUtil.serializeRequest(RequestType.DELETE, Person.class, serializedObject, null, null);
						MonitrackGuiUtil.sendRequest(jsonRequest);
						setComboboxWithPersons(personsTab.getDeletePersonsCombobox());
					}
				}
				else if(clickedButton == personsTab.getShowButton())
				{					
					fields.clear();
					values.clear();
					boolean correctFilters = addFilters();

					if(correctFilters)
					{
						personText = "";
						String jsonRequest = JsonUtil.serializeRequest(RequestType.SELECT, Person.class, null, fields, values);
						System.out.println(JsonUtil.indentJsonOutput(jsonRequest));
						String response = MonitrackGuiUtil.sendRequest(jsonRequest);
						persons = (List<Person>)JsonUtil.deserializeObject(response);
						
						if(persons.size() > 0)
						{
							for(Person person : persons)
							{
								personText += person.toString() + "\n";
							}
						}
						else
						{
							JOptionPane.showMessageDialog(personsTab, "Aucune donnée ne correspond à vos filtres !", "Aucune donnée", JOptionPane.ERROR_MESSAGE);
						}
						
					}

					personsTab.getTextArea().setText(personText);
				}
			}

			//Updates the Panel
			personsTab.revalidate();
			personsTab.repaint();
		}
		catch(IOException e1)
		{
			MonitrackGuiUtil.showNoConnectionMessage();			
		}
		catch(Exception e1)
		{
			log.error(e1.getMessage());
		}


	}

	@SuppressWarnings("unchecked")
	private void setComboboxWithPersons(JComboBox<String> combobox)
	{
		String jsonRequest = JsonUtil.serializeRequest(RequestType.SELECT, Person.class, null, null, null);
		try 
		{
			String response = MonitrackGuiUtil.sendRequest(jsonRequest);
			persons = (List<Person>)JsonUtil.deserializeObject(response);
			combobox.removeAllItems();
			for(Person person : persons)
			{
				combobox.addItem(person.toString());
			}
		} 
		catch (Exception e) 
		{
			log.error(e.getMessage());
		}
	}

	private boolean addFilters()
	{
		if(personsTab.getFilter1ForShowCombobox().getSelectedItem().equals("id"))
		{
			String value = personsTab.getFilter1TextField().getText().trim();
			if(value.length() > 0)
			{
				try 
				{
					Integer id = Integer.parseInt(value);
					fields.add("id");
					values.add(id.toString());
				} 
				catch (Exception e) 
				{
					JOptionPane.showMessageDialog(personsTab, "Le champ du filtre ID ne doit contenir que des chiffres", "Filtre ID incorrect", JOptionPane.ERROR_MESSAGE);
					log.error("The ID is not parsable because it is not an integer !");
					return false;
				}
				
			}
		}
		if(personsTab.getFilter2ForShowCombobox().getSelectedItem().equals("nom"))
		{
			String value = personsTab.getFilter2TextField().getText().trim();
			if(value.length() > 0)
			{
				fields.add("name");
				values.add(value);
			}

		}
		return true;
	}
*/
}
