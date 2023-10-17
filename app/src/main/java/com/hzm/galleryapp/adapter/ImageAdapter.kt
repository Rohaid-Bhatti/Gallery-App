package com.hzm.galleryapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hzm.galleryapp.DetailActivity
import com.hzm.galleryapp.R
import com.hzm.galleryapp.models.Hit
import com.hzm.galleryapp.models.HitWithFavorite
import com.hzm.galleryapp.viewModels.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ImageAdapter(
    var data: List<HitWithFavorite>,
    private val onFavoriteClick: (Hit) -> Unit,
    private val viewModel: MainViewModel,
) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_item, parent, false)
        return ImageViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val currentImage = data[position]

        // Use Glide to load and display the image from its URL
        Glide.with(holder.imageView.context)
            .load(currentImage.hit.largeImageURL)
            .placeholder(R.drawable.photo) // Use the placeholder image
            .into(holder.imageView)

        // Bind likes, views, and comments
        holder.textLikes.text = "Likes: ${currentImage.hit.likes}"
        holder.textViews.text = "Views: ${currentImage.hit.views}"
        holder.textComments.text = "Comments: ${currentImage.hit.comments}"

        // Check if the item is a favorite asynchronously using CoroutineScope
        CoroutineScope(Dispatchers.IO).launch {
            val isFavorite = viewModel.isFavorite(currentImage.hit.id)
            // Update the UI on the main thread
            withContext(Dispatchers.Main) {
                if (isFavorite) {
                    holder.imageFavorite.setImageResource(R.drawable.heart_red)
                } else {
                    holder.imageFavorite.setImageResource(R.drawable.heart)
                }
            }
        }

        // Set a click listener for the item view
        holder.itemView.setOnClickListener {
            // Create an intent to open the DetailsActivity
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)

            // Pass data related to the clicked image to the DetailsActivity
            intent.putExtra("image_url", currentImage.hit.largeImageURL)
            intent.putExtra("likes", currentImage.hit.likes)
            intent.putExtra("views", currentImage.hit.views)
            intent.putExtra("comments", currentImage.hit.comments)
            intent.putExtra("downloads", currentImage.hit.downloads)
            intent.putExtra("tags", currentImage.hit.tags)
            intent.putExtra("type", currentImage.hit.type)
            intent.putExtra("user", currentImage.hit.user)

            // Start the DetailsActivity
            holder.itemView.context.startActivity(intent)
        }


        // Set a click listener for the favorite icon
        holder.imageFavorite.setOnClickListener {
            // Check if the current image is heart
            if (holder.imageFavorite.drawable.constantState ==
                ContextCompat.getDrawable(holder.itemView.context, R.drawable.heart)?.constantState) {
                // Change the image to heart_red
                holder.imageFavorite.setImageResource(R.drawable.heart_red)
            } else {
                // Change the image to heart
                holder.imageFavorite.setImageResource(R.drawable.heart)
            }

            toggleFavorite(currentImage.hit)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val textLikes: TextView = itemView.findViewById(R.id.textLikes)
        val textViews: TextView = itemView.findViewById(R.id.textViews)
        val textComments: TextView = itemView.findViewById(R.id.textComments)
        val imageFavorite: ImageButton = itemView.findViewById(R.id.imageFavorite)
    }

    private fun toggleFavorite(image: Hit) {
        onFavoriteClick(image)
    }
}