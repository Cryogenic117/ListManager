/*
 *  UCF COP3330 Summer 2021 Assignment 4 Solution
 *  Copyright 2021 Brandon Knudson
 */
package ucf.assignments;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static javafx.application.Platform.exit;


public class ListController {
    public static ObservableList<Item> allList;

    //Table
    @FXML public TableView<Item> tableView;
    @FXML public TableColumn<Item, String> descriptionColumn;
    @FXML public TableColumn<Item, String> dueDateColumn;
    @FXML public TableColumn<Item, String> statusColumn;
    // Error Reporters
    @FXML public Label errorReporter;
    // Choice box
    @FXML ObservableList<String> displayBoxList = FXCollections.observableArrayList("All", "Incomplete", "Complete");
    @FXML private ChoiceBox<String> displayBox;

    // Initializers
        @FXML private void initialize() {
        initializeDisplayBox();
        initializeTable();
    }

    public void initializeDisplayBox() {
        displayBox.setValue("All");
        displayBox.setItems(displayBoxList);
    }

    public void initializeTable() {
        // sets up table columns
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        allList = ListFunctions.initialize();
        refreshView();

        //allow fields to be edited
        tableView.setEditable(true);
        descriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        dueDateColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    }

    public void refreshView() {
        tableView.setItems(allList);
    }

    // Allows user to change display
    public void changeDisplayType() {
        if (displayBox.getValue().compareTo("All") == 0) {
            refreshView();
        } else if (displayBox.getValue().compareTo("Incomplete") == 0) {
            tableView.setItems(ListFunctions.displayIncomplete(allList));
        } else if (displayBox.getValue().compareTo("Complete") == 0) {
            tableView.setItems(ListFunctions.displayComplete(allList));
        }
    }

    //Change columns content with double click
    public void changeDescriptionNameEvent(CellEditEvent cell) {
        Item itemSelected = tableView.getSelectionModel().getSelectedItem();
        if (cell.getNewValue().toString().length() <= 256) {
            itemSelected.setDescription(cell.getNewValue().toString());
            errorReporter.setText("");
        } else {
            itemSelected.setDescription("");
            errorReporter.setText("Error: Over Character limit of 256, Please Re-Enter a Description.");
            tableView.refresh();
        }
    }
    public void changeDueDateEvent(CellEditEvent cell) {

        try {
            Item itemSelected = tableView.getSelectionModel().getSelectedItem();

            //verify entry using local date
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(cell.getNewValue().toString(), formatter);

            itemSelected.setDueDate(date.toString());
            errorReporter.setText("");
            tableView.refresh();
        }
        catch (Exception e) {
            errorReporter.setText("Error: Invalid Date format please enter date in the format YYYY-MM-DD");
            tableView.refresh();
        }
    }

    // Button Functions
    public void deleteButtonClicked() {
        errorReporter.setText("");
        if(tableView.getSelectionModel().getSelectedItems().size() == 0) {
            errorReporter.setText("Error: No Items Selected");
        }
        ListFunctions.deleteListItem(tableView.getSelectionModel().getSelectedItems(), allList);
        // refreshes display according to display selection
        changeDisplayType();
    }

    // creates new empty item
    public void addButtonClicked() {
        errorReporter.setText("");
        ListFunctions.addEmptyListItem(allList);
    }

    // calls import function and then refreshes Display based on type
    public void importButtonClicked() {
        errorReporter.setText("");
        final FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(stage.getStage());
        if (file != null) {
            if (!FileManagement.importFile(file)) {
                errorReporter.setText("Error: Invalid File Please Try Again.");
            }
        }
        changeDisplayType();
    }

    // exports to file using given name and path
    public void exportButtonClicked() {
        errorReporter.setText("");
        if(!FileManagement.exportHandler()) {
            errorReporter.setText("Error: Failed to Export");
        }
    }

    // assign display to new empty list
    public void clearListButtonClicked() {
        errorReporter.setText("");
        // Only allowed to be used when using all view
        if (displayBox.getValue().toLowerCase().compareTo("all") == 0) {
            allList = FXCollections.observableArrayList();
            refreshView();
        }
    }
    public void closeButtonClicked() {
            exit();
    }
    public void aboutButtonClicked() {
            try {
                Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("popup.fxml")));
                Scene scene = new Scene(parent);
                Stage popup = new Stage();
                popup.setScene(scene);
                popup.setTitle("Help");
                popup.show();
            } catch (Exception ignored){}


    }
    public void toggleStatusButtonPressed() {
        if(tableView.getSelectionModel().getSelectedItems().size() == 0) {
            errorReporter.setText("Error: No Items Selected");
        }

        for (int i = 0; i < tableView.getSelectionModel().getSelectedItems().size(); i++) {
            String status = tableView.getSelectionModel().getSelectedItems().get(i).getStatus();
            if (status.toLowerCase().compareTo("incomplete") == 0) {
                tableView.getSelectionModel().getSelectedItems().get(i).setStatus("Complete");
            } else {
                tableView.getSelectionModel().getSelectedItems().get(i).setStatus("Incomplete");
            }
        }
        tableView.refresh();
    }
}

