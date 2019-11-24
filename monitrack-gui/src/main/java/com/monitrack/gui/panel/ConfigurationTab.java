package com.monitrack.gui.panel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import com.monitrack.entity.Location;
import com.monitrack.enumeration.Images;
import com.monitrack.enumeration.RequestSender;
import com.monitrack.enumeration.RequestType;
import com.monitrack.listener.ConfigurationTabListener;
import com.monitrack.shared.MonitrackGuiUtil;
import com.monitrack.util.JsonUtil;

public class ConfigurationTab extends JPanel{

	private static final long serialVersionUID = 1L;
	
	private final String[] status = {"ACTIVÉ", "DESACTIVÉ", "PAS_CONFIGURÉ"};

	/***** Menu Panels *****/
	private JPanel northPanel;
	private JPanel northPanelActionsChoice;
	private JPanel northPanelForConfigure;
	private JPanel northPanelForShow;
	
	private JComboBox<String> actionsCombobox;
	private JComboBox<String> configureSensorsCombobox;
	
	/***** Components of the overview menu *****/
	private JComboBox<String> filter1ForShowCombobox;
	private JComboBox<String> filter2ForShowCombobox;
	private JTextField filter1TextField;
	private JTextField filter2TextField;
	
	private JTextField filter3TextField;

	private ConfigurationTabListener listener;

	private JTextArea textArea;


	/***** Buttons for the CRUD (Create, Read, Update and Delete) *****/ 
	private JButton showButton;
	private JButton configureButton;

	/***** Dialog for updating a location *****/
	private JPanel configureCaptorPopupPanel;
	private JTextField oldMaxDangerThresholdTextField;
	private JTextField modifiedMaxDangerThresholdTextField;
	private JTextField oldMinDangerThresholdTextField;
	private JTextField modifiedMinDangerThresholdTextField;
	private JTextField oldActivityTextField;
	private JComboBox<String> modifiedCaptorStatusCombobox;
	private JTextField oldVersionTextField;
	private JTextField modifiedVersionTextField;
	private JTextField oldFrequencyTextField;
	private JTextField modifiedFrequencyTextField;
	private JTextField oldBeginTimeTextField;
	private JTextField modifiedBeginTimeTextField;
	private JTextField oldEndTimeTextField;
	private JTextField modifiedEndTimeTextField;
	
