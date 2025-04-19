package com.example.appmoney.ui.main.feature.transactionhistory

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.appmoney.R
import com.example.appmoney.databinding.ItemHistoryTransBinding


class TransDetailDiffCallback : DiffUtil.ItemCallback<TransactionDetail>() {
    override fun areItemsTheSame(oldItem: TransactionDetail, newItem: TransactionDetail): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: TransactionDetail, newItem: TransactionDetail): Boolean {
        return oldItem == newItem
    }

}

interface TransDetailListener {
    fun onItemClick(trans: TransactionDetail)
}

class HistoryTransAdapter :
    ListAdapter<TransactionDetail, HistoryTransAdapter.TransDetailViewHolder>(
        TransDetailDiffCallback()
    ) {

        private var listener: TransDetailListener? = null
        fun setListener(listener: TransDetailListener) {
            this.listener = listener
        }

    inner class TransDetailViewHolder(val binding: ItemHistoryTransBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindItem(
            item: TransactionDetail,
        ) {
            binding.apply {
                val imgRes = item.image?.resource ?: R.drawable.book
                imgHis.setImageResource(imgRes)

                val colorRes = item.color?.resource ?: R.color.black
                imgHis.imageTintList = ColorStateList
                    .valueOf(ContextCompat.getColor(root.context, colorRes))
                tvHis.text = item.desCat ?: "Trống"

                val type = item.typeTrans
                val prefix = when(type) {
                    "Expenditure" -> "-"
                    "Income" -> "+"
                    else -> "-"
                }
                val color = when(type) {
                    "Expenditure" -> ContextCompat.getColor(root.context, R.color.colorRed)
                    "Income" -> ContextCompat.getColor(root.context, R.color.colorGreen)
                    else -> ContextCompat.getColor(root.context, R.color.colorRed)
                }
                noteHis.text = "(${item.note})"
                moneyHis.text = "$prefix${item.amount}đ"
                tvDate.text = item.date
                moneyHis.setTextColor(color)

                root.setOnClickListener {
                    listener?.onItemClick(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TransDetailViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return TransDetailViewHolder(
            ItemHistoryTransBinding.inflate(inflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TransDetailViewHolder, position: Int) {
        val item = getItem(position)
        holder.bindItem(item)
    }
}