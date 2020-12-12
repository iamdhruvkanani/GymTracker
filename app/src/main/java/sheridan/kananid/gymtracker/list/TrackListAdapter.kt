/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package sheridan.kananid.gymtracker.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

import sheridan.kananid.gymtracker.R
import sheridan.kananid.gymtracker.database.TrackEntity
import sheridan.kananid.gymtracker.databinding.TrackItemBinding

/**
 * The adapter used by the RecyclerView to display the current list of donuts
 */
class TrackListAdapter(
    private var onEdit: (TrackEntity) -> Unit,
    private var onDelete: (TrackEntity) -> Unit) :
    ListAdapter<TrackEntity, TrackListAdapter.ViewHolder>(TrackDiffCallback()) {

    class ViewHolder(
        private val binding: TrackItemBinding,
        private var onEdit: (TrackEntity) -> Unit,
        private var onDelete: (TrackEntity) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        private var trackId: Long = -1
        private var nameView = binding.name
        private var excercise = binding.excerciseType
        private var thumbnail = binding.thumbnail
        private var calories = binding.calories
        private var distance = binding.distance
        private var track: TrackEntity? = null

        fun bind(track: TrackEntity) {
            trackId = track.id
            nameView.text = track.name
            excercise.text = track.excerciseType.toString()
            calories.text = track.calories.toString()
            distance.text = track.distance.toString()
            thumbnail.setImageResource(R.drawable.person)
            this.track = track
            binding.deleteButton.setOnClickListener {
                onDelete(track)
            }
            binding.root.setOnClickListener {
                onEdit(track)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            TrackItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onEdit,
            onDelete
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class TrackDiffCallback : DiffUtil.ItemCallback<TrackEntity>() {
        override fun areItemsTheSame(oldItem: TrackEntity, newItem: TrackEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TrackEntity, newItem: TrackEntity): Boolean {
            return oldItem == newItem
        }
    }


}