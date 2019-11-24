package com.monitrack.mock.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class SensorsFactory extends JPanel {

	private static final long serialVersionUID = 1L;
	private JPanel titlePanel;
	private JPanel centerPanel;

	public SensorsFactory() {
		super(new BorderLayout());
		titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		centerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel pageTitleLabel = new JLabel("Générer des capteurs");
		pageTitleLabel.setFont(new Font("Calibri", Font.BOLD, 17));
		pageTitleLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		titlePanel.add(pageTitleLabel);
		
		add(titlePanel, BorderLayout.NORTH);
		setBackground(Color.YELLOW);
	}
	
	

}
