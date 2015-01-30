package com.skyrat.w8.client.ui.sales.quotes;

import java.text.ParseException;
import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestionEvent;
import com.google.gwt.user.client.ui.SuggestionHandler;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.skyrat.w8.client.GreetingService;
import com.skyrat.w8.client.GreetingServiceAsync;
import com.skyrat.w8.client.dto.SaleSumaryDTO;
import com.skyrat.w8.client.dto.SalesOrderDTO;
import com.skyrat.w8.client.dto.StockItemDTO;
import com.skyrat.w8.client.dto.SupplierDetailsDTO;
import com.skyrat.w8.client.dto.W7QuoteDataDTO;
import com.skyrat.w8.client.dto.W8UserDTO;
import com.skyrat.w8.client.ui.general.Panels.DiscountCalculator;
import com.skyrat.w8.client.ui.general.Panels.RichTextEditPanel;
import com.skyrat.w8.client.ui.general.Panels.SalesOrderNotes;
import com.skyrat.w8.client.ui.general.Panels.SellingPriceCalculator;
import com.skyrat.w8.client.ui.stock.StockSearchPanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.skyrat.w8.client.ui.customers.CustomerDetailsPanel;
import com.skyrat.w8.client.ui.documents.QuoteIssue;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Label;
import com.skyrat.w8.client.ui.general.Panels.UserSelectPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.skyrat.w8.client.ui.general.Panels.SalesOrderViewPanel;
import com.skyrat.w8.client.ui.general.Panels.SalesOrderEntryPanel;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTMLPanel;


@SuppressWarnings("deprecation")
public class W7Quote extends Composite {

