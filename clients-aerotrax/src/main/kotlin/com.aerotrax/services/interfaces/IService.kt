package com.aerotrax.services.interfaces

interface IService {
    var currentCompanyId: String
    fun setCurrentCompanyDetails(userId: String?)
}