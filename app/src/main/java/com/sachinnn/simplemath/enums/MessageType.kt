package com.sachinnn.simplemath.enums

import com.sachinnn.simplemath.R

/**
 * @author Sachin De Silva
 * @param color is colorId in colors.xml
 * @since 19-03-2022
 */
enum class MessageType(val color: Int) {
    INFO(R.color.blue),
    SUCCESS(R.color.green),
    ERROR(R.color.red)
}