package com.example.estiamapp.ui.products

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.estiamapp.data.ProductRepository
import com.example.estiamapp.data.model.ProductDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface ProductsUiState {
    data object Loading : ProductsUiState
    data class Success(val products: List<ProductDto>, val isRefreshing: Boolean = false) : ProductsUiState
    data class Error(val message: String) : ProductsUiState
}

class ProductsViewModel : ViewModel() {
    private val repository = ProductRepository()

    private val _uiState = MutableStateFlow<ProductsUiState>(ProductsUiState.Loading)
    val uiState: StateFlow<ProductsUiState> = _uiState.asStateFlow()

    private var allProducts = emptyList<ProductDto>()
    private var currentPage = 0
    private val pageSize = 20

    init {
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch {
            try {
                _uiState.value = ProductsUiState.Loading

                Log.d("ProductsViewModel", "Fetching products from API...")
                allProducts = repository.fetchProducts()

                Log.i("ProductsViewModel", "Fetched ${allProducts.size} products")

                currentPage = 0
                val initialProducts = allProducts.take(pageSize)
                _uiState.value = ProductsUiState.Success(initialProducts)

            } catch (e: Exception) {
                Log.e("ProductsViewModel", "Error fetching products", e)
                _uiState.value = ProductsUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            try {
                val currentState = _uiState.value
                if (currentState is ProductsUiState.Success) {
                    _uiState.value = currentState.copy(isRefreshing = true)
                }

                Log.d("ProductsViewModel", "Refreshing products...")
                allProducts = repository.fetchProducts()

                currentPage = 0
                val initialProducts = allProducts.take(pageSize)
                _uiState.value = ProductsUiState.Success(initialProducts, isRefreshing = false)

                Log.i("ProductsViewModel", "Products refreshed")

            } catch (e: Exception) {
                Log.e("ProductsViewModel", "Error refreshing products", e)
                _uiState.value = ProductsUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun loadMore() {
        val currentState = _uiState.value
        if (currentState !is ProductsUiState.Success) return

        val nextPage = currentPage + 1
        val startIndex = nextPage * pageSize

        if (startIndex >= allProducts.size) {
            Log.d("ProductsViewModel", "No more products to load")
            return
        }

        val endIndex = minOf(startIndex + pageSize, allProducts.size)
        val newProducts = currentState.products + allProducts.subList(startIndex, endIndex)

        currentPage = nextPage
        _uiState.value = ProductsUiState.Success(newProducts)

        Log.d("ProductsViewModel", "Loaded page $nextPage (${newProducts.size}/${allProducts.size} products)")
    }
}