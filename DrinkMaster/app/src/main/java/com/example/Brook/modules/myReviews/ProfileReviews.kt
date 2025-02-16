package com.example.Brook.modules.myReviews

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.Brook.R
import com.example.Brook.data.review.ReviewModel
import com.example.Brook.databinding.FragmentBooksFeedBinding

class ProfileReviews : Fragment() {
    private var reviewsRecyclerView: RecyclerView? = null
    private var adapter: ProfileReviewsRecycleAdapter? = null
    private var _binding: FragmentBooksFeedBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ProfileReviewsViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBooksFeedBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModel = ViewModelProvider(this)[ProfileReviewsViewModel::class.java]
        reviewsRecyclerView = binding.Feed
        reviewsRecyclerView?.setHasFixedSize(true)
        reviewsRecyclerView?.layoutManager = LinearLayoutManager(context)
        adapter = ProfileReviewsRecycleAdapter(viewModel.reviews.value, viewModel.user.value)
        reviewsRecyclerView?.adapter = adapter
        viewModel.reviews.observe(viewLifecycleOwner) {
            adapter?.reviews = it
            adapter?.notifyDataSetChanged()
        }
        viewModel.user.observe(viewLifecycleOwner) {
            adapter?.user = it
            adapter?.notifyDataSetChanged()
        }
        binding.pullToRefresh.setOnRefreshListener {
            viewModel.reloadData()
        }
        viewModel.reviewsListLoadingState.observe(viewLifecycleOwner) { state ->
            binding.pullToRefresh.isRefreshing = state == ReviewModel.LoadingState.LOADING
        }
        return view
    }

    override fun onResume() {
        super.onResume()
        viewModel.reloadData()
    }

    override fun onDestroy() {
        super.onDestroy()
        super.onDestroyView()
        _binding = null
    }

}