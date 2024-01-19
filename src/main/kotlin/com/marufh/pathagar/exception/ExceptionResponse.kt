package com.marufh.pathagar.exception

class ExceptionResponse(
    val errorCode: Int,
    val message: String,
    val errors: List<String>
)
