package com.aerotrax.contracts

import net.corda.core.contracts.CommandData
import net.corda.core.contracts.Contract
import net.corda.core.contracts.TypeOnlyCommandData
import net.corda.core.transactions.LedgerTransaction

class DocumentContract : Contract {
    companion object {

        const val ID = "com.template.contracts.DocumentContract"
    }

    override fun verify(tx: LedgerTransaction) {

    }


    interface Commands : CommandData {
        class Create : TypeOnlyCommandData(), Commands
    }
}