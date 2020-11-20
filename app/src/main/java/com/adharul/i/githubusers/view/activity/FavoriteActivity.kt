package com.adharul.i.githubusers.view.activity

import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.adharul.i.githubusers.R
import com.adharul.i.githubusers.model.data.FavoriteUserDetails
import com.adharul.i.githubusers.model.db.DatabaseContract.FavoriteUsersColumns.Companion.CONTENT_URI
import com.adharul.i.githubusers.model.helper.MappingHelper
import com.adharul.i.githubusers.view.adapter.FavoriteUserRecyclerViewAdapter
import com.adharul.i.githubusers.view_model.FavoriteViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {

    private lateinit var favoriteUsersRVAdapter: FavoriteUserRecyclerViewAdapter
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var handler: Handler

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        //Toolbar
        setSupportActionBar(favorite_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Favorite Users"

        //ViewModel
        favoriteViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(FavoriteViewModel::class.java)

        //RecyclerView
        favoriteUsersRVAdapter = FavoriteUserRecyclerViewAdapter(this)
        favoriteUsersRVAdapter.notifyDataSetChanged()
        rvFavorite.layoutManager = LinearLayoutManager(this)
        rvFavorite.adapter = favoriteUsersRVAdapter

        favoriteViewModel.getFavoriteList().observe(this, Observer { favoriteList ->
            if (favoriteList != null) {
                favoriteUsersRVAdapter.mData = favoriteList
            }
        })

        //DB Observer
        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        handler = Handler(handlerThread.looper)
        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadNotesAsync()
            }
        }
        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        //SavedInstanceState
        if (savedInstanceState != null) {
            val list = savedInstanceState.getParcelableArrayList<FavoriteUserDetails>(
                EXTRA_STATE
            )
            if (list != null) {
                favoriteUsersRVAdapter.mData = list
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadNotesAsync()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun loadNotesAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            progressBarFavorite.visibility = View.VISIBLE
            val deferredFavoriteUsers = async(Dispatchers.IO) {
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }

            progressBarFavorite.visibility = View.INVISIBLE
            val favoriteUserDetails = deferredFavoriteUsers.await()
            if (favoriteUserDetails.size > 0) {
                favoriteViewModel.setFavoriteList(favoriteUserDetails)
            } else {
                favoriteViewModel.setFavoriteList(ArrayList())
                Snackbar.make(rvFavorite, getString(R.string.favorite_db_status), Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, favoriteUsersRVAdapter.mData)
    }
}
