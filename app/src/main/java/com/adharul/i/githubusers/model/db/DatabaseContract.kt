package com.adharul.i.githubusers.model.db

import android.net.Uri
import android.provider.BaseColumns

internal class DatabaseContract {
    companion object {
        const val AUTHORITY = "com.adharul.i.githubusers"
        const val SCHEME = "content"
    }

    internal class FavoriteUsersColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "favorite_users"
            const val COLUMN_ID = "_id"
            const val COLUMN_USERNAME = "username"
            const val COLUMN_AVATAR_URL = "avatar_url"

            // untuk membuat URI content://com.adharul.i.githubusers
            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}