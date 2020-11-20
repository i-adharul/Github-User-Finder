package com.adharul.i.githubusers.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.adharul.i.githubusers.R
import com.adharul.i.githubusers.model.data.ErrorDetails
import com.adharul.i.githubusers.model.data.UserDetails
import com.adharul.i.githubusers.view.activity.DetailActivity
import com.adharul.i.githubusers.view.adapter.UserSearchRecyclerViewAdapter
import com.adharul.i.githubusers.view_model.DetailViewModel
import kotlinx.android.synthetic.main.fragment_following.*

class FollowingFragment : Fragment() {

    private lateinit var searchRecyclerViewAdapter: UserSearchRecyclerViewAdapter
    private lateinit var detailViewModel: DetailViewModel
    private var username: String? = null

    companion object{
        private const val EXTRA_USERNAME = "extra_username"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments
        if (bundle != null) {
            username = bundle.getString(EXTRA_USERNAME)
        }

        detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            DetailViewModel::class.java)

        username?.let { followingViewCode(it) }
        recyclerViewCode()
    }

    private fun followingViewCode(username: String) {
        detailViewModel.setFollowingList(username)
    }

    private fun recyclerViewCode() {
        searchRecyclerViewAdapter = UserSearchRecyclerViewAdapter()
        searchRecyclerViewAdapter.notifyDataSetChanged()

        detailViewModel.getFollowingList().observe(viewLifecycleOwner, Observer { listUsers ->
            if (listUsers != null) {
                searchRecyclerViewAdapter.setData(listUsers)
            }
        })

        var mErrorDetails: ErrorDetails
        detailViewModel.getErrorFollowing().observe(viewLifecycleOwner, Observer { error ->
            mErrorDetails = error

            if (mErrorDetails.isError == true) {
                when (mErrorDetails.errCode) {
                    0 -> Toast.makeText(
                        context,
                        getString(R.string.error_no_internet),
                        Toast.LENGTH_SHORT
                    ).show()
                    422 -> Toast.makeText(
                        context,
                        getString(R.string.error_no_result),
                        Toast.LENGTH_SHORT
                    ).show()
                    else -> Toast.makeText(
                        context,
                        "${getString(R.string.network_error)} ${mErrorDetails.errCode}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                mErrorDetails.isError = false
            }
        })

        rvFollowing.layoutManager = LinearLayoutManager(context)
        rvFollowing.adapter = searchRecyclerViewAdapter

        searchRecyclerViewAdapter.setOnItemClickCallback(object : UserSearchRecyclerViewAdapter.OnItemClickCallback{

            override fun onItemClicked(userDetails: UserDetails) {

                val i = Intent(context, DetailActivity::class.java)
                i.putExtra(EXTRA_USERNAME, userDetails.username)
                startActivity(i)
            }
        })
    }
}
