package com.monitrack.mock.frame;

import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.monitrack.mock.enumeration.Page;
import com.monitrack.mock.listener.MockFrameListener;
import com.monitrack.mock.panel.MockChoicePage;
import com.monitrack.mock.panel.SensorsFactory;
import com.monitrack.mock.panel.SensorsPage;

public class MockFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private CardLayout cardLayout;
	private JPanel centerPanel;
	private JButton backToMenuButton;
	private MockFrameListener listener;
	
	public MockFrame() {
		super("Monitrack Mock");
		centerPanel = new JPanel();
		cardLayout = new CardLayout();
		listener = new MockFrameListener(this);
		
		centerPanel.setLayout(cardLayout);
		setSize(1200,700);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		centerPanel.add(new MockChoicePage(this), Page.HOME.getName());
		centerPanel.add(new SensorsPage(), Page.SENSOR_OVERVIEW.getName());
		centerPanel.add(new SensorsFactory(), Page.SENSORS_FACTORY.getName() );
		
		backToMenuButton = new JButton("Retour au menu");
		backToMenuButton.addActionListener(listener);

		this.getContentPane().add(centerPanel, BorderLayout.CENTER);		
		this.getContentPane().add(backToMenuButton, BorderLayout.SOUTH);
		
		setVisible(true);		
	}
	
	public void changePage(Page page) {
		cardLayout.show(centerPanel, page.getName());
	}

	public JButton getBackToMenuButton() {
		return backToMenuButton;
	}
	

}
