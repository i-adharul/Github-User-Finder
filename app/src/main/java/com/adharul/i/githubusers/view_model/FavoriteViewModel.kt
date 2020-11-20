package com.adharul.i.githubusers.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adharul.i.githubusers.model.data.FavoriteUserDetails

class FavoriteViewModel : ViewModel() {
    private val favoriteList = MutableLiveData<ArrayList<FavoriteUserDetails>>()

    fun setFavoriteList(list: ArrayList<FavoriteUserDetails>){
        favoriteList.postValue(list)
    }
    fun getFavoriteList(): LiveData<ArrayList<FavoriteUserDetails>> {
        return favoriteList
    }
}