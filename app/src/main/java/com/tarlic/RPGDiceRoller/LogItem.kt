package com.tarlic.RPGDiceRoller

import java.util.*

class LogItem {
    var text: String? = null
    var date: Date? = null

    constructor() : super() {}
    constructor(text: String?, date: Date?) : super() {
        this.text = text
        this.date = date
    }
}