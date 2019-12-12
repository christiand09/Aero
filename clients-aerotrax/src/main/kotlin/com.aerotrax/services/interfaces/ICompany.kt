package com.aerotrax.services.interfaces

import com.aerotrax.dto.*

interface ICompany
{
    fun getAllCompany(): List<MainCompanyDTO>
    fun getACompany(id: String): MainCompanyDTO
    fun createCompany(request: RegisterCompanyDTO): MainCompanyDTO
    fun requestConnection(id: String, request: RequestConnectionDTO): MainConnectionDTO
    fun approveRejectConnection(id: String, request: ApproveRejectConnectionDTO): MainConnectionDTO
    fun getAllConnections(id:String): List<MainCompanyDTO>
    fun getAllRequestingConnections(id:String): List<MainCompanyDTO>
}