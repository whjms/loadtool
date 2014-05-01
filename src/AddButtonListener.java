/**
 * Event handler for MainGui's 'Add Part' button. Gets the currently selected
 * item and adds it to MainGui's list of currently selected items.
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class AddButtonListener implements ActionListener {
	private JComboBox<Item> itemSelector;
	private MainGui gui;
	private PartTableModel model;
	
	/**
	 * Create this event handler
	 * @param item  combo box that contains list of parts for the selected type
	 * @param gui   the MainGui that created this object
	 */
	public AddButtonListener(JComboBox<Item> item, MainGui gui,
		PartTableModel tableModel) {
		this.itemSelector = item;
		this.gui = gui;
		this.model = tableModel;
	}

	// get the selected item and add it to the MainGui's list
	@Override
	public void actionPerformed(ActionEvent e) {
		this.gui.addItem((Item)this.itemSelector.getSelectedItem());
		this.model.fireTableDataChanged();
	}

}
