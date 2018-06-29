package com.harry1453.burst

import com.harry1453.burst.explorer.entity.Block
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormatterBuilder
import java.math.BigInteger

object BurstUtils {

    @JvmStatic
    fun formatBurstTimestamp(burstTimestamp: BigInteger?): String {
        return if (burstTimestamp != null) {
            val genesisDate = DateTime(2014, 8, 11, 4, 0, DateTimeZone.UTC)
            val timeStamp = DateTime(genesisDate.millis + burstTimestamp.multiply(BigInteger.valueOf(1000)).toLong())
            timeStamp.toString(DateTimeFormatterBuilder().appendPattern("dd/MM/yyyy HH:mm:ss").toFormatter().withZone(DateTimeZone.UTC))
        } else {
            ""
        }
    }

    @JvmStatic
    fun calculateBlockSpaceUsage(block: Block): Double {
        return block.size.toDouble() / (if (BurstConstants.PRE_DYMAXION_HF_BLOCKHEIGHT > block.blockNumber.toLong()) BurstConstants.BLOCK_MAX_SIZE_PRE_HF else BurstConstants.BLOCK_MAX_SIZE_POST_HF).toDouble()
    }

    @JvmStatic
    fun toBurstAddress(numeric: BigInteger): String {
        return ReedSolomon.encode(numeric)
    }

    @JvmStatic
    @Throws(ReedSolomon.DecodeException::class)
    fun toNumericID(burstRawAddress: String): BigInteger {
        var burstAddress = burstRawAddress
        if (burstAddress.startsWith("BURST-")) {
            burstAddress = burstRawAddress.substring(6)
        }
        return ReedSolomon.decode(burstAddress)
    }

    object ReedSolomon { // modelled on BRS's ReedSolomon.java

        private val initial_codeword = intArrayOf(1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
        private val gexp = intArrayOf(1, 2, 4, 8, 16, 5, 10, 20, 13, 26, 17, 7, 14, 28, 29, 31, 27, 19, 3, 6, 12, 24, 21, 15, 30, 25, 23, 11, 22, 9, 18, 1)
        private val glog = intArrayOf(0, 0, 1, 18, 2, 5, 19, 11, 3, 29, 6, 27, 20, 8, 12, 23, 4, 10, 30, 17, 7, 22, 28, 26, 21, 25, 9, 16, 13, 14, 24, 15)
        private val codewordMap = intArrayOf(3, 2, 1, 0, 7, 6, 5, 4, 13, 14, 15, 16, 12, 8, 9, 10, 11)
        private const val alphabet = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ"

        private const val base32length = 13
        private const val base10length = 20

        private val two64 = BigInteger("18446744073709551616")

        internal fun encode(plain: BigInteger): String {
            val plainString = toUnsignedLong(plain)
            var length = plainString.length
            val plainString10 = IntArray(base10length)
            for (i in 0 until length) {
                plainString10[i] = plainString[i].toInt() - '0'.toInt()
            }
            var codewordLength = 0
            val codeword = IntArray(initial_codeword.size)
            do {  // base 10 to base 32 conversion
                var newLength = 0
                var digit32 = 0
                for (i in 0 until length) {
                    digit32 = digit32 * 10 + plainString10[i]
                    if (digit32 >= 32) {
                        plainString10[newLength] = digit32 shr 5
                        digit32 = digit32 and 31
                        newLength += 1
                    } else if (newLength > 0) {
                        plainString10[newLength] = 0
                        newLength += 1
                    }
                }
                length = newLength
                codeword[codewordLength] = digit32
                codewordLength += 1
            } while (length > 0)
            val p = intArrayOf(0, 0, 0, 0)
            for (i in base32length - 1 downTo 0) {
                val fb = codeword[i] xor p[3]
                p[3] = p[2] xor gmult(30, fb)
                p[2] = p[1] xor gmult(6, fb)
                p[1] = p[0] xor gmult(9, fb)
                p[0] = gmult(17, fb)
            }
            System.arraycopy(p, 0, codeword, base32length, initial_codeword.size - base32length)
            val cypherStringBuilder = StringBuilder()
            for (i in 0..16) {
                val codewordIndex = codewordMap[i]
                val alphabetIndex = codeword[codewordIndex]
                cypherStringBuilder.append(alphabet[alphabetIndex])
                if (i and 3 == 3 && i < 13) {
                    cypherStringBuilder.append('-')
                }
            }
            return cypherStringBuilder.toString()
        }

        @Throws(DecodeException::class)
        internal fun decode(cypher_string: String): BigInteger {
            val codeword = IntArray(initial_codeword.size)
            System.arraycopy(initial_codeword, 0, codeword, 0, initial_codeword.size)
            var codewordLength = 0
            for (i in 0 until cypher_string.length) {
                val positionInAlphabet = alphabet.indexOf(cypher_string[i])
                if (positionInAlphabet <= -1 || positionInAlphabet > alphabet.length) continue
                if (codewordLength > 16) throw CodewordTooLongException()
                codeword[codewordMap[codewordLength]] = positionInAlphabet
                codewordLength += 1
            }
            if (codewordLength == 17 && !isCodewordValid(codeword) || codewordLength != 17) throw CodewordInvalidException()
            var length = base32length
            val cypherString32 = IntArray(length)
            for (i in 0 until length) {
                cypherString32[i] = codeword[length - i - 1]
            }
            val plainStringBuilder = StringBuilder()
            do { // base 32 to base 10 conversion
                var newLength = 0
                var digit10 = 0
                for (i in 0 until length) {
                    digit10 = digit10 * 32 + cypherString32[i]

                    if (digit10 >= 10) {
                        cypherString32[newLength] = digit10 / 10
                        digit10 %= 10
                        newLength++
                    } else if (newLength > 0) {
                        cypherString32[newLength] = 0
                        newLength++
                    }
                }
                length = newLength
                plainStringBuilder.append((digit10 + '0'.toInt()).toChar())
            } while (length > 0)
            return BigInteger(plainStringBuilder.reverse().toString())
        }

        private fun gmult(a: Int, b: Int): Int {
            if (a == 0 || b == 0) {
                return 0
            }
            val idx = (glog[a] + glog[b]) % 31
            return gexp[idx]
        }

        private fun isCodewordValid(codeword: IntArray): Boolean {
            var sum = 0
            for (i in 1..4) {
                var t = 0
                for (j in 0..30) {
                    if (j in 13..26) {
                        continue
                    }
                    var pos = j
                    if (j > 26) {
                        pos -= 14
                    }
                    t = t xor gmult(codeword[pos], gexp[i * j % 31])
                }
                sum = sum or t
            }
            return sum == 0
        }

        private fun toUnsignedLong(objectId: BigInteger): String {
            if (objectId >= BigInteger.ZERO) {
                return objectId.toString()
            }
            val id = objectId.add(two64)
            return id.toString()
        }

        abstract class DecodeException : Exception()

        private class CodewordTooLongException : DecodeException()

        private class CodewordInvalidException : DecodeException()
    }
}