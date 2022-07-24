package com.dew.common.domain.bus

interface Mediator {

    fun <T> send(request: Request<T>): T
}