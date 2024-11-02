package com.cursokotlin.movieapp.ddl.models

/*
data class ErrorState (
    val isError: Boolean = false,
    val message: String = ""
)*/

data class ErrorState (
    val isError: Boolean = false,
    val title: String = "",
    val message: String = ""
)