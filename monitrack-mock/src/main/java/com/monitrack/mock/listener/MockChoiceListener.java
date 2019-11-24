package com.monitrack.mock.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.monitrack.mock.enumeration.Page;
import com.monitrack.mock.frame.MockFrame;
import com.monitrack.mock.panel.MockChoicePage;

public class MockChoiceListener implements ActionListener {
	
	private MockFrame parentFrame;
	private MockChoicePage mockChoicePage;

	
	public MockChoiceListener(MockFrame parentFrame, MockChoicePage mockChoicePage) {
		this.mockChoicePage = mockChoicePage;
		this.parentFrame = parentFrame;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == mockChoicePage.getSensorOverviewButton()) {
			parentFrame.changePage(Page.SENSOR_OVERVIEW);			
		}
		else if(e.getSource() == mockChoicePage.getGenerateRandomSensorButton()) {
			parentFrame.changePage(Page.SENSORS_FACTORY);			
		}

	}

}
