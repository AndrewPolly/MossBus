package com.mossbuss.webapp.client.ui.print;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.mossbuss.webapp.client.GreetingService;
import com.mossbuss.webapp.client.GreetingServiceAsync;
import com.mossbuss.webapp.client.dto.BusDTO;
import com.mossbuss.webapp.client.dto.ClientDTO;
import com.mossbuss.webapp.client.dto.DriverDTO;
import com.mossbuss.webapp.client.dto.StudentDTO;
import com.mossbuss.webapp.client.dto.TripSheetDTO;

@SuppressWarnings("deprecation")
public class DriverGrid extends FlexTable {

	private boolean VatExempt;
	private int SelectedRow;
	private String SelectedStyle;
	private String curencySymbol;
	private BusDTO bus;
	private TripSheetDTO trip;
	RowFormatter GridRowFormatter;
	private ArrayList<DriverDTO> gridItems;
	private ArrayList<BusDTO> busList = new ArrayList<BusDTO>();
	private ArrayList<TripSheetDTO> tripList = new ArrayList<TripSheetDTO>();
	NumberFormat fmt;
	NumberFormat fmtQTY;
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);

	public DriverGrid() {
		VatExempt = false;
		curencySymbol = new String("");
		fmt = NumberFormat.getFormat("#,###,###,##0.00");
		fmtQTY = NumberFormat.getFormat("#,###,###,##0.##");
		gridItems = new ArrayList<DriverDTO>();
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
		this.setCellHeader(0, 0, "ID");
		this.setCellHeader(0, 1, "Driver Name");
		this.setCellHeader(0, 2, "Trip Sheet");
		this.setCellHeader(0, 3, "Bus");
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
		this.setText(1, 3, " ");
		// this.getColumnFormatter().
		if (this.gridItems == null || this.gridItems.size() <= 0) {
			this.setText(1, 2, "There are no items to display. " + gridItems.size());
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

//				if (gridItems.get(rowIndex).getBusID() > 0) {
//					greetingService.getBus(gridItems.get(rowIndex).getBusID(),
//							new AsyncCallback<BusDTO>() {
//
//								@Override
//								public void onFailure(Throwable caught) {
//									// TODO Fix This:
//									// errorLabel.setText(caught.getMessage());
//								}
//
//								@Override
//								public void onSuccess(BusDTO result) {
//									bus = new BusDTO();
//									bus.setBusName(result.getBusName());
//									bus.setDriverID(result.getDriverID());
//									bus.setID(result.getID());
//									bus.setTripSheetID(result.getTripSheetID());
//
//								}
//							});
//					this.setText(rowIndex + 1, 2, this.bus.getBusName());
//				} else {
//					this.setText(rowIndex + 1, 2, "No Bus Allocated");
//				}
//				if (gridItems.get(rowIndex).getTripSheetID() > 0) {
//					greetingService.getTripSheet(gridItems.get(rowIndex).getTripSheetID(),
//							new AsyncCallback<TripSheetDTO>() {
//
//								@Override
//								public void onFailure(Throwable caught) {
//									// TODO Fix This:
//									// errorLabel.setText(caught.getMessage());
//								}
//
//								@Override
//								public void onSuccess(TripSheetDTO result) {
//									trip = new TripSheetDTO();
//									trip.setBusID(result.getBusID());
//									trip.setTripName(result.getTripName());
//									trip.setDriverID(result.getDriverID());
//									trip.setDriverName(result.getDriverName());
//									trip.setTripName(result.getTripName());
//								}
//							});
//					this.setText(rowIndex + 1, 3, this.trip.getTripName());
//				} else {
//					this.setText(rowIndex + 1, 3, "No Trip Allocated");
//				}
				this.setText(rowIndex + 1, 0, ID);
				this.setText(rowIndex + 1, 1, gridItems.get(rowIndex).getName());
				this.setText(rowIndex + 1, 2, getTripName(gridItems.get(rowIndex).getTripSheetID()));
				this.setText(rowIndex + 1, 3, getBusName(gridItems.get(rowIndex).getBusID()));
				
			}
		}
	}

	private String getBusName(int busID) {
		for (int i = 0; i < this.busList.size(); i++) {
			if (busList.get(i).getID() == busID) {
				return busList.get(i).getBusName();
			}
		}
		return "No Bus Allocated";
	}

	private String getTripName(int tripSheetID) {
		for (int i = 0; i < this.tripList.size(); i++) {
			if (tripList.get(i).getID() == tripSheetID) {
				return tripList.get(i).getTripName();
			}
		}
		return "No Trip Allocated";
	}
	// dont use this method choppies!
	public void insertLineToGrid(DriverDTO newDriver) {
		gridItems.add(newDriver);
		drawGrid();
	}

	public ArrayList<DriverDTO> getGridtems() {
		return gridItems;
	}

	public void setGridItems(ArrayList<DriverDTO> drivers) {
		this.gridItems = new ArrayList<DriverDTO>();
		this.gridItems = drivers;
		System.out.println("Set grid items. size = " + this.gridItems.size() + " drivers.size() = " + drivers.size());
	}
	public void setBusItems(ArrayList<BusDTO> busses) {
		this.busList = new ArrayList<BusDTO>();
		this.busList = busses;
	}
	public void setTripItems(ArrayList<TripSheetDTO> trips) {
		this.tripList = new ArrayList<TripSheetDTO>();
		this.tripList = trips;
	}

}
