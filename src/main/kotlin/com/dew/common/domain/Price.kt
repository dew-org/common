package com.dew.common.domain

import io.micronaut.core.annotation.Creator
import io.micronaut.core.annotation.Introspected
import org.bson.codecs.pojo.annotations.BsonCreator
import org.bson.codecs.pojo.annotations.BsonProperty
import javax.validation.constraints.NotBlank

@Introspected
data class Price @Creator @BsonCreator constructor(
    @field:BsonProperty("amount")
    @param:BsonProperty("amount")
    @field:NotBlank
    val amount: Float,

    @field:BsonProperty("currency")
    @param:BsonProperty("currency")
    @field:NotBlank
    val currency: String
)