	private static W7QuoteUiBinder uiBinder = GWT.create(W7QuoteUiBinder.class);
	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);
	private W7QuoteDataDTO quoteDetails;
	
	private ItemGrid itemGrid;
	private ItemGrid optionItemGrid;
	private QuoteGrid quoteGrid;
	
	//used in grid
	private StockItemDTO stockItem;
	private RichTextEditPanel editTextPanel;
	private SuggestBox supplierNameText;
	private String desitnationBranch = "Customer has not Ordered Yet!!";
	
	private String paymentTerms;
	private String includesInstallationStr;
	private String excludesInstallationStr;
	
	private Boolean isOrdered;
	private W8UserDTO onlineUser;
	private W8UserDTO salesPerson;
	private W7Alocate allocation;
	private DiscountCalculator discountCalculator;
	MultiWordSuggestOracle supplierNameOracle;
	MultiWordSuggestOracle quoteSearchOracle;
	
	@UiField ScrollPanel scrollPanel;
	@UiField StockSearchPanel stockSearch;
	@UiField Button paymentTermsButton;
	@UiField RichTextEditPanel paymentTermsText;
	@UiField RichTextEditPanel installationTermsText;
	@UiField Button inclButton;
	@UiField Button exclButton;
	@UiField Button buttonFinalize;
	
	//private TextBox wsLoadText;
	@UiField AbsolutePanel absWS;
	@UiField CustomerDetailsPanel customerDetailsPanel;
	@UiField TextBox quoteHeadingText;
	@UiField TextBox deliveryTimeText;
	@UiField TextBox rateOfExchangeText;
	@UiField AbsolutePanel quoteDetailsTab;
	@UiField TabPanel tabPanel;
	@UiField Label wsNumberLabel;
	@UiField UserSelectPanel spSelection;
	@UiField FlowPanel soDataPanel;
	@UiField SalesOrderViewPanel soView;
	@UiField SalesOrderEntryPanel soEditPanel;
	@UiField ListBox Currency;
	@UiField CheckBox vatExempt;
	@UiField Button buttonComment;
	@UiField Button buttonDiscount;
	@UiField QuoteSearchPanel quoteSearch;
	@UiField Button printQuote;
	@UiField HTMLPanel pagepanel;
	@UiField Button exchangeButton;
	@UiField AbsolutePanel quoteOptionsTab;
	@UiField StockSearchPanel optionStockSearch;
	@UiField ScrollPanel optionsScrollPanel;

	interface W7QuoteUiBinder extends UiBinder<Widget, W7Quote> {
	}

	public W7Quote() {
		initWidget(uiBinder.createAndBindUi(this));
		paymentTerms = "COD unless account is held. All ownership of equipment or goods remains property of Wirsam Scientific until paid in full. Prepayment on ordering for all export orders.";
		includesInstallationStr="Prices quoted include installation and training.";
		excludesInstallationStr="Prices quoted exclude installation and training.";
		paymentTermsText.hideButtons();
		installationTermsText.hideButtons();
		supplierNameOracle = new MultiWordSuggestOracle();
		itemGrid = new ItemGrid();
		optionItemGrid = new ItemGrid();
		scrollPanel.add(itemGrid);
		optionsScrollPanel.add(optionItemGrid);
		SuggestionHandler handler = new SuggestionSelectionHandler();
		OptionSuggestionHandler optionHandler = new OptionSuggestionHandler();
		stockSearch.getItemCodeText().addEventHandler(handler);
		stockSearch.getItemDescriptionText().addEventHandler(handler);
		
		
		optionStockSearch.getItemCodeText().addEventHandler(optionHandler);
		optionStockSearch.getItemDescriptionText().addEventHandler(optionHandler);
		
		itemGrid.drawGrid();
		itemGrid.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				int cellIndex = itemGrid.getCellForEvent(event).getCellIndex();
                int rowIndex = itemGrid.getCellForEvent(event).getRowIndex();
				doGridChange(cellIndex, rowIndex);
			}
        });
		optionItemGrid.drawGrid();
		optionItemGrid.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				int cellIndex = optionItemGrid.getCellForEvent(event).getCellIndex();
                int rowIndex = optionItemGrid.getCellForEvent(event).getRowIndex();
				doOpionGridChange(cellIndex, rowIndex);
			}
        });
		
		quoteSearch.getSearchButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(quoteSearch.getTxtSearch().getText().length() >1){
					final DialogBox dialogbox = new DialogBox(false, true);
					Label lodLabel = new Label("Searching...");
					dialogbox.setWidget(lodLabel);
					dialogbox.center();
					dialogbox.setPopupPosition(200, 200);
					ArrayList<String> wordList = new ArrayList<String>();
					String[] words = quoteSearch.getTxtSearch().getText().split(" ");
					for (String word : words){
					   //System.out.println(word);
					   wordList.add(word);
					}
					try{ 
						String sql = "select Distinct q.Quote_Number, q.Company, q.Quote_Summary, q.Quote_Date, q.Sales_Person, q.Quote_Status,";
						sql = sql + " s.Date, q.Company, q.Contact, q.Quote_Summary, q.Quote_Date, ";
						sql = sql + " s.AccpacInvoiceNumber, s.CustOrderNo, q.Contact";
						sql = sql + " from quotedata q";
						sql = sql + " LEFT JOIN salesorders s on q.Quote_Number = s.QuoteRef";
						//String status = quoteSearch.getSearchStatus();
						sql = sql + " where "+ quoteSearch.getSearchCriteria() + " Like '%" + wordList.get(0)+"%' ";
						sql = sql + quoteSearch.getSearchStatus();
						sql = sql + quoteSearch.getSalesPerson();
						sql = sql + " ORDER BY Quote_Number DESC ";
						sql = sql + quoteSearch.getSearchLimit();
						
						greetingService.loadQuoteGridList(sql, new AsyncCallback<ArrayList<SaleSumaryDTO>>() {
	
							@Override
							public void onFailure(Throwable caught) {
								Window.alert("Error: "+ caught.getMessage());
								dialogbox.hide();
							}
	
							@Override
							public void onSuccess(ArrayList<SaleSumaryDTO> result) {
								quoteSearch.resultsPanel.clear();
								quoteGrid = new QuoteGrid();
								for(int i = 0; i < result.size(); i++){
									quoteGrid.insertLineToGrid(result.get(i));
								}
								quoteGrid.drawGrid();
								quoteGrid.addClickHandler(new ClickHandler() {
									@Override
									public void onClick(ClickEvent event) {
										int cellIndex = quoteGrid.getCellForEvent(event).getCellIndex();
						                int rowIndex = quoteGrid.getCellForEvent(event).getRowIndex();
										doQuoteGridChange(cellIndex, rowIndex);
									}
						        });
								quoteSearch.resultsPanel.add(quoteGrid);
								dialogbox.hide();
							}
						});
					} catch (NumberFormatException e) {
						clearWS();
						dialogbox.hide();
					}
				}
			}
		});
		quoteSearch.bugsButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				final DialogBox dialogbox = new DialogBox(false, true);
				Label lodLabel = new Label("Searching...");
				dialogbox.setWidget(lodLabel);
				dialogbox.center();
				dialogbox.setPopupPosition(200, 200);
				try{ 
					String sql = "select Distinct q.Quote_Number, s.QuoteRef, o.QuoteNumber, o.QTY, o.ConfirmedStock, "
							+ "s.Date, q.Company, q.Contact, q.Quote_Summary, q.Quote_Date, "
							+ "q.Sales_Person, q.Quote_Status, s.CustOrderNo "
							+ "from quotedata q "
							+ "LEFT JOIN orderdetails o on q.Quote_Number = o.QuoteNumber "
							+ "LEFT JOIN salesorders s on q.Quote_Number = s.QuoteRef "
							+ "where o.ConfirmedStock > o.QTY ORDER BY q.Quote_Number DESC Limit 50;";
					
					/*String sql = "select Distinct q.Quote_Number, o.QuoteNumber, o.QTY, o.ConfirmedStock, q.Company, q.Quote_Summary, q.Quote_Date, q.Sales_Person, q.Quote_Status "
							+ "from quotedata q LEFT JOIN orderdetails o on q.Quote_Number = o.QuoteNumber "
							+ "where o.ConfirmedStock > o.QTY ORDER BY q.Quote_Number DESC Limit 50;";*/
					
					greetingService.loadQuoteGridList(sql, new AsyncCallback<ArrayList<SaleSumaryDTO>>() {

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Error: "+ caught.getMessage());
							dialogbox.hide();
						}

						@Override
						public void onSuccess(ArrayList<SaleSumaryDTO> result) {
							quoteSearch.resultsPanel.clear();
							quoteGrid = new QuoteGrid();
							for(int i = 0; i < result.size(); i++){
								quoteGrid.insertLineToGrid(result.get(i));
							}
							quoteGrid.drawGrid();
							quoteGrid.addClickHandler(new ClickHandler() {
								@Override
								public void onClick(ClickEvent event) {
									int cellIndex = quoteGrid.getCellForEvent(event).getCellIndex();
					                int rowIndex = quoteGrid.getCellForEvent(event).getRowIndex();
									doQuoteGridChange(cellIndex, rowIndex);
								}
					        });
							quoteSearch.resultsPanel.add(quoteGrid);
							dialogbox.hide();
						}
					});

				} catch (NumberFormatException e) {
					clearWS();
					dialogbox.hide();
				}
				
			}
				
		});
		
		quoteSearch.goAdminButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				quoteSearch.loadingLabel.setText("Searching...");
				final DialogBox dialogbox = new DialogBox(false, true);
				Label lodLabel = new Label("Searching...");
				dialogbox.setWidget(lodLabel);
				dialogbox.center();
				dialogbox.setPopupPosition(200, 200);
				String soStatus = quoteSearch.selectStatus.getItemText(quoteSearch.selectStatus.getSelectedIndex());
				String soInterval = quoteSearch.intervalType.getItemText(quoteSearch.selectStatus.getSelectedIndex());
				int soInt;
				try {
					soInt = quoteSearch.intervalInteger.getValueOrThrow();
					try{ 
						String sql = "select Distinct q.Quote_Number, s.QuoteRef as QuoteNumber,s.SoStatus, s.date, q.Company, q.Quote_Summary, q.Quote_Date, "
								+ "q.Sales_Person, q.Quote_Status from quotedata q";
						sql = sql + " LEFT JOIN salesorders s on q.Quote_Number = s.QuoteRef";
						sql = sql + " Where s.SoStatus = '"+soStatus +"'";
						sql = sql + " AND date < Now() - INTERVAL "+ soInt +" "+ soInterval;
						if(quoteSearch.spSelect.getSelectedSalesPerson() != null){
							sql = sql + " AND q.Sales_Person = '"+quoteSearch.spSelect.getSelectedSalesPerson()+"'";
						}
						sql = sql + " ORDER BY q.Quote_Number Limit 50;";
						System.out.println(sql);
						greetingService.loadWSList(sql,new AsyncCallback<ArrayList<String>>() {
							@Override
							public void onFailure(Throwable caught) {
								quoteSearch.loadingLabel.setText("");
								Window.alert("Error: "+ caught.getMessage());
								dialogbox.hide();
							}

							@Override
							public void onSuccess(ArrayList<String> result) {
								quoteSearch.resultsPanel.clear();
								for(int i = 0; i < result.size(); i++){
									final Label dataLine = new Label(result.get(i));
									dataLine.setStyleName("linkLabel");
									quoteSearch.resultsPanel.add(dataLine);
									dialogbox.hide();
									dataLine.addClickHandler(new ClickHandler() {
										@Override
										public void onClick(ClickEvent event) {
											int endIndex = dataLine.getText().indexOf(" :: ");
											String wsNumberString = dataLine.getText().substring(0, endIndex);
											final DialogBox dialogbox = new DialogBox(false, true);
											Label lodLabel = new Label("Loading...");
											dialogbox.setWidget(lodLabel);
											dialogbox.center();
											dialogbox.setPopupPosition(200, 200);
											try{ 
												int wsNumber = Integer.parseInt(wsNumberString);
												greetingService.loadWS(wsNumber, onlineUser.getUserCode(), new AsyncCallback<W7QuoteDataDTO>() {
										
													@Override
													public void onFailure(Throwable caught) {
														Window.alert("Error: "+ caught.getMessage());
														dialogbox.hide();
													}
										
													@Override
													public void onSuccess(W7QuoteDataDTO result) {
														loadWS(result);
														dialogbox.hide();
													}
												});
											} catch (NumberFormatException e) {
												clearWS();
												dialogbox.hide();
											}
											
										}
									});
								}
								quoteSearch.loadingLabel.setText("");
							}
						});
				


					} catch (NumberFormatException e) {
						clearWS();
						dialogbox.hide();
					}
					
				} catch (ParseException e1) {
					Window.alert("Please enter a valid number");
					e1.printStackTrace();
				}
				
				
				
			}
				
		});
		quoteSearch.itemSearch.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				final DialogBox dialogbox = new DialogBox(false, true);
				Label lodLabel = new Label("Searching...");
				dialogbox.setWidget(lodLabel);
				dialogbox.center();
				dialogbox.setPopupPosition(200, 200);
				String itemCode = quoteSearch.itemCodeText.getText();
				String company = quoteSearch.companyText.getText();

				try{
					String sql = "select Distinct q.Quote_Number, d.ItemCode, s.QuoteRef as QuoteNumber,s.SoStatus, s.date, q.Company, q.Quote_Summary, q.Quote_Date, "
							+ "q.Sales_Person, q.Quote_Status from quotedata q";
					sql = sql + " LEFT JOIN salesorders s on q.Quote_Number = s.QuoteRef";
					sql = sql + " left join orderdetails d on q.Quote_Number = d.QuoteNumber";
					sql = sql + " where d.ItemCode = '"+ itemCode + "'";
					if(company != null && company.length() > 1){
						sql = sql + " AND company = '" + company +"'";
					}
					sql = sql + quoteSearch.getSearchStatus();
					sql = sql + " ORDER BY q.Quote_Number Limit 50;";
					System.out.println(sql);
					quoteSearch.loadingLabel.setText("Searching");
					greetingService.loadWSList(sql,new AsyncCallback<ArrayList<String>>() {
						
						@Override
						public void onFailure(Throwable caught) {
							quoteSearch.loadingLabel.setText("");
							Window.alert("Error: "+ caught.getMessage());
							dialogbox.hide();
						}

						@Override
						public void onSuccess(ArrayList<String> result) {
							quoteSearch.resultsPanel.clear();
							for(int i = 0; i < result.size(); i++){
								final Label dataLine = new Label(result.get(i));
								dataLine.setStyleName("linkLabel");
								quoteSearch.resultsPanel.add(dataLine);
								dialogbox.hide();
								dataLine.addClickHandler(new ClickHandler() {
									@Override
									public void onClick(ClickEvent event) {
										int endIndex = dataLine.getText().indexOf(" :: ");
										String wsNumberString = dataLine.getText().substring(0, endIndex);
										final DialogBox dialogbox = new DialogBox(false, true);
										Label lodLabel = new Label("Loading...");
										dialogbox.setWidget(lodLabel);
										dialogbox.center();
										dialogbox.setPopupPosition(200, 200);
										try{ 
											int wsNumber = Integer.parseInt(wsNumberString);
											greetingService.loadWS(wsNumber, onlineUser.getUserCode(), new AsyncCallback<W7QuoteDataDTO>() {
									
												@Override
												public void onFailure(Throwable caught) {
													Window.alert("Error: "+ caught.getMessage());
													dialogbox.hide();
												}
									
												@Override
												public void onSuccess(W7QuoteDataDTO result) {
													loadWS(result);
													dialogbox.hide();
												}
											});
										} catch (NumberFormatException e) {
											clearWS();
											dialogbox.hide();
										}
										
									}
								});
							}
							quoteSearch.loadingLabel.setText("");
						}
					});
			


				} catch (NumberFormatException e) {
					clearWS();
					dialogbox.hide();
				}
			}
				
		});

		/*quoteSearch.loadWSButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				final DialogBox dialogbox = new DialogBox(false, true);
				Label lodLabel = new Label("Loading...");
				dialogbox.setWidget(lodLabel);
				dialogbox.center();
				dialogbox.setPopupPosition(200, 200);
				try{ 
					int wsNumber = 1;
					quoteSearch.loadingLabel.setText("Searching");
					greetingService.loadWS(wsNumber, new AsyncCallback<W7QuoteDataDTO>() {
			
						@Override
						public void onFailure(Throwable caught) {
							quoteSearch.loadingLabel.setText("");
							Window.alert("Error: "+ caught.getMessage());
							dialogbox.hide();
						}
			
						@Override
						public void onSuccess(W7QuoteDataDTO result) {
							loadWS(result);
							dialogbox.hide();
							quoteSearch.loadingLabel.setText("Searching");
						}
					});
				} catch (NumberFormatException e) {
					clearWS();
					dialogbox.hide();
				}
				
			}
		});*/
		QuoteSearchChangeHandler quoteSearchChangeHandler = new QuoteSearchChangeHandler();
		quoteSearchOracle = (MultiWordSuggestOracle) quoteSearch.getTxtSearch().getSuggestOracle();
		QuoteSearchSelectionHandler quoteSearchSelectionHandler = new QuoteSearchSelectionHandler();
		quoteSearch.getTxtSearch().addEventHandler(quoteSearchSelectionHandler);
		quoteSearch.getTxtSearch().addKeyUpHandler(quoteSearchChangeHandler);

		Currency.addItem("Currency");
		Currency.addItem("R");
		Currency.addItem("$");
		Currency.addItem("\u20AC"); //Euro
		Currency.addItem("\u00A3"); //Pound
		Currency.addItem("\u00A5"); //Yen

		Currency.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				if(Currency.getSelectedIndex()>0){
					itemGrid.setCurencySymbol(Currency.getValue(Currency.getSelectedIndex()));
				}
			}
		});
		vatExempt.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				itemGrid.setVatExempt(vatExempt.getValue());
			}
		});
		tabPanel.selectTab(0);
		
	}
	public void doQuoteGridChange(final int cellIndex, final int rowIndex){
		
		int ws = quoteGrid.getWS(rowIndex);
		switch (cellIndex){
		case 0: 
			break;
		case 1: //Load WS
				if(ws > 0){
					final DialogBox dialogbox = new DialogBox(false, true);
					Label lodLabel = new Label("Loading...");
					dialogbox.setWidget(lodLabel);
					dialogbox.center();
					dialogbox.setPopupPosition(200, 200);
					try{ 
						
						greetingService.loadWS(ws, onlineUser.getUserCode(), new AsyncCallback<W7QuoteDataDTO>() {
				
							@Override
							public void onFailure(Throwable caught) {
								quoteSearch.loadingLabel.setText("");
								Window.alert("Error: "+ caught.getMessage());
								dialogbox.hide();
							}
				
							@Override
							public void onSuccess(W7QuoteDataDTO result) {
								loadWS(result);
								dialogbox.hide();
								quoteSearch.loadingLabel.setText("Searching");
							}
						});
					} catch (NumberFormatException e) {
						clearWS();
						dialogbox.hide();
					}
				}
			break;
		case 5:
			
			break;
		case 9 :
			if(ws > 0){
				final DialogBox dialogbox = new DialogBox(false, true);
				final SalesOrderNotes salesOrderNotes = new SalesOrderNotes();
				salesOrderNotes.setWSNumber(ws);
				salesOrderNotes.setOnlineUser(onlineUser.getUserCode());
				salesOrderNotes.getSaveButton().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						//salesOrderNotes.saveNote();
						dialogbox.hide();
						quoteGrid.drawGrid();
					}
				});
				dialogbox.setWidget(salesOrderNotes);
				dialogbox.center();
				dialogbox.setPopupPosition(200, 200);
				quoteGrid.getGridtems().get(rowIndex-1).setNotes(true);
			}
			break;
		}
		
	}
	public void doGridChange(final int cellIndex, final int rowIndex){
		switch (cellIndex){
		case 0: //Move Up
				itemGrid.moveItemUp(rowIndex);
			break;
		case 1: //Move Down
				itemGrid.moveItemDown(rowIndex);
			break;
		case 2: //Allocation
			if(true){
				final DialogBox dialogbox = new DialogBox(false, true);
				//dialogbox.setGlassEnabled(true);;
			//	dialogbox.setAnimationEnabled(true);
				allocation = new W7Alocate();
				if(getIsOrdered()){
					allocation.activateAllocationsPanel();
				}
				if(itemGrid.getStockItems().get(rowIndex-1).getAllocationID()>0){
					allocation.setAllocationID(itemGrid.getStockItems().get(rowIndex-1).getAllocationID());
				} else {
					allocation.setItemCode(itemGrid.getStockItems().get(rowIndex-1).getItemCode());
				}
				allocation.setDestination(getDesitnationBranch());
				//allocation.setAllocationDetails(itemGrid.getStockItems().get(rowIndex-1));
				//read allocations
				
				allocation.getAllocateButton().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						//update allocations
						dialogbox.hide();
					}
				});
				allocation.getCancelButton().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						dialogbox.hide();
					}
				});
				allocation.getFixButton().addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						
					}
				});
				dialogbox.setWidget(allocation);
				dialogbox.setSize("600px", "300px");
				dialogbox.center();
				dialogbox.setPopupPosition(200, 200);
			}
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
			supplierNameText = new SuggestBox(supplierNameOracle);
			supplierNameText.setStyleName("");
			supplierNameText.setWidth("250px");
			supplierNameText.setText(itemGrid.getStockItems().get(rowIndex-1).getSupplier());
			supplierNameText.getTextBox().addBlurHandler(new BlurHandler() {
				@Override
				public void onBlur(BlurEvent event) {
					if(supplierNameText.getText().length()>1){
						//this.setHTML(rowIndex + 1, 4, stockItems.get(rowIndex).getSupplier());
						//itemGrid.getStockItems().get(rowIndex-1).setSupplier(supplierNameText.getValue());
						itemGrid.drawGrid();
					}
				}
			});
			supplierNameText.addKeyUpHandler(new KeyUpHandler() {
				@Override
				public void onKeyUp(KeyUpEvent event) {
					if(supplierNameText.getText().length() > 0){
						ArrayList<String> wordList = new ArrayList<String>();
						String[] words = supplierNameText.getText().split(" ");
						for (String word : words){
						   wordList.add(word);
						}
						String sql = "select CompanyName, supplierID from suppliers where CompanyName Like '%" + wordList.get(0)+"%' ";
						if(wordList.size()>1){
							for(int i = 1; i < wordList.size(); i++){
								sql = sql + "AND CompanyName Like '%" + wordList.get(i)+"%' ";
							}
						}
						sql = sql + "Limit 0,50";
						System.out.println(sql);
						if(event.isAltKeyDown() || event.isControlKeyDown() || event.isDownArrow() || event.isLeftArrow() || event.isRightArrow() | event.isUpArrow()){
							//do nothing
						} else{
							//searchSupplierImage.setVisible(true);
							try {
								updateSupplierNameOracle(sql);
							}
							catch (Exception e) {
								System.out.println("Error: " + e.getMessage());
								e.printStackTrace();
							}
						}
					}
					
				}
			}); //codeChangeHandler
			supplierNameText.addEventHandler(new SuggestionHandler() {
				@Override
				public void onSuggestionSelected(SuggestionEvent event) {
					String suggestion = event.getSelectedSuggestion().getDisplayString();
					suggestion = suggestion.replaceAll("\\<.*?>","");
					suggestion = suggestion.replaceAll("&amp;","&");
					System.out.println("suggestion = " +suggestion);
					if(suggestion.contains(" :: ")){
						int endIndex = suggestion.indexOf(" :: ");
						suggestion = suggestion.substring(0, endIndex);
					}
					int supplierID = Integer.parseInt(suggestion);
					//Window.alert("suggestion = " +suggestion);
					greetingService.getSupplierDetails(supplierID, new AsyncCallback<SupplierDetailsDTO>() {
						public void onFailure(Throwable caught) {
							Window.alert("Error: "+caught.getMessage());
							System.out.println("Error: "+caught.getMessage());
						}
						@Override
						public void onSuccess(SupplierDetailsDTO result) {
							//Display result
						//	Window.alert("result.getCompanyName(): "+result.getCompanyName());
							itemGrid.getStockItems().get(rowIndex-1).setSupplier(result.getCompanyName());
							itemGrid.drawGrid();
						}
					});
					
				}
			});
			itemGrid.setWidget(rowIndex, cellIndex, supplierNameText);
			supplierNameText.getTextBox().setFocus(true);
			supplierNameText.getTextBox().selectAll();
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
			sellingPricePanel.getExchangeRateTextBox().setText("1");
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
		case 9: //delete
			itemGrid.removeItem(rowIndex);
		break;
		}
		
	}
	
	public void doOpionGridChange(final int cellIndex, final int rowIndex){
		switch (cellIndex){
		case 0: //Move Up
				optionItemGrid.moveItemUp(rowIndex);
			break;
		case 1: //Move Down
				optionItemGrid.moveItemDown(rowIndex);
			break;
		case 3: //display Description Edit Box
			final DialogBox dialogbox = new DialogBox(false, true);
			editTextPanel = new RichTextEditPanel();
			editTextPanel.setHTML(optionItemGrid.getStockItems().get(rowIndex-1).getItemDescription());
			editTextPanel.getCancelButton().addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					dialogbox.hide();
				}
			});
			editTextPanel.getApplyButton().addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					optionItemGrid.getStockItems().get(rowIndex-1).setItemDescription(editTextPanel.getHTML());
					optionItemGrid.drawGrid();
					dialogbox.hide();
				}
			});
			dialogbox.setWidget(editTextPanel);
			dialogbox.center();
			break;
		case 4: //show supplier Suggestion Box
			supplierNameText = new SuggestBox(supplierNameOracle);
			supplierNameText.setStyleName("");
			supplierNameText.setWidth("250px");
			supplierNameText.setText(itemGrid.getStockItems().get(rowIndex-1).getSupplier());
			supplierNameText.getTextBox().addBlurHandler(new BlurHandler() {
				@Override
				public void onBlur(BlurEvent event) {
					if(supplierNameText.getText().length()>1){
						//this.setHTML(rowIndex + 1, 4, stockItems.get(rowIndex).getSupplier());
						//itemGrid.getStockItems().get(rowIndex-1).setSupplier(supplierNameText.getValue());
						optionItemGrid.drawGrid();
					}
				}
			});
			supplierNameText.addKeyUpHandler(new KeyUpHandler() {
				@Override
				public void onKeyUp(KeyUpEvent event) {
					if(supplierNameText.getText().length() > 0){
						ArrayList<String> wordList = new ArrayList<String>();
						String[] words = supplierNameText.getText().split(" ");
						for (String word : words){
						   wordList.add(word);
						}
						String sql = "select CompanyName, supplierID from suppliers where CompanyName Like '%" + wordList.get(0)+"%' ";
						if(wordList.size()>1){
							for(int i = 1; i < wordList.size(); i++){
								sql = sql + "AND CompanyName Like '%" + wordList.get(i)+"%' ";
							}
						}
						sql = sql + "Limit 0,50";
						System.out.println(sql);
						if(event.isAltKeyDown() || event.isControlKeyDown() || event.isDownArrow() || event.isLeftArrow() || event.isRightArrow() | event.isUpArrow()){
							//do nothing
						} else{
							//searchSupplierImage.setVisible(true);
							try {
								updateSupplierNameOracle(sql);
							}
							catch (Exception e) {
								System.out.println("Error: " + e.getMessage());
								e.printStackTrace();
							}
						}
					}
					
				}
			}); //codeChangeHandler
			supplierNameText.addEventHandler(new SuggestionHandler() {
				@Override
				public void onSuggestionSelected(SuggestionEvent event) {
					String suggestion = event.getSelectedSuggestion().getDisplayString();
					suggestion = suggestion.replaceAll("\\<.*?>","");
					suggestion = suggestion.replaceAll("&amp;","&");
					System.out.println("suggestion = " +suggestion);
					if(suggestion.contains(" :: ")){
						int endIndex = suggestion.indexOf(" :: ");
						suggestion = suggestion.substring(0, endIndex);
					}
					int supplierID = Integer.parseInt(suggestion);
					//Window.alert("suggestion = " +suggestion);
					greetingService.getSupplierDetails(supplierID, new AsyncCallback<SupplierDetailsDTO>() {
						public void onFailure(Throwable caught) {
							Window.alert("Error: "+caught.getMessage());
							System.out.println("Error: "+caught.getMessage());
						}
						@Override
						public void onSuccess(SupplierDetailsDTO result) {
							//Display result
						//	Window.alert("result.getCompanyName(): "+result.getCompanyName());
							optionItemGrid.getStockItems().get(rowIndex-1).setSupplier(result.getCompanyName());
							optionItemGrid.drawGrid();
						}
					});
					
				}
			});
			optionItemGrid.setWidget(rowIndex, cellIndex, supplierNameText);
			supplierNameText.getTextBox().setFocus(true);
			supplierNameText.getTextBox().selectAll();
			break;
		case 5: // show Text Box for QTY
			final TextBox inQTY = new TextBox();
			inQTY.setWidth("25px");
			inQTY.setText(String.valueOf(optionItemGrid.getStockItems().get(rowIndex-1).getQTY()));
			optionItemGrid.setWidget(rowIndex, cellIndex, inQTY);
			inQTY.addBlurHandler(new BlurHandler() {
				@Override
				public void onBlur(BlurEvent event) {
					if(isDouble(inQTY.getText())){
						optionItemGrid.getStockItems().get(rowIndex-1).setQTY(String.valueOf(inQTY.getText()));
						optionItemGrid.drawGrid();
					}
				}
			});
			inQTY.addKeyUpHandler(new KeyUpHandler(){
				@Override
				public void onKeyUp(KeyUpEvent event) {
					//check for enter
					if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
						if(isDouble(inQTY.getText())){
							optionItemGrid.getStockItems().get(rowIndex-1).setQTY(String.valueOf(inQTY.getText()));
							optionItemGrid.drawGrid();
						}
					}
					if (event.getNativeKeyCode() == KeyCodes.KEY_ESCAPE) {
						optionItemGrid.drawGrid();
					}
				}
			});
			inQTY.selectAll();
			inQTY.setFocus(true);
			break;
		case 6: // show Text Box for Price
			final TextBox inPrice = new TextBox();
			inPrice.setWidth("50px");
			inPrice.setText(String.valueOf(optionItemGrid.getStockItems().get(rowIndex-1).getSellingPrice()));
			optionItemGrid.setWidget(rowIndex, cellIndex, inPrice);
			inPrice.addBlurHandler(new BlurHandler() {
				@Override
				public void onBlur(BlurEvent event) {
					if(isDouble(inPrice.getText())){
						optionItemGrid.getStockItems().get(rowIndex-1).setSellingPrice(Double.parseDouble(inPrice.getText()));
						optionItemGrid.drawGrid();
					}
				}
			});
			inPrice.addKeyUpHandler(new KeyUpHandler(){
				@Override
				public void onKeyUp(KeyUpEvent event) {
					//check for enter
					if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
						if(isDouble(inPrice.getText())){
							optionItemGrid.getStockItems().get(rowIndex-1).setSellingPrice(Double.parseDouble(inPrice.getText()));
							optionItemGrid.drawGrid();
						}
					}
					if (event.getNativeKeyCode() == KeyCodes.KEY_ESCAPE) {
						optionItemGrid.drawGrid();
					}
				}
			});
			inPrice.selectAll();
			inPrice.setFocus(true);
			break;
		case 7: //show Price Calculator Box
			final DialogBox calcDialogBox = new DialogBox(false, true);
			final SellingPriceCalculator sellingPricePanel = new SellingPriceCalculator();
			sellingPricePanel.setItemDescription(optionItemGrid.getStockItems().get(rowIndex-1).getItemDescription());
			sellingPricePanel.getUnitCostTextBox().setText(optionItemGrid.getStockItems().get(rowIndex-1).getItemCostPrice());
			sellingPricePanel.getSellingPriceTextBox().setText(String.valueOf(optionItemGrid.getStockItems().get(rowIndex-1).getSellingPrice()));
			//TODO: Set ExchangeRate sellingPricePanel.getExchangeRateTextBox().setText(String.valueOf(itemGrid.getStockItems().get(rowIndex-1).get));
			sellingPricePanel.getExchangeRateTextBox().setText("1");
			sellingPricePanel.getCancelButton().addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					calcDialogBox.hide();
				}
			});
			sellingPricePanel.getApplyButton().addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					optionItemGrid.getStockItems().get(rowIndex-1).setSellingPrice(sellingPricePanel.getSellingPrice());
					optionItemGrid.drawGrid();
					calcDialogBox.hide();
				}
			});
			calcDialogBox.setText(optionItemGrid.getStockItems().get(rowIndex-1).getItemCode());
			calcDialogBox.setWidget(sellingPricePanel);
			calcDialogBox.center();
			sellingPricePanel.setDefaultFocus();
			break;
		case 9: //delete
			optionItemGrid.removeItem(rowIndex);
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
						stockItem = result;
						stockItem.setQTY("0");
						stockItem.setSellingPrice(0.0);
						itemGrid.insertLineToGrid(stockItem);
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
	
	class OptionSuggestionHandler implements SuggestionHandler{
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
						stockItem = result;
						stockItem.setQTY("0");
						stockItem.setSellingPrice(0.0);
						optionItemGrid.insertLineToGrid(stockItem);
						optionStockSearch.clearData();
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
						stockItem = result;
						stockItem.setQTY("0");
						stockItem.setSellingPrice(0.0);
						optionItemGrid.insertLineToGrid(stockItem);
						stockSearch.clearData();
					}
				});
			}
		}
	}
	
	
	public void updateSupplierNameOracle(String sql) throws Exception{
		greetingService.updateSupplierNameOracle(sql, new AsyncCallback<ArrayList<String>>() {
			public void onFailure(Throwable caught) {
				
			}
			public void onSuccess(ArrayList<String> result) {
				updateSupplierNameOracle(result);
				//searchSupplierImage.setVisible(false);
				//supplierNameText.showSuggestionList();
			}
		});
	}
	public void updateSupplierNameOracle(ArrayList<String> newList){
		supplierNameOracle.clear();
		for(int i=0; i<newList.size(); i++){
			supplierNameOracle.add(newList.get(i));
		}
	}
	public void clearWS(){
		customerDetailsPanel.clearData();
		customerDetailsPanel.clearCompany();
		quoteHeadingText.setText("");
		deliveryTimeText.setText("");
		rateOfExchangeText.setText("");
		paymentTermsText.setHTML(paymentTerms);
		installationTermsText.setHTML(includesInstallationStr);
		itemGrid.clearGrid();
		wsNumberLabel.setText("");
		soDataPanel.clear();
		
	}
	public void loadWS(final W7QuoteDataDTO data){
		buttonFinalize.setEnabled(true);
		printQuote.setEnabled(true);
		setQuoteDetails(data);
		//Window.alert("WS = " + quoteDetails.getQuote_Number());
		customerDetailsPanel.setCustomerDetails(data.getCustomerID());
		quoteHeadingText.setText(data.getQuote_Summary());
		deliveryTimeText.setText(data.getDeliveryDetails());
		rateOfExchangeText.setText(data.getCurencyRate());
		paymentTermsText.setHTML(data.getPaymentTerms());
		paymentTermsText.hideButtons();
		installationTermsText.setHTML(data.getInsallmentTerms());
		installationTermsText.hideButtons();
		spSelection.setSelectedSalesPerson(data.getSales_Person());
		wsNumberLabel.setText("WS: " + String.valueOf(data.getQuote_Number()));
		itemGrid.clearGrid();
		
		itemGrid.setVatExempt(data.isVATexempt());
		itemGrid.setCurencySymbol(data.getCurrency());
		updateCurencySelection(data.getCurrency());
		soDataPanel.clear();
		//Get sales order details
		greetingService.getSalesOrderDetails(data.getQuote_Number(), new AsyncCallback<SalesOrderDTO>() {

			@Override
			public void onFailure(Throwable caught) {
				soDataPanel.clear();
				String errorString = caught.getMessage();
				if(caught.getMessage().equals("Index: 0, Size: 0")){
					setIsOrdered(false);
					errorString = "Not yet Ordered.";
					Label error = new Label(errorString);
					soDataPanel.add(error);
					Button orderButton = new Button("Order");
					orderButton.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							soDataPanel.clear();
							soEditPanel = new SalesOrderEntryPanel();
							SalesOrderDTO details = new SalesOrderDTO();
							details.setSalesPerson(quoteDetails.getSales_Person());
							details.setQuoteRef(String.valueOf(data.getQuote_Number()));
							soEditPanel.setDetails(details);
							soEditPanel.setOnlinUser(onlineUser);
							
							soEditPanel.getCaptureOrder().addClickHandler(new ClickHandler() {
								
								@Override
								public void onClick(ClickEvent event) {
									final DialogBox dialogbox = new DialogBox(false, true);
									Label lodLabel = new Label("Processing Sales Order...");
									dialogbox.setWidget(lodLabel);
									dialogbox.center();
									dialogbox.setPopupPosition(200, 200);
									greetingService.orderQuote(soEditPanel.getDetails(), new AsyncCallback<SalesOrderDTO>() {

										@Override
										public void onFailure(Throwable caught) {
											Window.alert(caught.getMessage());
											dialogbox.hide();
										}

										@Override
										public void onSuccess(SalesOrderDTO result) {
											//Load Quote:
											greetingService.loadWS(Integer.parseInt(result.getQuoteRef()), onlineUser.getUserCode(), new AsyncCallback<W7QuoteDataDTO>() {
												
												@Override
												public void onFailure(Throwable caught) {
													Window.alert("Error: "+ caught.getMessage());
													dialogbox.hide();
												}
									
												@Override
												public void onSuccess(W7QuoteDataDTO result) {
													quoteSearch.getTxtSearch().setText("");
													loadWS(result);
													dialogbox.hide();
												}
											});
											
										}
									});
									
								}
							});
							soDataPanel.add(soEditPanel);
						}
					});
					soDataPanel.add(orderButton);
				} else {
					Label error = new Label(errorString);
					soDataPanel.add(error);
					setIsOrdered(true);
				}
			}

			@Override
			public void onSuccess(SalesOrderDTO result) {
				soView.setDetails(result);
				setDesitnationBranch(result.getDeliveryOption());
				soDataPanel.clear();
				soDataPanel.add(soView);
				setIsOrdered(true);
			}
		
		});
		//Window.alert("Setting Tab to 3");
		if(data.isLocked()){
			itemGrid.lockQuote();
			buttonFinalize.setEnabled(false);
			printQuote.setEnabled(false);
			
		} else if(data.getQuoteItems()!= null && data.getQuoteItems().size()>0){
			
			for(int i = 0; i < data.getQuoteItems().size(); i++){
				itemGrid.insertLineToGrid(data.getQuoteItems().get(i));
			}
		}
		tabPanel.selectTab(3);
	}
	void clearQuoteDetails(){
		itemGrid.clearGrid();
		spSelection.setSelectedSalesPerson("None Selected");
	}
	@UiHandler("paymentTermsButton")
	void onPaymentTermsButtonClick(ClickEvent event) {
		paymentTermsText.setHTML(paymentTerms);
	}
	@UiHandler("exclButton")
	void onExclButtonClick(ClickEvent event) {
		installationTermsText.setHTML(excludesInstallationStr);
	}
	@UiHandler("inclButton")
	void onInclButtonClick(ClickEvent event) {
		installationTermsText.setHTML(includesInstallationStr);
	}
	

		
		
	
	@UiHandler("buttonFinalize")
	void onButtonFinalizeClick(ClickEvent event) {
		buttonFinalize.setEnabled(false);
		quoteDetails = new W7QuoteDataDTO();
		if(isOrdered){
			Window.alert("Unfortunately you cannot edit a sales order in W8 just yet, you can only edit and enter Quotes.");
		} else {
			try{
				//get info
				if(customerDetailsPanel.getCustomerID() > 0 ){
					quoteDetails.setCustomerID(customerDetailsPanel.getCustomerID());
				} else {
					throw new Exception("Please select a customer");
				}
				
				if(quoteHeadingText.getText().length()<2){
					throw new Exception("Please enter a heading for your Quote");
				} else {
					quoteDetails.setQuote_Summary(quoteHeadingText.getText());
				}
				
				quoteDetails.setSales_Lead_From(onlineUser.getUserCode());
				//quoteDetails.setQuote_Date(quote_Date);
				//quoteDetails.setIssue_Details(issue_Details);
				quoteDetails.setQuote_Status("Active");
				int selectedCurency = Currency.getSelectedIndex();
				if(selectedCurency == 0){
					throw new Exception ("Please select a curency");
				} else if(selectedCurency == 1){
					quoteDetails.setCurrency("R");
				} else if(selectedCurency == 2){
					quoteDetails.setCurrency("$");
				} else if(selectedCurency == 3){
					quoteDetails.setCurrency("&#8364;");
				} else if(selectedCurency == 4){
					quoteDetails.setCurrency("£");
				} else if(selectedCurency == 5){
					quoteDetails.setCurrency("¥");
				}
				if(deliveryTimeText.getText().length()<2){
					throw new Exception("Please enter delivery time details or N/A");
				} else {
					quoteDetails.setDeliveryDetails(deliveryTimeText.getText());
				}
				if(rateOfExchangeText.getText().length()<2){
					throw new Exception("Please enter the exchange rate used or N/A");
				} else {
					quoteDetails.setCurencyRate(rateOfExchangeText.getText());
				}
				if(spSelection.getSelectedSalesPerson() == null || spSelection.getSelectedSalesPerson().length() < 2){
					throw new Exception("Please select a sales Person");
				} else {
					quoteDetails.setSales_Person(spSelection.getSelectedSalesPerson());
					//Window.alert("Sales Person: " + quoteDetails.getSales_Person());
				}
				if(paymentTermsText.getHTML().length()<2){
					throw new Exception("Please enter Payment Terms time details or N/A");
				} else {
					quoteDetails.setPaymentTerms(paymentTermsText.getHTML());
				}
				if(installationTermsText.getHTML().length()<2){
					throw new Exception("Please enter installation terms details or N/A");
				} else {
					quoteDetails.setInsallmentTerms(installationTermsText.getHTML());
				}
				if(itemGrid.suppliersNotSet()){
					throw new Exception("Please make sure each item has a supplier set.");
				}
				quoteDetails.setVATexempt(itemGrid.isVatExempt());
				quoteDetails.setQuoteItems(itemGrid.getStockItems());
				//save info
				final DialogBox dialogbox = new DialogBox(false, true);
				Label lodLabel = new Label("Loading...");
				dialogbox.setWidget(lodLabel);
				dialogbox.center();
				dialogbox.setPopupPosition(200, 200);
				
				greetingService.saveWSToDB(quoteDetails, onlineUser.getUserCode(), new AsyncCallback<W7QuoteDataDTO>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Error: "+ caught.getMessage());
						dialogbox.hide();
						buttonFinalize.setEnabled(true);
					}

					@Override
					public void onSuccess(W7QuoteDataDTO result) {
						//Reload Quote
						
						loadWS(result);
						dialogbox.hide();
						buttonFinalize.setEnabled(true);
					}
				});
			
			} catch (Exception e){
				Window.alert(e.getMessage());
				buttonFinalize.setEnabled(true);
			}
		}
		
	}
	public void setOnlineUser(W8UserDTO onlineUser) {
		this.onlineUser = onlineUser;
	}
	public W8UserDTO getOnlineUser(){
		return onlineUser;
	}
	public W8UserDTO getSalesPerson(){
		String usrCode = getQuoteDetails().getSales_Person();
		greetingService.getSalesPerson(usrCode, new AsyncCallback<W8UserDTO>() {
			@Override
			public void onSuccess(W8UserDTO result) {
				setSalesPerson(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Sales Person has incomplete details!!!");
			}
		});
		return salesPerson;
	}
	public Boolean getIsOrdered() {
		return isOrdered;
	}

	public String getDesitnationBranch() {
		return desitnationBranch;
	}

	public void setDesitnationBranch(String desitnationBranch) {
		this.desitnationBranch = desitnationBranch;
	}

	@UiHandler("buttonComment")
	void onButtonCommentClick(ClickEvent event) {
		StockItemDTO comment = new StockItemDTO();
		comment.setItemCode("&nbsp;");
		comment.setSupplier("xxx");
		comment.setItemDescription("Comment");
		comment.setQTY("0");
		itemGrid.insertLineToGrid(comment);
	}
	@UiHandler("buttonDiscount")
	void onButtonDiscountClick(ClickEvent event) {
		final DialogBox dialogbox = new DialogBox(false, true);
		discountCalculator = new DiscountCalculator();
		
		discountCalculator.getCancelButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				dialogbox.hide();
			}
		});
		discountCalculator.getOkButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				StockItemDTO discount = new StockItemDTO();
				discount.setItemCode("&nbsp;");
				discount.setSupplier("xxx");
				discount.setItemDescription("Discount");
				discount.setQTY("1");
				discount.setConfirmedStock("1");
				discount.setSellingPrice(discountCalculator.getDiscount());
				//discount
				itemGrid.insertLineToGrid(discount);
				dialogbox.hide();
			}
		});
		dialogbox.setWidget(discountCalculator);
		//dialogbox.setSize("300px", "200px");
		dialogbox.center();
		dialogbox.setPopupPosition(200, 200);
	}
	class QuoteSearchChangeHandler implements KeyUpHandler {
		@Override
		public void onKeyUp(KeyUpEvent event) {
			if(quoteSearch.getSearchMode().isDown()){
				//Manual Search
				
			} else {
				//Auto Search
				if(quoteSearch.getTxtSearch().getText().length() >1){
					quoteSearch.setOnlineUserCode(onlineUser.getUserCode());
					quoteSearch.loadingLabel.setText("Searching...");
					ArrayList<String> wordList = new ArrayList<String>();
					String[] words = quoteSearch.getTxtSearch().getText().split(" ");
					for (String word : words){
					   //System.out.println(word);
					   wordList.add(word);
					}
					String sql = "select Distinct q.Quote_Number, q.Company, q.Quote_Summary, q.Quote_Date, q.Sales_Person, q.Quote_Status,";
					sql = sql + " s.AccpacInvoiceNumber, s.CustOrderNo, q.Contact";
					sql = sql + " from quotedata q";
					sql = sql + " LEFT JOIN salesorders s on q.Quote_Number = s.QuoteRef";
					//String status = quoteSearch.getSearchStatus();
					sql = sql + " where "+ quoteSearch.getSearchCriteria() + " Like '%" + wordList.get(0)+"%' ";
					sql = sql + quoteSearch.getSearchStatus();
					sql = sql + quoteSearch.getSalesPerson();
					sql = sql + " ORDER BY Quote_Number DESC ";
					sql = sql + "Limit 21";
					
					//Window.alert(sql);
					if(event.isAltKeyDown() || event.isControlKeyDown() || event.isDownArrow() || event.isLeftArrow() || event.isRightArrow() | event.isUpArrow()){
						//do nothing
					} else{
						try {
							updateQuoteSearchOracle(sql);
						}
						catch (Exception e) {
							quoteSearch.loadingLabel.setText("");
							System.out.println(sql);
							System.out.println("Error: " + e.getMessage());
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
	public void updateQuoteSearchOracle(String sql) throws Exception{
		//quoteSearch.getTxtSearch().setText("");
		greetingService.updateQuoteSearchOracle(sql, new AsyncCallback<ArrayList<String>>() {
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
				quoteSearch.loadingLabel.setText("");
			}
			public void onSuccess(ArrayList<String> result) {
				//dataPanel.clear();
				quoteSearch.loadingLabel.setText("");
				updateQuoteSearchOracle(result);
				quoteSearch.getTxtSearch().showSuggestionList();
			}
		});
	}
	public void updateQuoteSearchOracle(ArrayList<String> newList){
		quoteSearchOracle.clear();
		for(int i=0; i<newList.size(); i++){
			quoteSearchOracle.add(newList.get(i));
		}
	}
	class QuoteSearchSelectionHandler implements SuggestionHandler{
		@Override
		public void onSuggestionSelected(SuggestionEvent event) {
			//Get all Customer details for that company
			String suggestion = event.getSelectedSuggestion().getDisplayString();
			suggestion = suggestion.trim();
			int endIndex = suggestion.indexOf(" :: ");
			suggestion = suggestion.substring(0, endIndex);
			suggestion = suggestion.replaceAll("\\<.*?>","");
			final DialogBox dialogbox = new DialogBox(false, true);
			Label lodLabel = new Label("Loading...");
			dialogbox.setWidget(lodLabel);
			dialogbox.center();
			dialogbox.setPopupPosition(200, 200);
			try{ 
				int wsNumber = Integer.parseInt(suggestion);
				greetingService.loadWS(wsNumber, onlineUser.getUserCode(), new AsyncCallback<W7QuoteDataDTO>() {
		
					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Error: "+ caught.getMessage());
						dialogbox.hide();
					}
		
					@Override
					public void onSuccess(W7QuoteDataDTO result) {
						quoteSearch.getTxtSearch().setText("");
						loadWS(result);
						dialogbox.hide();
					}
				});
			} catch (NumberFormatException e) {
				clearWS();
				dialogbox.hide();
			}
		}
	}
	
	@UiHandler("printQuote")
	void onPrintQuoteClick(ClickEvent event) {
		final PopupPanel pPanel = new PopupPanel();
		QuoteIssue quoteIssue = new QuoteIssue();
		quoteIssue.setDetails(getQuoteDetails(), getOnlineUser(), getSalesPerson());
		pPanel.add(quoteIssue);
		
		quoteIssue.getCloseButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				//applicationBox.hide();
				pPanel.hide();
			}
		});
		pPanel.center();
	}

	public W7QuoteDataDTO getQuoteDetails() {
		return quoteDetails;
	}

	public void setQuoteDetails(W7QuoteDataDTO quoteDetails) {
		this.quoteDetails = quoteDetails;
	}
	@UiHandler("exchangeButton")
	void onExchangeButtonClick(ClickEvent event) {
		rateOfExchangeText.setText("N/A");
	}
	public void updateCurencySelection(String curency){
		/*
		curencies.add("Currency");
		curencies.add("R");
		curencies.add("$");
		curencies.add("\u20AC"); //Euro
		curencies.add("\u00A3"); //Pound
		curencies.add("\u00A5"); //Yen
		*/
		if(curency.equals("R")){
			Currency.setSelectedIndex(1);
		} else if(curency.equals("$")){
			Currency.setSelectedIndex(2);
		} else if(curency.equals("&#8364;")|| curency.equals("€")|| curency.equals("?")){
			Currency.setSelectedIndex(3);
		} else {
			Window.alert("Error with curency conversion. Please send this to Robain: "+ curency);
		}
	}
	public void setIsOrdered(Boolean isOrdered) {
		this.isOrdered = isOrdered;
	}
	public void setSalesPerson(W8UserDTO salesPerson) {
		this.salesPerson = salesPerson;
	}

}
