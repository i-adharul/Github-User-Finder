package com.adharul.i.githubusers.view.fragment

import android.app.Activity
import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.adharul.i.githubusers.R
import com.adharul.i.githubusers.model.data.ErrorDetails
import com.adharul.i.githubusers.model.db.DatabaseContract.FavoriteUsersColumns.Companion.COLUMN_AVATAR_URL
import com.adharul.i.githubusers.model.db.DatabaseContract.FavoriteUsersColumns.Companion.COLUMN_USERNAME
import com.adharul.i.githubusers.model.db.DatabaseContract.FavoriteUsersColumns.Companion.CONTENT_URI
import com.adharul.i.githubusers.model.helper.CircleTransformation
import com.adharul.i.githubusers.model.helper.MappingHelper
import com.adharul.i.githubusers.view.adapter.DetailPagerAdapter
import com.adharul.i.githubusers.view_model.DetailViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class DetailFragment(val activity: Activity) : Fragment() {
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var username: String
    private var isFavorite: Boolean = false
    private var avatarUrl: String? = null
    private var favoriteUserId: String? = null
    private var isFabAddClicked = false

    companion object {
        private const val EXTRA_USERNAME = "extra_username"
    }

    class DbFilterStatus{
        var isFilterFinish: Boolean? = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading(true)

        //Bundle
        arguments?.let {
            username = it.getString(EXTRA_USERNAME).toString()
        }

        //ViewModel
        detailViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(DetailViewModel::class.java)

        detailViewModel.setUserDetails(username)

        detailViewModel.getUserDetails().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                tvLoginUsername.text = nullStringConversion(it.username)
                tvNameDetail.text = nullStringConversion(it.realName)
                tvLocationDetail.text = nullStringConversion(it.location)
                tvCompanyDetail.text = nullStringConversion(it.company)
                avatarUrl = nullStringConversion(it.avatarUrl)

                Picasso.get()
                    .load(avatarUrl)
                    .fit()
                    .transform(
                        CircleTransformation(
                            false
                        )
                    )
                    .into(ivAvatarDetail)

                showLoading(false)
            }
        })

        var mErrorDetails: ErrorDetails
        detailViewModel.getErrorDetails().observe(viewLifecycleOwner, Observer { error ->
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
            showLoading(false)
        })

        detailViewModel.getDbFilterFinishStatus().observe(viewLifecycleOwner, Observer { dBFilterStatus ->
            dBFilterStatus.isFilterFinish?.let { it ->
                if (it) {
                    if (isFavorite) {
                        fabAdd.setImageResource(R.drawable.ic_favorite_pink_24dp)
                        if(isFabAddClicked){
                            val uriWithId = Uri.parse("$CONTENT_URI/$favoriteUserId")
                            activity.contentResolver.delete(uriWithId, null, null)
                            fabAdd.setImageResource(R.drawable.ic_favorite_white_24dp)
                            isFavorite = false
                            isFabAddClicked = false
                        }
                    } else {
                        fabAdd.setImageResource(R.drawable.ic_favorite_white_24dp)
                        if(isFabAddClicked){
                            val values = ContentValues()
                            values.put(COLUMN_USERNAME, username)
                            avatarUrl?.run{values.put(COLUMN_AVATAR_URL, avatarUrl)}
                            activity.contentResolver?.insert(CONTENT_URI, values)
                            isFavorite = true
                            isFabAddClicked = false
                            fabAdd.setImageResource(R.drawable.ic_favorite_pink_24dp)
                        }
                    }
                }
            }
        })

        //Floating Button
        fabAdd.setOnClickListener {
            loadNotesAsync(username)
            isFabAddClicked = true
        }

        //PagerAdapter and Tab Layout
        val pagerAdapter = DetailPagerAdapter(activity, childFragmentManager)
        username.let { pagerAdapter.setData(it) }
        viewPager.adapter = pagerAdapter
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun onStart() {
        super.onStart()
        loadNotesAsync(username)
    }

    private fun loadNotesAsync(username: String) {
        GlobalScope.launch(Dispatchers.Main) {

            val deferredFavoriteUsers = async(Dispatchers.IO) {
                val uriWithId = Uri.parse("$CONTENT_URI/$username")
                val cursor = activity.contentResolver.query(uriWithId, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }

            val favoriteUserDetails = deferredFavoriteUsers.await()
            if (favoriteUserDetails.size > 0) {
                favoriteUserId = favoriteUserDetails[0].id?.toString()
                isFavorite = true
                detailViewModel.setDbFilterFinishStatus()
            } else {
                isFavorite = false
                detailViewModel.setDbFilterFinishStatus()
            }
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBarDetail.visibility = View.VISIBLE
        } else {
            progressBarDetail.visibility = View.GONE
        }
    }

    private fun nullStringConversion(text: String?): String {
        lateinit var checked: String
        text?.let {
            checked = if (it == "null") "Not Set" else it
        }
        return checked
    }
}
