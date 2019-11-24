package com.monitrack.gui.panel;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.monitrack.listener.OpeningPageListener;

public class ConfigurationPage extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private JButton goToHomePageButton;
	
	private OpeningPageListener listener;
	
	public ConfigurationPage()
	{
		super(new BorderLayout());
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Configuration des capteurs", new ConfigurationTab());
		add(tabbedPane, BorderLayout.CENTER);
		goToHomePageButton = new JButton("Accéder à la page d'accueil");
		goToHomePageButton.addActionListener(listener);
		add(goToHomePageButton, BorderLayout.SOUTH);
	}
	
}
