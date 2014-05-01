import java.util.HashMap;

import javax.swing.JOptionPane;

/**
 * A class used to convert units.
 * 
 * @author Qasim Ali
 * 
 */
public class UnitTools {
	public static final int IMPERIAL = 0;
	public static final int METRIC = 1;
	// default unit set to interpret passed values as
	private static int defaultUnits;
	// current set of units to use
	private static int currentUnits;
	
	// mappings for units
	private static HashMap<Double,Double> imperialMap; // imperial -> metric
	private static HashMap<Double,Double> metricMap; // metric -> imperial
	
	/**
	 * Converts the given measure in defaultUnits to 'unit' units. Note that
	 * this actually looks up mappings for specific measurements from one unit
	 * to another. The units are not technically converted.
	 * @param measure
	 * @param unit
	 * @return the converted measure
	 */
	public static double convertMeasure(double measure, int unit) {
		try {
			if (unit == UnitTools.defaultUnits) {
				return measure;
			}
			// metric -> imperial
			else if (unit == UnitTools.IMPERIAL) {
				return UnitTools.metricMap.get(measure);
			}
			// imperial -> metric
			else if (unit == UnitTools.METRIC) {
				return UnitTools.imperialMap.get(measure);
			}
			// an unidentified unit type was requested, return a sentinel value
			else {
				return -3;
			}
		}
		// thrown when the given measure isn't found in unitmappings.cfg
		catch(NullPointerException e) {
			UnitTools.currentUnits = UnitTools.defaultUnits;
			JOptionPane.showMessageDialog(null, "Error converting the measure " +
					+ measure + UnitTools.getMeasureSymbol() +
					". Make sure the unit mapping is defined in " +
					"\'unitmappings.cfg\'.",
					"Error",
					JOptionPane.ERROR_MESSAGE);
			System.exit(1);
			return -1;
		}
	}

	/**
	 * Get the unit symbol for the measure for the current unit (i.e. " or mm)
	 * 
	 * @param unit
	 * @return a string representation of the unit symbol
	 */
	public static String getMeasureSymbol() {
		if (UnitTools.currentUnits == UnitTools.IMPERIAL)
			return "\"";
		else if (UnitTools.currentUnits == UnitTools.METRIC)
			return "mm";
		else
			return "units";
	}

	/**
	 * Get the unit symbol for the throughput in the given unit
	 * @param unit
	 * @return a string representation of the throughput unit's symbol
	 */
	public static String getThroughputSymbol() {
		if(UnitTools.currentUnits == UnitTools.IMPERIAL)
			return "lb/h";
		else if(UnitTools.currentUnits == UnitTools.METRIC)
			return "kg/h";
		else
			return " units";
	}
	/**
	 * Sets the default unit set to the given one
	 * 
	 * @param units
	 *            the new default unit set to use
	 */
	public static void setDefaultUnits(int units) {
		UnitTools.defaultUnits = units;
		UnitTools.currentUnits = UnitTools.defaultUnits;
	}

	/**
	 * @return the currentUnits
	 */
	public static int getCurrentUnits() {
		return currentUnits;
	}

	/**
	 * Set the current unit set to the given one
	 * @param currentUnits the currentUnits to set
	 */
	public static void setCurrentUnits(int currentUnits) {
		UnitTools.currentUnits = currentUnits;
	}

	/**
	 * Sets the mappings used for unit conversion to the given ones
	 * @param imperialMapping
	 * @param metricMapping
	 */
	public static void setMappings(HashMap<Double, Double> imperialMapping,
			HashMap<Double, Double> metricMapping) {
		UnitTools.imperialMap = imperialMapping;
		UnitTools.metricMap = metricMapping;
	}
	
}
