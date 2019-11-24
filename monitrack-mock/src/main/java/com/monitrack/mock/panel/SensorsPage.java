package com.monitrack.mock.panel;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.monitrack.enumeration.SensorActivity;
import com.monitrack.enumeration.SensorType;
import com.monitrack.mock.listener.SensorsTableModel;

public class SensorsPage extends JPanel {

	private static final long serialVersionUID = 1L;
	private final Insets insets = new Insets(5,10,0,0);
	private final Insets defaultInsets = new Insets(0,0,0,0);
	private GridBagConstraints c;
	//private SensorsTableListener sensorsTableListener;

	/***************** Filter bar **********************/
	private JTextField idTextField;
	private JComboBox<SensorType> sensorTypeComboBox;
	private JComboBox<SensorActivity> sensorActivityComboBox;
	private JTextField locationTextField;
	private JButton validateFiltersButton;
	private JButton loadDatasButton;

	/****************** Table **************************/
	private JScrollPane sensorsTableScrollPane;
	private JTable sensorsTable;
	private SensorsTableModel sensorsTableModel;

	/******* Changing message value *******/
	private JLabel rateValueLabel;
	private JTextField rateValueTextField;
	private JButton startSendingMessageButton;
	private JButton stopSendingMessageButton;

	public SensorsPage() {
		super(new GridBagLayout());		
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		sensorsTable = new JTable();
		sensorsTableModel = new SensorsTableModel(this, sensorsTable);
		sensorsTableScrollPane = new JScrollPane(sensorsTable);

		initFiltersBar();
		c.insets = defaultInsets;
		initSensorsTable();
		c.insets = defaultInsets;
		initRateChoiceBar();
		/*c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(new JButton("Super"), c);
		c.gridx = 0;
		c.gridy = 1;
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = 1;
		c.weightx = 0.4;
		c.weighty = 1;
		add(sensorsTableScrollPane, c);
		c.gridx = 1;
		c.gridy = 1;
		c.weightx = 0.1;
		add(new JButton("de"), c);
		c.gridx = 2;
		c.gridy = 1;
		c.weightx = 0.1;
		add(new JButton("la"), c);*/
	}

	private void initFiltersBar() {
		idTextField = new JTextField(5);
		sensorTypeComboBox = new JComboBox<SensorType>(SensorType.values());
		sensorActivityComboBox = new JComboBox<SensorActivity>(SensorActivity.values());
		locationTextField = new JTextField(15);
		validateFiltersButton = new JButton("Validate");
		validateFiltersButton.addActionListener(sensorsTableModel);
		loadDatasButton = new JButton("Load datas");
		loadDatasButton.addActionListener(sensorsTableModel);

		c.insets = insets;
		c.gridx = 0;
		c.gridy = 0;
		c.weighty = 0.2;
		add(new JLabel("Find by ID : "), c);
		c.gridx = 1;	 
		add(idTextField, c);

		c.gridx = 2;
		c.insets = insets;
		add(new JLabel("Find by type : "), c);
		c.gridx = 3;
		c.insets = defaultInsets;
		add(sensorTypeComboBox, c);
		c.insets = insets;

		c.gridx = 4;
		add(new JLabel("Find by activity : "), c);
		c.insets = defaultInsets;
		c.gridx = 5;
		add(sensorActivityComboBox, c);
		c.insets = insets;


		c.gridx = 6;
		add(new JLabel("Find by location : "), c);
		c.gridx = 7;
		add(locationTextField, c);

		c.insets = new Insets(0, 10, 0, 10);
		c.gridx = 8;
		add(validateFiltersButton, c);
	}

	private void initSensorsTable() {	

		c.weighty = 1;
		c.gridy = 2;
		c.gridx = 0;
		c.gridwidth = 7;
		c.gridheight = 2;
		add(sensorsTableScrollPane, c);		
		c.gridy = 10;
		add(loadDatasButton, c);
	}

	private void initRateChoiceBar() {
		c.gridy = 7;
		c.gridx = 0;
		c.gridwidth = 7;
		rateValueLabel = new JLabel("Current threshold : ");
		rateValueLabel.setFont(new Font("Calibri", Font.PLAIN, 25));
		rateValueTextField = new JTextField("0");
		rateValueTextField.setFont(new Font("Calibri", Font.PLAIN, 17));
		rateValueTextField.getDocument().addDocumentListener(sensorsTableModel);
		add(rateValueLabel, c);
		c.gridy = 8;
		c.insets = new Insets(50,0,0,0);
		c.gridwidth = 1;
		JLabel info = new JLabel("Enter the new threshold :  ");
		info.setFont(new Font("Calibri", Font.PLAIN, 17));
		add(info, c);
		c.gridx = 1;
		c.gridwidth = 2;
		add(rateValueTextField, c);
		startSendingMessageButton = new JButton("Start sending signal");
		startSendingMessageButton.addActionListener(sensorsTableModel);
		c.gridx = 3;
		c.insets = new Insets(50, 25, 0 , 12);
		add(startSendingMessageButton, c);
		c.gridx = 5;		
		stopSendingMessageButton = new JButton("Stop sending signal");
		stopSendingMessageButton.addActionListener(sensorsTableModel);
		add(stopSendingMessageButton, c);	
		
	}

	public JButton getValidateFiltersButton() {
		return validateFiltersButton;
	}

	public JTextField getIdTextField() {
		return idTextField;
	}

	public JComboBox<SensorType> getSensorTypeComboBox() {
		return sensorTypeComboBox;
	}

	public JComboBox<SensorActivity> getSensorActivityComboBox() {
		return sensorActivityComboBox;
	}

	public JTextField getLocationTextField() {
		return locationTextField;
	}


	public JTextField getRateValueTextField() {
		return rateValueTextField;
	}

	public JLabel getRateValueLabel() {
		return rateValueLabel;
	}

	public JButton getStartSendingMessageButton() {
		return startSendingMessageButton;
	}

	public JButton getStopSendingMessageButton() {
		return stopSendingMessageButton;
	}

	public JButton getLoadDatasButton() {
		return loadDatasButton;
	}


	/*northPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	northPanel.add(new JLabel("Rechercher les capteurs par : "));
	centerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 30));
	Sensor sensor = new Sensor(8, SensorActivity.ENABLED, SensorType.FLOW, 1, "192.168.20.15", "dsfsd", "dsfsdf", 
			1.0f, 2.0f, null, null, null, null, null, 2500f, "Decibel", 4.0f, 0.0f, 5.0f, 6.23f, 4.94f);
	Sensor sensor2 = new Sensor(9, SensorActivity.ENABLED, SensorType.SMOKE, 1, "192.168.20.15", "dsfsd", "dsfsdf", 
			1.0f, 2.0f, null, null, null, null, null, 0f, "Decibel", 4.0f, 0.0f, 5.0f, 6.23f, 4.94f);*/
	//centerPanel.add(new SensorInfoPanel(sensor));
	//centerPanel.add(new SensorInfoPanel(sensor2));		

	//centerPanel = new JPanel(new GridBagLayout());

}
