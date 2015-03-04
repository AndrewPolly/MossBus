package com.mossbuss.webapp.client.ui.print;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import com.mossbuss.webapp.client.dto.BusDTO;
import com.mossbuss.webapp.client.dto.DriverDTO;
import com.mossbuss.webapp.client.dto.TripSheetDTO;
import com.mossbuss.webapp.client.ui.maintenanceRecords.MaintenanceView;

@SuppressWarnings("deprecation")
public class BusGrid extends FlexTable {
	private boolean VatExempt;
	private int SelectedRow;
	private String SelectedStyle;
	private String curencySymbol;
	private ArrayList<DriverDTO> driverItems = new ArrayList<DriverDTO>();
	private ArrayList<TripSheetDTO> tripItems = new ArrayList<TripSheetDTO>();
	RowFormatter GridRowFormatter;
	private ArrayList<BusDTO> gridItems;
	NumberFormat fmt;
	NumberFormat fmtQTY;

	public BusGrid() {
		VatExempt = false;
		curencySymbol = new String("");
		fmt = NumberFormat.getFormat("#,###,###,##0.00");
		fmtQTY = NumberFormat.getFormat("#,###,###,##0.##");
		gridItems = new ArrayList<BusDTO>();
		SelectedStyle = "tableViewMosBuss";
		GridRowFormatter = new RowFormatter();
		this.setRowFormatter(GridRowFormatter);
		this.addStyleName(SelectedStyle);
		this.setCellPadding(0);
		this.setCellSpacing(0);
		this.setWidth("94%");
		this.getColumnFormatter().setWidth(0, "15px");
		this.getColumnFormatter().setWidth(1, "15px");
		this.getColumnFormatter().setWidth(2, "200px");
		// this.getColumnFormatter().setWidth(3, "400px" );
		// this.getColumnFormatter().setWidth(4, "200px" );
		// this.getColumnFormatter().setWidth(7, "15px" );

	}

	/**
	 * Native method to get a cell's element.
	 * 
	 * @param table
	 *            the table element
	 * @param row
	 *            the row of the cell
	 * @param col
	 *            the column of the cell
	 * @return the element
	 */
	private native Element getCellElement(Element table, int row, int col) /*-{
		return table.rows[row].cells[col];
	}-*/;

	/**
	 * Gets the TD element representing the specified cell unsafely (meaning
	 * that it doesn't ensure that <code>row</code> and <code>column</code> are
	 * valid).
	 * 
	 * @param row
	 *            the row of the cell to be retrieved
	 * @param column
	 *            the column of the cell to be retrieved
	 * @return the cell's TD element
	 */
	private Element getRawElement(int row, int column) {
		return getCellElement(this.getBodyElement(), row, column);
	}

	private void setCellHeader(int row, int column, String text) {
		this.prepareCell(row, column);
		Element td;
		td = getRawElement(row, column);
		internalClearCell(td, false);
		DOM.setInnerText(td, text);
		this.getRowFormatter().addStyleName(row, "Header");
	}

	public void showSelectedRow(int row) {
		if (SelectedRow != -1)
			this.getRowFormatter().removeStyleName(SelectedRow, SelectedStyle);
		this.getRowFormatter().addStyleName(row, SelectedStyle);
		SelectedRow = row;
	}

	public void clearSelectedRow() {
		if (SelectedRow != -1)
			this.getRowFormatter().removeStyleName(SelectedRow, SelectedStyle);
		SelectedRow = -1;
	}

	public void clearGrid() {
		while (this.getRowCount() > 0) {
			this.removeRow(0);
		}

	}

