package com.aerotrax.util

import com.aerotrax.states.CompanyState
import com.aerotrax.states.DocumentState
import com.aerotrax.states.ProductState
import com.aerotrax.webserver.NodeRPCConnection
import net.corda.core.contracts.StateAndRef
import net.corda.core.messaging.FlowHandle
import net.corda.core.transactions.SignedTransaction
import org.springframework.stereotype.Service

@Service
class Functions(private val rpc: NodeRPCConnection){

    fun productState(): List<StateAndRef<ProductState>>{
        return rpc.proxy.vaultQuery(ProductState::class.java).states
    }

    fun documentState(): List<StateAndRef<DocumentState>>{
        return rpc.proxy.vaultQuery(DocumentState::class.java).states
    }

    fun companyState(): List<StateAndRef<CompanyState>>{
        return rpc.proxy.vaultQuery(CompanyState::class.java).states
    }

    fun returnProductState(flowReturn: FlowHandle<SignedTransaction>): ProductState{
        return flowReturn.returnValue.get().coreTransaction.outputStates.first() as ProductState
    }

    fun returnDocumentState(flowReturn: FlowHandle<SignedTransaction>): DocumentState{
        return flowReturn.returnValue.get().coreTransaction.outputStates.first() as DocumentState
    }

    fun returnCompanyState(flowReturn: FlowHandle<SignedTransaction>): CompanyState{
        return flowReturn.returnValue.get().coreTransaction.outputStates.first() as CompanyState
    }
}