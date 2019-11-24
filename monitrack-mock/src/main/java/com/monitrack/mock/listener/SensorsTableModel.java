package com.monitrack.mock.listener;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import org.apache.commons.lang3.math.NumberUtils;

import com.monitrack.entity.Location;
import com.monitrack.entity.SensorConfiguration;
import com.monitrack.enumeration.RequestType;
import com.monitrack.mock.panel.SensorsPage;
import com.monitrack.mock.runnable.SensorSignal;
import com.monitrack.shared.MonitrackGuiUtil;
import com.monitrack.util.JsonUtil;

public class SensorsTableModel extends AbstractTableModel implements ListSelectionListener, ActionListener, DocumentListener {


	private JTable parent;
	private SensorsPage sensorsPage;
	private static final long serialVersionUID = 1L;
	private String[] header = {"ID", "TYPE", "ACTIVITY", "LOCATION", "MAC ADDRESS", "CURRENT THRESHOLD", "THRESHOLD MIN", "THRESHOLD MAX", "STATE"};
	private String [][] datas;
	private String [][] emptyDatas;
	private final int numberOfRows = 500;
	private final int numberOfColumns = 9;
	private int firstEmptyIndex;
	private List<SensorConfiguration> sensors;
	// Map to keep all launched Runnable
	private Map<Integer, SensorSignal> sensorSignalMap;
	private List<SensorConfiguration> filteredSensors;
	private float currentThresholdValue = 0;


	public SensorsTableModel(SensorsPage sensorsPage, JTable parent) {
		this.sensorsPage = sensorsPage;
		this.parent = parent;
		parent.getSelectionModel().addListSelectionListener(this);
		parent.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		parent.setModel(this);
		datas = new String[numberOfRows][numberOfColumns];
		emptyDatas = new String[numberOfRows][numberOfColumns];
		sensorSignalMap = new HashMap<Integer, SensorSignal>();
		updateTable();
		firstEmptyIndex = 0;
	}