	public ConfigurationTab()
	{
		setLayout(new BorderLayout());
		Font textAreaFont = new Font("Calibri", Font.PLAIN, 20);
		listener = new ConfigurationTabListener(this);
		northPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));


		//Panel to choose the CRUD Operation to do
		northPanelActionsChoice = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel actionLabel = new JLabel("Action : ");
		String[] items = {"Visualiser les capteurs","Configurer un capteur"};
		actionsCombobox = new JComboBox<String>(items);
		actionsCombobox.addActionListener(listener);
		northPanelActionsChoice.add(actionLabel);
		northPanelActionsChoice.add(actionsCombobox);
		
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setFont(textAreaFont);
		JScrollPane scroll = new JScrollPane(textArea);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);		

		setShowMenu();
		setConfigurationMenu();
		
		setConfigureSensorPopupPanel();

		actionsCombobox.setSelectedItem(items[0]);
		
		add(northPanel, BorderLayout.NORTH);
		add(scroll, BorderLayout.CENTER);
	}
	
	private void setShowMenu()
	{
		northPanelForShow = new JPanel(new FlowLayout(FlowLayout.LEFT));

		String[] filters1 = {"-", "Type", "Id"};
		String[] filters2 = {"-", "Activation", "Adresse IP"};
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
	
	
	private void setConfigurationMenu()
	{
		northPanelForConfigure = new JPanel(new FlowLayout(FlowLayout.LEFT));
		filter3TextField = new JTextField(7);
		northPanelForConfigure.add(new JLabel("ID du capteur: "));
		northPanelForConfigure.add(filter3TextField);
		configureButton = new JButton("Configurer");
		configureButton.addActionListener(listener);
	
		northPanelForConfigure.add(configureButton);

	}
	
	
	private void setConfigureSensorPopupPanel()
	{
		configureCaptorPopupPanel = new JPanel(new GridLayout(0, 2, 10, 2));

		oldMaxDangerThresholdTextField= new JTextField(20);
		oldMaxDangerThresholdTextField.setEditable(false);
		modifiedMaxDangerThresholdTextField= new JTextField(20);
		
		oldMinDangerThresholdTextField = new JTextField(15);
		oldMinDangerThresholdTextField.setEditable(false);
		modifiedMinDangerThresholdTextField = new JTextField(15);
		
		oldActivityTextField = new JTextField(15);
		oldActivityTextField.setEditable(false);
		modifiedCaptorStatusCombobox = new JComboBox<>(status);
		
		oldVersionTextField = new JTextField(15);
		oldVersionTextField.setEditable(false);
		modifiedVersionTextField = new JTextField(15);
		
		oldFrequencyTextField = new JTextField(15);
		oldFrequencyTextField.setEditable(false);
		modifiedFrequencyTextField = new JTextField(15);
		
		oldBeginTimeTextField = new JTextField(15);
		oldBeginTimeTextField.setEditable(false);
		modifiedBeginTimeTextField = new JTextField(15);
		
		oldEndTimeTextField = new JTextField(15);
		oldEndTimeTextField.setEditable(false);
		modifiedEndTimeTextField = new JTextField(15);

		//Line 1
		configureCaptorPopupPanel.add(new JLabel("Ancienne limite:"));
		configureCaptorPopupPanel.add(new JLabel("Nouvelle limite :"));
		
		//Line 2
		configureCaptorPopupPanel.add(oldMaxDangerThresholdTextField);
		configureCaptorPopupPanel.add(modifiedMaxDangerThresholdTextField);
		
		//Line 3
		configureCaptorPopupPanel.add(new JLabel("Ancien minimum :"));
		configureCaptorPopupPanel.add(new JLabel("Nouveau minimum :"));
		
		//Line 4
		configureCaptorPopupPanel.add(oldMinDangerThresholdTextField);
		configureCaptorPopupPanel.add(modifiedMinDangerThresholdTextField);
		
		//Line 5
		configureCaptorPopupPanel.add(new JLabel("Activité :"));
		configureCaptorPopupPanel.add(new JLabel("Activer le capteur :"));
		
		//Line 6
		configureCaptorPopupPanel.add(oldActivityTextField);
		configureCaptorPopupPanel.add(modifiedCaptorStatusCombobox);
		
		//Line 7
		configureCaptorPopupPanel.add(new JLabel("Ancienne version de logiciel :"));
		configureCaptorPopupPanel.add(new JLabel("Nouvelle version de logiciel :"));
		
		//Line 8
		configureCaptorPopupPanel.add(oldVersionTextField);
		configureCaptorPopupPanel.add(modifiedVersionTextField);
		
		//Line 9
		configureCaptorPopupPanel.add(new JLabel("Ancienne fréquence d'activation d'alerte :"));
		configureCaptorPopupPanel.add(new JLabel("Nouvelle fréquence d'activation d'alerte :"));
		
		//Line 10
		configureCaptorPopupPanel.add(oldFrequencyTextField);
		configureCaptorPopupPanel.add(modifiedFrequencyTextField);
		
		//Line 11
		configureCaptorPopupPanel.add(new JLabel("Ancienne heure de début d'activité :"));
		configureCaptorPopupPanel.add(new JLabel("Nouvelle heure de début d'activité :"));
		
		//Line 12
		configureCaptorPopupPanel.add(oldBeginTimeTextField);
		configureCaptorPopupPanel.add(modifiedBeginTimeTextField);
	
		//Line 13
		configureCaptorPopupPanel.add(new JLabel("Ancienne heure de fin d'activité :"));
		configureCaptorPopupPanel.add(new JLabel("Nouvelle heure de fin d'activité :"));
		
		//Line 14
		configureCaptorPopupPanel.add(oldEndTimeTextField);
		configureCaptorPopupPanel.add(modifiedEndTimeTextField);
	
	
	}

	public JPanel getNorthPanel() {
		return northPanel;
	}

	public void setNorthPanel(JPanel northPanel) {
		this.northPanel = northPanel;
	}

	public JPanel getNorthPanelActionsChoice() {
		return northPanelActionsChoice;
	}

	public void setNorthPanelActionsChoice(JPanel northPanelActionsChoice) {
		this.northPanelActionsChoice = northPanelActionsChoice;
	}

	public JPanel getNorthPanelForShow() {
		return northPanelForShow;
	}

	public void setNorthPanelForShow(JPanel northPanelForShow) {
		this.northPanelForShow = northPanelForShow;
	}

	public JComboBox<String> getActionsCombobox() {
		return actionsCombobox;
	}

	public void setActionsCombobox(JComboBox<String> actionsCombobox) {
		this.actionsCombobox = actionsCombobox;
	}

	public JComboBox<String> getFilter1ForShowCombobox() {
		return filter1ForShowCombobox;
	}

	public void setFilter1ForShowCombobox(JComboBox<String> filter1ForShowCombobox) {
		this.filter1ForShowCombobox = filter1ForShowCombobox;
	}

	public JComboBox<String> getFilter2ForShowCombobox() {
		return filter2ForShowCombobox;
	}

	public void setFilter2ForShowCombobox(JComboBox<String> filter2ForShowCombobox) {
		this.filter2ForShowCombobox = filter2ForShowCombobox;
	}

	public JTextField getFilter1TextField() {
		return filter1TextField;
	}

	public void setFilter1TextField(JTextField filter1TextField) {
		this.filter1TextField = filter1TextField;
	}

	public JTextField getFilter2TextField() {
		return filter2TextField;
	}

	public void setFilter2TextField(JTextField filter2TextField) {
		this.filter2TextField = filter2TextField;
	}

	public ConfigurationTabListener getListener() {
		return listener;
	}

	public void setListener(ConfigurationTabListener listener) {
		this.listener = listener;
	}

	public JTextArea getTextArea() {
		return textArea;
	}

	public void setTextArea(JTextArea textArea) {
		this.textArea = textArea;
	}

	public JButton getShowButton() {
		return showButton;
	}

	public void setShowButton(JButton showButton) {
		this.showButton = showButton;
	}

	

	public JTextField getFilter3TextField() {
		return filter3TextField;
	}

	public void setFilter3TextField(JTextField filter3TextField) {
		this.filter3TextField = filter3TextField;
	}

	public JPanel getConfigureCaptorPopupPanel() {
		return configureCaptorPopupPanel;
	}

	public void setConfigureCaptorPopupPanel(JPanel configureCaptorPopupPanel) {
		this.configureCaptorPopupPanel = configureCaptorPopupPanel;
	}

	public String[] getStatus() {
		return status;
	}

	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public JPanel getNorthPanelForConfigure() {
		return northPanelForConfigure;
	}

	public void setNorthPanelForConfigure(JPanel northPanelForConfigure) {
		this.northPanelForConfigure = northPanelForConfigure;
	}

	public JButton getConfigureButton() {
		return configureButton;
	}

	public void setConfigureButton(JButton configureButton) {
		this.configureButton = configureButton;
	}

	public JComboBox<String> getConfigureSensorsCombobox() {
		return configureSensorsCombobox;
	}

	public void setConfigureSensorsCombobox(JComboBox<String> configureSensorsCombobox) {
		this.configureSensorsCombobox = configureSensorsCombobox;
	}

	public JTextField getOldMaxDangerThresholdTextField() {
		return oldMaxDangerThresholdTextField;
	}

	public void setOldMaxDangerThresholdTextField(JTextField oldMaxDangerThresholdTextField) {
		this.oldMaxDangerThresholdTextField = oldMaxDangerThresholdTextField;
	}

	public JTextField getModifiedMaxDangerThresholdTextField() {
		return modifiedMaxDangerThresholdTextField;
	}

	public void setModifiedMaxDangerThresholdTextField(JTextField modifiedMaxDangerThresholdTextField) {
		this.modifiedMaxDangerThresholdTextField = modifiedMaxDangerThresholdTextField;
	}

	public JTextField getOldMinDangerThresholdTextField() {
		return oldMinDangerThresholdTextField;
	}

	public void setOldMinDangerThresholdTextField(JTextField oldMinDangerThresholdTextField) {
		this.oldMinDangerThresholdTextField = oldMinDangerThresholdTextField;
	}

	public JTextField getModifiedMinDangerThresholdTextField() {
		return modifiedMinDangerThresholdTextField;
	}

	public void setModifiedMinDangerThresholdTextField(JTextField modifiedMinDangerThresholdTextField) {
		this.modifiedMinDangerThresholdTextField = modifiedMinDangerThresholdTextField;
	}

	public JTextField getOldActivityTextField() {
		return oldActivityTextField;
	}

	public void setOldActivityTextField(JTextField oldActivityTextField) {
		this.oldActivityTextField = oldActivityTextField;
	}

	public JComboBox<String> getModifiedCaptorStatusCombobox() {
		return modifiedCaptorStatusCombobox;
	}

	public void setModifiedCaptorStatusCombobox(JComboBox<String> modifiedCaptorStatusCombobox) {
		this.modifiedCaptorStatusCombobox = modifiedCaptorStatusCombobox;
	}

	public JTextField getOldVersionTextField() {
		return oldVersionTextField;
	}

	public void setOldVersionTextField(JTextField oldVersionTextField) {
		this.oldVersionTextField = oldVersionTextField;
	}

	public JTextField getModifiedVersionTextField() {
		return modifiedVersionTextField;
	}

	public void setModifiedVersionTextField(JTextField modifiedVersionTextField) {
		this.modifiedVersionTextField = modifiedVersionTextField;
	}

	public JTextField getOldFrequencyTextField() {
		return oldFrequencyTextField;
	}

	public void setOldFrequencyTextField(JTextField oldFrequencyTextField) {
		this.oldFrequencyTextField = oldFrequencyTextField;
	}

	public JTextField getModifiedFrequencyTextField() {
		return modifiedFrequencyTextField;
	}

	public void setModifiedFrequencyTextField(JTextField modifiedFrequencyTextField) {
		this.modifiedFrequencyTextField = modifiedFrequencyTextField;
	}

	public JTextField getOldBeginTimeTextField() {
		return oldBeginTimeTextField;
	}

	public void setOldBeginTimeTextField(JTextField oldBeginTimeTextField) {
		this.oldBeginTimeTextField = oldBeginTimeTextField;
	}

	public JTextField getModifiedBeginTimeTextField() {
		return modifiedBeginTimeTextField;
	}

	public void setModifiedBeginTimeTextField(JTextField modifiedBeginTimeTextField) {
		this.modifiedBeginTimeTextField = modifiedBeginTimeTextField;
	}

	public JTextField getOldEndTimeTextField() {
		return oldEndTimeTextField;
	}

	public void setOldEndTimeTextField(JTextField oldEndTimeTextField) {
		this.oldEndTimeTextField = oldEndTimeTextField;
	}

	public JTextField getModifiedEndTimeTextField() {
		return modifiedEndTimeTextField;
	}

	public void setModifiedEndTimeTextField(JTextField modifiedEndTimeTextField) {
		this.modifiedEndTimeTextField = modifiedEndTimeTextField;
	}
	
}
