package com.example.newoasa.utils

import platform.Foundation.NSDate

/**
 * iOS implementation of currentTimeMillis
 */
actual fun currentTimeMillis(): Long = (NSDate().timeIntervalSince1970 * 1000).toLong()
