package com.adharul.i.githubfavoriteusers.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.adharul.i.githubfavoriteusers.R
import com.adharul.i.githubfavoriteusers.model.data.FavoriteUserDetails
import com.adharul.i.githubfavoriteusers.model.helper.CircleTransformation
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.user_in_favorite_list.view.*


class FavoriteUserRecyclerViewAdapter : RecyclerView.Adapter<FavoriteUserRecyclerViewAdapter.UserViewHolder>() {
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
                        Toast.makeText(context,"User ${favoriteUsers.username} has been clicked!",Toast.LENGTH_SHORT).show()
                    }
                }))
            }
        }
    }
}