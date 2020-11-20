package com.adharul.i.githubusers.view.adapter

import android.content.Context
import android.os.Bundle
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.adharul.i.githubusers.R
import com.adharul.i.githubusers.view.fragment.FollowerFragment
import com.adharul.i.githubusers.view.fragment.FollowingFragment


class DetailPagerAdapter (private val mContext: Context, fm: FragmentManager)
    : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var username: String? = null

    companion object {
        private const val EXTRA_USERNAME = "extra_username"
    }

    @StringRes
    private val tabTitles = intArrayOf(R.string.tab_text_follower, R.string.tab_text_following)

    fun setData(string: String){
        username = string
    }

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> {
                fragment = FollowerFragment()
                val bundle = Bundle()
                bundle.putString(EXTRA_USERNAME, username)
                fragment.arguments = bundle
            }
            1 -> {
                fragment = FollowingFragment()
                val bundle = Bundle()
                bundle.putString(EXTRA_USERNAME, username)
                fragment.arguments = bundle
            }
        }
        return fragment as Fragment
    }

    override fun getCount(): Int {
        return tabTitles.size
    }

    @Nullable
    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.resources.getString(tabTitles[position])
    }
}