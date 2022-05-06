package com.dew.common.domain.invoices

import io.micronaut.core.annotation.Introspected

@Introspected
data class PurchasedProduct(
    val code: String,
    val sku: String,
    val quantity: Int,
)
