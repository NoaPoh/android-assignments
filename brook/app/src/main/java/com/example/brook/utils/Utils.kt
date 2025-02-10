package com.example.brook.utils

import java.util.regex.Pattern

object Utils {
    @JvmStatic
    fun timeToString(timeInSeconds: Long): String {
        val hours = timeInSeconds / 3600
        val minutes = (timeInSeconds % 3600) / 60
        val seconds = timeInSeconds % 60

        var timeString = ""

        if (hours > 0) {
            if (hours == 1L) {
                timeString += "שעה אחת "
            } else {
                timeString += hours.toString() + " שעות "
            }

            if (minutes > 0) {
                timeString += "ו"
            }
        }

        if (minutes > 0) {
            if (minutes == 1L) {
                timeString += "דקה אחת "
            } else {
                timeString += minutes.toString() + " דקות "
            }

            if (seconds > 0) {
                timeString += "ו"
            }
        }

        if (seconds > 0) {
            if (seconds == 1L) {
                timeString += " שנייה אחת"
            } else {
                timeString += seconds.toString() + " שניות "
            }
        }

        if (timeString == "") {
            timeString = "בלי זמן"
        }

        return timeString
    }

    @JvmStatic
    fun patternMatches(emailAddress: String): Boolean {
        return Pattern.compile("^(.+)@(\\S+)$")
            .matcher(emailAddress)
            .matches()
    }
}
