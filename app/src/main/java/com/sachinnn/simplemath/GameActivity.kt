package com.sachinnn.simplemath

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sachinnn.simplemath.enums.ButtonTypes
import com.sachinnn.simplemath.enums.MessageType
import com.sachinnn.simplemath.model.Expression
import com.sachinnn.simplemath.helper.showCustomToast
import kotlin.random.Random

private var TAG = "GameActivity"


//static variables
private var TERM_RANGE = (0..3)
private var NUMBER_RANGE = (1..20)
private var OPERATORS = listOf("/", "x", "+", "-")
private var equationList: MutableList<String> = mutableListOf()

private var SUCCESS_MESSAGES =
    listOf("Correct!", "Good Game!", "Cheers!", "Damn!", "Awesome!", "You Rock!")
private var ERROR_MESSAGES =
    listOf("Wrong!", "Opps!", "Never Give Up!", "Try Again!", "C'mon!", "You got this wrong!")

class GameActivity : AppCompatActivity() {
    private lateinit var prefs: SharedPreferences

    //initialising objects
    private lateinit var viewScore: TextView
    private lateinit var viewTime: TextView
    private lateinit var viewLeftEquation: TextView
    private lateinit var viewRightEquation: TextView
    private lateinit var btnGreater: Button
    private lateinit var btnEqual: Button
    private lateinit var btnLess: Button
    private lateinit var countDownTimer: CountDownTimer

    private val initTime: Long = 50000  //in milliseconds (x) =>{x*1000}
    private val cdInternal: Long = 1000  //in milliseconds (x) =>{x*1000}

    private var leftTime: Long = initTime  //in milliseconds (x) =>{x*1000}
    private var correct: Int = 0
    private var total: Int = 0
    private lateinit var leftEquation: Expression
    private lateinit var rightEquation: Expression
    private var isGameStarted = false


    //overriding onCreate()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        //hiding action bar since its useless and boring in this activity
        supportActionBar?.hide()

        //assigning objs
        viewScore = findViewById(R.id.view_score)
        viewTime = findViewById(R.id.view_time)
        viewLeftEquation = findViewById(R.id.view_left_equation)
        viewRightEquation = findViewById(R.id.view_right_equation)
        btnGreater = findViewById(R.id.btn_greater)
        btnEqual = findViewById(R.id.btn_equal)
        btnLess = findViewById(R.id.btn_less)

        btnGreater.setOnClickListener { clickAnswerButton(ButtonTypes.GREATER) }
        btnEqual.setOnClickListener { clickAnswerButton(ButtonTypes.EQUAL) }
        btnLess.setOnClickListener { clickAnswerButton(ButtonTypes.LESS) }

        prefs = getSharedPreferences(getString(R.string.shared_pref), MODE_PRIVATE)
        isGameStarted = prefs.getBoolean("isGameStarted", isGameStarted)

