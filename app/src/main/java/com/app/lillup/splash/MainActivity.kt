package com.app.lillup.splash

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import com.app.lillup.R
import com.app.lillup.auth.activity.LoginActivity
import com.app.lillup.intro.activities.IntroScreenActivity
import com.app.lillup.utils.ClineId
import com.app.lillup.utils.WebAuthHelper
import java.util.Timer
import java.util.TimerTask

class MainActivity : AppCompatActivity() {
    lateinit var linearLayout: LinearLayout
    private lateinit var countdownTimer: CountDownTimer
    private var totalTimeInMillis: Long = 3 * 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        WebAuthHelper.initAuth(this,intent, ClineId)

        val logoImageView: ImageView = findViewById(R.id.logoImageView)
        val appNameTextView: TextView = findViewById(R.id.appNameTextView)

        linearLayout = findViewById(R.id.linearLayout)
        // Fade in animation for the text
        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.duration = 2000 // You can adjust the duration as needed
        fadeIn.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                val timer = Timer()

                // Schedule a task to run after 1 minute (60,000 milliseconds)
                timer.schedule(object : TimerTask() {
                    override fun run() {
                        // Your code to be executed after 1 minute
                        runOnUiThread {
                            val intent = Intent(this@MainActivity, LoginActivity::class.java)

                            // Set up a bundle for shared element transition
                            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                                this@MainActivity,
                                logoImageView,
                                getString(R.string.transition_logo)
                            )

                            // Start the LoginActivity with the shared element transition
//                            startActivity(intent)
                    startActivity(intent, options.toBundle())
                            finish()
                        }
                    }
                }, 3000) // 60,000 mi
//                Handler().postDelayed({
//                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
//
//                    // Set up a bundle for shared element transition
//                    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
//                        this@MainActivity,
//                        logoImageView,
//                        getString(R.string.transition_logo)
//                    )
//
//                    // Start the LoginActivity with the shared element transition
//                    startActivity(intent, options.toBundle())
//                    finish()
//                },3000)
//                countdownTimer =object : CountDownTimer(totalTimeInMillis, 1000) {
//                    //This will give you 30 sec timer with each tick at 1 second
//                    override fun onTick(millisUntilFinished: Long) {
//
//                    }
//                    override fun onFinish() {
//                        val intent = Intent(this@MainActivity, LoginActivity::class.java)
//
//                        // Set up a bundle for shared element transition
//                        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
//                            this@MainActivity,
//                            logoImageView,
//                            getString(R.string.transition_logo)
//                        )
//
//                        // Start the LoginActivity with the shared element transition
//                        startActivity(intent, options.toBundle())
//                        finish()
//                    }
//                }
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })

        // Start the animation on the text view
        appNameTextView.startAnimation(fadeIn)
    }

    override fun onDestroy() {
        super.onDestroy()
//        countdownTimer.cancel();
    }
}
