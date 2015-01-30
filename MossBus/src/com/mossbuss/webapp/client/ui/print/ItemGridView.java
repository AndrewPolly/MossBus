package com.skyrat.w8.client.ui.documents;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlexTable;
import com.skyrat.w8.client.dto.StockItemDTO;

public class ItemGridView extends Composite {

	private static ItemGridViewUiBinder uiBinder = GWT.create(ItemGridViewUiBinder.class);
	private boolean VatExempt;
	private String curencySymbol;
	private String quoteHeading;
	private ArrayList<StockItemDTO> stockItems;
	private NumberFormat fmt;
	private NumberFormat fmtQTY;
	
	@UiField FlexTable dataTable;

	interface ItemGridViewUiBinder extends UiBinder<Widget, ItemGridView> {
	}

	public ItemGridView() {
		initWidget(uiBinder.createAndBindUi(this));
		VatExempt = false;
		curencySymbol = new String("");
		fmt = NumberFormat.getFormat("#,###,###,##0.00");
		fmtQTY = NumberFormat.getFormat("#,###,###,##0.##");
		stockItems = new ArrayList<StockItemDTO>();
		dataTable.setWidth("100%");
		
		dataTable.getColumnFormatter().setWidth(0, "150px" );
		dataTable.getColumnFormatter().setWidth(1, "600px" );
		dataTable.getColumnFormatter().setWidth(2, "80px" );
		dataTable.getColumnFormatter().setWidth(3, "100px" );
		dataTable.getColumnFormatter().setWidth(4, "100px" );
	}
	public void drawGrid() {
		dataTable.removeAllRows();
		dataTable.getRowFormatter().addStyleName(0, "Header");
		dataTable.setText(0, 0, quoteHeading);
		dataTable.getFlexCellFormatter().setColSpan(0, 0, 5);
		dataTable.getRowFormatter().addStyleName(2, "Header");
		dataTable.setText(2, 0, "Part No.");
		dataTable.setText(2, 1, "Description");
		dataTable.setText(2, 2, "QTY");
		dataTable.setText(2, 3, "Unit Price");
		dataTable.setText(2, 4, "Total Price");
		if(stockItems == null || stockItems.size() <= 0){
			dataTable.setText(0, 0, "There are no items to display.");
			return;
		} else {
			Double subTotal = 0.0;
			int rowIndex;
			for(rowIndex = 0; rowIndex < stockItems.size(); rowIndex++) {
				dataTable.getCellFormatter().setVerticalAlignment(rowIndex+2, 0, HasVerticalAlignment.ALIGN_TOP);
				dataTable.getCellFormatter().setVerticalAlignment(rowIndex+2, 1, HasVerticalAlignment.ALIGN_TOP);
				dataTable.getCellFormatter().setVerticalAlignment(rowIndex+2, 2, HasVerticalAlignment.ALIGN_TOP);
				dataTable.getCellFormatter().setVerticalAlignment(rowIndex+2, 3, HasVerticalAlignment.ALIGN_TOP);
				dataTable.getCellFormatter().setVerticalAlignment(rowIndex+2, 4, HasVerticalAlignment.ALIGN_TOP);

				dataTable.setHTML(rowIndex+3, 0, stockItems.get(rowIndex).getItemCode().toString());
				dataTable.setHTML(rowIndex + 3, 1, stockItems.get(rowIndex).getItemDescription());
				Label QTYLabel = new Label(fmtQTY.format(Double.parseDouble(stockItems.get(rowIndex).getQTY())));
				QTYLabel.addStyleName("centerLabel");
				dataTable.setWidget(rowIndex+3, 2, QTYLabel);
				Label unitPriceLabel = new Label(fmt.format(stockItems.get(rowIndex).getSellingPrice()));
				unitPriceLabel.addStyleName("rightLabel");
				dataTable.setWidget(rowIndex+3, 3, unitPriceLabel);
				Double lineTotal = stockItems.get(rowIndex).getSellingPrice()* Double.parseDouble(stockItems.get(rowIndex).getQTY());
				subTotal = subTotal + lineTotal;
				Label totalPriceLabel = new Label(curencySymbol+fmt.format(lineTotal));
				totalPriceLabel.addStyleName("rightLabel");
				dataTable.setWidget(rowIndex+3, 4, totalPriceLabel);
			}
			dataTable.getFlexCellFormatter().setColSpan(rowIndex+4, 0, 3);
			dataTable.getFlexCellFormatter().setColSpan(rowIndex+5, 0, 3);
			dataTable.getFlexCellFormatter().setColSpan(rowIndex+6, 0, 3);
			Label totalLabel = new Label("Sub Total");
			dataTable.setWidget(rowIndex+4, 1, totalLabel);
			totalLabel.addStyleName("rightLabel");
			Label subTotalPriceLabel = new Label(curencySymbol+fmt.format(subTotal));
			subTotalPriceLabel.addStyleName("rightLabel");
			dataTable.setWidget(rowIndex+4, 2, subTotalPriceLabel);
			Label vatLabel = new Label("VAT");
			dataTable.setWidget(rowIndex+5, 1, vatLabel);
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
			dataTable.setWidget(rowIndex+5, 2, vatTotalLabel);
			
			Label finalTotalLabel = new Label("Final Total");
			dataTable.setWidget(rowIndex+6, 1, finalTotalLabel);
			finalTotalLabel.addStyleName("rightLabel");
			Label finalTotalPriceLabel = new Label(curencySymbol+fmt.format(subTotal + vatAmount));
			finalTotalPriceLabel.addStyleName("rightLabel");
			dataTable.setWidget(rowIndex+6, 2, finalTotalPriceLabel);
		}
	}
	
	public boolean isVatExempt() {
		return VatExempt;
	}
	public void setVatExempt(boolean vatExempt) {
		VatExempt = vatExempt;
	}
	public String getCurencySymbol() {
		return curencySymbol;
	}
	public void setCurencySymbol(String curencySymbol) {
		this.curencySymbol = curencySymbol;
	}
	public String getQuoteHeading() {
		return quoteHeading;
	}
	public void setQuoteHeading(String quoteHeading) {
		this.quoteHeading = quoteHeading;
	}
	public ArrayList<StockItemDTO> getStockItems() {
		return stockItems;
	}
	public void setStockItems(ArrayList<StockItemDTO> stockItems) {
		this.stockItems = stockItems;
	}
}
