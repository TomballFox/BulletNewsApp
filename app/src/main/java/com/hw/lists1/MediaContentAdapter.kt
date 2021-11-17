package com.hw.lists1

import android.net.Uri
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.MediaController
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import kotlinx.android.synthetic.main.item_video.*


class MediaContentAdapter() :RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    companion object{
        private const val  TYPE_VIDEO=1
        private const val TYPE_PICTURE=2
    }

    private var media:MutableList<MediaContent> = mutableListOf()

    fun setMedia(inputMedia:MutableList<MediaContent> = mutableListOf()){
        media = inputMedia
        //Log.d("BAPER","media size = ${media.size}")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            TYPE_VIDEO->VideoHolder(parent.inflate(R.layout.item_video))
            TYPE_PICTURE->PictureHolder(parent.inflate(R.layout.item_picture))
            else->error("Incorrect viewType = $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(media[position]){
            is MediaContent.Video-> TYPE_VIDEO
            is MediaContent.Picture-> TYPE_PICTURE
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is VideoHolder->{
                holder.bind(media[position].let { it as? MediaContent.Video }?:
                error("Media at position=$position is not a video"))
            }
            is PictureHolder->{
                holder.bind(media[position].let { it as? MediaContent.Picture }?:
                error("Media at position=$position is not a picture"))
            }
            else->error("Incorrect view holder = $holder")
        }
    }

    override fun getItemCount(): Int = media.size

    abstract class BaseMediaHolder(view: View):RecyclerView.ViewHolder(view)

    class VideoHolder(view:View):BaseMediaHolder(view){
        private val videoView: VideoView = view.findViewById(R.id.videoView)
        private val playButton: Button = view.findViewById(R.id.playButton)
        fun bind(media:MediaContent.Video){
            videoView.setVideoURI(Uri.parse(media.url))
            videoView.seekTo(3)
            videoView.requestFocus()
            playButton.setOnClickListener {
                playButton.visibility = View.INVISIBLE
                videoView.start()
            }
            videoView.setOnClickListener {
                videoView.pause()
                playButton.visibility = View.VISIBLE
            }

        }
    }

    class PictureHolder(view:View):BaseMediaHolder(view){
        private val imageView: ImageView = view.findViewById(R.id.imageView)
        fun bind(media: MediaContent.Picture){
            //Log.d("BAPER", "media picture url ="+media.url)
            Glide.with(itemView)
                .load(media.url)
                .placeholder(R.drawable.ic_baseline_error_24)
                .into(imageView)
        }
    }

}