package com.hzm.galleryapp.adapter

import android.content.Intent
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
import com.hzm.galleryapp.DetailActivity
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

        // Set a click listener for the item view
        holder.itemView.setOnClickListener {
            // Create an intent to open the DetailsActivity
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)

            // Pass data related to the clicked image to the DetailsActivity
            intent.putExtra("image_url", currentFavorite.largeImageURL)
            intent.putExtra("likes", currentFavorite.likes)
            intent.putExtra("views", currentFavorite.views)
            intent.putExtra("comments", currentFavorite.comments)
            intent.putExtra("downloads", currentFavorite.downloads)
            intent.putExtra("tags", currentFavorite.tags)
            intent.putExtra("type", currentFavorite.type)
            intent.putExtra("user", currentFavorite.user)

            // Start the DetailsActivity
            holder.itemView.context.startActivity(intent)
        }

        // Set a click listener for the remove button
        holder.buttonFavorite.setOnClickListener {
            onRemoveClick(currentFavorite)
        }
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