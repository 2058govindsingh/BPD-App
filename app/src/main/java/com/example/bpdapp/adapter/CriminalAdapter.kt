package com.example.bpdapp.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.example.bpdapp.R
import com.example.bpdapp.models.Criminal
import javax.sql.DataSource

class CriminalAdapter(
    private val data: MutableList<Criminal>,
    private val listener: CriminalClicked,
    private val context : Context
) : RecyclerView.Adapter<CriminalAdapter.CriminalViewHolder>() {

    class CriminalViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.criminal_name)
        val phone: TextView = view.findViewById(R.id.criminal_phone)
        val aadhaar: TextView = view.findViewById(R.id.criminal_aadhaar)
        val crime : TextView= view.findViewById(R.id.criminal_crime)
        val date : TextView =view.findViewById(R.id.criminal_crime_date)
        val image : ImageView = view.findViewById(R.id.criminal_photo)
        val location : TextView =view.findViewById(R.id.criminal_location)
        val progressBar : ProgressBar = view.findViewById(R.id.itemProgressBar)
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
        holder.crime.text = currentCriminal.crime
        holder.date.text = currentCriminal.date
        holder.location.text = currentCriminal.location
        if(currentCriminal.image!=""){
            holder.progressBar.visibility=View.VISIBLE
            Glide.with(holder.itemView.context).load(currentCriminal.image).listener(object :
                RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.progressBar.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable>?,
                    dataSource: com.bumptech.glide.load.DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.progressBar.visibility = View.GONE
                    return false
                }

            }).into(holder.image)
        }

        holder.button.setOnClickListener{
            listener.onCriminalClicked()
        }
    }

    override fun getItemCount(): Int = data.size

}
interface CriminalClicked {
    fun onCriminalClicked()
}