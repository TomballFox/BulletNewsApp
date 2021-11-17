package com.hw.lists1

import android.R
import android.content.Context
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import org.w3c.dom.Document
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.xml.sax.InputSource

import java.net.URL
import java.util.*
import java.util.regex.Pattern
import javax.xml.parsers.DocumentBuilderFactory


class ArticlesLoader() {

    private lateinit var articles: MutableList<Article>
    private lateinit var category: MutableSet<String>
    fun getXml(links: List<String>,
        onReady: (news: Pair<MutableList<Article>, MutableSet<String>>) -> Unit
    ) {
        articles = mutableListOf()
        category = mutableSetOf()
        val documentBuilderFactory = DocumentBuilderFactory.newInstance()
        val documentBuilder = documentBuilderFactory.newDocumentBuilder()

        val thread = Thread {
            for (link in links) {
                try {
                    val document = documentBuilder.parse(InputSource(URL(link).openStream()))
                    start(document)
                } catch (e: Exception) {
                    Log.d("BAPER 3", "${e.toString()} $link")
                }
            }
/*
            articles.shuffle(Random())
            val mixedCategory:MutableSet<String> = mutableSetOf()
            val mut:MutableList<String> = (category-"сводка").toMutableList()
            mut.shuffle(Random())
            mut.add(0,"сводка")
            for (item in mut){
                mixedCategory.add(item)
            }*/
            onReady(articles to category)
        }
        thread.start()
    }

    private var newArticle: MutableMap<String, Any?> = dafaultMap()

    private fun start(document: Document) {
        document.documentElement.normalize()
        val nodeList = document.getElementsByTagName(document.documentElement.nodeName)
        newArticle = dafaultMap()
        recursiveParse(nodeList)
    }

    private var itemEntity = -1
    private var previousItem = -1

    private fun dafaultMap(): MutableMap<String, Any?> = mutableMapOf(
        "title" to "",
        "pubDate" to "",
        "description" to "",
        "fullText" to "",
        "category" to "СВОДКА",
        "media" to mutableListOf<MediaContent>()
    )


