package com.mossbuss.webapp.client.ui.print;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import com.mossbuss.webapp.client.GreetingService;
import com.mossbuss.webapp.client.GreetingServiceAsync;
import com.mossbuss.webapp.client.dto.AttendanceDTO;
import com.mossbuss.webapp.client.dto.StudentDTO;
import com.mossbuss.webapp.client.ui.maintenanceRecords.MaintenanceView;

@SuppressWarnings("deprecation")
public class QuoteGrid extends FlexTable {
	private boolean VatExempt;
	private int SelectedRow;
	private String SelectedStyle;
	private String curencySymbol;
	RowFormatter GridRowFormatter;
	private ArrayList<StudentDTO> gridItems;
	NumberFormat fmt;
	NumberFormat fmtQTY;
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);

	public QuoteGrid() {
		VatExempt = false;
		curencySymbol = new String("");
		fmt = NumberFormat.getFormat("#,###,###,##0.00");
		fmtQTY = NumberFormat.getFormat("#,###,###,##0.##");
		gridItems = new ArrayList<StudentDTO>();
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
		this.setCellHeader(0, 1, "Student Name");
		this.setCellHeader(0, 2, "Address");
		this.getRowFormatter().setStyleName(0, "tableHead");
		this.getCellFormatter().getElement(0, 0).getStyle()
				.setProperty("fontSize", Integer.toString(16) + "px");
		this.getCellFormatter().getElement(0, 1).getStyle()
				.setProperty("fontSize", Integer.toString(16) + "px");
		this.getCellFormatter().getElement(0, 2).getStyle()
				.setProperty("fontSize", Integer.toString(16) + "px");
		this.setText(1, 0, " ");
		this.setText(1, 1, " ");
		this.setText(1, 2, " ");
		// this.getColumnFormatter().
		if (gridItems == null || gridItems.size() <= 0) {
			this.setText(1, 2, "There are no items to display.");
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

				this.setText(rowIndex + 1, 0, ID);
				this.setText(rowIndex + 1, 1, gridItems.get(rowIndex)
						.getStudentName());
				this.setText(rowIndex + 1, 2, gridItems.get(rowIndex)
						.getAddress());

				final Button attendance = new Button("Not Attended");
				attendance.setStyleName("generalButton");
				// test this final int sometime
				final int rowClickIndex = rowIndex;
				attendance.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						attendance.setText("Attended");
						attendance.setEnabled(false);
						AttendanceDTO newAtt = new AttendanceDTO();
						Date today = new Date();
						newAtt.setDateAttended(today);
						newAtt.setStudentID(gridItems.get(rowClickIndex)
								.getID());

						greetingService.saveAttendance(newAtt,
								new AsyncCallback<Void>() {

									@Override
									public void onFailure(Throwable caught) {
										// TODO Fix This:
										// errorLabel.setText(caught.getMessage());
									}

									@Override
									public void onSuccess(Void x) {

									}
								});
					}
				});
				this.setWidget(rowIndex + 1, 3, attendance);
			}
		}
	}

	public void insertLineToGrid(StudentDTO newStudent) {
		gridItems.add(newStudent);
		drawGrid();
	}

	public ArrayList<StudentDTO> getGridtems() {
		return gridItems;
	}

	public void setGridItems(ArrayList<StudentDTO> students) {
		this.gridItems = students;
		drawGrid();
	}

}
