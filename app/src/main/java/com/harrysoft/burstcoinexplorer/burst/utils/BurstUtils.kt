package com.harrysoft.burstcoinexplorer.burst.utils

import android.content.Context
import android.text.TextUtils
import com.harrysoft.burstcoinexplorer.R
import java.math.BigInteger

object BurstUtils {

    @JvmStatic
    fun burstName(context: Context, burstName: String): String {
        return if (TextUtils.isEmpty(burstName)) {
            context.getString(R.string.empty_name)
        } else {
            burstName
        }
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
            burstAddress = burstRawAddress.substring(6);
        }
        return ReedSolomon.decode(burstAddress)
    }

    object ReedSolomon { // taken from BRS, modified to use BigIntegers

        private val initial_codeword = intArrayOf(1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
        private val gexp = intArrayOf(1, 2, 4, 8, 16, 5, 10, 20, 13, 26, 17, 7, 14, 28, 29, 31, 27, 19, 3, 6, 12, 24, 21, 15, 30, 25, 23, 11, 22, 9, 18, 1)
        private val glog = intArrayOf(0, 0, 1, 18, 2, 5, 19, 11, 3, 29, 6, 27, 20, 8, 12, 23, 4, 10, 30, 17, 7, 22, 28, 26, 21, 25, 9, 16, 13, 14, 24, 15)
        private val codeword_map = intArrayOf(3, 2, 1, 0, 7, 6, 5, 4, 13, 14, 15, 16, 12, 8, 9, 10, 11)
        private val alphabet = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ"

        private val base_32_length = 13
        private val base_10_length = 20

        internal val two64 = BigInteger("18446744073709551616")

        internal fun encode(plain: BigInteger): String {

            val plain_string = toUnsignedLong(plain)
            var length = plain_string.length
            val plain_string_10 = IntArray(base_10_length)
            for (i in 0 until length) {
                plain_string_10[i] = plain_string[i].toInt() - '0'.toInt()
            }

            var codeword_length = 0
            val codeword = IntArray(initial_codeword.size)

            do {  // base 10 to base 32 conversion
                var new_length = 0
                var digit_32 = 0
                for (i in 0 until length) {
                    digit_32 = digit_32 * 10 + plain_string_10[i]
                    if (digit_32 >= 32) {
                        plain_string_10[new_length] = digit_32 shr 5
                        digit_32 = digit_32 and 31
                        new_length += 1
                    } else if (new_length > 0) {
                        plain_string_10[new_length] = 0
                        new_length += 1
                    }
                }
                length = new_length
                codeword[codeword_length] = digit_32
                codeword_length += 1
            } while (length > 0)

            val p = intArrayOf(0, 0, 0, 0)
            for (i in base_32_length - 1 downTo 0) {
                val fb = codeword[i] xor p[3]
                p[3] = p[2] xor gmult(30, fb)
                p[2] = p[1] xor gmult(6, fb)
                p[1] = p[0] xor gmult(9, fb)
                p[0] = gmult(17, fb)
            }

            System.arraycopy(p, 0, codeword, base_32_length, initial_codeword.size - base_32_length)

            val cypher_string_builder = StringBuilder()
            for (i in 0..16) {
                val codework_index = codeword_map[i]
                val alphabet_index = codeword[codework_index]
                cypher_string_builder.append(alphabet[alphabet_index])

                if (i and 3 == 3 && i < 13) {
                    cypher_string_builder.append('-')
                }
            }
            return cypher_string_builder.toString()
        }

        @Throws(DecodeException::class)
        internal fun decode(cypher_string: String): BigInteger {

            val codeword = IntArray(initial_codeword.size)
            System.arraycopy(initial_codeword, 0, codeword, 0, initial_codeword.size)

            var codeword_length = 0
            for (i in 0 until cypher_string.length) {
                val position_in_alphabet = alphabet.indexOf(cypher_string[i])

                if (position_in_alphabet <= -1 || position_in_alphabet > alphabet.length) {
                    continue
                }

                if (codeword_length > 16) {
                    throw CodewordTooLongException()
                }

                val codework_index = codeword_map[codeword_length]
                codeword[codework_index] = position_in_alphabet
                codeword_length += 1
            }

            if (codeword_length == 17 && !is_codeword_valid(codeword) || codeword_length != 17) {
                throw CodewordInvalidException()
            }

            var length = base_32_length
            val cypher_string_32 = IntArray(length)
            for (i in 0 until length) {
                cypher_string_32[i] = codeword[length - i - 1]
            }

            val plain_string_builder = StringBuilder()
            do { // base 32 to base 10 conversion
                var new_length = 0
                var digit_10 = 0

                for (i in 0 until length) {
                    digit_10 = digit_10 * 32 + cypher_string_32[i]

                    if (digit_10 >= 10) {
                        cypher_string_32[new_length] = digit_10 / 10
                        digit_10 %= 10
                        new_length++
                    } else if (new_length > 0) {
                        cypher_string_32[new_length] = 0
                        new_length++
                    }
                }
                length = new_length
                plain_string_builder.append((digit_10 + '0'.toInt()).toChar())
            } while (length > 0)

            return BigInteger(plain_string_builder.reverse().toString())
        }

        private fun gmult(a: Int, b: Int): Int {
            if (a == 0 || b == 0) {
                return 0
            }

            val idx = (glog[a] + glog[b]) % 31

            return gexp[idx]
        }

        private fun is_codeword_valid(codeword: IntArray): Boolean {
            var sum = 0

            for (i in 1..4) {
                var t = 0

                for (j in 0..30) {
                    if (j > 12 && j < 27) {
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
            if (objectId.compareTo(BigInteger.ZERO) >= 0) {
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