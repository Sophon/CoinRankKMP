package org.example.udemykmp.features.trade.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

//TODO: move this to gallery?
@Composable
fun CenteredAmountTextField(
    text: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) { focusRequester.requestFocus() }
    val currencyVisualTransformation = rememberCurrencyVisualTransformation()

    BasicTextField(
        value = text,
        onValueChange = { newValue ->
            val trimmedText = newValue
                .trimStart('0')
                .trim { it.isDigit().not() }

            onTextChange(trimmedText)
        },
        textStyle = TextStyle(
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            textAlign = TextAlign.Center,
        ),
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        decorationBox = { innerTextField ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .height(56.dp)
                    .wrapContentWidth()
            ) {
                innerTextField()
            }
        },
        cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground),
        visualTransformation = currencyVisualTransformation,
        modifier = modifier
            .focusRequester(focusRequester)
            .padding(16.dp)
    )
}