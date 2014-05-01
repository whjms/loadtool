import java.awt.Component;
import java.awt.Color;
import java.awt.SystemColor;
import java.awt.Font;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellRenderer;


@SuppressWarnings("serial")
public class PartTableRenderer extends JTextField implements TableCellRenderer {
	public PartTableRenderer() {
		this.setBorder(null);
		this.setHorizontalAlignment(JTextField.CENTER);
		setToolTipText("Double-click to edit");
	}
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean selected, boolean hasFocus, int row, int col) {
		// display the 'quantity' column differently from other columns
		if(col == 2) {
			this.setForeground(Color.blue);
			this.setText(((Integer)value).toString());
			this.setFont(new Font("Arial",Font.BOLD, 12));
		}
		
		if(selected) {
			this.setBackground(SystemColor.textHighlight);
			this.setForeground(Color.white);
		}
		else {
			this.setBackground(SystemColor.text);
		}
		return this;
	}

}
