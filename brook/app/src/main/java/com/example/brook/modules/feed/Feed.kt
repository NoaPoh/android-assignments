package com.example.brook.modules.feed


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.Brook.R
import com.example.Brook.databinding.FragmentBooksFeedBinding
import com.example.brook.data.review.ReviewModel

class Feed : Fragment() {

    private var reviewsRecyclerView: RecyclerView? = null
    private var adapter: FeedRecycleAdapter? = null
    private var _binding: FragmentBooksFeedBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: BooksFeedViewModel
    private lateinit var progressBar: ProgressBar

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBooksFeedBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel = ViewModelProvider(this)[BooksFeedViewModel::class.java]

        reviewsRecyclerView = binding.Feed
        reviewsRecyclerView?.setHasFixedSize(true)
        reviewsRecyclerView?.layoutManager = LinearLayoutManager(context)
        adapter = FeedRecycleAdapter(viewModel.reviews.value, viewModel.users.value)

        reviewsRecyclerView?.adapter = adapter
        progressBar = view.findViewById(R.id.progressBar)

        viewModel.reviews.observe(viewLifecycleOwner) {
            adapter?.updateReviews(it)
        }

        viewModel.users.observe(viewLifecycleOwner) {
            adapter?.updateUsers(it)
        }

        binding.pullToRefresh.setOnRefreshListener {
            viewModel.reloadData()
        }

        viewModel.reviewsListLoadingState.observe(viewLifecycleOwner) { state ->
            binding.pullToRefresh.isRefreshing = state == ReviewModel.LoadingState.LOADING

            when (state) {
                ReviewModel.LoadingState.LOADING -> progressBar.visibility = View.VISIBLE
                ReviewModel.LoadingState.LOADED -> progressBar.visibility = View.GONE
            }
        }

        return view
    }


    override fun onResume() {
        super.onResume()
        viewModel.reloadData()
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
