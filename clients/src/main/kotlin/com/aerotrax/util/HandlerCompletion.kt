package com.aerotrax.util

import net.corda.core.messaging.FlowHandle
import net.corda.core.transactions.SignedTransaction
import org.springframework.stereotype.Service

@Service
class HandlerCompletion {

    fun handlerCompletion(flowReturn: FlowHandle<SignedTransaction>) {
        listOf(flowReturn).forEach { test -> test.returnValue.get() }
    }

}