package com.udacity.asteroidradar.logs

import timber.log.Timber

class DebugLogTree : Timber.DebugTree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        super.log(priority, tag, message, t)
    }

    override fun createStackElementTag(element: StackTraceElement): String {
        return "${super.createStackElementTag(element)} - ${element.lineNumber}"
    }
}