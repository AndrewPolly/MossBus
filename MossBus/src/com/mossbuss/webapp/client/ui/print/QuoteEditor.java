package com.skyrat.w8.client.ui.sales.quotes;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.SuggestionEvent;
import com.google.gwt.user.client.ui.SuggestionHandler;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.skyrat.w8.client.GreetingService;
import com.skyrat.w8.client.GreetingServiceAsync;
import com.skyrat.w8.client.dto.StockItemDTO;
import com.skyrat.w8.client.ui.general.Panels.RichTextEditPanel;
import com.skyrat.w8.client.ui.general.Panels.SellingPriceCalculator;
import com.skyrat.w8.client.ui.stock.StockSearchPanel;
import com.google.gwt.user.client.ui.ListBox;


public class QuoteEditor extends Composite implements HasText {

	private static QuoteEditorUiBinder uiBinder = GWT.create(QuoteEditorUiBinder.class);
	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);
	private ItemGrid itemGrid;
	private StockItemDTO stockItem;
	private RichTextEditPanel editTextPanel;
	@UiField ScrollPanel scrollPanel;
	@UiField StockSearchPanel stockSearch;
	@UiField TextBox txtHeading;
	@UiField TextBox txtDeliveryTime;
	@UiField TextBox txtSpecialInstructions;
	@UiField RichTextEditPanel rtValidity;
	@UiField RichTextEditPanel rtDelivery;
	@UiField RichTextEditPanel rtGuarantee;
	@UiField RichTextEditPanel rtPayment;
	@UiField RichTextEditPanel rtInstallation;
	@UiField RichTextEditPanel rtService;
	@UiField ListBox spSelect;

	interface QuoteEditorUiBinder extends UiBinder<Widget, QuoteEditor> {
	}

	public QuoteEditor() {
		initWidget(uiBinder.createAndBindUi(this));
		itemGrid = new ItemGrid();
		scrollPanel.add(itemGrid);
		SuggestionHandler handler = new SuggestionSelectionHandler();
		stockSearch.getItemCodeText().addEventHandler(handler);
		stockSearch.getItemDescriptionText().addEventHandler(handler);
		itemGrid.drawGrid();
		itemGrid.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				int cellIndex = itemGrid.getCellForEvent(event).getCellIndex();
                int rowIndex = itemGrid.getCellForEvent(event).getRowIndex();
				doGridChange(cellIndex, rowIndex);
			}
        });
	}
	public void doGridChange(final int cellIndex, final int rowIndex){
		switch (cellIndex){
		case 0: //Move Up
				itemGrid.moveItemUp(rowIndex);
			break;
		case 1: //Move Down
				itemGrid.moveItemDown(rowIndex);
			break;
		case 2: //Do nothing
			break;
		case 3: //display Description Edit Box
			final DialogBox dialogbox = new DialogBox(false, true);
			editTextPanel = new RichTextEditPanel();
			editTextPanel.setHTML(itemGrid.getStockItems().get(rowIndex-1).getItemDescription());
			editTextPanel.getCancelButton().addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					dialogbox.hide();
				}
			});
			editTextPanel.getApplyButton().addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					itemGrid.getStockItems().get(rowIndex-1).setItemDescription(editTextPanel.getHTML());
					itemGrid.drawGrid();
					dialogbox.hide();
				}
			});
			dialogbox.setWidget(editTextPanel);
			dialogbox.center();
			break;
		case 4: //show supplier Suggestion Box
			//final DialogBox supllierDialogBox = new DialogBox(false, true);
			//final SupplierSelectPanel supplierPanel = new SupplierSelectPanel();
			//supplierPanel.getSupplierNameText().setText(itemGrid.getStockItems().get(rowIndex-1).getSupplier());
			//supplierPanel.getCancelButton().addClickHandler(new ClickHandler() {
			//	@Override
			//	public void onClick(ClickEvent event) {
			//		supllierDialogBox.hide();
			//	}
			//});
			//supplierPanel.getApplyButton().addClickHandler(new ClickHandler() {
				//@Override
				//public void onClick(ClickEvent event) {
				//	itemGrid.getStockItems().get(rowIndex-1).setSupplier(supplierPanel.getSupplierNameText().getText());
				//	itemGrid.drawGrid();
				//	supllierDialogBox.hide();
				//}
			//});
			//supllierDialogBox.setWidget(supplierPanel);
			//supllierDialogBox.center();
			//supplierPanel.setDefaultFocus();
			break;
		case 5: // show Text Box for QTY
			final TextBox inQTY = new TextBox();
			inQTY.setWidth("25px");
			inQTY.setText(String.valueOf(itemGrid.getStockItems().get(rowIndex-1).getQTY()));
			itemGrid.setWidget(rowIndex, cellIndex, inQTY);
			inQTY.addBlurHandler(new BlurHandler() {
				@Override
				public void onBlur(BlurEvent event) {
					if(isDouble(inQTY.getText())){
						itemGrid.getStockItems().get(rowIndex-1).setQTY(String.valueOf(inQTY.getText()));
						itemGrid.drawGrid();
					}
				}
			});
			inQTY.addKeyUpHandler(new KeyUpHandler(){
				@Override
				public void onKeyUp(KeyUpEvent event) {
					//check for enter
					if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
						if(isDouble(inQTY.getText())){
							itemGrid.getStockItems().get(rowIndex-1).setQTY(String.valueOf(inQTY.getText()));
							itemGrid.drawGrid();
						}
					}
					if (event.getNativeKeyCode() == KeyCodes.KEY_ESCAPE) {
						itemGrid.drawGrid();
					}
				}
			});
			inQTY.selectAll();
			inQTY.setFocus(true);
			break;
		case 6: // show Text Box for Price
			final TextBox inPrice = new TextBox();
			inPrice.setWidth("50px");
			inPrice.setText(String.valueOf(itemGrid.getStockItems().get(rowIndex-1).getSellingPrice()));
			itemGrid.setWidget(rowIndex, cellIndex, inPrice);
			inPrice.addBlurHandler(new BlurHandler() {
				@Override
				public void onBlur(BlurEvent event) {
					if(isDouble(inPrice.getText())){
						itemGrid.getStockItems().get(rowIndex-1).setSellingPrice(Double.parseDouble(inPrice.getText()));
						itemGrid.drawGrid();
					}
				}
			});
			inPrice.addKeyUpHandler(new KeyUpHandler(){
				@Override
				public void onKeyUp(KeyUpEvent event) {
					//check for enter
					if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
						if(isDouble(inPrice.getText())){
							itemGrid.getStockItems().get(rowIndex-1).setSellingPrice(Double.parseDouble(inPrice.getText()));
							itemGrid.drawGrid();
						}
					}
					if (event.getNativeKeyCode() == KeyCodes.KEY_ESCAPE) {
						itemGrid.drawGrid();
					}
				}
			});
			inPrice.selectAll();
			inPrice.setFocus(true);
			break;
		case 7: //show Price Calculator Box
			final DialogBox calcDialogBox = new DialogBox(false, true);
			final SellingPriceCalculator sellingPricePanel = new SellingPriceCalculator();
			sellingPricePanel.setItemDescription(itemGrid.getStockItems().get(rowIndex-1).getItemDescription());
			sellingPricePanel.getUnitCostTextBox().setText(itemGrid.getStockItems().get(rowIndex-1).getItemCostPrice());
			sellingPricePanel.getSellingPriceTextBox().setText(String.valueOf(itemGrid.getStockItems().get(rowIndex-1).getSellingPrice()));
			//TODO: Set ExchangeRate sellingPricePanel.getExchangeRateTextBox().setText(String.valueOf(itemGrid.getStockItems().get(rowIndex-1).get));
			sellingPricePanel.getCancelButton().addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					calcDialogBox.hide();
				}
			});
			sellingPricePanel.getApplyButton().addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					itemGrid.getStockItems().get(rowIndex-1).setSellingPrice(sellingPricePanel.getSellingPrice());
					itemGrid.drawGrid();
					calcDialogBox.hide();
				}
			});
			calcDialogBox.setText(itemGrid.getStockItems().get(rowIndex-1).getItemCode());
			calcDialogBox.setWidget(sellingPricePanel);
			calcDialogBox.center();
			sellingPricePanel.setDefaultFocus();
			break;
		}
	}
	private boolean isDouble(String text) {
		boolean isDouble = true;
		try{
			@SuppressWarnings("unused")
			double doubText = Double.parseDouble(text);
		}catch (Exception e){
			Window.alert("You can only use numbers with a decimal point");
			isDouble= false;
		}
		return isDouble;
	}
	
	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setText(String text) {
		// TODO Auto-generated method stub
		
	}
	class SuggestionSelectionHandler implements SuggestionHandler{
		@Override
		public void onSuggestionSelected(SuggestionEvent event) {
			//Get stock details
			final String selectedItemCode;
			final String selectedItemDescription;
			String suggestion = event.getSelectedSuggestion().getDisplayString();
			suggestion = suggestion.replaceAll("\\<.*?>","");
			if(suggestion.contains(" :: ")){
				int endIndex = suggestion.indexOf(" :: ");
				selectedItemCode = suggestion.substring(0, endIndex);
				selectedItemDescription = suggestion.substring(endIndex);
				greetingService.getItemStockDetails(selectedItemCode, new AsyncCallback<StockItemDTO>() {
					public void onFailure(Throwable caught) {
						Window.alert(caught.getMessage());
					}
					@Override
					public void onSuccess(StockItemDTO result) {
						//Display result
						//StockItemPanel stockItemPanel = new StockItemPanel();
						//stockItemPanel.setData(result);
						stockItem = result;
						stockItem.setQTY("0");
						stockItem.setSellingPrice(0.0);
						itemGrid.insertLineToGrid(stockItem);
						//itemGrid.drawGrid();
						//stockItemPanel = new StockItemPanel();
						//stockItemPanel.setData(result);
						//dataPanel.insert(stockItemPanel, 0);
						//stockItemList.add(result);
						//cellTable.setRowData(0, stockItemList);
						//cellTable.redraw();
						
						stockSearch.clearData();
						
					}
				});
			} else if(suggestion.contains(" :A: ")){
				int endIndex = suggestion.indexOf(" :A: ");
				selectedItemCode = suggestion.substring(0, endIndex);
				selectedItemDescription = suggestion.substring(endIndex + 5);
				StockItemDTO stockItemDTO = new StockItemDTO();
				stockItemDTO.setItemCode(selectedItemCode);
				greetingService.getAccpacItemStockDetails(stockItemDTO, new AsyncCallback<StockItemDTO>() {
					public void onFailure(Throwable caught) {
						Window.alert(caught.getMessage());
					}
					@Override
					public void onSuccess(StockItemDTO result) {
						//Display result
						//StockItemPanel stockItemPanel = new StockItemPanel();
						//stockItemPanel.setData(result);
						//dataPanel.insert(stockItemPanel, 0);
						stockItem = result;
						stockItem.setQTY("0");
						stockItem.setSellingPrice(0.0);
						itemGrid.insertLineToGrid(stockItem);
						stockSearch.clearData();
					}
				});
			}
		}
	}
}
