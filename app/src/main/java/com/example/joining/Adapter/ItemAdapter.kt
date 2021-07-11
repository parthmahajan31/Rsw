package com.example.joining.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.joining.R

class ItemAdapter(val context : Context, val mResults: ArrayList<String>, val select: Select) : RecyclerView.Adapter<ItemAdapter.MyViewHolder> (){


    public interface Select {
        fun delete(position: Int)
    }
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image : ImageView = itemView.findViewById(R.id.image)
        var deleteIMG : ImageView = itemView.findViewById(R.id.deleteIMG)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.list, parent, false)

        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mResults.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(context).load(mResults[position]).into(holder.image)
        holder.deleteIMG.setOnClickListener {
            select.delete(position)
        }
    }
}