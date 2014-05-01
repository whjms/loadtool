/**
 * A class that handles events relating to the item type selector in MainGui.
 * Updates MainGui's partSelector to show only parts relating to the currently
 * selected type
 * @author Qasim Ali
 * Date: August 26, 2013
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import java.util.*;

public class TypeSelectorListener implements ActionListener {
	private PartsList list;
	private JComboBox<Item> part;
	
	/**
	 * Create this event handler
	 * @param list  PartsList to get data from
	 * @param part  combo box that lists parts of a specific type
	 */
	public TypeSelectorListener(PartsList list, JComboBox<Item> part) {
		this.list = list;
		this.part = part;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		@SuppressWarnings("unchecked")
		JComboBox<Item> type = (JComboBox<Item>)e.getSource();
		this.part.removeAllItems();
		ArrayList<Item> items = this.list.getItems(
			(String)type.getSelectedItem());
		
		for(Item i : items) {
			this.part.addItem(i);
		}
	}
}
