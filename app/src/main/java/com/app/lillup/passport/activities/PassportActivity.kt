package com.app.lillup.passport.activities
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.app.lillup.R
import com.app.lillup.intro.activities.IntroScreenActivity
import com.google.android.material.button.MaterialButton

class PassportActivity : AppCompatActivity() {
    private lateinit var countdownTimer: CountDownTimer
    private var totalTimeInMillis: Long = 10 * 1000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_passport)
        val progressbar:ProgressBar=findViewById(R.id.customProgressBar)
        val ivImage:ImageView=findViewById(R.id.ivImage)
        val tvDone:TextView=findViewById(R.id.tvDone)
        val tvStatus:TextView=findViewById(R.id.tvStatus)
        val customDrawable: Drawable? = ContextCompat.getDrawable(this, R.drawable.progress_bar_success_bg)

        val btnDive: MaterialButton = findViewById(R.id.btnDive) // Re
//        btnDive.setBackgroundResource(R.drawable.transprent_bg)
// place with your button ID
        btnDive.setOnClickListener {
            val intent = Intent(this@PassportActivity, IntroScreenActivity::class.java)
            startActivity(intent)
            finish()
        }
        // Set the drawable to the ProgressBar
        startTimerNew(progressbar,{
            val scaleAnimation = ScaleAnimation(
                0f, 1f,  // Start and end values for the X axis scaling
                0f, 1f,  // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0.5f,  // Pivot point of X scaling (center)
                Animation.RELATIVE_TO_SELF, 0.5f   // Pivot point of Y scaling (center)
            )
            scaleAnimation.duration = 500 // Set the duration of the animation in milliseconds
            ivImage.startAnimation(scaleAnimation)

            progressbar.progressDrawable = customDrawable;
            ivImage.visibility = View.VISIBLE;
            totalTimeInMillis=2 * 1000;
            startTimerNew(progressbar,{
                ivImage.layoutParams.height=350
                ivImage.layoutParams.width=350
                tvStatus.visibility=View.GONE
                progressbar.visibility=View.GONE
                btnDive.visibility=View.VISIBLE
                tvDone.visibility=View.VISIBLE
            })

        })

    }

    private fun startTimerNew(
        progressBar: ProgressBar,
        callback:()->Unit
    ) {
        countdownTimer = object : CountDownTimer(totalTimeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                progressBar.progress = (progressBar.progress + 10)
                progressBar.max = 100
            }

            override fun onFinish() {
                callback();

            }
        }
        countdownTimer.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        countdownTimer.cancel();
    }
}