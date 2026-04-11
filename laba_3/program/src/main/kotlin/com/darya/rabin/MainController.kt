package com.darya.rabin

import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.layout.Pane
import javafx.stage.FileChooser
import java.io.File
import java.math.BigInteger
import java.nio.file.Files

@Suppress("IMPLICIT_CAST_TO_ANY")
class MainController {
    @FXML
    private lateinit var pane: Pane
    @FXML
    private lateinit var inputPText: TextField
    @FXML
    private lateinit var inputQText: TextField
    @FXML
    private lateinit var inputBText: TextField
    @FXML
    private lateinit var originalText: TextArea
    @FXML
    private lateinit var convertedText: TextArea
    @FXML
    private lateinit var oneByteButton: RadioButton
    @FXML
    private lateinit var fourBytesButton: RadioButton

    private var inputData: ByteArray = byteArrayOf()
    private var convertedData = mutableListOf<Byte>()
    private val group = ToggleGroup()
    private var countDisplayNumbs = 160

    fun initialize() {
        oneByteButton.toggleGroup = group
        fourBytesButton.toggleGroup = group
        oneByteButton.isSelected = true

         inputBText.textFormatter = TextFormatter<String> { change ->
            if (change.controlNewText.matches(Regex("""\d*""")))
                change
            else
                null
        }
        inputQText.textFormatter = TextFormatter<String> { change ->
            if (change.controlNewText.matches(Regex("""\d*""")))
                change
            else
                null
        }
        inputPText.textFormatter = TextFormatter<String> { change ->
            if (change.controlNewText.matches(Regex("""\d*""")))
                change
            else
                null
        }
    }

    private fun getData(): Logic? {
        val p = inputPText.text.toInt()
        val q = inputQText.text.toInt()
        val b = inputBText.text.toInt()

        val isValid = when {
            !p.isPrime() -> showAlert("p должно быть простым!")
            p % 4 != 3 -> showAlert("Остаток от деления p на 4 должен быть равен 3!")
            !q.isPrime() -> showAlert("q должно быть простым!")
            q % 4 != 3 -> showAlert("Остаток от деления q на 4 должен быть равен 3!")
            b > p * q -> showAlert("b должно быть меньше p * q!")
            else -> true
        }

        return if (isValid == true) Logic(p, q, b, inputData) else null
    }

    private fun Int.isPrime() = BigInteger.valueOf(this.toLong()).isProbablePrime(100)

    private fun showAlert(message: String) {
        val alert = Alert(Alert.AlertType.INFORMATION)
        alert.headerText = message
        alert.showAndWait()
    }

    @FXML
    fun encode() {
        getData()?.run {
            encrypt()
            convertedData = resultBytes
            convertedText.text = resultData
                .take(countDisplayNumbs)
                .joinToString(" ")
        }
    }

    @FXML
    fun decode() {
        getData()?.run {
            decrypt()
            convertedData = resultBytes
            convertedText.text = resultData
                .take(countDisplayNumbs)
                .joinToString(" ")
        }
    }

    @FXML
    fun openFile() {
        val fileChooser = FileChooser()

        val defaultDir = File("D:\\src_ti")
        if (defaultDir.exists() && defaultDir.isDirectory)
            fileChooser.initialDirectory = defaultDir

        val selectedFile = fileChooser.showOpenDialog(null) ?: return

        inputData = Files.readAllBytes(selectedFile.toPath())

        onRadioButtonClick()
    }

    @FXML
    fun saveFile() {
        val fileChooser = FileChooser()

        val defaultDir = File("D:\\src_ti")
        if (defaultDir.exists() && defaultDir.isDirectory)
            fileChooser.initialDirectory = defaultDir

        val file = fileChooser.showSaveDialog(null) ?: return

        Files.write(file.toPath(), convertedData.toByteArray())
    }

    @FXML
    fun onRadioButtonClick() {
        if (oneByteButton.isSelected) {
            originalText.text =  inputData.toList()
                .take(countDisplayNumbs)
                .map { it.toInt() and 0xFF }
                .joinToString (" ")
        } else {
            originalText.text = Logic.bytesToBlock(inputData)
                .take(countDisplayNumbs)
                .joinToString (" ")
        }
    }


    @FXML fun clear() {
        for (node in pane.children) {
            if (node is TextInputControl)
                node.clear()
        }
        convertedData.clear()
        inputData = byteArrayOf()
    }

}