/**
 * The main window for the application.
 * @author Qasim Ali
 * Date: August 27, 2013
 */
import javax.swing.*;
import java.awt.*;
import java.util.*;

import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;

@SuppressWarnings("serial")
public class MainGui extends JFrame {	
	private PartsList partList;
	// model for table
	private PartTableModel tableModel;
	// stores default values
	private Config cfg;
	
	// Components
	private JTable table;
	// containers
	private JPanel contentPane;
	private JPanel selectionBox;
	private JPanel unitsBox;
	private JPanel unitsSelectionBox;	// contains unit and box containers
	
	// comboboxes
	private JComboBox<String> typeSelector;
	private JComboBox<Item> partSelector;
	// buttons
	private JButton addButton;
	private JButton removeButton;
	private JButton exportButton;
	private JRadioButton unitsMetric;
	private JRadioButton unitsImperial;
	// labels
	private JLabel loadLabel; // total load of items
	private JLabel countLabel; // number of parts selected
	private JLabel instructionsLabel; // tell user to double-click quantity cell
	// used to group radio buttons together
	private ButtonGroup unitsGroup;
	public MainGui(PartsList list, Config cfg) {
		super("Load Estimator");
		this.partList = list;
		this.tableModel = new PartTableModel(this.partList);
		
		this.tableModel.setGui(this);
		this.initUI();
		this.cfg = cfg;
		this.setUnits(UnitTools.getCurrentUnits());
		this.updateLabels();
		this.layoutUI();
		this.registerActions();
		this.setVisible(true);
	}
	
	/**
	 * Initializes UI components
	 */
	private void initUI() {
		this.contentPane = new JPanel();
		this.selectionBox = new JPanel();
		this.unitsBox = new JPanel();
		this.unitsSelectionBox = new JPanel();
		
		// initialize type selector list with all types, sorted alphabetically
		// JComboBox doesn't accept ArrayLists as a constructor parameter, so
		// manually add items
		ArrayList<String> types = this.partList.getTypes();
		Collections.sort(types);
		this.typeSelector = new JComboBox<String>();
		for(int i = 0; i < types.size(); i++)
			typeSelector.addItem(types.get(i));
		
		// initialize part selector list with parts for the first item 
		this.partSelector = new JComboBox<Item>(
			new Vector<Item>(this.partList.getItems(
				(String)typeSelector.getSelectedItem())));
		this.addButton = new JButton("Add item");
		this.removeButton = new JButton("Remove item");
		this.exportButton = new JButton("Export list...");
		
		this.unitsImperial = new JRadioButton("Imperial");
		this.unitsMetric = new JRadioButton("Metric");
		if(UnitTools.getCurrentUnits() == UnitTools.IMPERIAL)
			this.unitsImperial.setSelected(true);
		else if(UnitTools.getCurrentUnits() == UnitTools.METRIC)
			this.unitsMetric.setSelected(true);
		
		this.unitsGroup = new ButtonGroup();
		this.unitsGroup.add(this.unitsImperial);
		this.unitsGroup.add(this.unitsMetric);
		
		this.loadLabel = new JLabel();
		this.countLabel = new JLabel();
		this.instructionsLabel = new JLabel("Double-click a quantity to change it.");
		
		this.table = new JTable(this.tableModel);
		this.table.setAutoCreateRowSorter(true);
		// center columns for load and quantity
		DefaultTableCellRenderer centerRenderer =
				new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		this.table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
		this.table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
	}
	
