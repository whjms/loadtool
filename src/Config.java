/** Reads values from 'config.cfg' in the current directory when constructed.
 * @author Qasim Ali
 * Date: March 16, 2014
 */
import java.io.*;
import java.util.*;

import javax.swing.JOptionPane;

public class Config {
	private double powerConsumption = -1;
	private double alternatePowerConsumption = -1;
	private double powerFactor = -1;
	private double maxPowerConsumption = -1;
	private int units = -1;
	
	// mappings from one unit to another
	private HashMap<Double, Double> imperialMapping; // imperial -> metric
	private HashMap<Double, Double> metricMapping;   // metric -> imperial
	public Config() {
		this.readConfig();
		this.readMappings();
	}
	
	// read data from loadtool.cfg, setting properties accordingly
	private void readConfig() {
		try {
			BufferedReader in = new BufferedReader(
					new FileReader("loadtool.cfg"));
			String currLine = in.readLine();
			
			// read power consumption
			while(currLine != null && powerConsumption == -1) {
				// skip comments
				if(currLine.indexOf("//") == 0) {
					currLine = in.readLine();
				}
				else {
					powerConsumption = Double.parseDouble(currLine);
					currLine = in.readLine();
				}
			}
			if(powerConsumption == -1) {
				in.close();
				throw new Exception("power consumption value not found.");
			}
			
			// read alternate power consumption
			while(currLine != null && alternatePowerConsumption == -1) {
				// skip comments
				if(currLine.indexOf("//") == 0) {
					currLine = in.readLine();
				}
				else {
					alternatePowerConsumption = Double.parseDouble(currLine);
					currLine = in.readLine();
				}
			}
			if(alternatePowerConsumption == -1) {
				in.close();
				throw new Exception("alternate power consumption value not found.");
			}
			
			// read power factor
			while(currLine != null && powerFactor == -1) {
				// skip comments
				if(currLine.indexOf("//") == 0) {
					currLine = in.readLine();
				}
				else {
					powerFactor = Double.parseDouble(currLine);
					currLine = in.readLine();
				}
			}
			if(powerFactor == -1) {
				in.close();
				throw new Exception("power factor not found.");
			}
			
			// read max power factor
			while(currLine != null && maxPowerConsumption == -1) {
				// skip comments
				if(currLine.indexOf("//") == 0) {
					currLine = in.readLine();
				}
				else {
					maxPowerConsumption = Double.parseDouble(currLine);
					currLine = in.readLine();
				}
			}
			if(maxPowerConsumption == -1) {
				in.close();
				throw new Exception("maximum power consumption not found.");
			}
			
			// read units
			while(currLine != null && units == -1) {
				// skip comments
				if(currLine.indexOf("//") == 0) {
					currLine = in.readLine();
				}
				else {
					String str_units = currLine;
					if(str_units.equals("metric"))
						units = UnitTools.METRIC;
					else if(str_units.equals("imperial"))
						units = UnitTools.IMPERIAL;
					currLine = in.readLine();
				}
			}
			if(units == -1) {
				in.close();
				throw new Exception("units not found.");
			}
			in.close();
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(null,
					"Error reading \'loadtool.cfg\': " + e.getMessage(),
					"Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
	}
	
	// read mappings from unitmappings.cfg, setting mappings accordingly
	private void readMappings() {
		int currLineNum = -1;
		try {
			BufferedReader in = new BufferedReader(
					new FileReader("unitmappings.cfg"));
			String currLine = in.readLine();
			Double imperial = new Double(0);
			Double metric = new Double(0);
			currLineNum = 1;
			this.imperialMapping = new HashMap<Double, Double>();
			this.metricMapping = new HashMap<Double, Double>();
			while(currLine != null) {
				// skip comments
				if(currLine.indexOf("//") == 0) {
					currLineNum++;
					currLine = in.readLine();
				}
				else {
					// split lines by whitespace
					String[] strings = currLine.split("\\s+");
					imperial = Double.valueOf(strings[0]);
					metric = Double.valueOf(strings[1]);
					
					this.imperialMapping.put(imperial, metric);
					this.metricMapping.put(metric, imperial);
					
					currLineNum++;
					currLine = in.readLine();
				}
			}
			in.close();
		}
		catch(Exception e) {
			if(currLineNum == -1) {
				JOptionPane.showMessageDialog(null,
						"Error reading \'unitmappings.cfg\': " + e.getMessage(),
						"Error", JOptionPane.ERROR_MESSAGE);
			}
			else {
				String message = "Error reading \'unitmappings.cfg\' on line " +
						currLineNum;
				JOptionPane.showMessageDialog(null, message, "Error",
						JOptionPane.ERROR_MESSAGE);
			}
			
			System.exit(1);
		}
	}
	
	public HashMap<Double, Double> getImperialMapping() {
		return this.imperialMapping;
	}
	
	public HashMap<Double, Double> getMetricMapping() {
		return this.metricMapping;
	}
	
	public double getPowerConsumption() {
		return this.powerConsumption;
	}
	
	public double getAlternatePowerConsumption() {
		return this.alternatePowerConsumption;
	}
	
	public double getPowerFactor() {
		return this.powerFactor;
	}
	
	public double getMaxPowerConsumption() {
		return this.maxPowerConsumption;
	}
	
	public int getUnits() {
		return this.units;
	}
}
