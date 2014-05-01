import java.util.*;
import java.io.*;

import javax.swing.*;


/**
 * The main class. Finds the names of all .parts files and passes a list 
 * containing them to a PartsList which is then passed to the GUI
 * @author Qasim Ali
 * Date: August 26, 2013
 */
public class MainClass {
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(
	            UIManager.getSystemLookAndFeelClassName());
		}
		catch(Exception e) {
			// not sure what to do here...
		}
		
		
		ArrayList<String> types = new ArrayList<String>();
		
		/* find all .parts files and add them to the list of types for PartsList
		 * to load
		 */
		File currentDir = new File("./");
		for(File f : currentDir.listFiles()) {
			// check if the extension is .parts
			String name = f.getName();
			int idx = name.lastIndexOf(".");
			// idx = -1 for extension-free files and directories, so make sure
			// exception isn't thrown
			if(idx != -1) {
				String ext = name.substring(name.lastIndexOf("."));
				if(ext.equals(".parts")) {
					// get the basename of the file and add it
					types.add(f.getName().substring(0, idx));
				}
			}
		}
		
		if(types.size() == 0) {
			JOptionPane.showMessageDialog(null,
				"No .parts files found, exiting.");
			System.exit(1);
		}
		Config cfg = new Config();
		UnitTools.setDefaultUnits(cfg.getUnits());
		PartsList list = new PartsList(types);
		UnitTools.setMappings(cfg.getImperialMapping(), cfg.getMetricMapping());
		@SuppressWarnings("unused")
		MainGui ui = new MainGui(list, cfg);
	}
}
