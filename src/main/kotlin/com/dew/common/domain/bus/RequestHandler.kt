package com.dew.common.domain.bus

interface RequestHandler<TRequest : Request<TResponse>, TResponse> {

    fun handle(request: TRequest): TResponse
}