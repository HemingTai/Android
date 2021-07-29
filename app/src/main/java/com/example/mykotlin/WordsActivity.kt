package com.example.mykotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mykotlin.adapter.LetterAdapter
import com.example.mykotlin.databinding.ActivityWordsBinding

class WordsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private var isLinearLayout = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityWordsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.recyclerView
        chooseLayout()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.layout_menu, menu)
        val layoutBtn = menu?.findItem(R.id.action_switch_layout)
        setIcon(layoutBtn)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_switch_layout -> {
                isLinearLayout = !isLinearLayout
                chooseLayout()
                setIcon(item)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun chooseLayout() {
        val layoutManager = if (isLinearLayout) LinearLayoutManager(this) else GridLayoutManager(this, 4)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = LetterAdapter()
    }

    private fun setIcon(menuItem: MenuItem?) {
        if (menuItem != null) {
            val id = if (isLinearLayout) R.drawable.ic_linear_layout else R.drawable.ic_grid_layout
            menuItem.icon =  ContextCompat.getDrawable(this, id)
        }
    }
}