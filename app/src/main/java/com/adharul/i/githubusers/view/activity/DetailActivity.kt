package com.adharul.i.githubusers.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.adharul.i.githubusers.R
import com.adharul.i.githubusers.view.fragment.DetailFragment
import com.adharul.i.githubusers.view_model.DetailViewModel
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {
    private lateinit var detailViewModel: DetailViewModel
    private var username: String? = null
    private lateinit var mFragmentManager: FragmentManager
    private lateinit var mDetailFragment: DetailFragment

    companion object {
        private const val EXTRA_USERNAME = "extra_username"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        //Toolbar
        setSupportActionBar(detail_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "User Details"
        supportActionBar?.elevation = 0f

        //Get Intent Data
        intent?.run {
            username = intent?.getStringExtra(EXTRA_USERNAME)
        }

        //ViewModel
        detailViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(DetailViewModel::class.java)

        //Bundle
        val detailBundle = Bundle()
        detailBundle.putString(EXTRA_USERNAME, username)
        mDetailFragment = DetailFragment(this)
        mDetailFragment.arguments = detailBundle

        //Fragment Manager
        mFragmentManager = supportFragmentManager
        val detailFragment =
            mFragmentManager.findFragmentByTag(DetailFragment::class.java.simpleName)
        if (detailFragment !is DetailFragment) {
            mFragmentManager
                .beginTransaction()
                .add(R.id.frame_detail, mDetailFragment, DetailFragment::class.java.simpleName)
                .commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.favorite_menu -> {
                val mIntent = Intent(this, FavoriteActivity::class.java)
                startActivity(mIntent)
                true
            }
            R.id.home -> {
                onBackPressed()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}