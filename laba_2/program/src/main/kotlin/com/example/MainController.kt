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

    fun formatKeyWithSpaces(key: String): String {
        return key.chunked(8).joinToString(" ")
    }

    @FXML
    fun onEncryptButtonClick() {
        if (inputKeyTextField.text.length < 38) {
            showError("Ошибка", "Короткий ключ!")
            return
        }
        logic = Logic()
        logic.register = logic.stringToByteList(inputKeyTextField.text)
        logic.plainText = logic.stringToByteList(originalDataTextArea.text)

        logic.encrypt()

        generatedKeyTextArea.text = formatKeyWithSpaces(logic.key.joinToString(separator = "") { it.toString() })
        convertedDataTextArea.text = formatKeyWithSpaces(logic.cipherText.joinToString(separator = "") { it.toString() })
    }

    @FXML
    fun onDecryptButtonClick() {
        if (inputKeyTextField.text.length < 38) {
            showError("Ошибка", "Короткий ключ!")
            return
        }
        logic = Logic()
        logic.register = logic.stringToByteList(inputKeyTextField.text)
        logic.cipherText = logic.stringToByteList(originalDataTextArea.text)

        logic.decrypt()

        generatedKeyTextArea.text = formatKeyWithSpaces(logic.key.joinToString(separator = "") { it.toString() })
        convertedDataTextArea.text = formatKeyWithSpaces(logic.decryptedText.joinToString(separator = "") { it.toString() })
    }


    fun onOpenFileButtonClick() {
        val fileChooser = FileChooser()
        fileChooser.title = "Выберите файл для чтения"

        val selectedFile = fileChooser.showOpenDialog(null) ?: return

        val bytes = Files.readAllBytes(selectedFile.toPath())

        val binaryString = bytes.joinToString(separator = "") { byte ->
            Integer.toBinaryString(byte.toInt() and 0xFF).padStart(8, '0')
        }

        originalDataTextArea.text = formatKeyWithSpaces(binaryString)
    }


    fun onSaveFileButtonClick() {
        val fileChooser = FileChooser()
        fileChooser.title = "Сохранить файл"

        val file = fileChooser.showSaveDialog(null) ?: return

        val binaryString = convertedDataTextArea.text.replace(" ", "").replace("\n", "")

        // преобразование блоков по 8 символов в байты
        val bytes = ByteArray(binaryString.length / 8)
        for (i in bytes.indices) {
            val byteString = binaryString.substring(i * 8, (i + 1) * 8)
            bytes[i] = byteString.toInt(2).toByte()
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

    fun setupBinaryField(textField: TextField) {
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
    }

    private fun showError(title: String, message: String) {
        val alert = Alert(Alert.AlertType.ERROR)
        alert.title = title
        alert.contentText = message
        alert.showAndWait()
    }
}

//запускать clean при новом преобразовании