package com.monitrack.main;

import javax.swing.SwingUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.monitrack.gui.frame.MonitrackFrame;

public class MonitrackMain {

	private static  final Logger log = LoggerFactory.getLogger(MonitrackMain.class);

	public static void main(String[] args) {		
		/*
		 * The swing objects are not thread-safe which means that some errors can happens when we are accessing to the same data
		 * In order to prevent this from happening, we have to use a special Thread called 'SwingUtilities.invokeLater'
		 */
		SwingUtilities.invokeLater(new Runnable() {
			
			@SuppressWarnings("unused")
			public void run() {
				log.info("Launching of Monitrack");
				//Displays the Graphical User Interface
				MonitrackFrame monitrack = new MonitrackFrame();
			}
		});
	}	
	
}