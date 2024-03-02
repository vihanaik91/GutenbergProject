/*
 * Copyright (C) 2021 The Android Open Source Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gutenberg.viewModel

import androidx.lifecycle.*
import com.gutenberg.model.BooksModel
import com.gutenberg.network.BooksApi
import kotlinx.coroutines.launch

enum class BooksApiStatus { LOADING, ERROR, DONE }

/**
 * The [ViewModel] that is attached to the [BooksFragment].
 */
class BooksViewModel(format: String?, topic: String?, page: Int, search: String?) : ViewModel() {

    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<BooksApiStatus>()

    // The external immutable LiveData for the request status
    val status: LiveData<BooksApiStatus> = _status

    // Internally, we use a MutableLiveData, because we will be updating the List of MarsPhoto
    // with new values
    private val _photos = MutableLiveData<BooksModel>()

    // The external LiveData interface to the property is immutable, so only this class can modify
    val photos: LiveData<BooksModel> = _photos

    /**ū
     * Call getBooksImages() on init so we can display status immediately.
     */
    init {
        getBooksImages(format, topic, page, search)
    }


    /**
     * Gets Mars photos information from the Mars API Retrofit service and updates the
     * [BooksModel] [List] [LiveData].
     */
    private fun getBooksImages(format: String?, topic: String?, page: Int, search: String?) {

        viewModelScope.launch {
            _status.value = BooksApiStatus.LOADING
            try {
                _photos.value = BooksApi.retrofitService.getBooks(format, topic, page, search)
                _status.value = BooksApiStatus.DONE
            } catch (e: Exception) {
                _status.value = BooksApiStatus.ERROR
                _photos.value = null
            }
        }
    }
}

class BooksViewModelFactory(var format: String?, var topic: String?, var page: Int, var search: String?) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BooksViewModel::class.java)) {
            return BooksViewModel(format, topic, page, search) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
