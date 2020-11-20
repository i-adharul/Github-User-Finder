package com.adharul.i.githubusers.view.adapter

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adharul.i.githubusers.R
import com.adharul.i.githubusers.model.data.FavoriteUserDetails
import com.adharul.i.githubusers.model.db.DatabaseContract.FavoriteUsersColumns.Companion.CONTENT_URI
import com.adharul.i.githubusers.model.helper.CircleTransformation
import com.adharul.i.githubusers.view.activity.DetailActivity
import com.adharul.i.githubusers.view.listener.CustomOnItemClickListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.user_in_favorite_list.view.*
import kotlinx.android.synthetic.main.user_in_searching_list.view.ivAvatarSearch
import kotlinx.android.synthetic.main.user_in_searching_list.view.tvLogin

class FavoriteUserRecyclerViewAdapter(private val activity: Activity) : RecyclerView.Adapter<FavoriteUserRecyclerViewAdapter.UserViewHolder>() {
    companion object {
        private const val EXTRA_USERNAME = "extra_username"
    }

    internal var mData = ArrayList<FavoriteUserDetails>()
        set(mData) {
            this.mData.clear()
            this.mData.addAll(mData)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): UserViewHolder {
        val mView = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.user_in_favorite_list, viewGroup, false)
        return UserViewHolder(mView)
    }

    override fun onBindViewHolder(userViewHolder: UserViewHolder, position: Int) {
        userViewHolder.bind(mData[position])
    }

    override fun getItemCount(): Int = mData.size

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(favoriteUsers: FavoriteUserDetails) {
            with(itemView) {
                tvLogin.text = favoriteUsers.username
                val url = favoriteUsers.avatarUrl

                Picasso.get()
                    .load(url)
                    .resize(60, 60)
                    .centerCrop()
                    .transform(
                        CircleTransformation(
                            true
                        )
                    )
                    .into(ivAvatarSearch)

                constraint_item_favorite.setOnClickListener(CustomOnItemClickListener(adapterPosition, object : CustomOnItemClickListener.OnItemClickCallback {
                    override fun onItemClicked(view: View, position: Int) {
                        val intent = Intent(activity, DetailActivity::class.java)
                        intent.putExtra(EXTRA_USERNAME, favoriteUsers.username)
                        activity.startActivity(intent)
                    }
                }))

                btnDelete.setOnClickListener(CustomOnItemClickListener(adapterPosition,object : CustomOnItemClickListener.OnItemClickCallback{
                    override fun onItemClicked(view: View, position: Int) {
                        val uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + favoriteUsers.id)
                        activity.contentResolver.delete(uriWithId, null, null)
                        removeItem(position)
                    }
                }))

            }
        }
    }
    fun removeItem(position: Int) {
        this.mData.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.mData.size)
    }
}