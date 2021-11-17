package com.hw.lists1

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ArticleViewModel : ViewModel() {
    private val repository = ArticlesLoader()
    private val news = MutableLiveData<Pair<MutableList<Article>, MutableSet<String>>>()

    val newsOutput: LiveData<Pair<MutableList<Article>, MutableSet<String>>>
        get() = news

    private val links = listOf(
        "https://www.vesti.ru/vesti.rss",
        //"http://www.aif.ru/rss/auto.php",
        "https://www.kommersant.ru/RSS/news.xml",
        "https://www.ixbt.com/export/news.rss",
        "https://rg.ru/tema/rss.xml",
        //"http://gazeta.ru/export/rss/busnews.xml",
        "https://3dnews.ru/news/rss/",
        "https://www.fontanka.ru/fontanka.rss",
        "https://news.mail.ru/rss/main/24/",
        "https://lenta.ru/rss/news",
        "http://static.feed.rbc.ru/rbc/logical/footer/news.rss",
        "https://vc.ru/rss/new",
        "https://www.cnews.ru/inc/rss/news.xml",
        "http://tass.ru/rss/v2.xml"

    )

    fun requestData() {

        repository.getXml(links) {
            news.postValue(it)
        }

    }


}