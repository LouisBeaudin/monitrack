package com.monitrack.gui.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.JButton;
import javax.swing.JPanel;
import com.monitrack.enumeration.Images;
import com.monitrack.gui.frame.MonitrackFrame;
import com.monitrack.listener.OpeningPageListener;

public class OpeningPage extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private Image projectNameImage;
	//private Image backgroundImage;
	private OpeningPageListener listener;
	
	private JButton goToHomePageButton;
	
	public OpeningPage(MonitrackFrame parentFrame)
	{
		setLayout(new BorderLayout());
		setBackground(new Color(255,255,255));
		
		listener = new OpeningPageListener(parentFrame, this);
		
		projectNameImage = Images.PROJECT_LOGO.getImage();
		//backgroundImage = Images.BACKGROUND_HOSPITAL.getImage();
		
		goToHomePageButton = new JButton("Accéder à la page d'accueil");
		goToHomePageButton.addActionListener(listener);
		add(goToHomePageButton, BorderLayout.SOUTH);
	}
	
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		
		int projectNameWidth = projectNameImage.getWidth(null);
		int panelWidth = this.getWidth();
		int leftOffset = (panelWidth - projectNameWidth) / 2;
		
		//g2.drawImage(backgroundImage, 0, -40, null);
		g2.drawImage(projectNameImage, leftOffset, 20, null);
		this.revalidate();
	}

	/**
	 * @return the accessToServerButton
	 */
	public JButton getGoToHomePageButton() {
		return goToHomePageButton;
	}	
	
	
}