        //check if there's an incomplete/paused game
        if (isGameStarted) {
            total = prefs.getInt("total", total)
            correct = prefs.getInt("correct", correct)
            leftTime = prefs.getLong("leftTime", initTime)
            leftEquation = Expression(
                prefs.getString("leftEquationValue", "").toString(),
                prefs.getInt("leftEquationResult", 0)
            )
            rightEquation = Expression(
                prefs.getString("rightEquationValue", "").toString(),
                prefs.getInt("rightEquationResult", 0)
            )
        } else {
            //generatingNewQuestions
            prepareNewQuestion()
        }
        //show elements display
        setUpDisplay()
        //count down start
        startTimer(leftTime)
    }



    //will be calling when user back btn pressed
    override fun onBackPressed() {
        super.onBackPressed()
        //calling super finish() to destroy the activity
        finish()
    }

    //overriding activity destroy
    override fun onDestroy() {
        super.onDestroy()
        //call save data
        flushData()
        Log.d(TAG, "destroyed")
    }

    //creating AboutActivity and starting it
    private fun startTimer(initTime: Long) {
        Log.d(TAG, "leftTime = $initTime")
        //initializing & assigning new objects
        countDownTimer = object : CountDownTimer(initTime, cdInternal) {
            init {
                Log.d(TAG, "new CountDownTimer Created")
            }
            //calling on every interval
            override fun onTick(leftMillis: Long) {
//                Log.d(TAG, "TickTock")
                leftTime = leftMillis
                viewTime.text = getString(R.string.txt_time, leftMillis / 1000)
            }
            //calling this when time is over
            override fun onFinish() {
                //updating the state
                isGameStarted = false
                createResultActivity()
                //calling super finish() to destroy the activity
                finish()
            }
        }
        //starting count down
        countDownTimer.start()
    }

    // buttons on click trigger event
    private fun clickAnswerButton(btnOption: ButtonTypes) {
        //identifying the answer
        val answer = when (leftEquation.result.compareTo(rightEquation.result)) {
            +1 -> ButtonTypes.GREATER
            0 -> ButtonTypes.EQUAL
            -1 -> ButtonTypes.LESS
            else -> ButtonTypes.EQUAL
        }
        if (btnOption == answer) {
            correct++
            //showing a success custom toast
            Toast(this).showCustomToast(
                this,
                MessageType.SUCCESS,
                SUCCESS_MESSAGES.random(),
                Toast.LENGTH_SHORT
            )
            if (correct % 2 == 0) {
                countDownTimer.cancel()
                leftTime += 10 * 1000
                startTimer(leftTime)
            }
        } else {
            //showing a error custom toast
            Toast(this).showCustomToast(
                this,
                MessageType.ERROR,
                ERROR_MESSAGES.random(),
                Toast.LENGTH_SHORT
            )
        }
        //calling to generate next questions
        prepareNewQuestion()
    }

    //calling summary activity
    private fun createResultActivity() {
        //save data
        flushData()
        //calling intent
        val intent = Intent(this, ResultActivity::class.java)
        startActivity(intent)
    }

    //saving data to shared pref
    private fun flushData() {
        if (isGameStarted) {
            countDownTimer.cancel()
        }
        val editor = prefs.edit()
        editor.putBoolean("isGameStarted", isGameStarted)
        editor.putInt("total", total)
        editor.putInt("correct", correct)
        editor.putLong("leftTime", leftTime)
        editor.putString("leftEquationValue", leftEquation.value)
        editor.putInt("leftEquationResult", leftEquation.result)
        editor.putString("rightEquationValue", rightEquation.value)
        editor.putInt("rightEquationResult", rightEquation.result)

        editor.apply()
    }

    private fun prepareNewQuestion() {
        total++
        leftEquation = generateEquation()
        rightEquation = generateEquation()
        setUpDisplay()
    }

    private fun setUpDisplay() {
        viewScore.text = getString(R.string.txt_score, correct, total)
        viewLeftEquation.text = leftEquation.value
        viewRightEquation.text = rightEquation.value
        isGameStarted = true
    }

    //creates a simple equation n returning the value and the result as an Expression object
    private fun generateEquation(): Expression {
        //random term count
        val termCount = TERM_RANGE.random()
        var tempExpression: Expression? = null
        // if term == 1 then returning a single term element
        if (termCount == 0) {
            tempExpression = Expression()
            tempExpression.result = NUMBER_RANGE.random()
            tempExpression.value = tempExpression.result.toString()
        }
        //if term >= 1 then calling recursively till term count satisfies and generating a unique simple expression
        if (termCount > 0) {
            for (i in 0 until termCount) {
                //calling to generate sub expression
                tempExpression = generateSubComp(tempExpression)
            }
        }
        //checking if generated expression is unique if not generating again else adding to the list
        if (equationList.contains(tempExpression!!.value)) {
            return generateEquation()
        } else {
            equationList.add(tempExpression.value)
        }
        return tempExpression
    }

    //creating a sub equation
    //sending null in lead will auto assign a value to lead
    private fun generateSubComp(lead: Expression?): Expression {
        val equation: String
        val expression = Expression()
        val result: Int
        val operator = OPERATORS[Random.nextInt(OPERATORS.size)]
        val num1Value: String
        val num1Result: Int
        if (lead == null) {
            num1Value = NUMBER_RANGE.random().toString()
            num1Result = num1Value.toInt()
        } else {
            num1Value = "(" + lead.value + ")"
            num1Result = lead.result
        }
        val num2 = NUMBER_RANGE.random().toString()

        //checking if division returns 0
        if (operator == "/") {
            if (num1Result % num2.toInt() != 0) {
                return generateSubComp(lead)
            }
        }
        //checking if 0 < expression result < 100
        result = getEquationResult(num1Result, operator, num2.toInt())
        if (result > 100 || result < 0) {
            return generateSubComp(lead)
        }
        equation = "$num1Value $operator $num2"

        expression.value = equation
        expression.result = result
        return expression
    }

    //doing the math here
    private fun getEquationResult(num1: Int, operator: String, num2: Int): Int {
        return when (operator) {
            "+" -> num1 + num2
            "-" -> num1 - num2
            "x" -> num1 * num2
            "/" -> num1 / num2
            else -> 0
        }
    }
}