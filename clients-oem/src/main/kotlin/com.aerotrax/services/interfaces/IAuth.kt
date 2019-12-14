package com.aerotrax.services.interfaces

import com.aerotrax.dto.*
import com.aerotrax.states.ParticipantState

interface IAuth
{
    fun seedCompany(): MainCompanyDTO
}