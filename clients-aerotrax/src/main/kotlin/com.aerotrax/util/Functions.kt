package com.aerotrax.util

import com.aerotrax.states.ContractState
import com.aerotrax.states.ProductState
import com.aerotrax.webserver.NodeRPCConnection
import net.corda.core.contracts.StateAndRef
import net.corda.core.flows.FlowLogic
import net.corda.core.messaging.FlowHandle
import net.corda.core.transactions.SignedTransaction
import org.springframework.stereotype.Service

@Service
class Functions(private val rpc: NodeRPCConnection){

    fun productState(): List<StateAndRef<ProductState>>{
        return rpc.proxy.vaultQuery(ProductState::class.java).states
    }

    fun flow(logicType: Class<out FlowLogic<SignedTransaction>>,vararg arg: Any?): FlowHandle<SignedTransaction>{
        return rpc.proxy.startFlowDynamic(logicType, arg)
    }

    fun returnProductState(flowReturn: FlowHandle<SignedTransaction>): ProductState{
        return flowReturn.returnValue.get().coreTransaction.outputStates.first() as ProductState
    }
}