    private fun recursiveParse(nodeList: NodeList) {
        for (i in 0 until nodeList.length) {
            val node = nodeList.item(i)
            //Log.d("BAPER",node.nodeName)
            if (node.nodeType == Node.ELEMENT_NODE) {
                //Log.d("BAPER 2",node.nodeName)
                when (node.nodeName) {
                    "item" -> itemEntity += 1
                    "title" -> {
                        newArticle["title"] = node.textContent; /*Log.d("BAPER",node.textContent)*/
                    }                   //1
                    "pubDate" -> newArticle["pubDate"] = node.textContent              //2
                    "description" -> newArticle["description"] =
                        node.textContent          //descriprion vesti riped
                    "yandex:full-text" -> newArticle["fullText"] =
                        node.textContent     //4  //yandex:full-text vesti riped
                    "mailru:full-text"-> newArticle["fullText"] =
                        node.textContent
                    "category" -> newArticle["category"] = node.textContent.uppercase()            //-1
                    "enclosure" -> (newArticle["media"] as MutableList<MediaContent>)
                        .add(MediaContent.Picture(node.attributes.item(0).textContent))
                    "media:content" -> (newArticle["media"] as MutableList<MediaContent>)
                        .add(MediaContent.Video(node.attributes.item(0).textContent))
                    "link" -> newArticle["link"] = node.textContent
                }
                //Log.d("BAPER", "entity =$itemEntity prev = $previousItem ${newArticle.title != null}, ${newArticle.category != null}, ${newArticle.fullText != null}")
                //Log.d("BAPER","${newArticle["title"]}, ${newArticle["category"]}, ${newArticle["title"]}")
                if (itemEntity != previousItem) {
                    val substr = newArticle["category"].toString().replace(":","").split(" ".toRegex())
                    if (newArticle["category"]==""){newArticle["category"] = "СВОДКА"}

                        for (item in category){
                            for (sub in substr){
                                if (sub.contains(item)||item.contains(sub)){newArticle["category"]=item; break}
                            }

                        }
                    if (substr.size>2){
                        newArticle["category"] = substr[0]
                    }
                    if(newArticle["category"].toString().contains("СОБЫТИЯ")||
                        newArticle["category"].toString().contains("ЧП")||
                        newArticle["category"].toString().contains("КРИМИНАЛ")||
                        newArticle["category"].toString().contains("ЧЕРЕЗВЫЧ")||
                        newArticle["category"].toString().contains("СИЛОВ")||
                        newArticle["category"].toString().contains("КРУШ")||
                        newArticle["category"].toString().contains("АВАР")||
                        newArticle["category"].toString().contains("ОБРУШ")||
                        newArticle["category"].toString().contains("ВЗРЫВ"))
                    {
                        newArticle["category"] = "ПРОИСШЕСТВИЯ"
                    }
                    if(newArticle["category"].toString().contains("ПОЛИТИ")||
                        newArticle["category"].toString().contains("МЕЖДУНАР"))
                    {
                        newArticle["category"] = "ПОЛИТИКА"
                    }

                    if(newArticle["category"].toString().contains("АРМИЯ")||
                        newArticle["category"].toString().contains("ВООРУЖЕННЫЕ СИЛЫ"))
                    {
                        newArticle["category"] = "БЕЗОПАСНОСТЬ"
                    }

                    if(newArticle["category"].toString().contains("НБА")||
                        newArticle["category"].toString().contains("НХЛ")||
                        newArticle["category"].toString().contains("ОЛИМПИАДА")||
                        newArticle["category"].toString().contains("ТУРНИР")||
                        newArticle["category"].toString().contains("СОСТЯЗ")||
                        newArticle["category"].toString().contains("ЧЕМПИ")||
                        newArticle["category"].toString().contains("ХОККЕЙ")||
                        newArticle["category"].toString().contains("ФУТБОЛ")||
                        newArticle["category"].toString().contains("ТЕНИС")||
                        newArticle["category"].toString().contains("ГОНКИ"))
                    {
                        newArticle["category"] = "СПОРТ"
                    }

                    if(newArticle["category"].toString().contains("НЕДВИЖ")||
                        newArticle["category"].toString().contains("ДОМ"))
                    {
                        newArticle["category"] = "НЕДВИЖИМОСТЬ"
                    }

                    if(newArticle["category"].toString().contains("НОВОСТ"))
                    {
                        newArticle["category"] = "НОВОСТИ"
                    }

                    if(newArticle["category"].toString().contains("ВАКЦИН")||
                        newArticle["category"].toString().contains("ПАНДЕМИЯ")||
                        newArticle["category"].toString().contains("COVID")||
                        newArticle["category"].toString().contains("КОВИД")||
                        newArticle["category"].toString().contains("ДОКТОР"))
                    {
                        newArticle["category"] = "ЗДОРОВЬЕ"
                    }

                    if(newArticle["category"].toString().contains("TECH")||
                        newArticle["category"].toString().contains("ТЕЛЕКОМ")||
                        newArticle["category"].toString().contains("ИНТЕРНЕТ"))
                    {
                        newArticle["category"] = "ТЕХНОЛОГИИ"
                    }

                    if (newArticle["fullText"] == "") {
                        newArticle["fullText"] = newArticle["link"]
                    }
                    if (newArticle["title"] != "" &&
                        newArticle["fullText"] != ""
                    ) {
                        //Log.d("BAPER", "CLEAR")
                        articles.add(
                            if ((newArticle["media"] as MutableList<MediaContent>).isNotEmpty())
                                Article.MediaArticle(
                                    newArticle["title"] as String,
                                    newArticle["category"] as String,
                                    newArticle["fullText"] as String,
                                    newArticle["pubDate"] as String,
                                    newArticle["description"] as String,
                                    newArticle["media"] as MutableList<MediaContent>,
                                    newArticle["link"] as String
                                )
                            else Article.NoMediaArticle(
                                newArticle["title"] as String,
                                newArticle["category"] as String,
                                newArticle["fullText"] as String,
                                newArticle["pubDate"] as String,
                                newArticle["description"] as String,
                                newArticle["link"] as String
                            )
                        )
                        category.add(newArticle["category"].toString())
                        newArticle = dafaultMap()
                    }
                }
                previousItem = itemEntity

                if (node.hasChildNodes()) {
                    recursiveParse(node.childNodes)
                }

            }
        }
    }


}
