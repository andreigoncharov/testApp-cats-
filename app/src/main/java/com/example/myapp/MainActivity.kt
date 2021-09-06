package com.example.myapp

import android.app.Activity
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import okhttp3.*
import org.json.JSONObject
import java.io.IOException


class MainActivity : AppCompatActivity() {

    private var loginET: EditText? = null
    private var passwordET: EditText? = null
    private var errorTV: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loginET = findViewById(R.id.loginET)
        passwordET = findViewById(R.id.passwordET)
        errorTV = findViewById(R.id.errorTV)
    }

    fun signIn(view: View) {
        var check: Boolean = true;
        if(loginET?.text.isNullOrEmpty()){
            loginET?.error = "Enter login"
            check = false
        }
        if(passwordET?.text.isNullOrEmpty()){
            passwordET?.error = "Enter password"
            check = false
        }
        if (check)
        {
            val client = OkHttpClient().newBuilder()
                .build()
            val mediaType: MediaType? = MediaType.parse("text/plain")
            val content: String = "{\n    \"login\":\"${loginET?.text}\",\n    \"password\":\"${passwordET?.text}\"\n}"
            val body = RequestBody.create(
                mediaType,
                content
            )
            val request: Request = Request.Builder()
                .url("http://mobile-test.itfox-web.com:80/public/testAuth")
                .method("POST", body)
                .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.i("RespAPI", "e: $e")
                }

                override fun onResponse(call: Call, response: Response) {
                    val jsonData: String = response.body()!!.string()
                    val Jobject = JSONObject(jsonData)
                    if(Jobject.has("accessToken")) {
                        var token: String  = Jobject.get("accessToken").toString()
                        val secondActivity = Intent(this@MainActivity, catsList::class.java)
                        secondActivity.putExtra("token", token)
                        startActivity(secondActivity)

                    }
                    else{
                        this@MainActivity.runOnUiThread(java.lang.Runnable {
                            errorTV?.visibility = View.VISIBLE
                        })

                    }

                }
            })
        }

    }

}