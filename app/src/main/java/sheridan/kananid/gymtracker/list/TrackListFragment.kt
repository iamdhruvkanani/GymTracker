package sheridan.kananid.gymtracker.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.track_list.*
import sheridan.kananid.gymtracker.R
import sheridan.kananid.gymtracker.ViewModelFactory
import sheridan.kananid.gymtracker.database.TrackDatabase
import sheridan.kananid.gymtracker.databinding.TrackListBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class TrackListFragment : Fragment() {
    private lateinit var listViewModel: TrackListViewModel

    private val adapter = TrackListAdapter(
        onEdit = { donut ->
            findNavController().navigate(
                TrackListFragmentDirections.actionListToEntry(donut.id)
            )
        },
        onDelete = { donut ->
            NotificationManagerCompat.from(requireContext()).cancel(donut.id.toInt())
            listViewModel.delete(donut)
        }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = TrackListBinding.bind(view)
        val trackDao = TrackDatabase.getDatabase(requireContext()).trackDao()
        listViewModel = ViewModelProvider(this, ViewModelFactory(trackDao))
            .get(TrackListViewModel::class.java)

        listViewModel.tracks.observe(viewLifecycleOwner) { tracks ->
            adapter.submitList(tracks)
        }

        recyclerView.adapter = adapter

        binding.fab.setOnClickListener { fabView ->
            fabView.findNavController().navigate(
                TrackListFragmentDirections.actionListToEntry()
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return TrackListBinding.inflate(inflater, container, false).root
    }
}