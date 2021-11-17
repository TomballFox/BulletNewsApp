package com.hw.lists1

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

fun <T:Fragment> T.withArguments(action: Bundle.() -> Unit): T{
    return apply {
        arguments = Bundle().apply(action)

    }
}

        ///private fun initList() {
//        categoryAdapterRV =
//            CategoryAdapterRV { position, entity -> actionArticle(position, entity) }
//        with(categoryRV) {
//            adapter = categoryAdapterRV
//            setHasFixedSize(true)
//        }
//    }