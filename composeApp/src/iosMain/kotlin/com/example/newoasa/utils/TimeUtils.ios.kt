package com.example.newoasa.utils

import platform.Foundation.NSDate

/**
 * iOS implementation of currentTimeMillis
 * In Kotlin/Native, Objective-C properties are accessed as functions
 */
actual fun currentTimeMillis(): Long {
    return (NSDate().timeIntervalSince1970() * 1000).toLong()
}
