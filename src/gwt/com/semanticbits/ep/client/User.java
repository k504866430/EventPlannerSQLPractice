package com.semanticbits.ep.client;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class User implements EntryPoint {
    
    private VerticalPanel mainPanel = new VerticalPanel();
	private FlexTable stocksFlexTable = new FlexTable();
	private HorizontalPanel addPanel = new HorizontalPanel();
	private TextBox newSymbolTextBox = new TextBox();
	private Button addStockButton = new Button("Add");
	private Label lastUpdatedLabel = new Label();
	private ArrayList<String> stocks = new ArrayList<String>();
	private final int REFRESH_INTERVAL = 5000;
	private UserServiceAsync userService;

	/**
	 * Entry point method.
	 */
	public void onModuleLoad() {
		userService = (UserServiceAsync) GWT.create(UserService.class);

    	ServiceDefTarget endpoint = (ServiceDefTarget) userService;

    	// Note the URL where the RPC service is located! 
    	String moduleRelativeURL = GWT.getModuleBaseURL() + "rpc"; 
    	endpoint.setServiceEntryPoint(moduleRelativeURL);
    	
    	//List<com.semanticbits.ep.User> users = myService.
    	// Call a method on the service! List users = myService.listUsers() ...
		// Create table for stock data.
		stocksFlexTable.setText(0, 0, "Symbol");
		stocksFlexTable.setText(0, 1, "Price");
		stocksFlexTable.setText(0, 2, "Change");
		stocksFlexTable.setText(0, 3, "Remove");
		stocksFlexTable.getRowFormatter().addStyleName(0, "watchListHeader");
		stocksFlexTable.addStyleName("watchList");
		stocksFlexTable.getCellFormatter().addStyleName(0, 1, "watchListNumericColumn");
	    stocksFlexTable.getCellFormatter().addStyleName(0, 2, "watchListNumericColumn");
	    stocksFlexTable.getCellFormatter().addStyleName(0, 3, "watchListRemoveColumn");
	 
	    // Add styles to elements in the stock list table.
	    stocksFlexTable.setCellPadding(6);
		
		// Assemble Add Stock panel.
		addPanel.add(newSymbolTextBox);
		addPanel.add(addStockButton);
		addPanel.addStyleName("addPanel");
		
		// Assemble Main panel.
		mainPanel.add(stocksFlexTable);
		mainPanel.add(addPanel);
		mainPanel.add(lastUpdatedLabel);

		// Associate the Main panel with the HTML host page.
		RootPanel.get("stockList").add(mainPanel);

		// Move cursor focus to the input box.
		newSymbolTextBox.setFocus(true);

		// Add mouse click listener to Add Stock buttin
		addStockButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				addStock();
			}
		});

		// Listen for keyboard events in the input box.
		newSymbolTextBox.addKeyPressHandler(new KeyPressHandler() {

			public void onKeyPress(KeyPressEvent event) {
				// System.out.println("["+event.getCharCode()+"] - "+KeyCodes.KEY_ENTER);
				if (event.getCharCode() == KeyCodes.KEY_ENTER) {
					addStock();
				}
			}
		});

		// Timer to refresh the code
		Timer refreshTimer = new Timer() {
			@Override
			public void run() {
				refreshWatchList();
			}
		};
		refreshTimer.scheduleRepeating(REFRESH_INTERVAL);
	}

	/**
	 * Add stock to FlexTable. Executed when the user clicks the addStockButton
	 * or presses enter in the newSymbolTextBox.
	 */
	private void addStock() {
		final String symbol = newSymbolTextBox.getText().toUpperCase().trim();
		newSymbolTextBox.setFocus(true);

		// Stock code must be between 1 and 10 chars that are numbers, letters,
		// or dots.
		if (!symbol.matches("^[0-9A-Z\\.]{1,10}$")) {
			Window.alert("'" + symbol + "' is not a valid symbol.");
			newSymbolTextBox.selectAll();
			return;
		}

		newSymbolTextBox.setText("");

		// TODO Don't add the stock if it's already in the table.
		if (stocks.contains(symbol)) {
			return;
		}

		// TODO Add the stock to the table.
		stocks.add(symbol);
		int rowIndex = stocksFlexTable.getRowCount();
		stocksFlexTable.setText(rowIndex, 0, symbol);
		stocksFlexTable.setWidget(rowIndex, 2, new Label());

		stocksFlexTable.getCellFormatter().addStyleName(rowIndex, 1, "watchListNumericColumn");
	    stocksFlexTable.getCellFormatter().addStyleName(rowIndex, 2, "watchListNumericColumn");
	    stocksFlexTable.getCellFormatter().addStyleName(rowIndex, 3, "watchListRemoveColumn");
		
		
		// TODO Add a button to remove this stock from the table.
		Button removeButton = new Button("X");
		removeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				stocksFlexTable.removeRow(stocks.indexOf(symbol) + 1);
				stocks.remove(symbol);
			}
		});
		removeButton.addStyleDependentName("remove");
		stocksFlexTable.setWidget(rowIndex, 3, removeButton);

		// Get the stock price.
		refreshWatchList();

	}

	/**
	 * Generate random stock prices.
	 */
	private void refreshWatchList() {
		final double MAX_PRICE = 100.0; // $100.00
		final double MAX_PRICE_CHANGE = 0.02; // +/- 2%

		StockPrice[] prices = new StockPrice[stocks.size()];
		for (int i = 0; i < stocks.size(); i++) {
			double price = Random.nextDouble() * MAX_PRICE;
			double change = price * MAX_PRICE_CHANGE
					* (Random.nextDouble() * 2.0 - 1.0);

			prices[i] = new StockPrice(stocks.get(i), price, change);
		}

		updateTable(prices);
	}

	/**
	 * Update the Price and Change fields all the rows in the stock table.
	 * 
	 * @param prices
	 *            Stock data for all rows.
	 */
	private void updateTable(StockPrice[] prices) {
		for (int i = 0; i < prices.length; i++) {
			updateTable(prices[i]);
		}
		
		userService.getAuthor(new AsyncCallback() {
        	public void onFailure(Throwable caught) {
				Window.alert("failed to load users");
			}

			public void onSuccess(Object result) {
				//Window.alert("success");
				// Display timestamp showing last refresh.
			    lastUpdatedLabel.setText("Last update : "
			        + DateTimeFormat.getMediumDateTimeFormat().format(new Date())
			        + "by" + result);
				//RootPanel.get().add(new HTML("<p>" + (String)result + "</p>"));
			}
		});
	}

	/**
	 * Update a single row in the stock table.
	 * 
	 * @param price
	 *            Stock data for a single row.
	 */
	private void updateTable(StockPrice price) {
		// Make sure the stock is still in the stock table.
		if (!stocks.contains(price.getSymbol())) {
			return;
		}

		int row = stocks.indexOf(price.getSymbol()) + 1;

		// Format the data in the Price and Change fields.
		String priceText = NumberFormat.getFormat("#,##0.00").format(
				price.getPrice());
		NumberFormat changeFormat = NumberFormat
				.getFormat("+#,##0.00;-#,##0.00");
		String changeText = changeFormat.format(price.getChange());
		String changePercentText = changeFormat
				.format(price.getChangePercent());

		// Populate the Price and Change fields with new data.
		stocksFlexTable.setText(row, 1, priceText);
//		stocksFlexTable.setText(row, 2, changeText + " (" + changePercentText
		//		+ "%)");
		Label changeWidget = (Label)stocksFlexTable.getWidget(row, 2);
	    changeWidget.setText(changeText + " (" + changePercentText + "%)");
	 // Change the color of text in the Change field based on its value.
	    String changeStyleName = "noChange";
	    if (price.getChangePercent() < -0.1f) {
	      changeStyleName = "negativeChange";
	    }
	    else if (price.getChangePercent() > 0.1f) {
	      changeStyleName = "positiveChange";
	    }

	    changeWidget.setStyleName(changeStyleName);
		
	}
}
