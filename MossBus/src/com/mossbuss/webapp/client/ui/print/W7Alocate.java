package com.skyrat.w8.client.ui.sales.quotes;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Button;
import com.skyrat.w8.client.GreetingService;
import com.skyrat.w8.client.GreetingServiceAsync;
import com.skyrat.w8.client.dto.StockItemDTO;
import com.google.gwt.user.client.ui.TextBox;
import com.skyrat.w8.client.ui.stock.StockLevels;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.AbsolutePanel;

public class W7Alocate extends Composite {

	private static W7AlocateUiBinder uiBinder = GWT.create(W7AlocateUiBinder.class);
	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);
	private String destination = "Customer has not Ordered Yet!!";
	private int orderDetailsID;
	private String itemCode;
	private Boolean stocklimit;
	
	@UiField Label itemDescription;
	@UiField Label allocationIDLbl;
	@UiField Label destinationBranch;
	@UiField ListBox fromBranch;
	@UiField Label itemSupplier;
	@UiField Button allocateButton;
	@UiField Button cancelButton;
	@UiField Label QTY;
	@UiField TextBox QTYR;
	@UiField TextBox QTYT;
	@UiField TextBox QTYO;
	@UiField Label confirmed;
	@UiField StockLevels stockLevels;
	@UiField CheckBox nullStockItem;
	@UiField Button fixButton;
	@UiField AbsolutePanel allocationsPannel;
	@UiField Label labelRS;
	@UiField Label labelTR;
	@UiField Label labelO;

	interface W7AlocateUiBinder extends UiBinder<Widget, W7Alocate> {
	}

	public W7Alocate() {
		initWidget(uiBinder.createAndBindUi(this));
		fromBranch.addItem("Select a Branch");
		stocklimit = false;
		fixButton.setVisible(false);
	}
	
	public void setAllocationDetails(StockItemDTO stockItem){
		labelRS.setText(stockItem.getQTYR());
		labelTR.setText(stockItem.getQTYT());
		labelO.setText(stockItem.getQTYO());
		allocationIDLbl.setText(String.valueOf(stockItem.getAllocationID()));
		orderDetailsID = stockItem.getAllocationID();
		itemDescription.setText(stockItem.getItemDescription());
		confirmed.setText(stockItem.getConfirmedStock());
		QTY.setText(String.valueOf(stockItem.getQTY()));
		
		try{
			if((Integer.parseInt(stockItem.getQTYR()) + Integer.parseInt(stockItem.getQTYT()) + Integer.parseInt(stockItem.getQTYO()) + Integer.parseInt(stockItem.getConfirmedStock()))>= Integer.parseInt(stockItem.getQTY())){
				stocklimit = true;
				QTYR.setStyleName("generalAlertText");
				fromBranch.setEnabled(false);
				fromBranch.setStyleName("generalAlertText");
				QTYT.setEnabled(false);
				QTYT.setStyleName("generalAlertText");
				QTYO.setEnabled(false);
				QTYO.setStyleName("generalAlertText");
				nullStockItem.setEnabled(false);
			}
			
			if(stockItem.getConfirmedStock()  != null && Integer.parseInt(stockItem.getConfirmedStock()) == Integer.parseInt(stockItem.getQTY())){
				confirmed.addStyleName("stockinLabel");
			} else if(stockItem.getConfirmedStock()  != null && Integer.parseInt(stockItem.getConfirmedStock()) < Integer.parseInt(stockItem.getQTY())){
				confirmed.addStyleName("PendingStock");
			}
			if(stockItem.getConfirmedStock() != null && Integer.parseInt(stockItem.getConfirmedStock()) > Integer.parseInt(stockItem.getQTY())){
				fixButton.setVisible(true);
			}
		}
		catch (NumberFormatException e){
			//do nothing...
		}
		//QTYR.setText(stockItem.getQTYR());
		//QTYT.setText(stockItem.getQTYR());
		//QTYO.setText(stockItem.getQTYO());
		//destinationBranch.setText(stockItem.get);
		itemSupplier.setText(stockItem.getSupplier());
		stockLevels.setData(stockItem);
	}
	public void setAllocationID(int orderDetailsID){
		this.orderDetailsID = orderDetailsID;
		updateAllocationDetails(orderDetailsID);
	}
	public void setItemCode(String itemCode){
		this.itemCode = itemCode;
		updateStockLevels();
	}
	public void updateStockLevels(){
		greetingService.getItemStockDetails(itemCode, new AsyncCallback<StockItemDTO>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error looking up Allocation details: " + caught.getMessage());
			}
			@Override
			public void onSuccess(StockItemDTO result) {
				setAllocationDetails(result);
			}
		});
	}
	public void updateAllocationDetails(int orderDetailsID){
		greetingService.getAllocationDetailsForItem(orderDetailsID, new AsyncCallback<StockItemDTO>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error looking up stock details: " + caught.getMessage());
			}
			@Override
			public void onSuccess(StockItemDTO result) {
				setAllocationDetails(result);
			}
		});
	}
	public void activateAllocationsPanel(){
		allocationsPannel.setVisible(true);
	}
	public Button getAllocateButton() {
		return allocateButton;
	}

	public void setAllocateButton(Button allocateButton) {
		this.allocateButton = allocateButton;
	}

	public Button getCancelButton() {
		return cancelButton;
	}

	public void setCancelButton(Button cancelButton) {
		this.cancelButton = cancelButton;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
		destinationBranch.setText(destination);
		if(stocklimit){
			QTYR.setStyleName("generalAlertText");
			fromBranch.setEnabled(false);
			fromBranch.setStyleName("generalAlertText");
			QTYT.setEnabled(false);
			QTYT.setStyleName("generalAlertText");
			QTYO.setEnabled(false);
			QTYO.setStyleName("generalAlertText");
			nullStockItem.setEnabled(false);
		} else {
			if(stockLevels.getStockAvailableAt(destination)>0){
				QTYR.setEnabled(true);
				QTYR.setStyleName("generalOKText");
			} else {
				QTYR.setEnabled(false);
				QTYR.setStyleName("generalAlertText");
			}
			boolean noBranchStock = true;
			if(!destination.equals("JHB") && stockLevels.getStockAvailableAt("JHB")>0){
				fromBranch.addItem("JHB");
				noBranchStock = false;
			}
			if(!destination.equals("CT") && stockLevels.getStockAvailableAt("CT")>0){
				fromBranch.addItem("CT");
				noBranchStock = false;
			}
			if(!destination.equals("PE") && stockLevels.getStockAvailableAt("PE")>0){
				fromBranch.addItem("PE");
				noBranchStock = false;
			}
			if(!destination.equals("DBN") && stockLevels.getStockAvailableAt("DBN")>0){
				fromBranch.addItem("DBN");
				noBranchStock = false;
			}
			if(!destination.equals("VT") && stockLevels.getStockAvailableAt("VT")>0){
				fromBranch.addItem("VT");
				noBranchStock = false;
			}
			if(noBranchStock){
				fromBranch.setEnabled(false);
				fromBranch.setStyleName("generalAlertText");
				QTYT.setEnabled(false);
				QTYT.setStyleName("generalAlertText");
			}
			if(destination.equals("Customer has not Ordered Yet!!")){
				allocationsPannel.setVisible(false);
				QTYO.setEnabled(false);
				QTYO.setStyleName("generalAlertText");
				nullStockItem.setEnabled(false);
			}
		}
	}

	public Button getFixButton() {
		return fixButton;
	}

	public void setFixButton(Button fixButton) {
		this.fixButton = fixButton;
	}
	@UiHandler("fixButton")
	void onFixButtonClick(ClickEvent event) {
		greetingService.fixAllocation(String.valueOf(getOrderDetailsID()), new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Fix has FAILED!!!: " + caught.getMessage());
				
			}

			@Override
			public void onSuccess(Void result) {
				Window.alert("Allocation is Fixed! Reload Sales Order to see the update.");
				updateAllocationDetails(getOrderDetailsID() );
				
			}
			
		});
	}

	public int getOrderDetailsID() {
		return orderDetailsID;
	}

	public void setOrderDetailsID(int orderDetailsID) {
		this.orderDetailsID = orderDetailsID;
	}
}
