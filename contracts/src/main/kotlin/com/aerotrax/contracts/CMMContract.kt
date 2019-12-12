package com.aerotrax.contracts

import net.corda.core.contracts.CommandData
import net.corda.core.contracts.Contract
import net.corda.core.contracts.TypeOnlyCommandData
import net.corda.core.transactions.LedgerTransaction

class CMMContract : Contract {
    companion object {

        const val ID = "com.aerotrax.contracts.CMMContract"
    }

    override fun verify(tx: LedgerTransaction) {

    }


    interface Commands : CommandData {
        class Create : TypeOnlyCommandData(), Commands
    }
}