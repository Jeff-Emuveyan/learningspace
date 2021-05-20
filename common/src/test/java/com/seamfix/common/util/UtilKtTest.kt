package com.seamfix.common.util

import com.seamfix.core.model.response.UserDto
import com.seamfix.core.model.table.User
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Assert
import org.junit.Test

class UtilKtTest{

    @Test
    fun toUserEntityList_validInput_returnList() {
        val userA = UserDto("IDA")
        val userB = UserDto("IDB")
        val userC = UserDto("IDC")
        val inputList = arrayListOf<UserDto>(userA, userB, userC)

        val result = toUserEntityList(inputList)

        assertThat(result?.size, `is`(equalTo(3)))
    }

    @Test
    fun toUserEntityList_nullInput_returnNull() {
        val result = toUserEntityList(null)
        assertThat(result, `is`(equalTo(null)))
    }
}