package com.example.quizapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.lifecycle.ViewModelProvider


private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"
private const val REQUEST_CODE_CHEAT = 0


class MainActivity : AppCompatActivity() {
    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProvider(this).get(QuizViewModel::class.java)
    }

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: Button
    private lateinit var prevButton: Button
    private lateinit var cheatButton: Button
    private lateinit var questionTextView: TextView
    private var rightAnswerCounter: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle) called")
        setContentView(R.layout.activity_main)
        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        quizViewModel.currentIndex = currentIndex


        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        prevButton = findViewById(R.id.prev_button)
        cheatButton = findViewById(R.id.cheat_button)
        questionTextView = findViewById(R.id.question_text_view)

        trueButton.setOnClickListener { view: View ->
            checkAnswer(true)
            falseButton.setAlpha(.5f)
            falseButton.isClickable = false
        }
        falseButton.setOnClickListener {
            checkAnswer(false)
            trueButton.alpha = 0.5f
            trueButton.isClickable = false
        }
        nextButton.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
            getButtonsActiveStatus()

        }
        prevButton.setOnClickListener {
            quizViewModel.moveToPrev()
            updateQuestion()
            getButtonsActiveStatus()
        }
        cheatButton.setOnClickListener {
       val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            startActivityForResult(intent, REQUEST_CODE_CHEAT)
        }

        updateQuestion()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            quizViewModel.isCheater =
                data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
        }
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG, "onSaveInstanceState")
        savedInstanceState.putInt(KEY_INDEX, quizViewModel.currentIndex)
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    private fun getButtonsActiveStatus() {
        falseButton.setAlpha(1f)
        falseButton.isClickable = true
        trueButton.alpha = 1f
        trueButton.isClickable = true
    }


    private fun updateQuestion() {

        val questionTextRes = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextRes)
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer

        val messageResId = when {
            quizViewModel.isCheater -> R.string.judgment_toast
            userAnswer == correctAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
        }
        if (userAnswer == correctAnswer)

            if (userAnswer == correctAnswer)
                rightAnswerCounter++
        if (quizViewModel.currentIndex == quizViewModel.currentQuizBankSize - 1)

            Toast.makeText(
                this,
                "Right answers ${Math.round((rightAnswerCounter.toFloat() / quizViewModel.currentQuizBankSize) * 100)} %",
                Toast.LENGTH_SHORT
            ).show()
        else
            Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }
}