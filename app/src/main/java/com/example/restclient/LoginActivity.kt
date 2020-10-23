package com.example.restclient

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import io.ktor.client.HttpClient
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.logging.DEFAULT
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.http.HttpMethod
import io.ktor.http.takeFrom
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class LoginActivity : AppCompatActivity() {
    private val baseUrl = "https://simple-rest-serv.herokuapp.com"
    val client = HttpClient() {
        install(JsonFeature) {
            serializer = GsonSerializer()
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        submit.setOnClickListener {
            val logi: String = login.text.toString()
            val pass: String = password.text.toString()
            var authRes: AuthRes
            GlobalScope.launch(Dispatchers.IO) {
                authRes = client.post<AuthRes> {
                    url.takeFrom("${baseUrl}/api/v1/authenticate")
                    header("Content-type", "application/json")
                    body = AuthReq(logi, pass)
                    method = HttpMethod.Post
                }
                Log.d("rest.client", authRes.toString())

                val intent = Intent()
                intent.putExtra("token", authRes.token)
                setResult(1, intent)
                finish()
            }
        }
    }


}
