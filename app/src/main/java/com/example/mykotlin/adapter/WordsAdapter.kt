package com.example.mykotlin.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.mykotlin.R
import com.example.mykotlin.WordsDetailActivity

class WordsAdapter(private val letterId: String, context: Context) : RecyclerView.Adapter<WordsAdapter.WordViewHolder>() {

    class WordViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val button = view.findViewById<Button>(R.id.word_item)
    }

    private var filteredWords: List<String>

    init {
        val words = context.resources.getStringArray(R.array.words)
        filteredWords = words
            .filter { it.startsWith(letterId, ignoreCase = true) }
            .shuffled()
            .take(5)
            .sorted()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.word_item, parent, false)
        return WordViewHolder(layout)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val item = filteredWords[position]
        holder.button.text = item
        holder.button.setOnClickListener {
            val queryUri = Uri.parse("${WordsDetailActivity.SEARCH_PREFIX}${item}")
            val intent = Intent(Intent.ACTION_VIEW, queryUri)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount() = filteredWords.size
}