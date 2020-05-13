package com.stayfit.config

import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.squareup.okhttp.*
import org.json.JSONObject
import java.io.IOException


abstract class BaseHTTPAction : AppCompatActivity() {

    abstract fun responseRunnable(response: String?): Runnable?

    companion object {
        private const val TAG = "BaseAction"
    }

    /**
     *
     */
    var baseURL = "https://us-central1-stayfit-87c1a.cloudfunctions.net"

    val JSON = MediaType.parse("application/json; charset=utf-8")

    /**
     *
     */
    fun sendMessageToCloudFunction(httpBuilder: HttpUrl.Builder) {
        val httpClient = OkHttpClient()
        val request: Request = Request.Builder().url(httpBuilder.build()).build()
        httpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(request: Request?, e: IOException?) {
                Log.e(
                    TAG,
                    "error response firebase cloud functions"
                )
                runOnUiThread(Runnable {
                    Toast.makeText(
                        this@BaseHTTPAction,
                        "Action failed, please try again",
                        Toast.LENGTH_SHORT
                    ).show()
                })
            }

            @Throws(IOException::class)
            override fun onResponse(response: Response?) {
                val responseBody: ResponseBody? = response?.body()
                var resp = ""
                if (response != null) {
                    if (!response.isSuccessful()) {
                        Log.e(TAG, "action failed")
                        resp = "Failed perform the action, please try again"
                    } else {
                        try {
                            if (responseBody != null) {
                                resp = responseBody.string()
                            }
                            Log.e(TAG, "Response $resp")
                        } catch (e: IOException) {
                            resp = "Problem in reading resposne"
                            Log.e(
                                TAG,
                                "Problem in reading response $e"
                            )
                        }
                    }
                }
                runOnUiThread(responseRunnable(resp))
            }
        })
    }

    fun sendGetToCloudFunction(httpBuilder: HttpUrl.Builder)
    {
        Log.d(TAG, "sendGetToCloudFunction: $httpBuilder")
        val httpClient = OkHttpClient()

        val request: Request = Request.Builder().url(httpBuilder.build()).get().build()

        httpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(request: Request?, e: IOException?) {
                Log.e(
                    TAG,
                    "error response firebase cloud functions"
                )
                runOnUiThread(Runnable {
                    Toast.makeText(
                        this@BaseHTTPAction,
                        "Action failed, please try again",
                        Toast.LENGTH_SHORT
                    ).show()
                })
            }

            @Throws(IOException::class)
            override fun onResponse(response: Response?) {
                val responseBody: ResponseBody? = response?.body()
                var resp = ""
                if (response != null) {
                    if (!response.isSuccessful()) {
                        Log.e(TAG, "action failed")
                        resp = "Failed perform the action, please try again"
                    } else {
                        try {
                            if (responseBody != null) {
                                resp = responseBody.string()
                            }
                            Log.e(TAG, "Response $resp")
                        } catch (e: IOException) {
                            resp = "Problem in reading resposne"
                            Log.e(
                                TAG,
                                "Problem in reading response $e"
                            )
                        }
                    }
                }
                runOnUiThread(responseRunnable(resp))
            }
        })
    }

    /**
     *
     */
    fun sendPostToCloudFunction(httpBuilder: HttpUrl.Builder, body: RequestBody)
    {
        val httpClient = OkHttpClient()

        val request: Request = Request.Builder().url(httpBuilder.build()).post(body).build()
        httpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(request: Request?, e: IOException?) {
                Log.e(
                    TAG,
                    "error response firebase cloud functions"
                )
                runOnUiThread(Runnable {
                    Toast.makeText(
                        this@BaseHTTPAction,
                        "Action failed, please try again",
                        Toast.LENGTH_SHORT
                    ).show()
                })
            }

            @Throws(IOException::class)
            override fun onResponse(response: Response?) {
                val responseBody: ResponseBody? = response?.body()
                var resp = ""
                if (response != null) {
                    if (!response.isSuccessful()) {
                        Log.e(TAG, "action failed")
                        resp = "Failed perform the action, please try again"
                    } else {
                        try {
                            if (responseBody != null) {
                                resp = responseBody.string()
                            }
                            Log.e(TAG, "Response $resp")
                        } catch (e: IOException) {
                            resp = "Problem in reading resposne"
                            Log.e(
                                TAG,
                                "Problem in reading response $e"
                            )
                        }
                    }
                }
                runOnUiThread(responseRunnable(resp))
            }
        })
    }
}