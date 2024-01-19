package com.marufh.pathagar.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "file.path")
data class FileProperties(
    var base: String = "",
    var book: String = "",
    var author: String = "",
    var category: String = ""
)
