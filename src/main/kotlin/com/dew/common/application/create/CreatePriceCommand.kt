package com.dew.common.application.create

import io.micronaut.core.annotation.Introspected

@Introspected
data class CreatePriceCommand(val amount: Float, val currency: String)
