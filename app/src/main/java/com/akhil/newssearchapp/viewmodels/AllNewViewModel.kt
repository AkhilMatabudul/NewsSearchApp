package com.akhil.newssearchapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akhil.newssearchapp.modals.NewsApiResponse
import com.akhil.newssearchapp.utils.RetrofitInstance
import kotlinx.coroutines.launch

class AllNewViewModel : ViewModel() {
    private val _myData = MutableLiveData<NewsApiResponse>()
    val allNewsList: LiveData<NewsApiResponse> get() = _myData

    // News search function in viewModel
    fun fetchData(search:String,key:String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.apiService.getNews(search,"en",key)
                if (response.isSuccessful) {
                    _myData.value = response.body()
                } else {
                    // Handle error
                }
            } catch (e: Exception) {
                // Handle exception
            }
        }
    }

    // News search function in viewModel using date range
    fun getNewsDate(search:String,from:String,key:String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.apiService.getNewsDate(search,"en",from,from,key)
                if (response.isSuccessful) {
                    _myData.value = response.body()
                } else {
                    // Handle error
                }
            } catch (e: Exception) {
                // Handle exception
            }
        }
    }
}