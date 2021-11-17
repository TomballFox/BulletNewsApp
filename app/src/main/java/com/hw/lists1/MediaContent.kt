package com.hw.lists1

sealed class MediaContent {
    data class Video(
        val url:String
    ):MediaContent()
    data class Picture(
        val url:String
    ):MediaContent()
}