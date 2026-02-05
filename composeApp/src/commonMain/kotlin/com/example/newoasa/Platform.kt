package com.example.newoasa

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform