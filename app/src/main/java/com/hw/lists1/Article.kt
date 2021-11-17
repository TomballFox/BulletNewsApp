package com.hw.lists1


sealed class Article(
    var categoryGlobal: String? = null
) {
    data class MediaArticle(
        var title: String? = null,
        var category: String? = null,
        var fullText: String? = null,
        var pubDate: String? = null,
        var description: String? = null,
        val media: MutableList<MediaContent> = mutableListOf(),
        var link:String? = null
    ) : Article(category)
    data class NoMediaArticle(
        var title: String? = null,
        var category: String? = null,
        var fullText: String? = null,
        var pubDate: String? = null,
        var description: String? = null,
        var link: String? = null
    ) : Article(category)

}
