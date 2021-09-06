package com.example.myapp

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp.model.CatsResponse
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_card.view.*
import kotlinx.android.synthetic.main.item_card.view.catAgeTV
import kotlinx.android.synthetic.main.item_card.view.catNameTV
import kotlinx.android.synthetic.main.item_card.view.radio
import kotlinx.android.synthetic.main.small_item_card.view.*

class CatsAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list = ArrayList<CatsResponse>()

    inner class Small_Card_ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(cat:CatsResponse){
                with(itemView){
                    catNameSTV.text = cat.name
                    catAgeSTV.text = cat.age
                    Picasso.get().load(cat.imageUrl).fit().into(catSIV)
                }
        }

    }

    inner class Card_ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(cat:CatsResponse){
                with(itemView) {
                    catNameTV.text = cat.name
                    catAgeTV.text = cat.age
                    Picasso.get().load(cat.imageUrl).fit().into(catIV)
                }

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when(viewType){
            R.layout.item_card -> Card_ViewHolder(inflater.inflate(viewType, parent, false))

            R.layout.small_item_card -> Small_Card_ViewHolder(inflater.inflate(viewType, parent, false))

            else -> throw IllegalArgumentException("Unsupported layout")
        }
    }

    var  selectedRBPosition = -1

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val element = list[position]

        when(holder) {
            is Card_ViewHolder -> {
                holder.bind(element)
                holder.itemView.radio.isChecked = selectedRBPosition == position
                    holder.itemView.setOnClickListener {
                        selectedRBPosition = holder.adapterPosition
                        notifyDataSetChanged()
                    }

            }

            is Small_Card_ViewHolder -> {
                holder.bind(element)
                holder.itemView.radio.isChecked = selectedRBPosition == position

                        holder.itemView.setOnClickListener {
                            selectedRBPosition = holder.adapterPosition
                            notifyDataSetChanged()
                        }

            }
        }
    }

    override fun getItemCount(): Int = list.size

    override fun getItemViewType(position: Int): Int {
        val element = list[position]
        val type = element.type
        if(type == "SMALL_CARD")
            return R.layout.small_item_card
        else
            return R.layout.item_card
    }

    fun addList(items:ArrayList<CatsResponse>){
        list.addAll(items)
        notifyDataSetChanged()
    }


    fun clear(){
        list.clear()
        notifyDataSetChanged()
    }
}