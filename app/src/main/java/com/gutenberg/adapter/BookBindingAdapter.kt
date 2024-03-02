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

package com.gutenberg.adapter

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.gutenberg.R
import com.gutenberg.model.BooksModel
import com.gutenberg.network.BooksApi
import com.gutenberg.viewModel.BooksApiStatus

/**
 * Updates the data shown in the [RecyclerView].
 */
@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, booksModel: BooksModel?) {
    try {
        val adapter = recyclerView.adapter as BooksAdapter
        if (booksModel != null) {
            adapter.addNewListItems(booksModel.results.toMutableList())
            if (booksModel.next.isNullOrEmpty())
                adapter.isLimitReached = true
        }
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
}


/**
 * Uses the Coil library to load an image by URL into an [ImageView]
 */
@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: Any) {
    imgUrl.let {
        val image = (imgUrl as Map<String, String>)["image/jpeg"]
        val imgUri = image.toString().toUri().buildUpon().scheme("https").build()
        imgView.load(imgUri) {
            placeholder(R.drawable.loading_animation)
            error(R.drawable.ic_broken_image)
        }
    }
}


@BindingAdapter("booksApiStatus")
fun bindStatus(statusImageView: ProgressBar, status: BooksApiStatus?) {
    when (status) {
        BooksApiStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
        }
        BooksApiStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
        }
        BooksApiStatus.DONE -> {
            statusImageView.visibility = View.INVISIBLE
        }
        else -> {}
    }
}