/**
 * Prompts the user for export-specific information (i.e. sheet title, power
 * factor and other variables) before exporting the data contained in the given
 * HashMap to a user-specified Excel file
 * @author Qasim Ali
 * Date: August 27, 2013
 */

import java.util.*;
import java.awt.event.*;

import javax.swing.JFileChooser;
import javax.swing.filechooser.*;
import javax.swing.JOptionPane;

import java.io.*;

import jxl.*;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.write.*;
import jxl.write.Number;

public class ExportButtonListener implements ActionListener {
	private PartsList list;
	private Config cfg;

	public ExportButtonListener(PartsList list, Config cfg) {
		this.list = list;
		this.cfg = cfg;
	}

	// ask for data and export it
	public void actionPerformed(ActionEvent e) {
		// get the title of this sheet
		String sheetTitle = JOptionPane.showInputDialog(null, 
			"Enter sheet title:");
		// if the user entered nothing/chose to cancel, stop
		if(sheetTitle == null)
			return;
		
		// get the throughput
		String stringThroughput = JOptionPane.showInputDialog(
			"Enter throughput (" +
			UnitTools.getThroughputSymbol() + "):");
		// stop if the user clicked 'cancel' on the input dialog
		if(stringThroughput == null)
			return;
		// validate user input
		int throughput = 0;
		while(true) {
			try {
				throughput = Integer.parseInt(stringThroughput);
				break;
			}
			catch(NumberFormatException ex) {
				JOptionPane.showMessageDialog(null, "'" + stringThroughput + 
					"' is not a valid integer.", "Error", 
					JOptionPane.ERROR_MESSAGE);
				stringThroughput = JOptionPane.showInputDialog(
						"Enter throughput (kg/h):");
			}
		}
		
		// get the filename to save to
		JOptionPane.showMessageDialog(null, "Select a file to save to.");
		JFileChooser fc = new JFileChooser();
		// initialize file selector with the sheet title
		fc.setSelectedFile(new File(sheetTitle + ".xls"));
		fc.setFileFilter(new FileNameExtensionFilter("Excel 97/2000/XP/2003 " +
			"spreadsheet (.xls)", "xls"));
		int retVal = fc.showSaveDialog(null);
		// stop if the user didn't select a file
		if(retVal != JFileChooser.APPROVE_OPTION)
			return;
		// add the .xls extension if it's not in the filename
		String fileName = fc.getSelectedFile().getPath();
		if(fileName.lastIndexOf(".xls") != fileName.length() - 4)
			fileName += ".xls";
		
		// use this so that we can still print the load in lb/hr in the
		// spreadsheet
		double convertedThroughput = throughput;
		// calc. running load, converting throughput to kg/hr if needed
		if(UnitTools.getCurrentUnits() == UnitTools.IMPERIAL)
			convertedThroughput /= 2.2;
		
		double runningLoad =
				Math.ceil((this.cfg.getPowerConsumption() * convertedThroughput) /
						  cfg.getPowerFactor());
		double alternateRunningLoad = 
				Math.ceil((this.cfg.getAlternatePowerConsumption() * convertedThroughput) /
						  cfg.getPowerFactor());
		
		double maxRunningLoad =
				Math.ceil((this.cfg.getMaxPowerConsumption() * convertedThroughput) /
						  cfg.getPowerFactor());
		
		// write out the spreadsheet
		try {
			File spreadsheetFile = new File(fileName);
			WritableWorkbook wb = Workbook.createWorkbook(spreadsheetFile);
			WritableSheet sheet = wb.createSheet("Sheet 1", 0);
			
			// format for headers
			WritableFont boldFont = new WritableFont(WritableFont.ARIAL,
				10, WritableFont.BOLD);
			WritableCellFormat boldFormat = new WritableCellFormat(boldFont);
			
			// format for cells with borders
			WritableCellFormat borderFormat = new WritableCellFormat();
			borderFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
			
			// format for bold cells with borders
			WritableCellFormat boldBorderFormat = new WritableCellFormat(
				boldFormat);
			boldBorderFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
			
			// format for centred, bordered cells
			WritableCellFormat centeredBorderedFormat =
					new WritableCellFormat(borderFormat);
			
			// centered, non-bordered cells
			WritableCellFormat centeredFormat =
					new WritableCellFormat();
			centeredFormat.setAlignment(Alignment.CENTRE);
			centeredBorderedFormat.setAlignment(Alignment.CENTRE);
			
			// small font
			WritableCellFormat smallFormat =
					new WritableCellFormat();
			WritableFont smallFont = new WritableFont(WritableFont.ARIAL, 8);
			smallFormat.setFont(smallFont);
			
			// headers
			sheet.addCell(new Label(0, 0, sheetTitle, boldBorderFormat));
			sheet.addCell(new Label(0, 1, "Item", boldBorderFormat));
			sheet.addCell(new Label(1, 1, "Qty", boldBorderFormat));
			sheet.addCell(new Label(2, 1, "Connected Load (KVA)",
					boldBorderFormat));

			// add data for each item to the sheet
			Item currItem = null;
			LinkedHashMap<Item, Integer> quantities = this.list.getQuantities();
			int numItems = quantities.keySet().size();
			for(int i = 0; i < numItems; i++) {
				currItem = (Item)quantities.keySet().toArray()[i];
				// name of this item
				sheet.addCell(new Label(0, i + 2, currItem.getName(),
					borderFormat));
				// quantity of this item
				sheet.addCell(new Number(1, i + 2,
					quantities.get(currItem), centeredBorderedFormat));
				// calculated load of the item
				sheet.addCell(new Number(2, i + 2, quantities.get(currItem)
					* currItem.getLoad(), centeredBorderedFormat));
			}
			
			// add label and formula for total
			sheet.addCell(new Label(0, numItems + 3,
				"Total Connected Load (KVA):"));
			// sum of cells from C3 (cell containing first item's load) to the
			// cell containing last item's load
			Formula totalSum = new Formula(2, numItems + 3,
					"SUM(C3:C" + (numItems + 2) + ")", centeredFormat);
			sheet.addCell(totalSum);
			
			// add label and cell for max load
			sheet.addCell(new Label(0, numItems + 4,
					"Maximum running load for sizing utilities (KVA):"));
			sheet.addCell(new Number(2, numItems + 4, maxRunningLoad,
					centeredFormat));
			
			// add label and cell for running load
			sheet.addCell(new Label(0, numItems + 6, "Estimated running load" +
				" for " + throughput + " " + UnitTools.getThroughputSymbol() + 
				" throughput (KVA)*:"));
			sheet.addCell(new Label(2, numItems + 6,
					runningLoad + " to " + alternateRunningLoad,
					centeredFormat));
			
			// add info labels at bottom
			sheet.addCell(new Label(0, numItems + 7,
					"*Higher outputs will decrease power consumption/Kg.",
					smallFormat));
			sheet.addCell(new Label(0, numItems + 8,
					"*Lower outputs will increase power consumption/Kg.",
					smallFormat));
			sheet.addCell(new Label(0, numItems + 9,
					"*The above estimated running load does not include air" +
					"compressor and chiller loads.",
					smallFormat));
			// auto-resize all columns
			CellView cv = null;
			for(int i = 0; i < 3; i++) {
				cv = sheet.getColumnView(i);
				cv.setAutosize(true);
				sheet.setColumnView(i, cv);
			}
			
			wb.write();
			wb.close();
			
			// alert the user
			JOptionPane.showMessageDialog(null, "List saved to '" +
					fileName + "'.");
			
			// open in the default spreadsheet editor
			java.awt.Desktop.getDesktop().open(spreadsheetFile);
		}
		catch(Exception ex) {
			// alert the user
			JOptionPane.showMessageDialog(null, "Error saving list: " + 
					ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
