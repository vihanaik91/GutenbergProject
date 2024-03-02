package com.gutenberg

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.gutenberg.adapter.BookGenreAdapter
import com.gutenberg.databinding.ActivityBookGenreBinding
import com.gutenberg.viewModel.BookGenreViewModel


class BookGenreActivity : AppCompatActivity() {

    private lateinit var context: Context

    private val bookGenreModel: BookGenreViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        val binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_book_genre
        ) as ActivityBookGenreBinding
        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this
        // Giving the binding access to the OverviewViewModel
        binding.viewModel = bookGenreModel
        // Sets the adapter of the photosGrid RecyclerView
        binding.rvBookCategory.adapter = BookGenreAdapter(context)
    }
}