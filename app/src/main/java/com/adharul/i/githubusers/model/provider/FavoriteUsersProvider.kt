package com.adharul.i.githubusers.model.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.adharul.i.githubusers.model.db.DatabaseContract.Companion.AUTHORITY
import com.adharul.i.githubusers.model.db.DatabaseContract.FavoriteUsersColumns.Companion.CONTENT_URI
import com.adharul.i.githubusers.model.db.DatabaseContract.FavoriteUsersColumns.Companion.TABLE_NAME
import com.adharul.i.githubusers.model.db.FavoriteUserHelper

class FavoriteUsersProvider : ContentProvider() {

    companion object {
        private const val FAVORITE_USER = 1
        private const val FAVORITE_USER_ID = 2
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var favoriteUserHelper: FavoriteUserHelper
        init {
            // URI content://com.adharul.i.githubusers/favorite_users
            sUriMatcher.addURI(
                AUTHORITY,
                TABLE_NAME, FAVORITE_USER)
            // URI content://com.adharul.i.githubusers/favorite_users/id
            sUriMatcher.addURI(
                AUTHORITY,
                "$TABLE_NAME/#",
                FAVORITE_USER_ID)
        }
    }

    override fun onCreate(): Boolean {
        favoriteUserHelper = FavoriteUserHelper.getInstance(context as Context)
        favoriteUserHelper.open()
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        return when (sUriMatcher.match(uri)) {
            FAVORITE_USER -> favoriteUserHelper.queryAll()
            else -> favoriteUserHelper.queryByUsername(uri.lastPathSegment.toString())
        }
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, contentValues: ContentValues?): Uri? {
        val added: Long = when (FAVORITE_USER) {
            sUriMatcher.match(uri) -> favoriteUserHelper.insert(contentValues)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleted: Int = when (FAVORITE_USER_ID) {
            sUriMatcher.match(uri) -> favoriteUserHelper.deleteById(uri.lastPathSegment.toString())
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return deleted
    }
}
