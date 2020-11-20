package com.adharul.i.githubusers.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adharul.i.githubusers.R
import com.adharul.i.githubusers.model.data.UserDetails
import com.adharul.i.githubusers.model.helper.CircleTransformation
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.user_in_searching_list.view.*

class UserSearchRecyclerViewAdapter : RecyclerView.Adapter<UserSearchRecyclerViewAdapter.UserViewHolder>() {
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    private val mData = ArrayList<UserDetails>()

    fun setData(items: ArrayList<UserDetails>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): UserViewHolder {
        val mView = LayoutInflater.from(viewGroup.context).inflate(R.layout.user_in_searching_list, viewGroup, false)
        return UserViewHolder(mView)
    }

    override fun onBindViewHolder(userViewHolder: UserViewHolder, position: Int) {
        userViewHolder.bind(mData[position])
    }
    override fun getItemCount(): Int = mData.size

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(userDetails: UserDetails) {
            with(itemView){
                tvLogin.text = userDetails.username
                val url = userDetails.avatarUrl

                Picasso.get()
                    .load(url)
                    .resize(60,60)
                    .centerCrop()
                    .transform(
                        CircleTransformation(
                            true
                        )
                    )
                    .into(ivAvatarSearch)

                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(userDetails) }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(userDetails: UserDetails)
    }
}