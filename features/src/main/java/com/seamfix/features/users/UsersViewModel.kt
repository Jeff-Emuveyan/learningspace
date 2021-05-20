package com.seamfix.features.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seamfix.common.NetworkChecker
import com.seamfix.common.util.toUserEntityList
import com.seamfix.core.model.table.User
import com.seamfix.core.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class UsersViewModel @Inject constructor(var userRepository: UserRepository,
                                                  var networkChecker: NetworkChecker) : ViewModel() {


    suspend fun getUsers(): List<User>?{
        //get users from remote database:
        var users = toUserEntityList(userRepository.getUsersFromRemoteSource())
            if(users != null){
                //if we were able to fetch user's from  the service, then we need to save these users
                // in the local database for offline purpose:
                saveUsers(users)

            }else{//if no user was found, we fetch from local database:
                users = userRepository.getUsersFromLocalSource()
            }
        return users
    }


    open suspend fun saveUsers(users: List<User>){
        for(user in users){
            userRepository.save(user)
        }
    }

    fun listenForNetworkChanges() {
        viewModelScope.launch(Dispatchers.IO) {
            networkChecker.listenForNetworkChanges()
        }
    }
}
