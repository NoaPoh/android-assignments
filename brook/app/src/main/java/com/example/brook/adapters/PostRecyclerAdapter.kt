package com.example.brook.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.brook.R
import com.example.brook.model.Country
import com.example.brook.model.CountryModel.Companion.getInstance
import com.example.brook.model.Post
import com.example.brook.utils.Utils
import com.squareup.picasso.Picasso
import java.util.Locale

public class PostViewHolder(
    itemView: View,
    listener: PostRecyclerAdapter.OnItemClickListener,
    data: MutableList<Post>?
) : RecyclerView.ViewHolder(itemView) {
    var userNameTV: TextView
    var titleTV: TextView
    var timeTV: TextView
    var countryNameTV: TextView
    var userImg: ImageView
    var dishImg: ImageView
    var countryFlagImageView: ImageView?
    var data: MutableList<Post>?
    private val mCountries: MutableMap<String?, String?> = mutableMapOf<String?, String?>()

    init {
        this.data = data
        userNameTV = itemView.findViewById<TextView>(R.id.recipeCardUserName)
        titleTV = itemView.findViewById<TextView>(R.id.recipeCardTitle)
        timeTV = itemView.findViewById<TextView>(R.id.recipeCardTime)
        countryNameTV = itemView.findViewById<TextView>(R.id.recipeCardCountryName)
        userImg = itemView.findViewById<ImageView>(R.id.recipeCardUserPic)
        dishImg = itemView.findViewById<ImageView>(R.id.recipeCardDishImg)
        countryFlagImageView = itemView.findViewById<ImageView?>(R.id.countryFlag)
        itemView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                val pos = getAdapterPosition()
                listener.onItemClick(pos)
            }
        })
        val englishLanguage = Locale.Builder().setLanguage("en").build()
        for (iso in Locale.getISOCountries()) {
            val l = Locale("", iso)
            mCountries.put(l.getDisplayCountry(englishLanguage), iso)
        }
    }

    fun bind(post: Post, position: Int) {
        titleTV.setText(post.title)
        timeTV.setText(Utils.timeToString(post.getTime()))

        val data = getInstance().getCountryByName(post.getCountryName())
        val countryCode = mCountries.get(post.countryName)
        data.observeForever(object : Observer<Country?> {
            override fun onChanged(country: Country?) {
                if (country != null) {
                    Picasso.get().load(country.flag!!.imageUrlPng).resize(48, 48)
                        .into(countryFlagImageView)
                    countryNameTV.setText(country.getHebrewName())
                } else {
                    Picasso.get()
                        .load("https://flagsapi.com/" + mCountries.get(post.countryName) + "/flat/32.png")
                        .fit()
                        .into(countryFlagImageView)
                    val countryLocale = Locale.Builder().setRegion(countryCode).build()
                    val hebrewLanguage = Locale.Builder().setLanguage("he").build()

                    countryNameTV.setText(countryLocale.getDisplayCountry(hebrewLanguage))
                }
                data.removeObserver(this)
            }
        })

        if (!post.getDishImg()!!.isEmpty() && post.getDishImg() != null) {
            Picasso.get().load(post.getDishImg()).into(dishImg)
        }

        val user = post.user
        userNameTV.setText(user!!.getUserName())
        if (post.getDishImg() != null && post.getDishImg()!!.length > 5) {
            Picasso.get().load(post.getDishImg()).placeholder(R.drawable.icon_person).into(userImg)
        } else {
            userImg.setImageResource(R.drawable.icon_person)
        }
    }
}

class PostRecyclerAdapter(inflater: LayoutInflater, data: MutableList<Post>?) :
    RecyclerView.Adapter<PostViewHolder?>() {
    var listener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(pos: Int)
    }

    var inflater: LayoutInflater
    var data: MutableList<Post>?

    init {
        this.inflater = inflater
        this.data = data
    }


    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = inflater.inflate(R.layout.fragment_recipe_card, parent, false)
        return PostViewHolder(view, listener!!, data)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val recipe = data!!.get(position)
        holder.titleTV.setText(recipe.getTitle())

        // Clear the previous image before loading a new one
        Glide.with(holder.itemView.getContext()).clear(holder.dishImg)

        Glide.with(holder.itemView.getContext())
            .load(recipe.getDishImg())
            .into(holder.dishImg)

        holder.bind(data!!.get(position), position)
    }

    override fun getItemCount(): Int {
        if (data == null) return 0
        return data!!.size
    }
}
