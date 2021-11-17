package com.hw.lists1

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.*
import java.lang.Exception

class MainFragment : Fragment(R.layout.fragment_main) {
    private lateinit var articles: MutableList<Article>
    private lateinit var category: MutableSet<String>
    private lateinit var filteredCategory: MutableSet<String>
    private lateinit var articleViewModel: ArticleViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(savedInstanceState==null) {
            val articleViewModel: ArticleViewModel by activityViewModels()
            this.articleViewModel = articleViewModel
            articleViewModel.requestData()
        }
        else{
                val articleViewModel: ArticleViewModel by activityViewModels()
                this.articleViewModel = articleViewModel
            }

            articles = mutableListOf()
            category = mutableSetOf()

            articleViewModel.newsOutput.observe(viewLifecycleOwner) {
                articles = it.first
                category = it.second
                //Log.d("BAPER", "MainFragment global category = ${articles[5].categoryGlobal}")
                processNews(articles to category)
                checks = BooleanArray(category.size)
                filterButton.setOnClickListener {
                    showMultiChoiceDialog(category)
                }
            }

        /*
        AlertDialog.Builder(this.requireActivity())
                    .setTitle("Ошибка")
                    .setMessage("Отсутствие соединения с сервером")
                    .create()
                    .show()
         */

    }
    private lateinit var checks:BooleanArray
    private fun showMultiChoiceDialog(category: MutableSet<String>) {
        filteredCategory = mutableSetOf()
        lateinit var dialog: AlertDialog
        val items = category.toTypedArray()

        val builder = AlertDialog.Builder(this.requireActivity())
        builder.setTitle("Сегодня в сводке новостей:")
        builder.setMultiChoiceItems(items, checks) { dialog, which, isChecked ->
            checks[which] = isChecked
        }
        builder.setPositiveButton("Показать") { _, _ ->
            for (i in 0 until category.size) {
                if (checks[i]) {
                    filteredCategory.add(category.elementAt(i))
                }
            }
            processNews(Pair(articles, filteredCategory))
        }
        dialog = builder.create()
        dialog.show()
    }

    private fun processNews(news: Pair<MutableList<Article>, MutableSet<String>>) {
        val mainAdapter = MainAdapter(news.second, this.requireActivity())
        viewPager.adapter = mainAdapter
        viewPager.offscreenPageLimit = 2
        viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        viewPager.isUserInputEnabled = false
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = news.second.elementAt(position)
        }.attach()
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
            }
        })
        viewPager.setPageTransformer { page, position ->
            when {
                position < -1 || position > 1 -> {
                    page.alpha = 0f
                }
                position <= 0 -> {
                    page.alpha = 1 + position
                }
                position <= 1 -> {
                    page.alpha = 1 - position
                }
            }
        }
    }


}

