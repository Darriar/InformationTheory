package com.example

import kotlin.experimental.xor

class Logic {
    //x^38 + x^6 + x^5 + x + 1
    private val maxPower = 38
    private val indexes = listOf(38, 6, 5, 1)

    var plainText: ArrayList<Byte> = ArrayList()
    var register: ArrayList<Byte> = ArrayList(List(maxPower) { 0.toByte() })
    var convertedText: ArrayList<Byte> = ArrayList()
    var key: ArrayList<Byte> = ArrayList()


    private fun generateKeyBit(): Byte {
        val outBit = register.first

        var newBit = 0
        for (i in 0..<indexes.size)
            newBit = newBit xor register[maxPower - indexes[i]].toInt()

        register.removeFirst()
        register.addLast(newBit.toByte())

        return outBit
    }

    fun convertText() {
        for (element in plainText) {
            val outKeyBit = generateKeyBit()
            key.add(outKeyBit)
            var convertedElement = element xor outKeyBit
            convertedText.add(convertedElement)
        }
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