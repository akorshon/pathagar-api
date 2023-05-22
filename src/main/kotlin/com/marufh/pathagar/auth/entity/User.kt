package com.marufh.pathagar.auth.entity

import com.marufh.pathagar.entity.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "user")
data class User(
    @Column
    var email: String = "",

    @Column
    var password: String = "",
): BaseEntity()
