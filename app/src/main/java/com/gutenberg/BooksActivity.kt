package com.gutenberg

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.gutenberg.adapter.BooksAdapter
import com.gutenberg.databinding.ActivityBooksBinding
import com.gutenberg.viewModel.BooksViewModel
import com.gutenberg.viewModel.BooksViewModelFactory


class BooksActivity : AppCompatActivity() {
    var strPage: Int = 1

    lateinit var viewModel: BooksViewModel
    var booksAdapter: BooksAdapter? = null

    var strFormat: String? = "image/jpeg"
    var strGenre: String? = null
    var strSearch: String? = null
    private lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        // Inflate the layout for this fragment
        val binding =
            DataBindingUtil.setContentView(this, R.layout.activity_books) as ActivityBooksBinding
        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this
        init(binding)
        // Giving the binding access to the BooksModel
        binding.viewModel =
            ViewModelProvider(
                this,
                BooksViewModelFactory(strFormat, strGenre, strPage, strSearch)
            )[BooksViewModel::class.java]

        // Sets the adapter of the photosGrid RecyclerView
        booksAdapter = BooksAdapter(context)
        binding.rvBooks.adapter = booksAdapter
        // adding on scroll change listener method for our nested scroll view.
        binding.nsBooks.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            // on scroll change we are checking when users scroll as bottom.
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                // in this method we are incrementing page number,
                // making progress bar visible and calling get data method.
                strPage++
                getDataFromAPI(binding)
            }
        })

        binding.etSearch.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus)
                binding.rlSearch.background =
                    ContextCompat.getDrawable(context, R.drawable.search_bg_focused)
            else
                binding.rlSearch.background =
                    ContextCompat.getDrawable(context, R.drawable.search_bg)
        }
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {

                Handler(Looper.getMainLooper()).postDelayed(Runnable {
                    strSearch = binding.etSearch.text.toString().trim()
                    booksAdapter!!.searchEnabled = true
                    booksAdapter!!.isLimitReached = false
                    getDataFromAPI(binding)
                }, 200)
            }
        })

        binding.imgCancel.setOnClickListener {
            if (binding.etSearch.text.toString().trim().isNotEmpty()) {
                binding.etSearch.setText("")
                strSearch = binding.etSearch.text.toString().trim()
                booksAdapter!!.searchEnabled = false
                booksAdapter!!.isLimitReached = false
                getDataFromAPI(binding)
            }
        }
        binding.imgBack.setOnClickListener {
            finish()
        }
        binding.rlSearch.background = ContextCompat.getDrawable(context, R.drawable.search_bg)
    }

    private fun init(binding: ActivityBooksBinding) {
        if (intent.hasExtra("genre")) {
            strGenre = intent.getStringExtra("genre").toString()
            binding.lblGenre.text = strGenre
        }
    }

    private fun getDataFromAPI(binding: ActivityBooksBinding) {
        if (booksAdapter!!.isLimitReached) {
            // hiding our progress bar.
            binding.imgBookStatus.visibility = View.INVISIBLE
            return
        }
        binding.viewModel = BooksViewModel(strFormat, strGenre, strPage, strSearch)
    }

    public fun generateAlertDialog(context: Context, title: String, text: String) {
        AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(text)
            .setPositiveButton(android.R.string.ok) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}