	/**
	 * Lays out UI components
	 */
	private void layoutUI() {
		this.setContentPane(this.contentPane);
		this.contentPane.setLayout(new BoxLayout(
			this.contentPane, BoxLayout.Y_AXIS));
		this.selectionBox.setLayout(new BoxLayout(
				this.selectionBox, BoxLayout.Y_AXIS));
		this.unitsBox.setLayout(new BoxLayout(
				this.unitsBox, BoxLayout.Y_AXIS));
		this.unitsSelectionBox.setLayout(new BoxLayout(
				this.unitsSelectionBox, BoxLayout.X_AXIS));
		
		// set empty borders for padding
		this.contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		this.loadLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.countLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.selectionBox.setBorder(
				BorderFactory.createTitledBorder("Select Part"));
		this.unitsBox.setBorder(
				BorderFactory.createTitledBorder("Units"));
		this.selectionBox.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.table.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.countLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.loadLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.removeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.exportButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.instructionsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		this.selectionBox.add(this.typeSelector);
		this.selectionBox.add(Box.createRigidArea(new Dimension(0, 5)));
		this.selectionBox.add(this.partSelector);
		this.selectionBox.add(Box.createRigidArea(new Dimension(0, 5)));
		this.selectionBox.add(this.addButton);
	
		this.unitsBox.add(this.unitsImperial);
		this.unitsBox.add(this.unitsMetric);
		
		this.unitsSelectionBox.add(this.selectionBox);
		this.unitsSelectionBox.add(this.unitsBox);
		
		this.contentPane.add(this.unitsSelectionBox);
		this.contentPane.add(Box.createRigidArea(new Dimension(0, 5)));
		this.contentPane.add(this.instructionsLabel);
		this.contentPane.add(Box.createRigidArea(new Dimension(0, 5)));
		this.contentPane.add(new JScrollPane(this.table));
		this.contentPane.add(Box.createRigidArea(new Dimension(0, 5)));
		this.contentPane.add(this.removeButton);
		this.contentPane.add(this.loadLabel);
		this.contentPane.add(this.countLabel);
		this.contentPane.add(this.exportButton);
		this.pack();
	}
	
	/**
	 * Sets up event handlers for components
	 */
	private void registerActions() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.typeSelector.addActionListener(
			new TypeSelectorListener(this.partList, this.partSelector));
		this.addButton.addActionListener(
			new AddButtonListener(this.partSelector, this, this.tableModel));
		this.removeButton.addActionListener(
			new RemoveButtonListener(this.tableModel, this.table, this));
		this.exportButton.addActionListener(
			new ExportButtonListener(this.partList, this.cfg));
		this.unitsImperial.addActionListener(
			new UnitButtonListener(this));
		this.unitsMetric.addActionListener(
			new UnitButtonListener(this));
	}
	
	/**
	 * Add an item to this class' list of selected items
	 * @param item  the item to add
	 */
	public void addItem(Item i) {
		this.partList.addItem(i);
		this.updateLabels();
	}
	
	/**
	 * Set an item's quantity
	 * @param item the item whose quantity to change
	 * @param qty  the item's new quantity
	 */
	public void setQuantity(Item i, int qty) {
		this.partList.setQuantity(i, qty);
		this.updateLabels();
	}
	
	/**
	 * Remove an item from this class' list of selected parts
	 * @param item  the name of the item to remove
	 */
	public void removeItem(Item i) {
		this.partList.removeItem(i);
		this.updateLabels();
	}
	
	/**
	 * Update the value for the total load and reload the label
	 */
	public void updateLabels() {
		double totalLoad = 0;
		int itemCount = 0;
		LinkedHashMap<Item, Integer> quantities = this.partList.getQuantities();
		for(Item i : quantities.keySet()) {
			totalLoad += i.getLoad() * quantities.get(i).intValue();
			itemCount += quantities.get(i).intValue();
		}
		this.loadLabel.setText(String.format("Total load: %1$.3f", totalLoad));
		this.countLabel.setText(itemCount + " items");
	}
	
	/**
	 * Return the list of selected items and their quantities
	 */
	public LinkedHashMap<Item, Integer> getList() {
		return this.partList.getQuantities();
	}
	
	/**
	 * Update units
	 */
	public void setUnits(int units) {
		UnitTools.setCurrentUnits(units);
		this.partList.updateUnits();
		this.tableModel.fireTableDataChanged();
		// force the combobox to update - I should find a better way to do this
		this.partSelector.setSelectedIndex(
				this.partSelector.getSelectedIndex());
		this.updateLabels();
	}
}
