package com.ebay.manualapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginBtn.setOnClickListener {
            tvWelcome.text = "지마켓, 옥션, G9에서\n구매내역을\n가져오고 있습니다."
            loginBtn.visibility = View.INVISIBLE
            tvWating.visibility = View.VISIBLE
            loginBar.visibility = View.VISIBLE
            Handler().postDelayed({ callback(1) }, 350)
        }
    }

    fun callback(cnt: Int) {
        when (cnt) {
            1 -> loginBar.setImageResource(R.drawable.login_bar_2)
            2 -> loginBar.setImageResource(R.drawable.login_bar_3)
            3 -> loginBar.setImageResource(R.drawable.login_bar_4)
            4 -> loginBar.setImageResource(R.drawable.login_bar_5)
            else -> {
                val intent = Intent(this, MainActivity::class.java)
                finish()
                startActivity(intent)
            }
        }

        if (cnt < 5) {
            Handler().postDelayed({ callback(cnt+1) }, 350)
        }
    }
}
