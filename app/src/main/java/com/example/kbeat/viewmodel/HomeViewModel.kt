package com.example.kbeat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kbeat.model.Category
import com.example.kbeat.model.dummyCategories
import com.example.kbeat.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val _categories = MutableStateFlow<UiState<List<Category>>>(UiState.Loading)
    val categories: StateFlow<UiState<List<Category>>> = _categories

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            _categories.value = UiState.Loading
            delay(1000) // 1 second delay to simulate loading effect
            try {
                _categories.value = UiState.Success(dummyCategories)
            } catch (e: Exception) {
                _categories.value = UiState.Error("Failed to load categories")
            }
        }
    }
}