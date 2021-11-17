package com.hw.lists1

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import kotlinx.android.synthetic.main.fragment_detail_web.*
import kotlin.properties.Delegates

class DetailWebFragment: Fragment(R.layout.fragment_detail_web) {

    companion object {
        private const val KEY_PARAM1 = "category"
        private const val KEY_PARAM2 = "position"
        fun newInstance(category: String, position: Int) =
            DetailWebFragment().withArguments {
                putString(KEY_PARAM1, category)
                putInt(KEY_PARAM2, position)
            }
    }

    private lateinit var category: String
    private var position by Delegates.notNull<Int>()
    private lateinit var article: Article
    private lateinit var articleViewModel: ArticleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            category = it.getString(KEY_PARAM1).toString()
            position = it.getInt(KEY_PARAM2)
        }
        val articleViewModel: ArticleViewModel by activityViewModels()
        this.articleViewModel = articleViewModel
        Log.d("BAPER", "HEREIS4")
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        article = articleViewModel.newsOutput.value?.first?.filter {
            it.categoryGlobal == category
        }?.elementAt(position) ?: error("DetailFragment null elementAt position $position")
        webView.settings.javaScriptEnabled = true
        when (article) {
            is Article.MediaArticle -> {
                webView.loadUrl((article as Article.MediaArticle).fullText.toString())
            }
            is Article.NoMediaArticle -> {
                webView.loadUrl((article as Article.NoMediaArticle).fullText.toString())
            }
        }
    }




}