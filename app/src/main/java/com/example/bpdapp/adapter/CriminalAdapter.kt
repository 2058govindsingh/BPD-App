package com.example.bpdapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bpdapp.R
import com.example.bpdapp.models.Criminal

class CriminalAdapter(
    private val data: MutableList<Criminal>,
    private val listener: CriminalClicked
) : RecyclerView.Adapter<CriminalAdapter.CriminalViewHolder>() {

    class CriminalViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.criminal_name)
        val phone: TextView = view.findViewById(R.id.criminal_phone)
        val aadhaar: TextView = view.findViewById(R.id.criminal_aadhaar)
        val button: Button = view.findViewById(R.id.button_found)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CriminalViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.item_criminal, parent, false)
        return CriminalViewHolder(adapterLayout)
    }


    override fun onBindViewHolder(holder: CriminalViewHolder, position: Int) {
        val currentCriminal = data[position]
        holder.name.text = currentCriminal.name
        holder.phone.text = currentCriminal.phone
        holder.aadhaar.text = currentCriminal.aadhaar
        holder.button.setOnClickListener{
            listener.onCriminalClicked()
        }
    }

    override fun getItemCount(): Int = data.size

}
interface CriminalClicked {
    fun onCriminalClicked()
}