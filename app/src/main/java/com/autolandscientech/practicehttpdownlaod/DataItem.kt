package com.autolandscientech.practicehttpdownlaod

import android.util.Log
import java.net.URL

data class DataItem(val albumId: Int, val id: Int, val title: String, val url: String, val thumbnailUrl: String) {

    val TAG = "DataItem"

    companion object {
        const val ALBUM_ID = "albumId"
        const val ID = "id"
        const val TITLE = "title"
        const val URL = "url"
        const val THUMBNAIL_URL = "thumbnailUrl"
    }

    fun getUrl(): URL {
        return URL(url)
    }

    fun getThumbnailUrl(): URL {
        return URL(thumbnailUrl)
    }

    override fun toString() :String {
        Log.d(TAG, String.format("id: [ %d ], \nurl: [ %s ]", id, url))
        return String.format("id: [ %d ], \nurl: [ %s ]", id, url)
    }
}