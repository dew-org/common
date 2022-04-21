package com.dew.common.domain.invoices

data class PurchasedProduct(
    val code: String,
    val sku: String,
    val quantity: Int,
)
