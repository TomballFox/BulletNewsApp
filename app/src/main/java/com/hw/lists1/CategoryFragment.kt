package com.hw.lists1

import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.URLUtil
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_article_opener.*
import kotlinx.android.synthetic.main.fragment_detail_web.*
import kotlinx.android.synthetic.main.fragment_main.*

class CategoryFragment : Fragment(R.layout.fragment_article_opener) {

    companion object {
        private const val KEY_PARAM: String = "category"
        fun newInstance(category: String) =
            CategoryFragment().withArguments {
                putString(KEY_PARAM, category)
            }
    }

    private lateinit var category: String
    private lateinit var articles: MutableList<Article>
    private lateinit var articleViewModel: ArticleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            category = it.getString(KEY_PARAM).toString()
        }
        val articleViewModel: ArticleViewModel by activityViewModels()
        this.articleViewModel = articleViewModel

    }

    private var categoryAdapterRV: CategoryAdapterRV? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayNews()
        emptyTV.visibility=View.INVISIBLE
        moreNewsButton.setOnClickListener {
            val categoryViewModel:ArticleViewModel by viewModels()
            categoryViewModel.requestData()
            categoryViewModel.newsOutput.observe(viewLifecycleOwner) {result->
                articles = result.first.filter {
                    it.categoryGlobal == category
                }?.toMutableList()
                categoryAdapterRV?.updateArticles(articles)
                categoryAdapterRV?.notifyDataSetChanged()
                if(articles.isEmpty()) notifyEmpty()
                categoryRV.scrollToPosition(0)
            }
        }
        //удалить
        plusButton.setOnClickListener {
            val newArticle = articles.random()
            articles = (listOf(newArticle)+articles).toMutableList()
            categoryAdapterRV?.updateArticles(articles)
            categoryAdapterRV?.notifyItemInserted(0)
            categoryRV.scrollToPosition(0)
        }
        //удалить
    }

    private fun displayNews(){
        articles = articleViewModel.newsOutput.value?.first?.filter {
            it.categoryGlobal == category
        }?.toMutableList() ?: error("Error: articleViewModel.first null value")
        //Log.d("BAPER","CategoryFragment articles-size = ${articles.size}")
        initList()
        categoryAdapterRV?.updateArticles(articles)
        categoryAdapterRV?.notifyDataSetChanged()
    }

    private fun initList() {
        categoryAdapterRV =
            CategoryAdapterRV { position, entity -> actionArticle(position, entity) }
        with(categoryRV) {
            adapter = categoryAdapterRV
            setHasFixedSize(true)
        }
    }

    private fun actionArticle(position: Int, entity: Int) {
        when (entity) {
            0 -> openArticle(position)
            1 -> deleteArticle(position)
        }
    }

    private fun openArticle(position: Int) {
        Log.d("BAPER", "HEREIS3")
        val currentArticle = articleViewModel.newsOutput.value?.first?.filter {
            it.categoryGlobal == category
        }?.elementAt(position) ?: error("DetailFragment null elementAt position $position")
        when (currentArticle) {
            is Article.MediaArticle -> {
                isWebContent(currentArticle.fullText.toString(), position)
            }
            is Article.NoMediaArticle -> {
                isWebContent(currentArticle.fullText.toString(), position)
            }
        }
    }

    private fun isWebContent (url:String, position:Int){
        if(URLUtil.isValidUrl(url)){
            parentFragmentManager
                .beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in,
                    R.anim.slide_out,
                    R.anim.slide_in,
                    R.anim.slide_out
                )
                .replace(R.id.jetContainer, DetailWebFragment.newInstance(category, position))
                .addToBackStack(null)
                .commit()
        }
        else
        {
            parentFragmentManager
                .beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in,
                    R.anim.slide_out,
                    R.anim.slide_in,
                    R.anim.slide_out
                )
                .replace(R.id.jetContainer, DetailFragment.newInstance(category, position))
                .addToBackStack(null)
                .commit()
        }
    }

    private fun deleteArticle(position: Int) {
        articles = articles.filterIndexed { index, _ -> index != position }.toMutableList()
        categoryAdapterRV?.updateArticles(articles)
        categoryAdapterRV?.notifyItemRemoved(position)
        if (categoryAdapterRV?.itemCount == 0) {
            notifyEmpty()
        }
    }

    private fun notifyEmpty(){
        AlertDialog.Builder(this.requireActivity())
            .setTitle("Список пуст")
            .setMessage("Cтатьи указанной категории отсутствуют")
            .create()
            .show()
        emptyTV.visibility=View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        categoryAdapterRV = null
    }




}

