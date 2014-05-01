import java.util.*;
import java.io.*;

import javax.swing.JOptionPane;
/**
 * Maintains a list of parts, separated by categories. Also manages loading of
 * parts.
 * @author Qasim Ali
 * Date: August 26, 2013
 */
public class PartsList {
	// list of item classes
	private ArrayList<String> itemTypes;
	// key-value pair of item type and all items of said type
	private HashMap<String, ArrayList<Item>> parts;
	// units to use for items
	private LinkedHashMap<Item, Integer> quantities;
	
	/**
	 * Create the list, loading and initializing the list of parts at the same
	 * time.
	 */
	public PartsList(ArrayList<String> types) {
		this.itemTypes = types;
		this.parts = new HashMap<String, ArrayList<Item>>(types.size());
		this.quantities = new LinkedHashMap<Item, Integer>();
		loadItems();
	}
	
	/**
	 * Loads and initializes all items from their files.
	 */
	private void loadItems() {
		for(String type : this.itemTypes) {
			this.loadType(type);
		}
	}
	
	/**
	 * Load the items for a given type from 'type'.parts
	 * @param type	the item type to load
	 */
	private void loadType(String type) {
		try {
			BufferedReader br = new BufferedReader(
					new FileReader("./" + type + ".parts"));
			ArrayList<Item> tempItems = new ArrayList<Item>();
			String currLine = br.readLine();
			int currLineNumber = 1;
			
			/* go through every line, parse it (if possible), and initialize
			 * a corresponding Item
			 */
			while(currLine != null) {
				// the current line is a comment/contains only blanks, skip it
				if(currLine.indexOf("//") == 0 || 
						currLine.replaceAll("\\s", "").length() == 0) {
					currLine = br.readLine();
					currLineNumber++;
					continue;
				}
				// the current line isn't a comment/blank, so try parsing it
				else {
					// read the Item's properties
					String delim = "::";
					String[] params = currLine.split(delim); 
					try {
						String name = params[0];
						double load = Double.parseDouble(params[1]);
						
						// add the item to the temporary list
						// if there are three parameters, the last one is the 
						// part's measure
						if(params.length == 3) {
							double measure = Double.parseDouble(params[2]);
							tempItems.add(new Item(load, name, type,
									UnitTools.getCurrentUnits()));
							tempItems.get(tempItems.size() - 1).setMeasure(measure);
						}
						else
							tempItems.add(new Item(load, name, type,
									UnitTools.getCurrentUnits()));
					}
					// something went wrong while parsing this line
					catch(Exception e) {
						String msg = "Error parsing " +
							"line " + currLineNumber + " of '" + type + 
							".parts', exiting";
						JOptionPane.showMessageDialog(null, msg,
								"Error parsing parts list", 
								JOptionPane.ERROR_MESSAGE);
						System.exit(1);
					}
				}
				currLine = br.readLine();
				currLineNumber++;
			}
			br.close();
			
			/*
			 * done parsing the file and adding all the items to the temp list,
			 * now add them to the proper item list
			 */
			parts.put(type, tempItems);
		}
		// couldn't open the file, abort
		catch(FileNotFoundException e){
			String msg = "Error loading parts file '" + type + ".parts':\n" + 
				"File not found, exiting"; 
			JOptionPane.showMessageDialog(null, msg, "Critical Error",
				JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		// something messed up happened while the file was open, exit
		catch(IOException e){
			String msg = "Error reading parts file '" + type + ".parts':\n" + 
					e.getMessage(); 
				JOptionPane.showMessageDialog(null, msg, "Critical Error",
					JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
	}
	
	/**
	 * Get the list of parts for the given type
	 * @return a list of Items
	 */
	public ArrayList<Item> getItems(String type) {
		/* Hashtable.get returns null if the given key isn't found, but there's
		 * no reason that the list would be given an incorrect type
		 */
		return this.parts.get(type);
	}
	
	public LinkedHashMap<Item, Integer> getQuantities() {
		return this.quantities;
	}
	
	/**
	 * Get a list of part types
	 * @return a list of types
	 */
	public ArrayList<String> getTypes() {
		return new ArrayList<String>(this.parts.keySet());
	}
	
	/**
	 * Add an item to the quantity list - does not increment item count
	 * @param item the item to add
	 */
	public void addItem(Item i) {
		if(!this.quantities.containsKey(i)) {
			this.quantities.put(i, 1);
		}
	}
	
	/**
	 * Remove an item from this class' list of selected parts - does not
	 * decrement part count.
	 * @param item  the name of the item to remove
	 */
	public void removeItem(Item i) {
		this.quantities.remove(i);
	}
	
	/**
	 * Set an item's quantity
	 * @param item the item whose quantity to change
	 * @param qty  the item's new quantity
	 */
	public void setQuantity(Item i, int qty) {
		this.quantities.put(i, qty);
	}
	
	/**
	 * Set the units for every part to the given one
	 */
	public void updateUnits() {
		for(String type : this.parts.keySet()) {
			for(Item part : this.parts.get(type))
				part.setUnits(UnitTools.getCurrentUnits());
		}
		for(Item i : this.quantities.keySet()) {
			i.setUnits(UnitTools.getCurrentUnits());
		}
	}
}
