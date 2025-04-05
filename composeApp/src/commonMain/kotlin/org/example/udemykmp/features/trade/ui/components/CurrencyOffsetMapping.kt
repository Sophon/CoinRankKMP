package org.example.udemykmp.features.trade.ui.components

import androidx.compose.ui.text.input.OffsetMapping

class CurrencyOffsetMapping(
    originalText: String,
    formattedText: String,
): OffsetMapping {
    private val originalLength = originalText.length
    private val indices = findDigitIndices(originalText, formattedText)

    // 123 -> $123; indices are [1,2,3]
    override fun originalToTransformed(offset: Int): Int {
        return if (offset >= originalLength) {
            indices.last() + 1
        } else {
            indices[offset]
        }
    }

    override fun transformedToOriginal(offset: Int): Int {
        return indices
            .indexOfFirst { it >= offset }
            .takeIf { it != -1 } ?: originalLength
    }

    private fun findDigitIndices(firstString: String, secondString: String): List<Int> {
        val digitIndices = mutableListOf<Int>()
        var indexOfFirst = 0

        for (digit in firstString) {
            val indexOfSecond = secondString.indexOf(digit, indexOfFirst)

            if (indexOfSecond == -1) {
                //not found
                return emptyList()
            } else {
                digitIndices.add(indexOfSecond)
                indexOfFirst = indexOfSecond + 1
            }
        }

        return digitIndices
    }
}