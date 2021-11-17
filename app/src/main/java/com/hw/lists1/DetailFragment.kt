package com.hw.lists1

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlin.properties.Delegates


class DetailFragment : Fragment(R.layout.fragment_detail) {

    companion object {
        private const val KEY_PARAM1 = "category"
        private const val KEY_PARAM2 = "position"
        fun newInstance(category: String, position: Int) =
            DetailFragment().withArguments {
                putString(KEY_PARAM1, category)
                putInt(KEY_PARAM2, position)
            }
    }

    //////
    private lateinit var category: String
    private var position by Delegates.notNull<Int>()
    private lateinit var article: Article
    private lateinit var articleViewModel: ArticleViewModel
    //////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            category = it.getString(KEY_PARAM1).toString()
            position = it.getInt(KEY_PARAM2)
        }
        val articleViewModel: ArticleViewModel by activityViewModels()
        this.articleViewModel = articleViewModel
        Log.d("BAPER", "HEREIS5")
    }


    private var mediaContentAdapter: MediaContentAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        article = articleViewModel.newsOutput.value?.first?.filter {
            it.categoryGlobal == category
        }?.elementAt(position) ?: error("DetailFragment null elementAt position $position")
        when (article) {
            is Article.MediaArticle -> {
                val temp = article as Article.MediaArticle
                titleTV.text = temp.title
                descriptionTV.text = temp.fullText
                dateTV.text = temp.pubDate
                initList()
                mediaContentAdapter?.setMedia(temp.media)
                mediaContentAdapter?.notifyDataSetChanged()
            }
            is Article.NoMediaArticle -> {
                val temp = article as Article.NoMediaArticle
                titleTV.text = temp.title
                descriptionTV.text = temp.fullText
                dateTV.text = temp.pubDate
            }
        }
    }

    private fun initList() {
        mediaContentAdapter = MediaContentAdapter()
        with(mediaRV) {
            adapter = mediaContentAdapter
            setHasFixedSize(true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mediaContentAdapter = null
    }
}