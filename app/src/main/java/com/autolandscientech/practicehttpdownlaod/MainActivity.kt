package com.autolandscientech.practicehttpdownlaod

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedInputStream
import java.net.URL


class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private val JSON_DATA_URL = "https://jsonplaceholder.typicode.com/photos"

    private lateinit var context: Context
    private lateinit var requestJsonBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        context = this


        requestJsonBtn = findViewById(R.id.request_json_data_btn)

        requestJsonBtn.setOnClickListener(View.OnClickListener {

            val task: RequestTask = RequestTask(this)
            task.setOnProcess(object: RequestTask.TaskProcess {
                override val currentContext: Context
                    get() = context

                @SuppressLint("NewApi")
                override fun onRunning(result: RequestTask.Result): RequestTask.Result {
                    try {
                        val urlConnection = URL(JSON_DATA_URL).openConnection()
                        urlConnection.connectTimeout = 8000

                        val inputStream = BufferedInputStream(urlConnection.getInputStream())
                        val content: String = inputStream.bufferedReader().use { it.readText() }

                        if (content.isNotEmpty()) {
                            result.status = RequestTask.Result.SUCCESS
                            result.data = content
                            return result
                        }
                    } catch (e: Exception) {
                        Log.d(TAG, "[Error]-> [ ${ e } ].")
                        if (e.message.isNullOrEmpty()) result.message = e.message.toString()
                    }
                    result.status = RequestTask.Result.FAIL
                    return result
                }
                override fun onFinish(result: RequestTask.Result) {

                    (currentContext as Activity).runOnUiThread(Runnable {
                        request_json_data_btn.text = "test"

                        if (result.data.isEmpty()) return@Runnable

                        val jArray: JSONArray = JSONArray(result.data)

                        for (index in 0 until jArray.length()) {

                            val jObject = jArray.getJSONObject(index)

                            val albumId = jObject.getInt(DataItem.ALBUM_ID)
                            val id = jObject.getInt(DataItem.ID)
                            val title = jObject.get(DataItem.TITLE).toString()
                            val url = jObject.get(DataItem.URL).toString()
                            val thumbnailUrl = jObject.get(DataItem.THUMBNAIL_URL).toString()

                            val item = DataItem(albumId, id, title, url, thumbnailUrl)

                            item.toString()
                        }
                    })
                }

                override fun onFail(result: RequestTask.Result) {
                    Log.d(TAG, "[Error]-> [ ${result.message} ]")
                }
            }).run()

        })
    }
}