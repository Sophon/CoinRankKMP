package org.example.udemykmp.features.trade.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import org.example.udemykmp.core.util.formatFiat

private class CurrencyVisualTransformation: VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text.trim()

        if (
            originalText.isEmpty()
            || originalText.isNumeric().not()
        ) {
            return TransformedText(text, OffsetMapping.Identity)
        }

        val formattedText = formatFiat(
            amount = originalText.toDouble(),
            showDecimal = false,
        )

        return TransformedText(
            AnnotatedString(formattedText),
            CurrencyOffsetMapping(originalText, formattedText)
        )
    }
}

@Composable
fun rememberCurrencyVisualTransformation(): VisualTransformation {
    val inspectionMode = LocalInspectionMode.current

    return remember {
        if (inspectionMode) {
            VisualTransformation.None
        } else {
            CurrencyVisualTransformation()
        }
    }
}

private fun String.isNumeric(): Boolean = this.all { it.isDigit() }