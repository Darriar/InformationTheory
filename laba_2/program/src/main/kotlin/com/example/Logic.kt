package com.example

import kotlin.experimental.xor

class Logic {
    //x^38 + x^6 + x^5 + x + 1
    val maxPower = 38

    var plainText: ArrayList<Byte> = ArrayList()
    var register: ArrayList<Byte> = ArrayList(List(maxPower) { 0.toByte() })
    var cipherText: ArrayList<Byte> = ArrayList()
    var decryptedText: ArrayList<Byte> = ArrayList()
    var key: ArrayList<Byte> = ArrayList()


    fun generateKeyBit(): Byte {
        val outBit = register.last

        var newBit = register.get(37) xor register.get(5) xor register.get(4) xor register.get(0)

        register.removeLast()
        register.addFirst(newBit)

        return outBit
    }

    fun convertText(text: ArrayList<Byte>): ArrayList<Byte> {
        var convertedText: ArrayList<Byte> = ArrayList()
        for (element in text) {
            val outKeyBit = generateKeyBit()
            key.add(outKeyBit)
            var convertedElement = element xor outKeyBit
            convertedText.add(convertedElement)
        }

        return convertedText
    }

    fun encrypt() {
        cipherText = convertText(plainText)
    }

    fun decrypt() {
        decryptedText = convertText(cipherText)
    }

    fun stringToByteList(input: String): ArrayList<Byte> {
        var nums =  input.replace(" ", "")
        val list = ArrayList<Byte>()
        for (char in nums) {
            list.add(char.digitToInt().toByte())
        }
        return list
    }

}