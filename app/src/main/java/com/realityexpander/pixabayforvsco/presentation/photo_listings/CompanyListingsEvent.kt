package com.realityexpander.pixabayforvsco.presentation.photo_listings

sealed class CompanyListingsEvent{
    object OnRefresh : CompanyListingsEvent()
    data class OnSearchQueryChanged(val query: String) : CompanyListingsEvent()

}
