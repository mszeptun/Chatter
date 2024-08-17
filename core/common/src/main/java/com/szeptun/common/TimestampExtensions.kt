package com.szeptun.common

const val ONE_HOUR_MILLIS = 60 * 60 * 1000
const val TWENTY_SECONDS_MILLIS = 20 * 1000

fun Long.isMoreThanOneHourAgo(previousMessageTimestamp: Long): Boolean =
    this - previousMessageTimestamp > ONE_HOUR_MILLIS

fun Long.isLessThanTwentySecondsAgo(
    previousMessageTimestamp: Long,
): Boolean = this - previousMessageTimestamp < TWENTY_SECONDS_MILLIS