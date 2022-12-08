/*
 *  UCF COP3330 Summer 2021 Assignment 4 Solution
 *  Copyright 2021 Brandon Knudson
 */
package ucf.assignments;

import javafx.stage.FileChooser;

import java.io.*;
import java.util.Scanner;

public class FileManagement {

    public static Boolean exportHandler() {
        final FileChooser chooser = new FileChooser();
        chooser.setInitialFileName("Untitled");
        chooser.setTitle("Save");
        chooser.setTitle("Save");
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Saved List", "*.txt*"));
        //chooser.setSelectedExtensionFilter(filter);
        File file = chooser.showSaveDialog(stage.getStage());
        // if the file doesn't write properly give user error
        return writeToFile(file);
    }

   public static Boolean writeToFile(File file) {
       PrintWriter writer;

       try {
           FileWriter fw = new FileWriter(file);
           writer = new PrintWriter(fw);
       } catch (IOException e) {
           e.printStackTrace();
           return false;
       }
       // iterate trough list and print item to text file
       writer.println(ListController.allList.size());
       for(int i = 0; i < ListController.allList.size(); i++) {
           writer.println(ListController.allList.get(i).toString());
       }
       writer.close();

       return true;
   }

   public static Boolean importFile(File filePath) {
       int itemCount;
       try {
           Scanner sc = new Scanner(filePath);
           // get item count
           if (sc.hasNextInt()) {
               itemCount = sc.nextInt();
           }
           // if file format doesn't include number of items
           else {
               return false;
           }
           sc.nextLine();
           // Iterate through text file and read each item by line
           for(int i = 1; i <= itemCount; i++) {
               String description, dueDate, status;
               String temp = sc.nextLine();
               // Separate line by |
               String[] itemContents = temp.split("\\|");
               // assign values
               description = itemContents[0];
               status = itemContents[1];
               dueDate = itemContents[2];
               // create list with assigned values
               ListFunctions.addListItem(description, dueDate, status);

           }
           sc.close();
           return true;
       } catch (FileNotFoundException e) {
           e.printStackTrace();
           return false;
       }
   }
}
