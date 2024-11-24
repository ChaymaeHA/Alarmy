package com.example.alarmy

data class Alarm(
    val time: String,
    val date: String,
    var isActive: Boolean,
    var soundUri: String? = null  // URI de la sonnerie, par défaut null
)
