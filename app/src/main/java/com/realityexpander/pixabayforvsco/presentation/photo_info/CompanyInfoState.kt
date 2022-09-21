package com.realityexpander.pixabayforvsco.presentation.photo_info

import com.realityexpander.pixabayforvsco.domain.model.CompanyInfo
import com.realityexpander.pixabayforvsco.domain.model.IntradayInfo

data class CompanyInfoState(
    val intradayInfos: List<IntradayInfo> = emptyList(),
    val companyInfo: CompanyInfo? = null,
    val isLoadingIntradayInfos: Boolean = false,
    val isLoadingCompanyInfo: Boolean = false,
    val errorMessageIntradayInfos: String? = null,
    val errorMessageCompanyInfo: String? = null
)
