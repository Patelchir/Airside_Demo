package com.example.airside_demo.network.model

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class ListOfJson<T>(wrapper: Class<T>) : ParameterizedType {
    private val wrapped: Class<*>

    init {
        wrapped = wrapper
    }

    override fun getRawType() = MutableList::class.java

    override fun getOwnerType(): Type? {
        return null
    }

    override fun getActualTypeArguments(): Array<Type> {
        return arrayOf<Type>(wrapped)
    }


}