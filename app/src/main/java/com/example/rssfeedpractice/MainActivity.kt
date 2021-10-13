package com.example.rssfeedpractice

import android.os.AsyncTask
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    lateinit var rvQuestions: RecyclerView
    private var questionsArray = mutableListOf<Question>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvQuestions = findViewById(R.id.rvQuestions)


        rvQuestions.layoutManager = LinearLayoutManager(this)
        BringRecentQuestions().execute()
    }

    private inner class BringRecentQuestions : AsyncTask<Void, Void, MutableList<Question>>() {
        val parser = XMLParser()
        override fun doInBackground(vararg p0: Void?): MutableList<Question> {
            val url = URL("https://stackoverflow.com/feeds")
            val urlConnection = url.openConnection() as HttpURLConnection
            questionsArray =
                urlConnection.getInputStream()?.let {
                    parser.parse(it)
                }
                        as MutableList<Question>
            return questionsArray
        }

        override fun onPostExecute(result: MutableList<Question>?) {
            super.onPostExecute(result)
            rvQuestions.adapter = result?.let { RVquestions(it) }
           rvQuestions.adapter?.notifyDataSetChanged()
            rvQuestions.scrollToPosition(questionsArray.size - 1)

        }
    }

}
