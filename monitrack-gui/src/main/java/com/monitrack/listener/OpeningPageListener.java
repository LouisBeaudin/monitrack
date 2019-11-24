package com.monitrack.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import com.monitrack.gui.frame.MonitrackFrame;
import com.monitrack.gui.panel.OpeningPage;

public class OpeningPageListener implements ActionListener {

	private MonitrackFrame monitrackFrame;
	private OpeningPage openingPage;

	public OpeningPageListener(MonitrackFrame monitrackFrame, OpeningPage openingPage) 
	{
		this.monitrackFrame = monitrackFrame;
		this.openingPage = openingPage;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() instanceof JButton)
		{
			JButton clickedButton = (JButton)e.getSource();

			if(clickedButton == openingPage.getGoToHomePageButton())
			{				
				monitrackFrame.changePage(monitrackFrame.getHomePageName());
				//Updates the monitrackFrame
				monitrackFrame.setVisible(true);	
			}
		}

		
	}

}
