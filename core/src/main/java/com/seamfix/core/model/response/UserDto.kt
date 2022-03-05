package com.seamfix.core.model.response

import com.seamfix.core.model.table.Location
import com.seamfix.core.model.table.User


class UserDto(var id: String,
           var title: String? = null,
           var firstName: String? = null,
           var lastName: String? = null,
           var gender: String? = null,
           var email: String? = null,
           var dateOfBirth: String? = null,
           var registerDate: String? = null,
           var picture: String? = null,
           var location: LocationDto? = null){


    fun toEntity(): User {
        return User(id, title, firstName, lastName, gender, email, dateOfBirth, registerDate, picture, location?.toEntity())
    }
}


class LocationDto( var street: String? = null,
                var city: String? = null,
                var state: String? = null,
                var country: String? = null,
                var timezone: String? = null){

    override fun toString(): String {
        return "Street: $street, city: $city, state: $state, country: $country, timezone: $timezone."
    }

    fun toEntity(): Location {
        return Location(street, city, state, country, timezone)
    }
}