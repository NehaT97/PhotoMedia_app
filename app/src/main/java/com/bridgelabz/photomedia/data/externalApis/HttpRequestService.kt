package com.bridgelabz.photomedia.data.externalApis

import android.util.Log
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class HttpRequestService {


    @Throws(IOException::class)
    fun get(url: String): String {
        val connectionUrl = URL(url)
        val connection = connectionUrl.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        val responseCode = connection.responseCode
        Log.i("Http GET Response Code", "$responseCode")
        return getResponse(responseCode, connection)
    }

    private fun getResponse(
        responseCode: Int,
        connection: HttpURLConnection
    ): String {
        return if (responseCode == HttpURLConnection.HTTP_OK) {
            val input = BufferedReader(InputStreamReader(connection.inputStream))
            var inputLine: String?
            val response = StringBuffer()
            while (input.readLine().also { inputLine = it } != null) {
                response.append(inputLine)
            }
            input.close()
            Log.i("API Response", "Post API Response : ${response.toString()}")
            response.toString()
        } else {
            ""
        }
    }

    @Throws(IOException::class)
    fun post(url: String, postBody: JSONObject): String {
        val connectionUrl = URL(url)
        val connection = connectionUrl.openConnection() as HttpURLConnection
        connection.readTimeout = 3000
        connection.connectTimeout = 3000
        connection.requestMethod = "POST"
        connection.doInput = true
        connection.doOutput = true
        val outputStream = connection.outputStream
        val writer = BufferedWriter(OutputStreamWriter(outputStream, "UTF-8"))
        writer.write(encodeParams(postBody))
        writer.flush()
        writer.close()
        outputStream.close()
        val responseCode = connection.responseCode
        Log.i("Http POST Response Code", "$responseCode")
        return getResponse(responseCode, connection)
    }

    @Throws(IOException::class)
    private fun encodeParams(params: JSONObject): String? {
        val result = StringBuilder()
        var first = true
        val itr = params.keys()
        while (itr.hasNext()) {
            val key = itr.next()
            val value = params[key]
            if (first) first = false else result.append("&")
            result.append(URLEncoder.encode(key, "UTF-8"))
            result.append("=")
            result.append(URLEncoder.encode(value.toString(), "UTF-8"))
        }
        return result.toString()
    }
}