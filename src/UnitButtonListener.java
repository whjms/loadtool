/**
 * Listener for unit switcher radio buttons.
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JRadioButton;

public class UnitButtonListener implements ActionListener {

	private MainGui ui;
	public UnitButtonListener(MainGui ui) {
		this.ui = ui;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		JRadioButton button = (JRadioButton)e.getSource();
		if(button.getText().equals("Imperial")) {
			this.ui.setUnits(UnitTools.IMPERIAL);
		}
		else if(button.getText().equals("Metric")) {
			this.ui.setUnits(UnitTools.METRIC);
		}
	}

}
