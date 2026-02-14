package com.example.laba_1;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class FilesProcess {

    public static void main(String[] args) throws IOException {
        //saveToFile("asdfghjkl");
      //  System.out.println(readFromFile());
    }

    public static void saveToFile(Stage primaryStage, String text)  {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName("output.txt");
        File file = fileChooser.showSaveDialog(primaryStage);

        if (file != null) {
            try {
                Files.writeString(file.toPath(), text);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static String readFromFile(Stage primaryStage) {

        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(primaryStage);

        String text = "";
        if (file != null) {
            try {
                text = Files.readString(file.toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return text;

     /*   StringBuilder text = new StringBuilder();
        try {
            Scanner scanner = new Scanner(new File("input.txt"));

            while (scanner.hasNextLine()) {
                text.append(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        return text.toString();*/
    }
}
