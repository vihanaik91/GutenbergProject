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

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gutenberg.databinding.RowBooksBinding
import com.gutenberg.model.BooksModel
import com.gutenberg.model.Results
import kotlin.coroutines.coroutineContext
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.ContextCompat.startActivity
import com.gutenberg.BooksActivity
import java.net.URISyntaxException


/**
 * This class implements a [RecyclerView] [ListAdapter] which uses Data Binding to present [List]
 * data, including computing diffs between lists.
 */
class BooksAdapter(var context: Context) :
    ListAdapter<Results, BooksAdapter.ItemViewHolder>(DiffCallback) {
    /**
     * The ItemViewHolder constructor takes the binding variable from the associated
     * GridViewItem, which nicely gives it access to the full [BooksModel] information.
     */
    var isLimitReached: Boolean = false
    var searchEnabled = false

    class ItemViewHolder(
        private var binding: RowBooksBinding,
        var context: Context
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(booksModel: Results) {
            binding.results = booksModel
            var authorName = ""
            try {
                if (booksModel.authors.isNotEmpty() && !booksModel.authors[0].name.isNullOrEmpty() && booksModel.authors[0].name.toString()
                        .split(",").isNotEmpty() && booksModel.authors[0].name.toString()
                        .split(",").size > 1
                )
                    authorName = booksModel.authors[0].name.toString().trim()
                        .split(",")[1].trim() + " " + booksModel.authors[0].name.toString().trim()
                        .split(",")[0].trim()
                else
                    authorName = booksModel.authors[0].name.toString().trim()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            binding.lblBookAuthor.text = authorName
            binding.cardBooks.setOnClickListener {
                try {
                    var actionUrl = ""
                    if (!(booksModel.formats as Map<String, String>)["text/html; charset=utf-8"].isNullOrEmpty())
                        actionUrl = booksModel.formats["text/html; charset=utf-8"].toString()
                    else if (!booksModel.formats["application/pdf"].isNullOrEmpty())
                        actionUrl = booksModel.formats["application/pdf"].toString()
                    else if (!booksModel.formats["text/plain; charset=us-ascii"].isNullOrEmpty())
                        actionUrl = booksModel.formats["text/plain; charset=us-ascii"].toString()
                    else if (!booksModel.formats["text/plain; charset=utf-8"].isNullOrEmpty())
                        actionUrl = booksModel.formats["text/plain; charset=utf-8"].toString()
                    else {
                        (context as BooksActivity).generateAlertDialog(
                            context,
                            "Alert",
                            "No viewable version available"
                        )
                        return@setOnClickListener
                    }
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(actionUrl)
                    context.startActivity(intent)
                } catch (ex: URISyntaxException) {
                    ex.printStackTrace()
                } catch (ex: java.lang.Exception) {
                    ex.printStackTrace()
                }

            }
            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings()
        }
    }

    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of
     * [BooksModel] has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<Results>() {
        override fun areItemsTheSame(oldItem: Results, newItem: Results): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Results, newItem: Results): Boolean {
            return oldItem == newItem
        }
    }

    /**
     * Create new [RecyclerView] item views (invoked by the layout manager)
     */


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        return ItemViewHolder(
            RowBooksBinding.inflate(LayoutInflater.from(parent.context)),
            context
        )

    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun addNewListItems(listItem: MutableList<Results>) {
        val objList = ArrayList<Results>()
        if (!searchEnabled)
            objList.addAll(currentList)
        objList.addAll(listItem)
        submitList(objList)
    }

    override fun submitList(list: List<Results>?) {
        super.submitList(list?.let {
            ArrayList(it)
        })
    }
}
