package com.adharul.i.githubusers.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adharul.i.githubusers.model.api.AccessingGithubApi
import com.adharul.i.githubusers.model.data.ErrorDetails
import com.adharul.i.githubusers.model.data.UserDetails
import com.adharul.i.githubusers.view.fragment.DetailFragment

class DetailViewModel : ViewModel() {
    private val listUsers = MutableLiveData<ArrayList<UserDetails>>()
    private val mUserDetails = MutableLiveData<UserDetails>()
    private val mFilterStatus = MutableLiveData<DetailFragment.DbFilterStatus>()
    private val mErrorDetails = MutableLiveData<ErrorDetails>()
    private val mErrorFollower = MutableLiveData<ErrorDetails>()
    private val mErrorFollowing = MutableLiveData<ErrorDetails>()

    private val mAccessingGithubApi = AccessingGithubApi()

    fun setUserDetails(usernameQuery: String){
        mAccessingGithubApi.getUserObject(usernameQuery, mUserDetails,mErrorDetails)
    }
    fun setFollowerList(usernameQuery: String){
        mAccessingGithubApi.getFollowerArray(usernameQuery, listUsers,mErrorFollower)
    }
    fun setFollowingList(usernameQuery: String){
        mAccessingGithubApi.getFollowingArray(usernameQuery, listUsers,mErrorFollowing)
    }
    fun setDbFilterFinishStatus(){
        val dBFilterStatus = DetailFragment.DbFilterStatus()
        dBFilterStatus.isFilterFinish = true

        mFilterStatus.postValue(dBFilterStatus)
    }

    fun getUserDetails(): MutableLiveData<UserDetails>{
        return mUserDetails
    }
    fun getFollowingList(): LiveData<ArrayList<UserDetails>> {
        return listUsers
    }
    fun getFollowerList(): LiveData<ArrayList<UserDetails>> {
        return listUsers
    }
    fun getDbFilterFinishStatus(): LiveData<DetailFragment.DbFilterStatus>{
        return mFilterStatus
    }
    fun getErrorDetails(): LiveData<ErrorDetails>{
        return mErrorDetails
    }
    fun getErrorFollower(): LiveData<ErrorDetails>{
        return mErrorFollower
    }
    fun getErrorFollowing(): LiveData<ErrorDetails>{
        return mErrorFollowing
    }
}