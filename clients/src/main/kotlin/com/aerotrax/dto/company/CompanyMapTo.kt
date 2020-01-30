package com.aerotrax.dto.company

import com.aerotrax.states.CompanyState

fun mapToMainCompanyDTO(state: CompanyState) : MainCompanyDTO {
    return MainCompanyDTO(
            name = state.name,
            email =  state.email,
            password= state.password,
            type= state.type,
            node= state.node,
            logoImage= state.logoImage,
            contactNumber= state.contactNumber,
            rate= state.rate,
            website= state.website,
            linkedIn= state.linkedIn,
            about= state.about,
            location= state.location,
            addressLine1= state.addressLine1,
            addressLine2= state.addressLine2,
            city= state.city,
            state= state.state,
            country= state.country,
            zipCode= state.zipCode,
            review =  state.review,
            createdBy = state.createdBy,
            createdAt =  state.createdAt,
            updatedAt = state.updatedAt,
            linearId = state.linearId.id.toString(),
            participants = state.participants.map { it.name.x500Principal.name.toString() }
    )
}