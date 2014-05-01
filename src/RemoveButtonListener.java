/**
 * Event handler for the 'remove button' in MainGui. Reads the value of the 
 * selected part from the given PartTableModel and tells MainGui to remove it
 * from the list of selected items
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class RemoveButtonListener implements ActionListener {
	private PartTableModel tableModel;
	private JTable table;
	private MainGui gui;
	
	public RemoveButtonListener(PartTableModel ptm, JTable table, MainGui gui) {
		this.tableModel = ptm;
		this.table = table;
		this.gui = gui;
	}

	// find the selected item and remove it
	@Override
	public void actionPerformed(ActionEvent e) {
		int selRow = this.table.getSelectedRow();
		// getSelectedRow returns -1 if nothing's selected, make sure the user
		// has selected a row
		if(selRow != -1) {
			Item selItem = this.tableModel.getItem(selRow);
			this.gui.removeItem(selItem);
			this.tableModel.fireTableDataChanged();
		}
	}

}
