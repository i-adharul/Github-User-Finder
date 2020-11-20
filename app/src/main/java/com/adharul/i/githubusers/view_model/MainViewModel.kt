package com.adharul.i.githubusers.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adharul.i.githubusers.model.api.AccessingGithubApi
import com.adharul.i.githubusers.model.data.ErrorDetails
import com.adharul.i.githubusers.model.data.UserDetails
import com.adharul.i.githubusers.view.fragment.MainFragment

class MainViewModel : ViewModel() {
    private val listUsers = MutableLiveData<ArrayList<UserDetails>>()
    private val mErrorDetails = MutableLiveData<ErrorDetails>()
    private val mListStatus = MutableLiveData<MainFragment.ListStatus>()

    private val mAccessingGithubApi = AccessingGithubApi()

    fun setSearchingList(usernameQuery: String){
        mAccessingGithubApi.getSearchingObject(usernameQuery, listUsers, mErrorDetails)
    }

    fun clearSearchingList(){
        val newListItems = ArrayList<UserDetails>()

        val listStatus = MainFragment.ListStatus()
        listStatus.isListCleared = true

        mListStatus.postValue(listStatus)
        listUsers.postValue(newListItems)
    }

    fun getSearchingList(): LiveData<ArrayList<UserDetails>> {
        return listUsers
    }
    fun getErrorDetails(): LiveData<ErrorDetails>{
        return mErrorDetails
    }
    fun getListStatus(): LiveData<MainFragment.ListStatus>{
        return mListStatus
    }
}