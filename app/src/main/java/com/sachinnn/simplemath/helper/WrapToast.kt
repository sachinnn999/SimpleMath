package com.sachinnn.simplemath.helper

import android.app.Activity
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.sachinnn.simplemath.R
import com.sachinnn.simplemath.enums.MessageType


/**
 * @author Sachin De Silva
 * @since 19-03-2022
 *
 *  ref - https://www.geeksforgeeks.org/how-to-add-a-custom-styled-toast-in-android-using-kotlin/
 * referred on - 19-03-2022 21:30
 * comment - changed according to my requirement
 */
fun Toast.showCustomToast(activity: Activity, messageType: MessageType ,message: String, time: Int)
{
    //inflating layout
    val layout = activity.layoutInflater.inflate (
        R.layout.custom_toast_layout,
        activity.findViewById(R.id.toast_container)
    )

    // set the text of the TextView of the message
    val textView = layout.findViewById<TextView>(R.id.toast_text)
    textView.text = message

    //set frame colour on request
    val frame = layout.findViewById<FrameLayout>(R.id.frame_accent_border)
    frame?.setBackgroundColor(ContextCompat.getColor(activity, messageType.color))

    // use the application extension function
    this.apply {
        setGravity(Gravity.TOP, 40, 40)
        duration = time
        view = layout
        show()
    }
}

