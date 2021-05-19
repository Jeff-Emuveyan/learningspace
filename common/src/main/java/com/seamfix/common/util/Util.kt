package com.seamfix.common.util

import com.seamfix.core.model.response.UserDto
import com.seamfix.core.model.table.User

fun toUserEntityList(inputList: ArrayList<UserDto>?): List<User>?{
    val list: MutableList<User> = mutableListOf()
    if(inputList == null) return null

    for(userDTO in inputList){
        val user = User(userDTO.id,
            userDTO.title,
            userDTO.firstName,
            userDTO.lastName,
            userDTO.gender,
            userDTO.email,
            userDTO.dateOfBirth,
            userDTO.registerDate,
            userDTO.picture,
            userDTO.location?.toEntity())
        list.add(user)
    }
    return list
}
