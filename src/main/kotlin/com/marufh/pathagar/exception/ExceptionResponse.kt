package com.marufh.pathagar.exception

class ExceptionResponse(
    val errorCode: Int,
    val message: String,
    errors: List<String>
)
