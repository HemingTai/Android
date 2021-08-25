package com.example.mykotlin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.mykotlin.LetterListFragmentDirections
import com.example.mykotlin.R

class LetterAdapter : RecyclerView.Adapter<LetterAdapter.LetterViewHolder>() {

    class LetterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val button: Button = view.findViewById(R.id.word_item)
    }

    private val list = ('A').rangeTo('Z').toList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LetterViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.word_item, parent, false)
        return LetterViewHolder(layout)
    }

    override fun onBindViewHolder(holder: LetterViewHolder, position: Int) {
        holder.button.text = list[position].toString()
        holder.button.setOnClickListener {
            val action = LetterListFragmentDirections.actionLetterListFragmentToWordListFragment(holder.button.text.toString())
            holder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount() = list.size

}