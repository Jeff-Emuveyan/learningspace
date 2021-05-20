package com.seamfix.features.users.userdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seamfix.common.NetworkChecker
import com.seamfix.core.model.table.User
import com.seamfix.core.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class UserDetailViewModel @Inject constructor(var userRepository: UserRepository,
                                              var networkChecker: NetworkChecker) : ViewModel() {

    open suspend fun getUser(userID: String): User? {
        //get user from remote database:
        var user = userRepository.getUserFromRemoteSource(userID)?.toEntity()
        if(user != null){
           userRepository.save(user)
        }else{//if no user was found, we fetch from local database:
            user = userRepository.getUserFromLocalSource(userID)
        }
        return user
    }

    fun listenForNetworkChanges() {
        viewModelScope.launch(Dispatchers.IO) {
            networkChecker.listenForNetworkChanges()
        }
    }
}