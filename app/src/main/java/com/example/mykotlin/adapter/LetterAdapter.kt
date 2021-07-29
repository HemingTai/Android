package com.example.mykotlin.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.mykotlin.R
import com.example.mykotlin.WordsDetailActivity

class LetterAdapter : RecyclerView.Adapter<LetterAdapter.LetterViewHolder>() {

    class LetterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val button = view.findViewById<Button>(R.id.word_item)
    }

    private val list = ('A').rangeTo('Z').toList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LetterViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.word_item, parent, false)
        return LetterViewHolder(layout)
    }

    override fun onBindViewHolder(holder: LetterAdapter.LetterViewHolder, position: Int) {
        holder.button.text = list[position].toString()
        holder.button.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, WordsDetailActivity::class.java)
            intent.putExtra(WordsDetailActivity.LETTER, holder.button.text.toString())
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = list.size

}