	public void drawGrid() {

		this.clearGrid();
		this.setCellHeader(0, 0, "Bus Name");
		this.setCellHeader(0, 1, "Trip Name");
		this.setCellHeader(0, 2, "Driver Name");
		this.setCellHeader(0, 3, "Go to Maintenance");
		this.getRowFormatter().setStyleName(0, "tableHead");
		this.getCellFormatter().getElement(0, 0).getStyle()
				.setProperty("fontSize", Integer.toString(16) + "px");
		this.getCellFormatter().getElement(0, 1).getStyle()
				.setProperty("fontSize", Integer.toString(16) + "px");
		this.getCellFormatter().getElement(0, 2).getStyle()
				.setProperty("fontSize", Integer.toString(16) + "px");
		this.getCellFormatter().getElement(0, 3).getStyle()
				.setProperty("fontSize", Integer.toString(16) + "px");
		this.setText(1, 0, " ");
		this.setText(1, 1, " ");
		this.setText(1, 2, " ");

		// this.getColumnFormatter().
		if (gridItems == null || gridItems.size() <= 0) {
			this.setText(1, 0, "There are no items to display.");
		
			// this.setText(1, 2, "");
			return;
		} else {
			int rowIndex;
			for (rowIndex = 0; rowIndex < gridItems.size(); rowIndex++) {
				this.getCellFormatter().setVerticalAlignment(rowIndex + 1, 0,
						HasVerticalAlignment.ALIGN_TOP);
				this.getCellFormatter().setVerticalAlignment(rowIndex + 1, 1,
						HasVerticalAlignment.ALIGN_TOP);
				this.getCellFormatter().setVerticalAlignment(rowIndex + 1, 2,
						HasVerticalAlignment.ALIGN_TOP);

				String ID = "" + gridItems.get(rowIndex).getID();
				if (rowIndex % 2 == 0) {
					// this.getRowFormatter().setStyleName(rowIndex+1,
					// "rowFormatBlue");
				} else {
					this.getRowFormatter().setStyleName(rowIndex + 1,
							"rowFormatOrg");
				}

				this.setText(rowIndex + 1, 0, gridItems.get(rowIndex)
						.getBusName());
				this.setText(rowIndex + 1, 1,
						getTripName(gridItems.get(rowIndex).getID()));
				this.setText(rowIndex + 1, 2,
						getDriverName(gridItems.get(rowIndex).getID()));

				Button goToMR = new Button("Maintenance");
				goToMR.setStyleName("generalButton");
				// test this final int sometime
				final int rowClickIndex = rowIndex;
				goToMR.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						// int removedIndex = stocks.indexOf(symbol);
						// stocks.remove(removedIndex);
						// stocksFlexTable.removeRow(removedIndex + 1);

						final MaintenanceView recordView = new MaintenanceView();
						recordView.setBus(gridItems.get(rowClickIndex));
						recordView.init();
						final PopupPanel pPanel = new PopupPanel();
						pPanel.add(recordView);
//						pPanel.setStyleName("gwt-DialogBox");
						pPanel.addStyleName("recordPopUpPanel");
//						pPanel.addStyleName("tableViewMosBuss");
						pPanel.show();
						recordView.getCancelButton().addClickHandler(
								new ClickHandler() {

									public void onClick(ClickEvent event) {
										pPanel.remove(recordView);
									}
								});
						
					}
				});
				this.setWidget(rowIndex + 1, 3, goToMR);

				// this.getColumnFormatter().setStyleName(0, "tableColumn");
				// this.getColumnFormatter().setStyleName(1, "tableColumn");
				// this.getColumnFormatter().setStyleName(2, "tableColumn");
				// this.getColumnFormatter().setStyleName(3, "tableColumn");
			}
		}

	}

	private String getDriverName(int driverID) {
		for (int i = 0; i < this.driverItems.size(); i++) {
			if (driverItems.get(i).getID() == driverID) {
				return driverItems.get(i).getName();
			}
		}
		return "No Bus Allocated";
	}

	private String getTripName(int tripSheetID) {
		for (int i = 0; i < this.tripItems.size(); i++) {
			if (tripItems.get(i).getID() == tripSheetID) {
				return tripItems.get(i).getTripName();
			}
		}
		return "No Trip Allocated";
	}

	public void setDriverItems(ArrayList<DriverDTO> drivers) {
		this.driverItems = new ArrayList<DriverDTO>();
		this.driverItems = drivers;
	}

	public void setTripItems(ArrayList<TripSheetDTO> trips) {
		this.tripItems = new ArrayList<TripSheetDTO>();
		this.tripItems = trips;
	}

	public void insertLineToGrid(BusDTO newBus) {
		gridItems.add(newBus);
		drawGrid();
	}

	public ArrayList<BusDTO> getGridtems() {
		return gridItems;
	}

	public void setGridItems(ArrayList<BusDTO> busses) {
		this.gridItems = busses;
		drawGrid();
	}

}
