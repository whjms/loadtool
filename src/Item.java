/** Basic item that has a name, load, and belongs to an item class
 * @author Qasim Ali
 * Date: Aug. 26, 2013
 */

public class Item {
	private double load;
	private String name;
	private String type;
	private int units;
	private double measure;
	private boolean hasMeasure;
	
	/** Create this Item
	 * @param load  the item's load 'units' units
	 * @param name  the name of this item
	 * @param type  the class this item belongs to
	 * @param units the default units to use for this item
	 */
	public Item(double load, String name, String type, int units) {
		this.load = load;
		this.name = name;
		this.type = type;
		this.units = units;
		this.hasMeasure = false;
	}
	
	/** Set this item's load to the given measure
	 * @param measure  the value of the measure
	 */
	public void setMeasure(double measure) {
		this.measure = measure;
		this.hasMeasure = true;
	}
	
	/** Get the Item's load
	 * @return the item's load
	 */
	public double getLoad() {
		return this.load;
		//return UnitConverter.convertLoad(this.load, this.units);
	}

	/**
	 * @return the name of the part, with string placeholders
	 */
	public String getName() {
		if(this.hasMeasure) {
			String measure;
			// use whole numbers for measurements in metric
			if(UnitTools.getCurrentUnits() == UnitTools.METRIC) {
				measure = Integer.toString(
						(int)UnitTools.convertMeasure(this.measure, this.units)) +
						UnitTools.getMeasureSymbol();
			}
			else {
				measure = Double.toString(
						UnitTools.convertMeasure(this.measure, this.units)) +
						UnitTools.getMeasureSymbol();
			}
			return this.name.replace("%f", measure);
		}
		else {
			return this.name;
		}
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * Set this item's units
	 * @param units  the new set of units
	 */
	public void setUnits(int units) {
		this.units = units;
	}
	
	/**
	 * @return a string representation of this item
	 */
	@Override
	public String toString() {
		return this.getName();
	}
}
