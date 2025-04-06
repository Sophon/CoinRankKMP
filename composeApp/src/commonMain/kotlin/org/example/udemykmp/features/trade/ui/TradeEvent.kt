package org.example.udemykmp.features.trade.ui

sealed interface TradeEvent {
    data object PurchaseSuccess : TradeEvent
}