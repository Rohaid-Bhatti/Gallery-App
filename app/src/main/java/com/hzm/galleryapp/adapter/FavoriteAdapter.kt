package com.hzm.galleryapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hzm.galleryapp.R
import com.hzm.galleryapp.models.HitWithFavorite
import com.hzm.galleryapp.roomDb.FavoriteEntity

class FavoriteAdapter(
    private val onFavoriteClick: (FavoriteEntity) -> Unit,
    private val onRemoveClick: (FavoriteEntity) -> Unit
) : ListAdapter<FavoriteEntity, FavoriteAdapter.FavoriteViewHolder>(FavoriteDiffCallback()) {

    // List to store favorite image IDs
    private var favoriteImageIds: List<Int> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_favorite, parent, false)
        return FavoriteViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val currentFavorite = getItem(position)

        // Bind data to the ViewHolder
        holder.textTitleFavorite.text = currentFavorite.user
        Glide.with(holder.itemView.context)
            .load(currentFavorite.largeImageURL)
            .into(holder.imageFavorite)

        // Set the favorite icon based on whether the image is a favorite or not
        if (favoriteImageIds.contains(currentFavorite.imageId)) {
            holder.buttonFavorite.setImageResource(R.drawable.heart_red)
        } else {
            holder.buttonFavorite.setImageResource(R.drawable.heart)
        }

        // Set a click listener for the remove button
        holder.buttonFavorite.setOnClickListener {
            onRemoveClick(currentFavorite)
        }

//        // Set a click listener for the favorite icon
//        holder.buttonFavorite.setOnClickListener {
//            onFavoriteClick(currentFavorite)
//        }
    }

    inner class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textTitleFavorite: TextView = itemView.findViewById(R.id.textTitleFavorite)
        val imageFavorite: ImageView = itemView.findViewById(R.id.imageFavorite)
        val buttonFavorite: ImageButton = itemView.findViewById(R.id.buttonFavorite)
    }

    class FavoriteDiffCallback : DiffUtil.ItemCallback<FavoriteEntity>() {
        override fun areItemsTheSame(oldItem: FavoriteEntity, newItem: FavoriteEntity): Boolean {
            return oldItem.favoriteId == newItem.favoriteId
        }

        override fun areContentsTheSame(oldItem: FavoriteEntity, newItem: FavoriteEntity): Boolean {
            return oldItem == newItem
        }
    }

    // Function to set the list of favorite image IDs
    fun setFavoriteImageIds(imageIds: List<Int>) {
        favoriteImageIds = imageIds
        notifyDataSetChanged()
    }
}