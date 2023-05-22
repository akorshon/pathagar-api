package com.marufh.pathagar.controller.exception

class ExceptionResponse(
    val errorCode: Int,
    val message: String,
    errors: List<String>
)
