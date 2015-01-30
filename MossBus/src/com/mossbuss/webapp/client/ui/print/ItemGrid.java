package com.skyrat.w8.client.ui.sales.quotes;

import java.util.ArrayList;
import java.util.Collections;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.skyrat.w8.client.dto.StockItemDTO;

public class ItemGrid  extends FlexTable {
	private boolean VatExempt;
	private int SelectedRow;
	private String SelectedStyle;
	private String curencySymbol;
	RowFormatter GridRowFormatter;
	private ArrayList<StockItemDTO> stockItems;
	NumberFormat fmt;
	NumberFormat fmtQTY;
	

	public ItemGrid(){
		VatExempt = false;
		curencySymbol = new String("");
		fmt = NumberFormat.getFormat("#,###,###,##0.00");
		fmtQTY = NumberFormat.getFormat("#,###,###,##0.##");
		stockItems = new ArrayList<StockItemDTO>();
		SelectedStyle = "SortableGrid-selected";
		GridRowFormatter = new RowFormatter();
		this.setRowFormatter(GridRowFormatter);
		this.addStyleName("SortableGrid");
		this.setCellPadding(0);
		this.setCellSpacing(0);
		this.setWidth("100%");
		this.getColumnFormatter().setWidth(0, "15px" );
		this.getColumnFormatter().setWidth(1, "15px" );
		this.getColumnFormatter().setWidth(2, "200px" );
		this.getColumnFormatter().setWidth(3, "400px" );
		this.getColumnFormatter().setWidth(4, "200px" );
		this.getColumnFormatter().setWidth(7, "15px" );
		
	}
	/**
     * Native method to get a cell's element.
     * 
     * @param table the table element
     * @param row the row of the cell
     * @param col the column of the cell
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
     * @param row the row of the cell to be retrieved
     * @param column the column of the cell to be retrieved
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
		if(SelectedRow != -1)
			this.getRowFormatter().removeStyleName(SelectedRow, SelectedStyle);
		this.getRowFormatter().addStyleName(row, SelectedStyle);
		SelectedRow = row;
	}
	
	public void clearSelectedRow() {
		if(SelectedRow != -1)
			this.getRowFormatter().removeStyleName(SelectedRow, SelectedStyle);
		SelectedRow = -1;
	}
	
	public void clearGrid(){
         while(this.getRowCount() > 0){
        	 this.removeRow(0);
         }
         stockItems = new ArrayList<StockItemDTO>();
         drawGrid();
	}
    
	public void drawGrid() {
		//for(int rowIndex = 0; rowIndex < this.getRowCount(); rowIndex++)
		//	this.removeRow(rowIndex);
		this.clear();
		this.setCellHeader(0, 2, "Item Code");
	    this.setCellHeader(0, 3, "Item Description");
	    this.setCellHeader(0, 4, "Item Supplier");
		this.setCellHeader(0, 5, "QTY");
		this.setCellHeader(0, 6, "Unit");
		this.setCellHeader(0, 7, "Calc");
		this.setCellHeader(0, 8, "Total");
		this.setCellHeader(0, 9, "");
		//this.setCellHeader(0, 9, "Del");
		
		
		if(stockItems == null || stockItems.size() <= 0){
			this.setText(1, 2, "There are no items to display.");
			this.setText(1, 3, "");
			return;
		} else {
			Double subTotal = 0.0;
			int rowIndex;
			for(rowIndex = 0; rowIndex < stockItems.size(); rowIndex++) {
				this.getCellFormatter().setVerticalAlignment(rowIndex+1, 0, HasVerticalAlignment.ALIGN_TOP);
				this.getCellFormatter().setVerticalAlignment(rowIndex+1, 1, HasVerticalAlignment.ALIGN_TOP);
				this.getCellFormatter().setVerticalAlignment(rowIndex+1, 2, HasVerticalAlignment.ALIGN_TOP);
				this.getCellFormatter().setVerticalAlignment(rowIndex+1, 3, HasVerticalAlignment.ALIGN_TOP);
				this.getCellFormatter().setVerticalAlignment(rowIndex+1, 4, HasVerticalAlignment.ALIGN_TOP);
				this.getCellFormatter().setVerticalAlignment(rowIndex+1, 5, HasVerticalAlignment.ALIGN_TOP);
				this.getCellFormatter().setVerticalAlignment(rowIndex+1, 6, HasVerticalAlignment.ALIGN_TOP);
				this.getCellFormatter().setVerticalAlignment(rowIndex+1, 7, HasVerticalAlignment.ALIGN_TOP);
				this.getCellFormatter().setVerticalAlignment(rowIndex+1, 8, HasVerticalAlignment.ALIGN_TOP);
				this.getCellFormatter().setVerticalAlignment(rowIndex+1, 9, HasVerticalAlignment.ALIGN_TOP);
				Image delImage = new Image("delete.png");
				if(rowIndex+1 <= (stockItems.size())){
					this.setWidget(rowIndex+1, 9, delImage);
					delImage.setSize("15px", "15px");
				}
				
				if(rowIndex > 0){
					//add up arrow
					Image upImage = new Image("up.png");
					upImage.setSize("15px", "15px");
					this.setWidget(rowIndex+1, 0, upImage);
				}
				if(rowIndex +1 < stockItems.size()+1){
					//add Down Arrow
					Image downImage = new Image("down.png");
					downImage.setSize("15px", "15px");
					this.setWidget(rowIndex+1, 1, downImage);
				}
				this.setHTML(rowIndex+1, 2, stockItems.get(rowIndex).getItemCode().toString());
				this.setHTML(rowIndex + 1, 3, stockItems.get(rowIndex).getItemDescription());
				this.setHTML(rowIndex + 1, 4, stockItems.get(rowIndex).getSupplier());
				Label QTYLabel = new Label(fmtQTY.format(Double.parseDouble(stockItems.get(rowIndex).getQTY())));
				QTYLabel.addStyleName("rightLabel");
				this.setWidget(rowIndex+1, 5, QTYLabel);
				Label unitPriceLabel = new Label(fmt.format(stockItems.get(rowIndex).getSellingPrice()));
				unitPriceLabel.addStyleName("rightLabel");
				this.setWidget(rowIndex+1, 6, unitPriceLabel);
				Image calcImage = new Image("edit.png");
				calcImage.setSize("15px", "15px");
				this.setWidget(rowIndex+1, 7, calcImage);
				Double lineTotal = stockItems.get(rowIndex).getSellingPrice()* Double.parseDouble(stockItems.get(rowIndex).getQTY());
				subTotal = subTotal + lineTotal;
				Label totalPriceLabel = new Label(curencySymbol+fmt.format(lineTotal));
				totalPriceLabel.addStyleName("rightLabel");
				this.setWidget(rowIndex+1, 8, totalPriceLabel);
			}
			Label totalLabel = new Label("Sub Total");
			this.setWidget(rowIndex+1, 6, totalLabel);
			totalLabel.addStyleName("rightLabel");
			Label subTotalPriceLabel = new Label(curencySymbol+fmt.format(subTotal));
			subTotalPriceLabel.addStyleName("rightLabel");
			this.setWidget(rowIndex+1, 8, subTotalPriceLabel);
			Label vatLabel = new Label("VAT");
			this.setWidget(rowIndex+2, 6, vatLabel);
			vatLabel.addStyleName("rightLabel");
			Label vatTotalLabel;
			Double vatAmount = 0.0;
			if(VatExempt){
				vatAmount = 0.0;
			} else{
				vatAmount = subTotal * 14/100;
			}
			vatTotalLabel= new Label(curencySymbol+fmt.format(vatAmount));
			vatTotalLabel.addStyleName("rightLabel");
			this.setWidget(rowIndex+2, 8, vatTotalLabel);
			
			Label finalTotalLabel = new Label("Final Total");
			this.setWidget(rowIndex+3, 6, finalTotalLabel);
			finalTotalLabel.addStyleName("rightLabel");
			Label finalTotalPriceLabel = new Label(curencySymbol+fmt.format(subTotal + vatAmount));
			finalTotalPriceLabel.addStyleName("rightLabel");
			this.setWidget(rowIndex+3, 8, finalTotalPriceLabel);
		}
	}
	public void setVatExempt(boolean exempt){
		VatExempt = exempt;
		drawGrid();
	}
	public Boolean isVatExempt(){
		return VatExempt;
	}
	
	public void insertLineToGrid(StockItemDTO newLine){
		stockItems.add(newLine);
		drawGrid();
	}
	public ArrayList<StockItemDTO> getStockItems(){
		return stockItems;
	}
	public void doUpdateQTY(int rowIndex, int cellIndex, String text) {
		this.setText(rowIndex, cellIndex, text);
	}
    public void moveItemUp(int index){
    	if(index > 1){
	    	//Window.alert("Up Index = " + index);
	    	Collections.swap(stockItems, index-1, index -2);
	    	drawGrid();
    	}
    }
    public void moveItemDown(int index){
    	if(index < stockItems.size()+1){
	    	//Window.alert("Down Index = " + index + "And StockItem.size = "+ stockItems.size());
	    	Collections.swap(stockItems, index-1, index);
	    	drawGrid();
    	}
    }
    public void removeItem(int index){
    	//Window.alert("Index = " + index + "And StockItem.size = "+ stockItems.size());
    	if(index <= stockItems.size()){
    		stockItems.remove(index -1);
	    	drawGrid();
    	}
    }
    public String getCurencySymbol() {
		return curencySymbol;
	}
	public void setCurencySymbol(String curencySymbol) {
		this.curencySymbol = curencySymbol;
		drawGrid();
	}
	public boolean suppliersNotSet() {
		boolean notSet = false;
		for(int i = 0; i< stockItems.size(); i++){
			if(stockItems.get(i).getSupplier()== null){
				notSet = true;
			}
		}
		return notSet;
	}
	public void lockQuote(){
		clearGrid();
		this.setText(1, 2, "WARNING:");
		this.setText(1, 3, "You do not have access to this Order.");
		
	}
}
