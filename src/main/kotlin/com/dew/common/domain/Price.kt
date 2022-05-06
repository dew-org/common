package com.dew.common.domain

import io.micronaut.core.annotation.Introspected

@Introspected
data class Price(val amount: Float, val currency: String)