	@SuppressWarnings("unchecked")
	private void loadDatas() {
		try {
			String jsonRequest = JsonUtil.serializeRequest(RequestType.SELECT, SensorConfiguration.class, null, null, null, null);
			String response = MonitrackGuiUtil.sendRequest(jsonRequest);
			sensors = (List<SensorConfiguration>) JsonUtil.deserializeObject(response);
			filteredSensors = new ArrayList<>(sensors);
			if(sensors != null) {
				int size = sensors.size();
				for(int i = 0; i < size; i++) {
					initSensorConfigurationDatas(sensors.get(i));					
				}				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public int getColumnCount() {
		return header.length;
	}

	@Override
	public int getRowCount() {
		return datas.length;
	}

	@Override
	public Object getValueAt(int arg0, int arg1) {
		return datas[arg0][arg1];
	}

	public SensorConfiguration getValueAt(int line) {
		if(line >= 0 && line < sensors.size())
			return sensors.get(line);
		return null;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		super.setValueAt(aValue, rowIndex, columnIndex);
	}

	@Override
	public String getColumnName(int column) {
		return header[column];
	}

	public void initSensorConfigurationDatas(SensorConfiguration sensorConfiguration) {
		String id = sensorConfiguration.getSensorConfigurationId().toString();
		String type = sensorConfiguration.getSensorType().getEnglishLabel();
		String activity = sensorConfiguration.getSensorActivity().name();
		String location = "";
		String macAddress = sensorConfiguration.getMacAddress();
		String currentThreshold = sensorConfiguration.getCurrentThreshold().toString();
		String minThreshold = sensorConfiguration.getMinDangerThreshold().toString();
		String maxThreshold = sensorConfiguration.getMaxDangerThreshold().toString();
		datas[firstEmptyIndex] = new String[]{id, type, activity, location, 
				macAddress, currentThreshold, 
				minThreshold, maxThreshold, 
		"STATE"};
		firstEmptyIndex++;
		updateTable(); 
	}

	private void updateTable() {
		fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
		fireTableDataChanged();
	}

	public List<SensorConfiguration> getSensors() {
		return sensors;
	}

	public void setSensors(List<SensorConfiguration> sensors) {
		this.sensors = sensors;
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if(!e.getValueIsAdjusting()) {
			int[] rows =  parent.getSelectedRows();
			for(int i : rows) {
				//FIXME
				System.out.println(i);
			}
		}

	}

	public void setParent(JTable parent) {
		this.parent = parent;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		firstEmptyIndex = 0;
		if(e.getSource() == sensorsPage.getValidateFiltersButton()) {
			datas = emptyDatas.clone();
			if(sensors == null) {
				JOptionPane.showMessageDialog(null, "You need to load the datas first", "Datas not loaded", JOptionPane.ERROR_MESSAGE);
			}
			else {

				int size = sensors.size();
				filteredSensors.clear();
				for(int i = 0 ; i < size; i++) {
					SensorConfiguration sensor = sensors.get(i);	
					if(isSensorCorrect(sensor)) {
						initSensorConfigurationDatas(sensor);
						filteredSensors.add(sensor);
					}				
				}
			}
		}
		else if(e.getSource() == sensorsPage.getStartSendingMessageButton()) {
			startStopSensorSignal(true);
		}
		else if(e.getSource() == sensorsPage.getStopSendingMessageButton()) {
			startStopSensorSignal(false);

		}
		else if(e.getSource() == sensorsPage.getLoadDatasButton()) {
			loadDatas();			
		}

		updateTable();
	}

	private boolean isSensorCorrect(SensorConfiguration sensor) {
		String idString = sensorsPage.getIdTextField().getText().trim();
		if(idString.length() > 0 && NumberUtils.toInt(idString) != sensor.getSensorConfigurationId())
			return false;
		if(!sensorsPage.getSensorTypeComboBox().getSelectedItem().toString().equalsIgnoreCase(sensor.getSensorType().name()))
			return false;
		if(!sensorsPage.getSensorActivityComboBox().getSelectedItem().toString().equalsIgnoreCase(sensor.getSensorActivity().name()))
			return false;

		String locationFilter = sensorsPage.getLocationTextField().getText().trim();
		Location location = sensor.getLocation();
		String locationName = (location != null) ? location.getNameLocation() : "";
		if(!locationFilter.equalsIgnoreCase(locationName))
			return false;

		return true;
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		getThreshold(e, sensorsPage.getRateValueTextField());				
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		getThreshold(e, sensorsPage.getRateValueTextField());		

	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		getThreshold(e, sensorsPage.getRateValueTextField());		
	}

	private void getThreshold(DocumentEvent e, JTextField field) {
		if(e.getDocument() == sensorsPage.getRateValueTextField().getDocument()) {

			float defaultValue = 0.015498f;
			String str = field.getText().trim();
			Float value = NumberUtils.toFloat(str, defaultValue);
			if(value == defaultValue) {
				field.setBackground(Color.ORANGE);			
			} else {
				field.setBackground(Color.WHITE);	
				sensorsPage.getRateValueLabel().setText("Current threshold : " + value);
				currentThresholdValue = value;

			}
		}
	}

	private void startStopSensorSignal(boolean sendSignal) {
		int[] rows =  parent.getSelectedRows();
		int filteredSize = filteredSensors.size();
		for(int i : rows) {
			if(i >= 0 && i < filteredSize) {
				SensorConfiguration sensor = filteredSensors.get(i);
				int id = sensor.getSensorConfigurationId();
				SensorSignal signal = null;
				if(sensorSignalMap.containsKey(sensor.getSensorConfigurationId())){
					signal = sensorSignalMap.get(id);
					signal.getSensorConfiguration().setCurrentThreshold(currentThresholdValue);
					signal.setSendMessage(sendSignal);
				}
				else {
					sensor.setCurrentThreshold(currentThresholdValue);
					signal = new SensorSignal(sensor);	
					signal.setSendMessage(sendSignal);		
					sensorSignalMap.put(id, signal);
					Thread thread = new Thread(signal);
					thread.start();
				}
			}
		}
		

	}

}
