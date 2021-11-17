package com.hw.lists1

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CategoryAdapterRV(
    private val onItemClick: (position: Int, entity:Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val MEDIA_ARTICLE = 1
        private const val NO_MEDIA_ARTICLE = 2
    }

    private var articles: MutableList<Article> = mutableListOf()

    fun updateArticles(newArticles: MutableList<Article>) {
        articles = newArticles
        //Log.d("BAPER","Category adapterRV articles-size = ${articles.size}")
    }

    override fun getItemCount(): Int = articles.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            MEDIA_ARTICLE -> MediaHolderArticle(
                parent.inflate(R.layout.fragment_media_article),
                onItemClick
            )
            NO_MEDIA_ARTICLE -> NoMediaHolderArticle(
                parent.inflate(R.layout.fragment_no_media_article),
                onItemClick
            )
            else -> error("Incorrect viewType=$viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (articles[position]) {
            is Article.MediaArticle -> MEDIA_ARTICLE
            is Article.NoMediaArticle -> NO_MEDIA_ARTICLE
            else -> error("Incorrect ArticleTYPE")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MediaHolderArticle -> {
                val article = articles[position].let { it as? Article.MediaArticle }
                    ?: error("Article at position = $position is not a media article")
                holder.bind(article)
            }
            is NoMediaHolderArticle -> {
                val article = articles[position].let { it as? Article.NoMediaArticle }
                    ?: error("Article at position = $position is not a NO media article")
                holder.bind(article)
            }
            else -> error("Incorrect view holder $holder")
        }
    }

    abstract class BaseHolderArticle(view: View, onItemClick: (position: Int, entity:Int) -> Unit) :
        RecyclerView.ViewHolder(view) {
        private val titleTV: TextView = view.findViewById(R.id.titleTV)
        private val descriptionTV: TextView = view.findViewById(R.id.descriptionTV)
        private val dateTV: TextView = view.findViewById(R.id.dateTV)
        private val dislikeButton:Button = view.findViewById(R.id.dislikeButton)

        init {
            view.setOnClickListener {
                onItemClick(bindingAdapterPosition, 0)
            }
            dislikeButton.setOnClickListener {
                onItemClick(bindingAdapterPosition, 1)
            }
        }

        protected fun bindMainInfo(
            title: String,
            description: String,
            date: String
        ) {
            titleTV.text = title
            descriptionTV.text = description
            dateTV.text = date
        }
    }


    class MediaHolderArticle(
        view: View,
        onItemClick: (position: Int, entity:Int) -> Unit
    ) : BaseHolderArticle(view, onItemClick) {
        private val mediaList: RecyclerView = view.findViewById(R.id.mediaRV)
        private var mediaContentAdapter: MediaContentAdapter? = null
        fun bind(article: Article.MediaArticle) {
            bindMainInfo(
                article.title ?: "Error: Empty Title",
                article.description ?: "Error:? Empty Description",
                article.pubDate ?: "Error:? Empty Publication Date"
            )
            initList()
            mediaContentAdapter?.setMedia(article.media)
            mediaContentAdapter?.notifyDataSetChanged()
        }

        private fun initList() {
            mediaContentAdapter = MediaContentAdapter()
            with(mediaList) {
                adapter = mediaContentAdapter
                setHasFixedSize(true)
            }
        }
    }

    class NoMediaHolderArticle(
        view: View,
        onItemClick: (position: Int, entity:Int) -> Unit
    ) : BaseHolderArticle(view, onItemClick) {
        fun bind(article: Article.NoMediaArticle) {
            bindMainInfo(
                article.title ?: "Error: Empty Title",
                article.description ?: "Error:? Empty Description",
                article.pubDate ?: "Error:? Empty Publication Date"
            )

        }
    }


}