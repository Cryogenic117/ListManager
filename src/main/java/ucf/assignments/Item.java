/*
 *  UCF COP3330 Summer 2021 Assignment 4 Solution
 *  Copyright 2021 Brandon Knudson
 */
package ucf.assignments;
import javafx.beans.property.SimpleStringProperty;

public class Item {
    private final SimpleStringProperty description;
    private final SimpleStringProperty status;
    private final SimpleStringProperty dueDate;

    public Item(String description, String status, String dueDate) {
        this.description = new SimpleStringProperty(description);
        this.status = new SimpleStringProperty(status);
        this.dueDate = new SimpleStringProperty(dueDate);
    }


    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public String getStatus() {
        return status.get();
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public String getDueDate() {
        return dueDate.get();
    }

    public void setDueDate(String dueDate) {
        this.dueDate.set(dueDate);
    }

    @Override
    public String toString() {
        return getDescription()+"|"+getDueDate()+"|"+getStatus();
    }
}