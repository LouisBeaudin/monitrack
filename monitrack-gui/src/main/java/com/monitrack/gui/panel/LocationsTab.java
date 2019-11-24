package com.monitrack.gui.panel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import com.monitrack.enumeration.Images;
import com.monitrack.listener.LocationsTabListener;

public class LocationsTab extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private final String[] buildingWinds = {"NORD", "SUD", "OUEST", "EST"};
	
	/***** Menu Panels *****/
	private JPanel northPanel;
	private JPanel northPanelActionsChoice;
	private JPanel northPanelForCreate;
	private JPanel northPanelForModify;
	private JPanel northPanelForDelete;
	private JPanel northPanelForShow;
	
	private JComboBox<String> actionsCombobox;
	private JComboBox<String> modifyLocationsCombobox;
	private JComboBox<String> deleteLocationsCombobox;
	
	/***** Components of the overview menu *****/
	private JComboBox<String> filter1ForShowCombobox;
	private JComboBox<String> filter2ForShowCombobox;
	private JTextField filter1TextField;
	private JTextField filter2TextField;

	private LocationsTabListener listener;

	private JTextArea textArea;


	/***** Buttons for the CRUD (Create, Read, Update and Delete) *****/ 
	private JButton createButton;
	private JButton modifyButton;
	private JButton deleteButton;
	private JButton showButton;

	/***** Dialog for creating a location *****/
	private JPanel createLocationPopupPanel;
	private JTextField newLocationNameTextField;
	private JTextField newFloorTextField;
	private JTextField newLocationSizeTextField;
	private JComboBox<String> newBuildingWingCombobox;

	/***** Dialog for updating a location *****/
	private JPanel modifyLocationPopupPanel;
	private JTextField oldNameTextField;
	private JTextField modifiedNameTextField;
	private JTextField oldFloorTextField;
	private JTextField modifiedFloorTextField;
	private JTextField oldLocationSizeTextField;
	private JTextField modifiedLocationSizeTextField;
	private JTextField oldBuildingWingTextField;
	private JComboBox<String> modifiedBuildingWingCombobox;

	public LocationsTab()
	{
		setLayout(new BorderLayout());
		Font textAreaFont = new Font("Calibri", Font.PLAIN, 20);
		listener = new LocationsTabListener(this);
		northPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

		//Panel to choose the CRUD Operation to do
		northPanelActionsChoice = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel actionLabel = new JLabel("Action : ");
		String[] items = {"Ajouter", "Modifier", "Supprimer", "Visualiser"};
		actionsCombobox = new JComboBox<String>(items);
		actionsCombobox.addActionListener(listener);
		northPanelActionsChoice.add(actionLabel);
		northPanelActionsChoice.add(actionsCombobox);

		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setFont(textAreaFont);
		JScrollPane scroll = new JScrollPane(textArea);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);		

		setCreateMenu();
		setModifyMenu();
		setDeleteMenu();
		setShowMenu();

		setCreateLocationPopupPanel();
		setModifyLocationPopupPanel();

		actionsCombobox.setSelectedItem(items[0]);
		
		add(northPanel, BorderLayout.NORTH);
		add(scroll, BorderLayout.CENTER);
	}

	private void setCreateMenu()
	{
		northPanelForCreate = new JPanel(new FlowLayout(FlowLayout.LEFT));
		createButton = new JButton("Créer");
		createButton.setIcon(Images.ADD_ICON.getIcon());
		createButton.addActionListener(listener);
		northPanelForCreate.add(createButton);
	}

	private void setModifyMenu()
	{
		northPanelForModify = new JPanel(new FlowLayout(FlowLayout.LEFT));
		modifyLocationsCombobox = new JComboBox<>();
		modifyButton = new JButton("Modifier");
		modifyButton.addActionListener(listener);
		modifyButton.setIcon(Images.MODIFY_ICON.getIcon());

		northPanelForModify.add(new JLabel("Sélectionner un emplacement : "));
		northPanelForModify.add(modifyLocationsCombobox);
		northPanelForModify.add(modifyButton);

	}

	private void setDeleteMenu()
	{
		northPanelForDelete = new JPanel(new FlowLayout(FlowLayout.LEFT));
		deleteButton = new JButton("Supprimer");
		deleteButton.setIcon(Images.DELETE_ICON.getIcon());
		deleteButton.addActionListener(listener);
		deleteLocationsCombobox = new JComboBox<>();

		northPanelForDelete.add(new JLabel("Sélectionner un emplacement : "));
		northPanelForDelete.add(deleteLocationsCombobox);
		northPanelForDelete.add(deleteButton);
	}

	private void setShowMenu()
	{
		northPanelForShow = new JPanel(new FlowLayout(FlowLayout.LEFT));

		String[] filters1 = {"-", "Nom", "Aile"};
		String[] filters2 = {"-", "Etage", "Superficie"};
		filter1ForShowCombobox = new JComboBox<>(filters1);
		filter1ForShowCombobox.addActionListener(listener);
		filter2ForShowCombobox = new JComboBox<>(filters2);
		filter2ForShowCombobox.addActionListener(listener);
		filter1TextField = new JTextField(7);
		filter2TextField = new JTextField(7);
		northPanelForShow.add(new JLabel("Chercher par : "));
		northPanelForShow.add(filter1ForShowCombobox);
		northPanelForShow.add(filter1TextField);
		northPanelForShow.add(new JLabel(" Et par : "));
		northPanelForShow.add(filter2ForShowCombobox);
		northPanelForShow.add(filter2TextField);
		showButton = new JButton("Visualiser");
		showButton.setIcon(Images.SEARCH_ICON.getIcon());
		showButton.addActionListener(listener);
		
		northPanelForShow.add(showButton);
	}

	private void setCreateLocationPopupPanel()
	{
		createLocationPopupPanel = new JPanel();
		createLocationPopupPanel.setLayout(new BoxLayout(createLocationPopupPanel, BoxLayout.Y_AXIS));

		newLocationNameTextField = new JTextField(15);
		newFloorTextField = new JTextField(15);
		newLocationSizeTextField = new JTextField(15);
		newBuildingWingCombobox = new JComboBox<>(buildingWinds);

		createLocationPopupPanel.add(new JLabel("Nom du nouvel emplacement *:"));
		createLocationPopupPanel.add(newLocationNameTextField);
		createLocationPopupPanel.add(Box.createRigidArea(new Dimension(0,10)));
		createLocationPopupPanel.add(new JLabel("Etage *:"));
		createLocationPopupPanel.add(newFloorTextField);
		createLocationPopupPanel.add(Box.createRigidArea(new Dimension(0,10)));
		createLocationPopupPanel.add(new JLabel("Aile *:"));
		createLocationPopupPanel.add(newBuildingWingCombobox);
		createLocationPopupPanel.add(Box.createRigidArea(new Dimension(0,10)));
		createLocationPopupPanel.add(new JLabel("Superficie (en m²) :"));
		createLocationPopupPanel.add(newLocationSizeTextField);
		createLocationPopupPanel.add(Box.createRigidArea(new Dimension(0,10)));

		JLabel label = new JLabel("* Champs obligatoires");
		label.setFont(new Font("Arial", Font.ITALIC, 12));
		createLocationPopupPanel.add(label);
	}

	private void setModifyLocationPopupPanel()
	{
		modifyLocationPopupPanel = new JPanel(new GridLayout(0, 2, 10, 2));

		oldNameTextField = new JTextField(20);
		oldNameTextField.setEditable(false);
		modifiedNameTextField = new JTextField(20);
		
		oldFloorTextField = new JTextField(15);
		oldFloorTextField.setEditable(false);
		modifiedFloorTextField = new JTextField(15);
		
		oldBuildingWingTextField = new JTextField(15);
		oldBuildingWingTextField.setEditable(false);
		modifiedBuildingWingCombobox = new JComboBox<>(buildingWinds);
		
		oldLocationSizeTextField = new JTextField(15);
		oldLocationSizeTextField.setEditable(false);
		modifiedLocationSizeTextField = new JTextField(15);

		//Line 1
		modifyLocationPopupPanel.add(new JLabel("Ancien nom :"));
		modifyLocationPopupPanel.add(new JLabel("Nouveau nom :"));
		
		//Line 2
		modifyLocationPopupPanel.add(oldNameTextField);
		modifyLocationPopupPanel.add(modifiedNameTextField);
		
		//Line 3
		modifyLocationPopupPanel.add(new JLabel("Ancien étage :"));
		modifyLocationPopupPanel.add(new JLabel("Nouvel étage :"));
		
		//Line 4
		modifyLocationPopupPanel.add(oldFloorTextField);
		modifyLocationPopupPanel.add(modifiedFloorTextField);
		
		//Line 5
		modifyLocationPopupPanel.add(new JLabel("Ancienne aile du bâtiment :"));
		modifyLocationPopupPanel.add(new JLabel("Nouvelle aile du bâtiment :"));
		
		//Line 6
		modifyLocationPopupPanel.add(oldBuildingWingTextField);
		modifyLocationPopupPanel.add(modifiedBuildingWingCombobox);
		
		//Line 7
		modifyLocationPopupPanel.add(new JLabel("Ancienne superficie (en m²) :"));
		modifyLocationPopupPanel.add(new JLabel("Nouvelle superficie (en m²) :"));
		
		//Line 8
		modifyLocationPopupPanel.add(oldLocationSizeTextField);
		modifyLocationPopupPanel.add(modifiedLocationSizeTextField);
	}
	/**
	 * @return the createLocationPopupPanel
	 */
	public JPanel getCreateLocationPopupPanel() {
		return createLocationPopupPanel;
	}

	/**
	 * @return the modifyLocationPopupPanel
	 */
	public JPanel getModifyLocationPopupPanel() {
		return modifyLocationPopupPanel;
	}

	/**
	 * @return the actionsCombobox
	 */
	public JComboBox<String> getActionsCombobox() {
		return actionsCombobox;
	}

	/**
	 * @return the modifyLocationsCombobox
	 */
	public JComboBox<String> getModifyLocationsCombobox() {
		return modifyLocationsCombobox;
	}

	/**
	 * @return the deleteLocationsCombobox
	 */
	public JComboBox<String> getDeleteLocationsCombobox() {
		return deleteLocationsCombobox;
	}	

	/**
	 * @return the northPanel
	 */
	public JPanel getNorthPanel() {
		return northPanel;
	}

	/**
	 * @return the northPanelActionsChoice
	 */
	public JPanel getNorthPanelActionsChoice() {
		return northPanelActionsChoice;
	}

	/**
	 * @return the northPanelForCreate
	 */
	public JPanel getNorthPanelForCreate() {
		return northPanelForCreate;
	}

	/**
	 * @return the northPanelForModify
	 */
	public JPanel getNorthPanelForModify() {
		return northPanelForModify;
	}

	/**
	 * @return the northPanelForDelete
	 */
	public JPanel getNorthPanelForDelete() {
		return northPanelForDelete;
	}

	/**
	 * @return the northPanelForShow
	 */
	public JPanel getNorthPanelForShow() {
		return northPanelForShow;
	}

	/**
	 * @return the createButton
	 */
	public JButton getCreateButton() {
		return createButton;
	}

	/**
	 * @return the modifyButton
	 */
	public JButton getModifyButton() {
		return modifyButton;
	}

	/**
	 * @return the deleteButton
	 */
	public JButton getDeleteButton() {
		return deleteButton;
	}

	/**
	 * @return the showButton
	 */
	public JButton getShowButton() {
		return showButton;
	}

	/**
	 * @return the textArea
	 */
	public JTextArea getTextArea() {
		return textArea;
	}

	/**
	 * @return the newLocationNameTextField
	 */
	public JTextField getNewLocationNameTextField() {
		return newLocationNameTextField;
	}

	/**
	 * @return the newFloorTextField
	 */
	public JTextField getNewFloorTextField() {
		return newFloorTextField;
	}

	/**
	 * @return the newLocationSizeTextField
	 */
	public JTextField getNewLocationSizeTextField() {
		return newLocationSizeTextField;
	}

	/**
	 * @return the oldNameTextField
	 */
	public JTextField getOldNameTextField() {
		return oldNameTextField;
	}

	/**
	 * @return the modifiedNameTextField
	 */
	public JTextField getModifiedNameTextField() {
		return modifiedNameTextField;
	}

	/**
	 * @return the oldFloorTextField
	 */
	public JTextField getOldFloorTextField() {
		return oldFloorTextField;
	}

	/**
	 * @return the modifiedFloorTextField
	 */
	public JTextField getModifiedFloorTextField() {
		return modifiedFloorTextField;
	}

	/**
	 * @return the oldLocationSizeTextField
	 */
	public JTextField getOldLocationSizeTextField() {
		return oldLocationSizeTextField;
	}

	/**
	 * @return the modifiedLocationSizeTextField
	 */
	public JTextField getModifiedLocationSizeTextField() {
		return modifiedLocationSizeTextField;
	}

	/**
	 * @return the oldBuildingWingTextField
	 */
	public JTextField getOldBuildingWingTextField() {
		return oldBuildingWingTextField;
	}

	/**
	 * @return the filter1ForShowCombobox
	 */
	public JComboBox<String> getFilter1ForShowCombobox() {
		return filter1ForShowCombobox;
	}

	/**
	 * @return the filter2ForShowCombobox
	 */
	public JComboBox<String> getFilter2ForShowCombobox() {
		return filter2ForShowCombobox;
	}

	/**
	 * @return the newBuildingWingCombobox
	 */
	public JComboBox<String> getNewBuildingWingCombobox() {
		return newBuildingWingCombobox;
	}

	/**
	 * @return the modifiedBuildingWingCombobox
	 */
	public JComboBox<String> getModifiedBuildingWingCombobox() {
		return modifiedBuildingWingCombobox;
	}

	/**
	 * @return the filter1TextField
	 */
	public JTextField getFilter1TextField() {
		return filter1TextField;
	}

	/**
	 * @return the filter2TextField
	 */
	public JTextField getFilter2TextField() {
		return filter2TextField;
	}

}
