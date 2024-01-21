package com.marufh.pathagar.file.repository

import com.marufh.pathagar.file.entity.FileMeta
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface FileMetaRepository: JpaRepository<FileMeta, String> {

    @Query("SELECT f FROM FileMeta f WHERE f.hash = ?1 ")
    fun findByHash(search: String): FileMeta?

}