package com.example.dagger_core.model.table

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(@PrimaryKey(autoGenerate = true) var id: Int = 0,
           var firstName: String? = null,
           var age: Int? = null)