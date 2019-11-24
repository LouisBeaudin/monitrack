package com.monitrack.gui.frame;


import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.monitrack.enumeration.Images;
import com.monitrack.gui.panel.ConfigurationPage;
import com.monitrack.gui.panel.HomePage;
import com.monitrack.gui.panel.OpeningPage;
import com.monitrack.listener.MonitrackListener;
import com.monitrack.shared.MonitrackGuiUtil;

public class MonitrackFrame extends JFrame
{
	private static final long serialVersionUID = -1L;
	
	private OpeningPage openingPage;
	private String openingPageName;
	private HomePage homePage;
	private String homePageName;
	private ConfigurationPage configurationPage;
	private String configurationPageName;
	
	//Button to access to the developer mode
	private JButton developerModeButton;
	
	//Button to access to the agent mode
	private JButton agentModeButton;
	
	//Button to configure different sensors
	private JButton configurationButton;
	
	//Button to generate some random values according to the entity selected
	private JButton superUserModeButton;

	//Button to reserve a connection
	private JButton reserveConectionButton;
	//Label to indication the remaining time before the reserved connection is released
	private JLabel timerLabel;
	
	//Button to open the .log file
	private JButton openLogFileButton;

	//Panel which contains the superUserModeButton
	private JPanel northPanel;

	private CardLayout cardLayout;
	//The center panel will keep the cardLayout
	private JPanel centerPanel;

	private MonitrackListener listener;


	public MonitrackFrame()
	{
		this.setExtendedState(Frame.MAXIMIZED_BOTH);
		listener = new MonitrackListener(this);

		northPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		northPanel.setBackground(Color.WHITE);

		developerModeButton = new JButton(Images.DEVELOPER.getIcon());
		developerModeButton.addActionListener(listener);
		developerModeButton.setToolTipText("Mode développeur");
		
		agentModeButton = new JButton(Images.MAINTENANCE.getIcon());
		agentModeButton.addActionListener(listener);
		agentModeButton.setToolTipText("Mode agent");
		
		configurationButton = new JButton(Images.CONFIGURER.getIcon());
		configurationButton.addActionListener(listener);
		configurationButton.setToolTipText("Configurer les capteurs");

		superUserModeButton = new JButton(Images.SUPER.getIcon());
		superUserModeButton.addActionListener(listener);
		superUserModeButton.setToolTipText("Générer des valeurs aléatoires");

		reserveConectionButton = new JButton(Images.INFINITE_LOOP.getIcon());
		reserveConectionButton.addActionListener(listener);
		reserveConectionButton.setToolTipText("Réserver une connexion");
		
		openLogFileButton = new JButton(Images.LOG_FILE.getIcon());
		openLogFileButton.addActionListener(listener);
		openLogFileButton.setToolTipText("Ouvrir le fichier monitrack.log");		

		timerLabel = new JLabel("");
		timerLabel.setIcon(Images.CLOCK.getIcon());
		timerLabel.setFont(new Font("Calibri", Font.PLAIN, 25));
		timerLabel.setVisible(false);

		setNorthPanel(false, false);

		cardLayout = new CardLayout();
		centerPanel = new JPanel(cardLayout);

		openingPage = new OpeningPage(this);
		homePage = new HomePage();	
		configurationPage = new ConfigurationPage();

		openingPageName = "OPENING_PAGE";
		centerPanel.add(openingPage, openingPageName);

		homePageName = "HOME_PAGE";
		centerPanel.add(homePage, homePageName);

		configurationPageName = "CONFIGURATION_PAGE";
		centerPanel.add(configurationPage,configurationPageName);

		this.setTitle("MONITRACK version " + MonitrackGuiUtil.getApplicationVersion());
		cardLayout.show(centerPanel, openingPageName);

		this.getContentPane().add(centerPanel, BorderLayout.CENTER);
		this.getContentPane().add(northPanel, BorderLayout.NORTH);
		//this.setSize(1000, 600);
		setLocationRelativeTo(null);
		this.addWindowListener(listener);
		//this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	public void changePage(String pageName) {
		cardLayout.show(centerPanel, pageName);
	}
	
	public void setNorthPanel(boolean isDeveloperModeActive, boolean isAgentModeActive)
	{
		northPanel.removeAll();
		
		if(isDeveloperModeActive)
		{
			northPanel.add(superUserModeButton);
			northPanel.add(reserveConectionButton);
			northPanel.add(openLogFileButton);
			northPanel.add(timerLabel);
		}
		else if(isAgentModeActive){
			northPanel.add(developerModeButton);
			northPanel.add(configurationButton);
		}
		else {
			northPanel.add(developerModeButton);
			northPanel.add(agentModeButton);
		}
		
		northPanel.repaint();
		northPanel.revalidate();
	}

	/**
	 * @return the homePageName
	 */
	public String getHomePageName() {
		return homePageName;
	}

	public String getConfigurationPageName() {
		return configurationPageName;
	}
	/**
	 * @return the superUserModeButton
	 */
	public JButton getSuperUserModeButton() {
		return superUserModeButton;
	}

	/**
	 * @return the reserveConnectionButton
	 */
	public JButton getReserveConnectionButton() {
		return reserveConectionButton;
	}

	/**
	 * @return the developerModeButton
	 */
	public JButton getDeveloperModeButton() {
		return developerModeButton;
	}

	/**
	 * 
	 * @return the configurationButton
	 */
	public JButton getConfigurationButton() {
		return configurationButton;
	}

	/**
	 * @return the agentModeButton
	 */
	public JButton getAgentModeButton() {
		return agentModeButton;
	}


	/**
	 * @return the openLogFileButton
	 */
	public JButton getOpenLogFileButton() {
		return openLogFileButton;
	}

	/**
	 * @return the timerLabel
	 */
	public JLabel getTimerLabel() {
		return timerLabel;
	}
	
}