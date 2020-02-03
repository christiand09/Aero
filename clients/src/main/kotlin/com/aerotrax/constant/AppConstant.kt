package com.aerotrax.constant

class AppConstants {

    companion object {

        /** company id **/
        const val DEFAULT_COMPANY_ID = ""

        /** email template path **/
        const val EMAIL_TEMPLATES_PATH = "/opt/corda/email-templates"

        /** file storage path **/
        const val FILE_STORAGE_PATH = "/opt/corda/file-storage"

        /** file storage path local **/
        const val FILE_STORAGE_PATH_LOCAL = "C:\\Users\\joyce.mamac\\Desktop\\Aero\\clients-aerotrax"

        const val LOCAL_NODE_AEROTRAX = "O=Aerotrax, L=London, C=GB"
        const val LOCAL_NODE_AIRLINE1 = "O=Airline1, L=London, C=GB"
        const val LOCAL_NODE_MRO = "O=MRO, L=London, C=GB"
        const val LOCAL_NODE_OEM = "O=OEM, L=London, C=GB"

        const val SERVER_NODE_AEROTRAX = "O=qwe, L=London, C=GB"
        const val SERVER_NODE_AIRLINE1 = "O=asd, L=London, C=GB"
        const val SERVER_NODE_MRO = "O=zxc, L=London, C=GB"
        const val SERVER_NODE_OEM = "O=iop, L=London, C=GB"

    }
}