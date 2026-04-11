package com.darya.rabin

import java.nio.ByteBuffer
import kotlin.math.abs

class Logic(private val p: Int, private val q: Int, private val b: Int, private val bytes: ByteArray) {
    private val n = p * q

    var resultData = mutableListOf<Long>()
    var resultBytes = mutableListOf<Byte>()

    fun encrypt() {
        resultData.clear()

        for (byte in bytes) {
            val m = byte.toLong() and 0xFF
            val c = (m * (m + b)) % n

            resultData.add(c)
            val buffer = ByteBuffer.allocate(4).putInt(c.toInt()).array()
            resultBytes.addAll(buffer.toList())
        }
    }

    fun decrypt() {
        resultData.clear()
        val numbs = bytesToBlock(bytes)

        for (c in numbs) {
            val D = (b * b + 4 * c) % n

            val exponentP = ((p + 1) / 4).toLong()
            val mp = powerMod(D, exponentP, p.toLong())

            val exponentQ = ((q + 1) / 4).toLong()
            val mq = powerMod(D, exponentQ, q.toLong())

            val yp = euclide(p, q)
            val yq = euclide(q, p)

            val d1 = (yp * p * mq  +  yq * q*  mp) % n
            val d2 = n - d1
            val d3 = abs(yp * p * mq  - yq * q * mp ) % n
            val d4 = n - d3
            val rootsD = listOf(d1, d4, d3, d2)

            var m: Long = 0
            for (d in rootsD) {
                val diff = (d - b) % n
                val positiveDiff = if (diff < 0) diff + n else diff

                if (positiveDiff % 2 == 0L) {
                    m = (positiveDiff / 2) % n
                } else {
                    m = ((positiveDiff + n) / 2) % n
                }

                if (m < 256)
                    break
            }
            resultData.add(m)
            resultBytes.add(m.toByte())
        }
    }

    private fun powerMod(base: Long, exp: Long, mod: Long): Long {
        var res = 1L
        var b = base % mod
        var e = exp

        while (e > 0) {
            if (e % 2 == 1L) res = (res * b) % mod
            b = (b * b) % mod
            e /= 2
        }
        return res
    }

    private fun euclide(base: Int, m: Int): Int {
        var a = base
        var mod = m
        var x0 = 0
        var x1 = 1

        if (mod == 1) return 0

        while (a > 1) {
            val q = a / mod
            var temp = mod

            mod = a % mod
            a = temp
            temp = x0

            x0 = x1 - q * x0
            x1 = temp
        }
        if (x1 < 0) x1 += m
        return x1
    }


    companion object {
        fun bytesToBlock(bytes: ByteArray): List<Long> {
            val bytesInBlock = 4
            val size = ((bytes.size + bytesInBlock - 1) / bytesInBlock) * bytesInBlock

            val numbs = bytes.copyOf(size)
                .toList()
                .chunked(bytesInBlock)
                .map { block ->
                    val signedInt = ByteBuffer.wrap(block.toByteArray()).getInt()
                    signedInt.toLong() and 0xFFFFFFFFL
                }
            return numbs
        }
    }


}