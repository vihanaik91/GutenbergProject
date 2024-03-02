package com.gutenberg.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gutenberg.BooksActivity
import com.gutenberg.adapter.BookGenreAdapter.ItemViewHolder
import com.gutenberg.databinding.RowBookGenreBinding
import com.gutenberg.model.BookGenreModel

class BookGenreAdapter(var context: Context) :
    ListAdapter<BookGenreModel, ItemViewHolder>(DiffCallback) {

    class ItemViewHolder(private var binding: RowBookGenreBinding, var context: Context) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindModelToAdapter(bookGenreModel: BookGenreModel) {
            binding.imgLogo.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    bookGenreModel.imgGenre
                )
            )
            binding.txtTitle.text = bookGenreModel.titleGenre
            binding.cardBooksGenre.setOnClickListener {
                context.startActivity(
                    Intent(context, BooksActivity::class.java)
                        .putExtra("genre", bookGenreModel.titleGenre)
                )
            }
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<BookGenreModel>() {
        override fun areItemsTheSame(oldItem: BookGenreModel, newItem: BookGenreModel): Boolean {
            return oldItem.titleGenre == newItem.titleGenre
        }

        override fun areContentsTheSame(oldItem: BookGenreModel, newItem: BookGenreModel): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            RowBookGenreBinding.inflate(LayoutInflater.from(parent.context)),
            context
        )
    }


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bindModelToAdapter(getItem(position))
    }


}