package com.example.devcommunity.splash

import android.animation.ValueAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import com.example.devcommunity.MainActivity
import com.example.devcommunity.R
import com.example.devcommunity.dashboard.CommentActivity
import com.example.devcommunity.databinding.ActivitySplashBinding
import com.example.devcommunity.login.DetailActivity
import com.example.devcommunity.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplashBinding
    lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this,R.layout.activity_splash)
        binding.root

        val animator = ValueAnimator.ofFloat(0f, 1f)
        animator.addUpdateListener {
            binding.animationView.setProgress(animator.animatedValue as Float)
        }
        animator.start()
        val label="Turning Dreams into Reality Changing the Game.."
        val stringBuilder=StringBuilder()

        Thread{
            for (i in label){
                stringBuilder.append(i)
                Thread.sleep(100)
                runOnUiThread{
                    binding.labelText.text=stringBuilder.toString()
                }
            }
        }.start()

        val txt= AnimationUtils.loadAnimation(this,R.anim.text_animation)
        binding.meetup.startAnimation(txt)
        Handler(Looper.getMainLooper()).postDelayed({
            if(FirebaseAuth.getInstance().currentUser!=null){
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }else{
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }

        },4500)
    }
}