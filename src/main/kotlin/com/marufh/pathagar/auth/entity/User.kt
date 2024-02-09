package com.marufh.pathagar.auth.entity

import com.marufh.pathagar.base.entity.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "user")
data class User(
    @Column
    var email: String = "",

    @Column
    var password: String = "",

    @ElementCollection(targetClass = Role::class)
    @CollectionTable(name = "user_role", joinColumns = [JoinColumn(name = "user_id")])
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    var roles: Set<Role> = emptySet()
): BaseEntity()
