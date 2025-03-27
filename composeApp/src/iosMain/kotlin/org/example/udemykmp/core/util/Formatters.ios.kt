package org.example.udemykmp.core.util

import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSNumberFormatterDecimalStyle

actual fun formatFiat(amount: Double, showDecimal: Boolean): String {
    val formatter = NSNumberFormatter().apply { numberStyle = NSNumberFormatterDecimalStyle }

    when {
        showDecimal.not() -> {
            formatter.apply {
                minimumFractionDigits = 0.toULong()
                maximumFractionDigits = 0.toULong()
            }
        }
        amount >= 0.01 -> {
            formatter.apply {
                minimumFractionDigits = 2.toULong()
                maximumFractionDigits = 2.toULong()
            }
        }
        else -> {
            formatter.apply {
                minimumFractionDigits = 8.toULong()
                maximumFractionDigits = 8.toULong()
            }
        }
    }

    val formattedAmountString = formatter.stringFromNumber(NSNumber(amount))

    return if (formattedAmountString != null) "$ $formattedAmountString" else ""
}

actual fun formatCoinUnit(amount: Double, symbol: String): String {
    val formatter = NSNumberFormatter().apply {
        numberStyle = NSNumberFormatterDecimalStyle
        minimumFractionDigits = 8.toULong()
        maximumFractionDigits = 8.toULong()
    }

    return "${formatter.stringFromNumber(NSNumber(amount))} $symbol"
}

actual fun formatPercentage(amount: Double): String {
    val formatter = NSNumberFormatter().apply {
        numberStyle = NSNumberFormatterDecimalStyle
        minimumFractionDigits = 2.toULong()
        maximumFractionDigits = 2.toULong()
    }
    val prefix = if (amount >= 0) "+" else ""

    return prefix + formatter.stringFromNumber(NSNumber(amount)) + " %"
}