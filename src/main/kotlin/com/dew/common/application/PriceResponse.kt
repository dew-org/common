package com.dew.common.application

import io.micronaut.core.annotation.Introspected

@Introspected
data class PriceResponse(val amount: Float, val currency: String)
