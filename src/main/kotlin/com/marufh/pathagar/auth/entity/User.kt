package com.marufh.pathagar.auth.entity

import com.marufh.pathagar.entity.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "user")
data class User(
    @Column
    var email: String = "",

    @Column
    var password: String = "",

    @ElementCollection(targetClass = Role::class)
    @JoinTable(name = "user_role")
    @JoinColumn(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    var roles: Set<Role> = emptySet()
): BaseEntity()
