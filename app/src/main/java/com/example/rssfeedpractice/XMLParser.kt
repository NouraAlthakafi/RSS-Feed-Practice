package com.example.rssfeedpractice

import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import java.io.InputStream

class XMLParser{
    private val qr: String? = null

    fun parse(inputStream: InputStream): ArrayList<Question>{
        inputStream.use { inputStream ->
            val parser: XmlPullParser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(inputStream, null)
            parser.nextTag()
            return readRecentQuestionsRssFeed(parser)
        }
    }
    private fun readRecentQuestionsRssFeed(parser: XmlPullParser): ArrayList<Question>{
        val questionsRecent = ArrayList<Question>()

        parser.require(XmlPullParser.START_TAG, qr, "feed")

        while(parser.next() != XmlPullParser.END_TAG){

            if(parser.eventType != XmlPullParser.START_TAG){
                continue
            }
            if(parser.name == "entry"){
                parser.require(XmlPullParser.START_TAG, qr, "entry")
                var id: String? = null
                var title: String? = null
                while(parser.next() != XmlPullParser.END_TAG){
                    if(parser.eventType != XmlPullParser.START_TAG){
                        continue
                    }
                    when (parser.name){
                        "id" -> id = readId(parser)
                        "title" -> title = readTitle(parser)
                        else -> skip(parser)
                    }
                }
                questionsRecent.add(Question(id, title))
            } else {
                skip(parser)
            }
        }
        return questionsRecent
    }
    private fun readId(parser: XmlPullParser): String{
        parser.require(XmlPullParser.START_TAG, qr, "id")
        val id = readText(parser)
        parser.require(XmlPullParser.END_TAG, qr, "id")
        return id
    }private fun readTitle(parser: XmlPullParser): String{
        parser.require(XmlPullParser.START_TAG, qr, "title")
        val title = readText(parser)
        parser.require(XmlPullParser.END_TAG, qr, "title")
        return title
    }
    private fun readText(parser: XmlPullParser): String{
var result = ""
        if(parser.next() == XmlPullParser.TEXT){
            result = parser.text
            parser.nextTag()
        }
        return result
    }
    private fun skip(parser: XmlPullParser){
        if(parser.eventType != XmlPullParser.START_TAG){
throw IllegalStateException()
        }
        var deep = 1
        while (deep != 0){
when(parser.next()){
    XmlPullParser.END_TAG -> deep--
    XmlPullParser.START_TAG -> deep++
}
        }
    }
}