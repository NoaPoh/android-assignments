package com.example.brook.modules.search

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.Brook.R
import com.example.Brook.databinding.FragmentSearchBinding
import com.example.brook.data.review.ReviewModel

class SearchFragment : Fragment() {
    private var searchRecyclerView: RecyclerView? = null
    private var _binding: FragmentSearchBinding? = null
    private var adapter: SearchAdapter? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SearchViewModel
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModel = ViewModelProvider(this)[SearchViewModel::class.java]
        progressBar = view.findViewById(R.id.progressBar)

        searchRecyclerView = binding.searchResultsLayout
        searchRecyclerView?.setHasFixedSize(true)
        searchRecyclerView?.layoutManager = LinearLayoutManager(context)
        adapter = SearchAdapter(viewModel.books.value)

        searchRecyclerView?.adapter = adapter

        viewModel.books.observe(viewLifecycleOwner) {
            Log.d("TAG", "books size ${it?.size}")
            adapter?.books = it
            adapter?.notifyDataSetChanged()
        }

        viewModel.loadingState.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                ReviewModel.LoadingState.LOADING -> progressBar.visibility = View.VISIBLE
                ReviewModel.LoadingState.LOADED -> progressBar.visibility = View.GONE
            }
        })

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.clearBooks()
                viewModel.refreshBooks(query)

                binding.SearchTextView.visibility = View.GONE
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (viewModel.books.value?.isNotEmpty() == true) binding.SearchTextView.visibility =
                    View.GONE
                else binding.SearchTextView.visibility = View.VISIBLE

                return false
            }
        })

        return view
    }


}
