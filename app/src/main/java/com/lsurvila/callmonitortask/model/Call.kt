package com.lsurvila.callmonitortask.model

data class Call(val incomingCall: Boolean, val number: String?, val name: String? = null)