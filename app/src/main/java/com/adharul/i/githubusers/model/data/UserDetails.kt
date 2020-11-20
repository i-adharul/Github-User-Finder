package com.adharul.i.githubusers.model.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserDetails (
    var username: String? = null,
    var avatarUrl: String? = null,
    var followerUrl: String? = null,
    var followingUrl: String? = null,
    var realName: String? = null,
    var company: String? = null,
    var location: String? = null
) : Parcelable