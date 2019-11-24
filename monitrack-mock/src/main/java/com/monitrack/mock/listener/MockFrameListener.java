package com.monitrack.mock.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.monitrack.mock.enumeration.Page;
import com.monitrack.mock.frame.MockFrame;

public class MockFrameListener implements ActionListener {
	
	private MockFrame mockFrame;
	

	public MockFrameListener(MockFrame mockFrame) {
		this.mockFrame = mockFrame;
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == mockFrame.getBackToMenuButton()) {
			mockFrame.changePage(Page.HOME);
		}

	}

}
