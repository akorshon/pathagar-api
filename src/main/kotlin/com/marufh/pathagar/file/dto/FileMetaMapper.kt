package com.marufh.pathagar.file.dto

import com.marufh.pathagar.file.entity.FileMeta
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface FileMetaMapper {
    fun toDto(fileMeta: FileMeta): FileMetaDto
    fun toEntity(fileMetaDto: FileMetaDto): FileMeta
}