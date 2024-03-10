package com.example.booksfinal20.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class BookViewModel(private val repository: BookRepository) : ViewModel() {

    private val _books = MutableLiveData<List<BookItem>>()
    val books: LiveData<List<BookItem>> = _books

    private val _error = MutableLiveData<String>()
    //val error: LiveData<String> = _error

    fun searchBooks(query: String) {
        viewModelScope.launch {
            val response = repository.searchBooks(query)
            if (response.isSuccessful) {
                _books.value = response.body()?.items ?: emptyList()
            } else {
                _error.value = "Error al cargar los libros"
            }
        }
    }
}
