package com.sachinnn.simplemath.model

class Expression {
    var value = ""
    var result = 0

    constructor() {}
    constructor(value: String, result: Int) {
        this.value = value
        this.result = result
    }
    override fun toString(): String {
        return "value $value \nresult $result"
    }
}