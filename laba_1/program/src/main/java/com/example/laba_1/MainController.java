package com.example.laba_1;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {
    private Stage primaryStage;

    @FXML
    private TextField keyTextField;

    @FXML
    private TextArea plainTextArea, resultTextArea;

    @FXML
    private RadioButton columnButton, vigenerButton;


    @FXML
    private void onCodeButtonClick() {
        String key = processKey(keyTextField.getText());
        String plainText = processInputText(plainTextArea.getText());
        String codeText = new String();

        if (vigenerButton.isSelected())
            codeText = VigenerAlgorithm.encodeVigener(plainText, key);
        else
            codeText = ColumnMethod.encodeColumn(plainText, key);

        resultTextArea.setText(codeText);
    }

    @FXML
    private void onDecodeButtonClick() {
        String key = processKey(keyTextField.getText());
        String cipherText = processInputText(plainTextArea.getText());
        String plainText = new String();

        if (vigenerButton.isSelected())
            plainText = VigenerAlgorithm.decodeVigener(cipherText, key);
        else
            plainText = ColumnMethod.decodeColumn(cipherText, key);


        resultTextArea.setText(plainText);
    }

    @FXML
    private void onOpenFileButtonClick() {
        plainTextArea.setText(FilesProcess.readFromFile(primaryStage));
        resultTextArea.clear();
    }

    @FXML
    private void onSaveButtonClick() throws IOException {
        FilesProcess.saveToFile(primaryStage, resultTextArea.getText());
    }


    @FXML
    private void clear() {
        keyTextField.clear();
        plainTextArea.clear();;
        resultTextArea.clear();
    }

    private String processKey(String key) {
        return key.toUpperCase().replaceAll("[^А-ЯЁ]", "");
    }

    private String processInputText(String inputText) {
        return inputText.toUpperCase();
    }


}