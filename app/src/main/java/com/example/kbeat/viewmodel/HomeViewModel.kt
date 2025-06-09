package com.example.kbeat.viewmodel

import androidx.lifecycle.ViewModel
import com.example.kbeat.model.Category
import com.example.kbeat.model.dummyCategories
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories

    init {
        _categories.value = dummyCategories
    }
}