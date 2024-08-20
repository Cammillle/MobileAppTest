package com.example.mobileapptest

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapptest.databinding.RecyclerViewItemBinding
import com.squareup.picasso.Picasso
import kotlin.math.floor

class CoinsAdapter: ListAdapter<CoinList, CoinsAdapter.CoinsHolder>(Comparator()) {
    private var onClickListener: OnClickListener? = null

    class CoinsHolder(view: View): RecyclerView.ViewHolder(view){
        private val binding = RecyclerViewItemBinding.bind(view)
        fun bind(coin:CoinList) = with(binding){
            tvName.text = coin.name
            tvPrice.text = "$ "+coin.current_price.toString()
            tvSymbol.text= coin.symbol
            val capChangePercentage = floor(coin.market_cap_change_percentage_24h * 100)/100
            if(capChangePercentage > 0){
                tvDuration.text = "+ " + capChangePercentage.toString() + "%"
            }else{
                tvDuration.text = capChangePercentage.toString() + "%"
                tvDuration.setTextColor(Color.RED)
            }
            Picasso.get().load(coin.image).into(ImageIcon)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinsHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_item,parent,false)
        return CoinsHolder(view)
    }

    override fun onBindViewHolder(holder: CoinsHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            onClickListener?.onClick(position,getItem(position))
        }

    }

    fun setOnClickListener(listener:OnClickListener?){
        this.onClickListener = listener
    }
    // Interface for the click listener
    interface OnClickListener {
        fun onClick(position: Int, model: CoinList)
    }


    class Comparator: DiffUtil.ItemCallback<CoinList>() {
        override fun areItemsTheSame(oldItem: CoinList, newItem: CoinList): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CoinList, newItem: CoinList): Boolean {
            return oldItem == newItem
        }
    }
}