package com.adharul.i.githubusers.model.api

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.adharul.i.githubusers.BuildConfig
import com.adharul.i.githubusers.model.data.ErrorDetails
import com.adharul.i.githubusers.model.data.UserDetails
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.androidnetworking.interfaces.JSONObjectRequestListener
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class AccessingGithubApi {
    private val token = BuildConfig.TOKEN
    private val searchingUrl = BuildConfig.SEARCHING_URL
    private val userFollowerUrl = BuildConfig.FOLLOWER_URL
    private val userFollowingUrl = BuildConfig.FOLLOWING_URL
    private val userDetailUrl = BuildConfig.DETAIL_URL

    companion object{
        private const val USERNAME = "username"
        private const val AUTHORIZATION = "Authorization"
        private const val TAG = "TAG"

        //JSON KEY
        private const val ITEMS = "items"
        private const val LOGIN = "login"
        private const val AVATAR_URL = "avatar_url"
        private const val NAME = "name"
        private const val COMPANY = "company"
        private const val LOCATION = "location"
        private const val FOLLOWING_URL = "following_url"
        private const val FOLLOWERS_URL = "followers_url"
    }

    fun getSearchingObject(
        usernameQuery: String,
        listUsers: MutableLiveData<ArrayList<UserDetails>>,
        mErrorDetails: MutableLiveData<ErrorDetails>
    ) {
        val error = ErrorDetails()
        val listItems = ArrayList<UserDetails>()
        AndroidNetworking.get(searchingUrl)
            .addPathParameter(USERNAME, usernameQuery)
            .addHeaders(AUTHORIZATION, token)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    val itemResponse = response?.getJSONArray(ITEMS)
                    listItems.clear()
                    try {
                        itemResponse?.run{
                            for (i in 0 until itemResponse.length()) {
                                val userItems = UserDetails()
                                val value = itemResponse.getJSONObject(i)
                                userItems.username = value.getString(LOGIN)
                                userItems.avatarUrl = value.getString(AVATAR_URL)
                                listItems.add(userItems)
                            }
                            listUsers.postValue(listItems)
                        }
                    } catch (e: JSONException) {
                        Log.i(TAG, "fun getSearchingObject, onResponse, catch JsonException")
                        Log.e(TAG,  e.printStackTrace().toString())
                    }
                }

                override fun onError(anError: ANError?) {
                    Log.i(TAG, "fun getSearchingObject, onError: ${anError?.errorCode.toString()}")

                    error.isError = true
                    error.errCode = anError?.errorCode

                    mErrorDetails.postValue(error)
                    listUsers.postValue(listItems)
                }
            })
    }

    fun getFollowingArray(
        usernameQuery: String,
        listUsers: MutableLiveData<ArrayList<UserDetails>>,
        mErrorFollowing: MutableLiveData<ErrorDetails>
    ){
        val error = ErrorDetails()
        val listItems = ArrayList<UserDetails>()
        AndroidNetworking.get(userFollowingUrl)
            .addPathParameter(USERNAME, usernameQuery)
            .addHeaders(AUTHORIZATION, token)
            .build()
            .getAsJSONArray(object : JSONArrayRequestListener {
                override fun onResponse(itemResponse: JSONArray?) {
                    listItems.clear()
                    itemResponse?.run{
                        try {
                            for (i in 0 until itemResponse.length()) {
                                val userItems = UserDetails()
                                val value = itemResponse.getJSONObject(i)
                                userItems.username = value.getString(LOGIN)
                                userItems.avatarUrl = value.getString(AVATAR_URL)
                                listItems.add(userItems)
                            }
                            listUsers.postValue(listItems)
                        } catch (e: JSONException) {
                            Log.i(TAG, "fun getFollowingArray, onResponse, catch JsonException")
                            Log.e(TAG, e.printStackTrace().toString())
                        }
                    }
                }

                override fun onError(anError: ANError?) {
                    Log.i(TAG, "fun getFollowingArray, onError: ${anError?.errorCode.toString()}")

                    error.isError = true
                    error.errCode = anError?.errorCode

                    mErrorFollowing.postValue(error)
                    listUsers.postValue(listItems)
                }
            })
    }

    fun getFollowerArray(
        usernameQuery: String,
        listUsers: MutableLiveData<ArrayList<UserDetails>>,
        mErrorFollower: MutableLiveData<ErrorDetails>
    ){
        val error = ErrorDetails()
        val listItems = ArrayList<UserDetails>()
        AndroidNetworking.get(userFollowerUrl)
            .addPathParameter(USERNAME, usernameQuery)
            .addHeaders(AUTHORIZATION, token)
            .build()
            .getAsJSONArray(object : JSONArrayRequestListener {
                override fun onResponse(itemResponse: JSONArray?) {
                    listItems.clear()
                    itemResponse?.run{
                        try {
                            for (i in 0 until itemResponse.length()) {
                                val userItems = UserDetails()
                                val value = itemResponse.getJSONObject(i)
                                userItems.username = value.getString(LOGIN)
                                userItems.avatarUrl = value.getString(AVATAR_URL)
                                listItems.add(userItems)
                            }
                            listUsers.postValue(listItems)
                        } catch (e: JSONException) {
                            Log.i(TAG, "fun getFollowerArray, onResponse, catch JsonException")
                            Log.e(TAG, e.printStackTrace().toString())                        }
                    }
                }

                override fun onError(anError: ANError?) {
                    Log.i(TAG, "fun getFollowerArray, onError: ${anError?.errorCode.toString()}")
                    error.isError = true
                    error.errCode = anError?.errorCode

                    mErrorFollower.postValue(error)
                    listUsers.postValue(listItems)
                }
            })
    }

    fun getUserObject(
        usernameQuery: String,
        mUserDetails: MutableLiveData<UserDetails>,
        mErrorDetails: MutableLiveData<ErrorDetails>
    ) {
        val error = ErrorDetails()
        val userDetails = UserDetails()
        AndroidNetworking.get(userDetailUrl)
            .addPathParameter(USERNAME, usernameQuery)
            .addHeaders(AUTHORIZATION, token)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    response?.let {
                        userDetails.username = it.getString(LOGIN)
                        userDetails.avatarUrl = it.getString(AVATAR_URL)
                        userDetails.realName = it.getString(NAME)
                        userDetails.followingUrl = it.getString(FOLLOWING_URL)
                        userDetails.followerUrl = it.getString(FOLLOWERS_URL)
                        userDetails.company = it.getString(COMPANY)
                        userDetails.location = it.getString(LOCATION)
                    }
                    mUserDetails.postValue(userDetails)
                }

                override fun onError(anError: ANError?) {
                    Log.i(TAG, "fun getUserObject, onError: ${anError?.errorCode.toString()}")

                    error.isError = true
                    error.errCode = anError?.errorCode

                    mErrorDetails.postValue(error)
                }
            })
    }
}