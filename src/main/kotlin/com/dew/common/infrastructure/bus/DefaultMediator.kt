package com.dew.common.infrastructure.bus

import com.dew.common.domain.bus.Mediator
import com.dew.common.domain.bus.Request
import com.dew.common.domain.bus.RequestHandler
import io.micronaut.context.ApplicationContext
import io.micronaut.context.event.ApplicationEventListener
import io.micronaut.context.event.StartupEvent
import io.micronaut.inject.BeanDefinition
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

@Singleton
class DefaultMediator(private val applicationContext: ApplicationContext) : Mediator,
    ApplicationEventListener<StartupEvent> {

    private val handlerClassNameToTypeMap: MutableMap<String, Class<RequestHandler<*, *>>> = HashMap()

    @Suppress("UNCHECKED_CAST")
    override fun <T> send(request: Request<T>): T {
        val requestClass = request.javaClass
        val handlerClass = handlerClassNameToTypeMap[requestClass.name]
            ?: throw IllegalArgumentException("No handler found for request class: $requestClass")

        val handler = applicationContext.getBean(handlerClass) as RequestHandler<Request<T>, T>

        return handler.handle(request)
    }

    private fun getRequestClassForHandler(clazz: Class<*>): Class<*> {
        // check that clazz implements RequestHandler interface
        if (!RequestHandler::class.java.isAssignableFrom(clazz)) {
            throw RuntimeException("class ${clazz.canonicalName} must implement ${RequestHandler::class.java.simpleName}")
        }
        return findRawRequestType(clazz.genericInterfaces)
            ?: throw RuntimeException("class ${clazz.canonicalName} must implement ${RequestHandler::class.java.simpleName}")
    }

    private fun findRawRequestType(types: Array<Type?>): Class<*>? {
        for (type in types) {
            if (type is ParameterizedType) {
                val parametrized: ParameterizedType = type
                return findRawRequestType(parametrized.actualTypeArguments)
            }
            if (type is Class<*>) {
                if (Request::class.java.isAssignableFrom(type)) {
                    return type
                }
            }
        }
        return null
    }

    override fun onApplicationEvent(event: StartupEvent?) {
        val beanDefinitions: Collection<BeanDefinition<RequestHandler<*, *>>> =
            applicationContext.getBeanDefinitions(RequestHandler::class.java)

        for (beanDefinition in beanDefinitions) {
            try {
                val handlerClass = Class.forName(beanDefinition.name)
                val requestClass = getRequestClassForHandler(handlerClass)

                val previous = handlerClassNameToTypeMap.putIfAbsent(requestClass.name, beanDefinition.beanType)
                if (previous != null) {
                    log.warn("{} already associated with {}", requestClass.name, beanDefinition.name)
                }

            } catch (e: ClassNotFoundException) {
                log.warn("Could not find class for bean {}", beanDefinition.name)
            }
        }
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(DefaultMediator::class.java)
    }
}