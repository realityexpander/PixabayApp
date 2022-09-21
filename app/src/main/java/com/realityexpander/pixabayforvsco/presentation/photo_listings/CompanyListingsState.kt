package com.realityexpander.pixabayforvsco.presentation.photo_listings

import com.realityexpander.pixabayforvsco.domain.model.CompanyListing

data class CompanyListingsState(
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null,
    val companyListings: List<CompanyListing> = emptyList(),
)
