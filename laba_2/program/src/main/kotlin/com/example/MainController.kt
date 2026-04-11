package com.example

import javafx.fxml.FXML
import javafx.scene.control.Alert
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.control.TextFormatter
import java.util.function.UnaryOperator
import javafx.stage.FileChooser
import java.io.File
import java.nio.file.Files
import java.util.ArrayList

class MainController {
    @FXML
    private lateinit var inputKeyTextField: TextField
    @FXML
    private lateinit var originalDataTextArea: TextArea
    @FXML
    private lateinit var convertedDataTextArea: TextArea
    @FXML
    private lateinit var generatedKeyTextArea: TextArea

    private var logic = Logic()
    private var isTextEdited = true

    private fun formatKeyWithSpaces(key: String): String {
        return key.take(8 * 128).chunked(8).joinToString(" ")     // отобразить первые 128 байт
    }

    @FXML
    fun convertData() {
        if (inputKeyTextField.text.length < 38) {
            showError("Ошибка", "Короткий ключ!")
            return
        }

        logic.convertedText.clear()
        logic.key.clear()
        logic.register = logic.stringToByteList(inputKeyTextField.text)

        if (isTextEdited)
            logic.plainText = logic.stringToByteList(originalDataTextArea.text)

        logic.convertText()

        generatedKeyTextArea.text = formatKeyWithSpaces(logic.key.joinToString(separator = "") { it.toString() })
        convertedDataTextArea.text =formatKeyWithSpaces(logic.convertedText.joinToString(separator = "") { it.toString() })
    }


    fun onOpenFileButtonClick() {
        val fileChooser = FileChooser()
        fileChooser.initialDirectory = File("D:\\src_ti")
        fileChooser.title = "Выберите файл для чтения"

        val selectedFile = fileChooser.showOpenDialog(null) ?: return

        val bytes = Files.readAllBytes(selectedFile.toPath())

        logic.plainText = bytes.flatMap { b ->
            (7 downTo 0).map { i -> (b.toInt() shr i and 1).toByte() }
        }.toCollection(ArrayList())

        originalDataTextArea.text = formatKeyWithSpaces(logic.plainText.joinToString(""))
        isTextEdited = false
    }


    fun onSaveFileButtonClick() {
        val fileChooser = FileChooser()
        fileChooser.initialDirectory = File("D:\\src_ti")
        fileChooser.title = "Сохранить файл"

        val file = fileChooser.showSaveDialog(null) ?: return
        val bitList = logic.convertedText
        val bytes = ByteArray(bitList.size / 8)

        for (i in bytes.indices) {
            var currentByte = 0
            for (bitIndex in 0..7) {
                val bit = bitList[i * 8 + bitIndex].toInt() and 1
                currentByte = (currentByte shl 1) or bit
            }
            bytes[i] = currentByte.toByte()
        }
        Files.write(file.toPath(), bytes)
    }

    fun onCleanButtonClick() {
        logic = Logic()
        inputKeyTextField.clear()
        originalDataTextArea.clear()
        generatedKeyTextArea.clear()
        convertedDataTextArea.clear()
    }

    private fun setupBinaryField(textField: TextField) {
        val filter = UnaryOperator<TextFormatter.Change> { change ->
            val newText = change.controlNewText

            if (newText.length <= 38 && newText.matches(Regex("[01]*"))) {
                change // Разрешаем изменение
            } else {
                null // Отклоняем изменение
            }
        }

        textField.textFormatter = TextFormatter<String>(filter)
    }

    @FXML
    fun initialize() {
        setupBinaryField(inputKeyTextField)

        originalDataTextArea.textProperty().addListener { _, _, _ -> isTextEdited = true }
    }

    private fun showError(title: String, message: String) {
        val alert = Alert(Alert.AlertType.ERROR)
        alert.title = title
        alert.contentText = message
        alert.showAndWait()
    }
}