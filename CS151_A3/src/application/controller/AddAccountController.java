package application.controller;

import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import application.CommonObjects;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

public class AddAccountController {
	
	@FXML 
	private TextField accountNameField;
	
	@FXML 
	private TextField accountTypeField;
	
	@FXML
	private DatePicker openingDateField;
	
	@FXML 
	private TextField openingBalanceField;
	
	@FXML 
	private Button addButton;
	
	@FXML
	private Label accountNameError;

	@FXML
	private Label accountTypeError;

	@FXML
	private Label openingDateError;

	@FXML
	private Label openingBalanceError;
	
	@FXML
    public void initialize() {
       
        openingDateField.setValue(LocalDate.now());
    }
	
	
	private CommonObjects commonObjects = CommonObjects.getInstance();
	
	@FXML public void AddAccount() {
		
	if(validateFields()) {
		// verify account is valid before input
		BankAccount account = new BankAccount(accountNameField.getText(), openingDateField.getValue(), Double.parseDouble(openingBalanceField.getText()));	
		
        	saveAccountToFile(account); 
        
		URL url = getClass().getClassLoader().getResource("view/Content2.fxml");
		
		
		try {
			AnchorPane pane2 = (AnchorPane) FXMLLoader.load(url);
			
			HBox mainBox = commonObjects.getMainBox();
			
			if (mainBox.getChildren().size() > 1) {
				mainBox.getChildren().remove(1);
			}
			
			mainBox.getChildren().add(pane2);
			mainBox.requestLayout(); // refresh ui line
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		    	
	}

}
		
	URL resource = getClass().getResource("/data/accounts.csv");
	String filePath = resource.getPath(); 
	private ObservableList<BankAccount> accountsList;

	    public AddAccountController() {
	        // Initialize the list and load accounts from the file
	        accountsList = FXCollections.observableArrayList();
	        //loadAccountsFromFile();
	    }

	
	public void saveAccountToFile(BankAccount account) {
	    	if(validateFields()) {
		        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
		           
		            String line = account.getName()  + "," + account.getOpeningDate()+ "," + account.getBalance();
		            writer.write(line);
		            writer.newLine();
		            
		            System.out.println("Account successfully saved: " + line);
		            
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
	    	}
	    }
	
	
	 public void addAccount(BankAccount account) {
	        accountsList.add(account);  // Add to memory
	        saveAccountToFile(account);  // Save to file	      
	        
	    }

	private boolean validateFields() {
	    // Clear previous error messages
	    accountNameError.setText("");
	    accountTypeError.setText("");
	    openingDateError.setText("");
	    openingBalanceError.setText("");

	    boolean isValid = true;

	    if (accountNameField.getText().isEmpty()) {
	        accountNameError.setText("Account Name is required.");
	        isValid = false;
	    }

	    String accountType = accountTypeField.getText();
	    if (accountType.isEmpty() || (!accountType.equals("Savings") && !accountType.equals("Checking"))) {
	        accountTypeError.setText("Account Type must be 'Savings' or 'Checking'.");
	        isValid = false;
	    }

	    
	    if (openingDateField.getValue().isAfter(LocalDate.now()) ) {
	        openingDateError.setText("Opening Date cannot be in the future.");
	        isValid = false;
	    }

	   
	    try {
	    	double openingBalance = Double.parseDouble(openingBalanceField.getText());
	        if (openingBalance < 0) {
	            openingBalanceError.setText("Opening Balance cannot be negative.");
	            isValid = false;
	        }
	    } catch (NumberFormatException e) {
	        openingBalanceError.setText("Opening Balance must be a valid number.");
	        isValid = false;
	    }
	    
	    return isValid; 
	}

	public ObservableList<BankAccount> getAccountsList() {
        return accountsList;
    }
	
}

