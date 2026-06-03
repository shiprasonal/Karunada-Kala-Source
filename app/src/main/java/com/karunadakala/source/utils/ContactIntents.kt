package com.karunadakala.source.utils

import android.content.Context
import android.content.Intent
import android.net.Uri

fun Context.openDialer(phoneRaw: String) {
    val trimmed = phoneRaw.trim()
    if (trimmed.isEmpty()) return
    val uri = Uri.parse("tel:${Uri.encode(trimmed)}")
    val intent = Intent(Intent.ACTION_DIAL, uri)
    startActivity(intent)
}

fun Context.openWhatsAppChat(phoneRaw: String) {
    val digits = phoneRaw.filter { it.isDigit() }
    if (digits.isEmpty()) return
    val uri = Uri.parse("https://wa.me/$digits")
    val intent = Intent(Intent.ACTION_VIEW, uri)
    startActivity(intent)
}
