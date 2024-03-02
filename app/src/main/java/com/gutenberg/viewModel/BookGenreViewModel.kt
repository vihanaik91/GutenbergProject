package com.gutenberg.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gutenberg.R
import com.gutenberg.model.BookGenreModel
import kotlinx.coroutines.launch

class BookGenreViewModel : ViewModel() {

    private val listImages = mutableListOf(
        R.drawable.ic_fiction,
        R.drawable.ic_drama,
        R.drawable.ic_humour,
        R.drawable.ic_politics,
        R.drawable.ic_philosophy,
        R.drawable.ic_history,
        R.drawable.ic_adventure
    )
    private val listLabels = mutableListOf(
        "Fiction",
        "Drama",
        "Humour",
        "Politics",
        "Philosophy",
        "History",
        "Adventure"
    )

    // Internally, we use a MutableLiveData, because we will be updating the List of Book Genre
    // with new values
    private val _listBookGenre = MutableLiveData<ArrayList<BookGenreModel>>()

    // The external LiveData interface to the property is immutable, so only this class can modify
    val listBookGenre: LiveData<ArrayList<BookGenreModel>> = _listBookGenre

    init {
        getContentsForGenre()
    }


    private fun getContentsForGenre() {
        viewModelScope.launch {
            try {
                _listBookGenre.value = getData()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getData(): ArrayList<BookGenreModel> {
        val list = ArrayList<BookGenreModel>()
        for (i in listImages.indices) {
            val model = BookGenreModel(
                listImages[i],
                listLabels[i]
            )
            list.add(
                model
            )
        }
        return list
    }
}
