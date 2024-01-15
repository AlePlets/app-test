package com.app.lillup.auth.activity

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.lillup.R
import com.app.lillup.auth.adapter.LoginAdapter
import com.app.lillup.intro.activities.IntroScreenActivity
import com.app.lillup.passport.activities.PassportActivity
import com.app.lillup.utils.ClineId
import com.app.lillup.utils.WebAuthHelper
import com.google.android.material.button.MaterialButton



class LoginActivity : AppCompatActivity() {
    lateinit var relativeLayout:RelativeLayout
    lateinit var llRoot:LinearLayout
    lateinit var ivImageView:ImageView
    var customDrawable: Drawable?=null
    lateinit var progressbar: ProgressBar
    lateinit var recyclerView:RecyclerView
    lateinit var tvDone: TextView
    lateinit var ivImage: ImageView
    lateinit var tvStatus: TextView
    lateinit var btnDive: MaterialButton


    private lateinit var countdownTimer: CountDownTimer
    private var totalTimeInMillis: Long = 10 * 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)
        initViews()
        recyclerView.adapter=LoginAdapter()
        animationDefaultUI()
        initActions()

    }
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

//        Log.e("loginV1","New Intent ${intent?.data}");
        WebAuthHelper.web3Auth?.let {
            it.setResultUrl(intent?.data)
        }


        intent?.extras?.let { bundle ->
            for (key in bundle.keySet()) {
                Log.e("loginV1", "Extra: $key = ${bundle[key]}")
            }
        }
//        web3Auth.setResultUrl(intent?.data)
    }
    private fun initActions() {
        ivImageView.setOnClickListener {
                WebAuthHelper.loginV2({
                    val intent = Intent(this@LoginActivity, PassportActivity::class.java)
                    startActivity(intent)
                })
//            showPassport()
//            startActivity(Intent(this@LoginActivity,PassportActivity::class.java))
        }
        btnDive.setOnClickListener {
            val intent = Intent(it.context, IntroScreenActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun animationDefaultUI() {
        Handler().postDelayed({
            relativeLayout.visibility = View.VISIBLE
        }, 1000)
    }

    private fun initViews() {
        relativeLayout = findViewById(R.id.relativeLayout)
        llRoot = findViewById(R.id.llRoot)
        ivImageView = findViewById(R.id.ivImageView)
        recyclerView = findViewById(R.id.recyclerView)
        progressbar = findViewById(R.id.customProgressBar)
        ivImage = findViewById(R.id.ivImage)
        tvDone = findViewById(R.id.tvDone)
        tvStatus = findViewById(R.id.tvStatus)
        btnDive = findViewById(R.id.btnDive)
        customDrawable = ContextCompat.getDrawable(this, R.drawable.progress_bar_success_bg)
        progressbar.max=100
    }

    fun showPassport(){
//        val fragmentManager = supportFragmentManager
//        val fragmentTransaction = fragmentManager.beginTransaction()
//
//        val passportFragment = PassportFragment()
//        fragmentTransaction.replace(R.id.llRoot, passportFragment)
//        fragmentTransaction.addToBackStack(null)
//        fragmentTransaction.commit()

        WebAuthHelper.login({
            runOnUiThread({
                val scaleAnimation = ScaleAnimation(
                    0f, 1f,
                    0f, 1f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f
                )
                scaleAnimation.duration = 500
                ivImage.startAnimation(scaleAnimation)

                progressbar.progressDrawable = customDrawable
                ivImage.visibility = View.VISIBLE
                totalTimeInMillis = 2 * 1000
                startTimerNew(progressbar, {
                    ivImage.layoutParams.height = 350
                    ivImage.layoutParams.width = 350
                    tvStatus.visibility = View.GONE
                    progressbar.visibility = View.GONE
                    btnDive.visibility = View.VISIBLE
                    tvDone.visibility = View.VISIBLE
                })
            })


        },{
            Toast.makeText(this,"${it}",Toast.LENGTH_LONG).show()
        },{
            runOnUiThread({
                progressbar.progress=(progressbar.progress)
            })
        })

        relativeLayout.visibility=View.GONE
        ivImageView.visibility=View.GONE
        llRoot.visibility=View.VISIBLE

    }
    fun hidePassport(){
        relativeLayout.visibility=View.VISIBLE
        ivImageView.visibility=View.VISIBLE
        llRoot.visibility=View.GONE
    }
    private fun startTimerNew(
        progressBar: ProgressBar,
        callback: () -> Unit
    ) {
        countdownTimer = object : CountDownTimer(totalTimeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                progressBar.progress = (progressBar.progress + 10)
                progressBar.max = 100
            }

            override fun onFinish() {
                callback()
            }
        }
        countdownTimer.start()
    }
    override fun onDestroy() {
        super.onDestroy()
        countdownTimer.cancel()
    }
    fun loadProgress(){
        startTimerNew(progressbar, {
            val scaleAnimation = ScaleAnimation(
                0f, 1f,
                0f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
            )
            scaleAnimation.duration = 500
            ivImage.startAnimation(scaleAnimation)

            progressbar.progressDrawable = customDrawable
            ivImage.visibility = View.VISIBLE
            totalTimeInMillis = 2 * 1000
            startTimerNew(progressbar, {
                ivImage.layoutParams.height = 350
                ivImage.layoutParams.width = 350
                tvStatus.visibility = View.GONE
                progressbar.visibility = View.GONE
                btnDive.visibility = View.VISIBLE
                tvDone.visibility = View.VISIBLE
            })
        })
    }
}