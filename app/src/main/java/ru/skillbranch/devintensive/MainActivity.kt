package ru.skillbranch.devintensive

import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.PersistableBundle
import android.text.InputType
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import ru.skillbranch.devintensive.extensions.hideKeyboard
import ru.skillbranch.devintensive.extensions.isKeyboardOpen
import ru.skillbranch.devintensive.models.Bender

class MainActivity : AppCompatActivity() , View.OnClickListener , TextView.OnEditorActionListener {


    lateinit var benderImage: ImageView
    lateinit var textTv: TextView
    lateinit var messageEt: EditText
    lateinit var sendBtn: ImageView
    lateinit var benderObg: Bender

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        benderImage = iv_bender
        textTv = tv_text
        messageEt = et_message
        messageEt.setOnEditorActionListener(this)
        sendBtn = iv_send
        
        val status = savedInstanceState?.getString("STATUS") ?: Bender.Status.NORMAL.name
        val question = savedInstanceState?.getString("QUESTION") ?: Bender.Question.NAME.name
        
        benderObg = Bender(Bender.Status.valueOf(status) , Bender.Question.valueOf(question))
        Log.d("M_MainActivity", "onCreate $status $question")

        val (r,g,b) = benderObg.status.color
        benderImage.setColorFilter(Color.rgb(r,g,b) , PorterDuff.Mode.MULTIPLY)

        textTv.text = benderObg.askQuestion()

        sendBtn.setOnClickListener(this)
        
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("M_MainActivity", "onRestart")
    }

    override fun onStart() {
        super.onStart()
        Log.d("M_MainActivity", "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("M_MainActivity", "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("M_MainActivity", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("M_MainActivity", "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("M_MainActivity", "onDestroy")
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        
        outState?.putString("STATUS" , benderObg.status.name)
        outState?.putString("QUESTION" , benderObg.question.name)
        Log.d("M_MainActivity", "onSaveInstanceState ${benderObg.status.name} ${benderObg.question.name}")

    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.iv_send) {
            setText()
        }
    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        return if (actionId == EditorInfo.IME_ACTION_DONE) {
            setText()
            true
        } else {
            false
        }
    }

    private fun setText() {
        val (phrase , color) = benderObg.listenAnswer(messageEt.text.toString())
        messageEt.setText("")
        val (r,g,b) = color
        benderImage.setColorFilter(Color.rgb(r,g,b) , PorterDuff.Mode.MULTIPLY)
        textTv.text = phrase
        hideKeyboard()
    }
}
