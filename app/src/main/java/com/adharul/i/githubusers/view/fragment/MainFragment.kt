package com.adharul.i.githubusers.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
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
import com.adharul.i.githubusers.view_model.MainViewModel
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {

    private lateinit var userSearchRVAdapter: UserSearchRecyclerViewAdapter
    private lateinit var mainViewModel: MainViewModel

    companion object {
        private const val EXTRA_USERNAME = "extra_username"
    }

    class ListStatus {
        var isListCleared: Boolean? = false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showLoading()

        mainViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(MainViewModel::class.java)

        searchView()
        recyclerView()
    }

    private fun searchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(usernameQuery: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(usernameQuery: String?): Boolean {
                usernameQuery?.run {
                    if (usernameQuery == "") {
                        mainViewModel.clearSearchingList()
                    } else {
                        showLoading(true)
                        mainViewModel.setSearchingList(usernameQuery)
                    }
                }
                return false
            }
        })
    }

    private fun recyclerView() {
        userSearchRVAdapter = UserSearchRecyclerViewAdapter()
        userSearchRVAdapter.notifyDataSetChanged()

        var mErrorDetails = ErrorDetails()
        var mListStatus = ListStatus()

        mainViewModel.getSearchingList().observe(viewLifecycleOwner, Observer { listUsers ->
            if (listUsers != null) {
                userSearchRVAdapter.setData(listUsers)
                showLoading(false)
            }
            showLoading(false)

            mainViewModel.getErrorDetails().observe(viewLifecycleOwner, Observer { error ->
                mErrorDetails = error
            })

            mainViewModel.getListStatus().observe(viewLifecycleOwner, Observer { status ->
                mListStatus = status
            })

            if (mListStatus.isListCleared != true) {
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
                    mListStatus.isListCleared = false
                    mErrorDetails.isError = false
                }
            }
            mListStatus.isListCleared = false
            mErrorDetails.isError = false
        })

        rvMain.layoutManager = LinearLayoutManager(context)
        rvMain.adapter = userSearchRVAdapter

        userSearchRVAdapter.setOnItemClickCallback(object :
            UserSearchRecyclerViewAdapter.OnItemClickCallback {

            override fun onItemClicked(userDetails: UserDetails) {

                val i = Intent(context, DetailActivity::class.java)
                i.putExtra(EXTRA_USERNAME, userDetails.username)
                startActivity(i)
            }
        })
    }

    private fun showLoading(state: Boolean = false) {
        if (state) {
            progressBarSearching.visibility = View.VISIBLE
        } else {
            progressBarSearching.visibility = View.GONE
        }
    }
}