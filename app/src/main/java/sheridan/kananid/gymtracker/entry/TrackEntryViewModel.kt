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
package sheridan.kananid.gymtracker.entry

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope


import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sheridan.kananid.gymtracker.database.TrackDao
import sheridan.kananid.gymtracker.database.TrackEntity

class TrackEntryViewModel(private val trackDao: TrackDao) : ViewModel() {

    private var trackLiveData: LiveData<TrackEntity>? = null

    fun get(id: Long): LiveData<TrackEntity> {
        return trackLiveData ?: liveData {
            emit(trackDao.get(id))
        }.also {
            trackLiveData = it
        }
    }

    fun addData(
        id: Long,
        name: String,
        excerciseType: String,
        height: String,
        weight: String,
        calories: String,
        distance: String,
        setupNotification: (Long) -> Unit
    ) {
        val track = TrackEntity(id, name,height,weight,excerciseType,calories,distance )

        CoroutineScope(Dispatchers.Main.immediate).launch {
            var actualId = id

            if (id > 0) {
                update(track)
            } else {
                actualId = insert(track)
            }

            setupNotification(actualId)
        }
    }

    private suspend fun insert(donut: TrackEntity): Long {
        return trackDao.insert(donut)
    }

    private fun update(donut: TrackEntity) = viewModelScope.launch(Dispatchers.IO) {
        trackDao.update(donut)
    }
}
