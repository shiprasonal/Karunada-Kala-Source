package com.karunadakala.source.model

/**
 * Offline catalog entry for a cultural workshop offered by an artisan cluster.
 */
data class WorkshopProgram(
    val id: String,
    val title: String,
    val artForm: String,
    val hostName: String,
    val district: String,
    val schedule: String,
    val duration: String,
    val feeInr: Int,
)
