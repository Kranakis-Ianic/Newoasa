package com.example.newoasa.utils

import platform.Foundation.NSDate

/**
 * iOS implementation of currentTimeMillis
 * NSDate.timeIntervalSince1970 is a property, not a method
 */
actual fun currentTimeMillis(): Long {
    return (NSDate().timeIntervalSince1970 * 1000).toLong()
}
