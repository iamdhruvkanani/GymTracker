package sheridan.kananid.gymtracker.entry

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.track_entryfragment.*
import kotlinx.android.synthetic.main.track_entryfragment.view.*
import sheridan.kananid.gymtracker.Notifier
import sheridan.kananid.gymtracker.R
import sheridan.kananid.gymtracker.ViewModelFactory
import sheridan.kananid.gymtracker.database.TrackDatabase
import sheridan.kananid.gymtracker.database.TrackEntity
import sheridan.kananid.gymtracker.databinding.TrackEntryfragmentBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class TrackEntryFragment : Fragment() {
    private lateinit var entryViewModel: TrackEntryViewModel

    private enum class EditingState {
        NEW_TRACK,
        EXISTING_TRACK
    }





    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val trackDao = TrackDatabase.getDatabase(requireContext()).trackDao()

        entryViewModel = ViewModelProvider(this, ViewModelFactory(trackDao))
            .get(TrackEntryViewModel::class.java)

        val binding = TrackEntryfragmentBinding.bind(view)

        var track: TrackEntity? = null
        val args: TrackEntryFragmentArgs by navArgs()
        val editingState =
            if (args.itemId > 0) EditingState.EXISTING_TRACK
            else EditingState.NEW_TRACK

        // If we arrived here with an itemId of >= 0, then we are editing an existing item
        if (editingState == EditingState.EXISTING_TRACK) {
            // Request to edit an existing item, whose id was passed in as an argument.
            // Retrieve that item and populate the UI with its details
            entryViewModel.get(args.itemId).observe(viewLifecycleOwner) { trackItem ->
                binding.name.setText(trackItem.name)
                binding.userHeight.setText(trackItem.excerciseType)
                binding.userCal.setText(trackItem.calories)
                binding.userDistance.setText(trackItem.distance)
                binding.userHeight.setText(trackItem.height)
                binding.userWeight.setText(trackItem.weight)
                track = trackItem
            }
        }

        // When the user clicks the Done button, use the data here to either update
        // an existing item or create a new one
        binding.doneButton.setOnClickListener {
            // Grab these now since the Fragment may go away before the setupNotification
            // lambda below is called
            val context = requireContext().applicationContext
            val navController = findNavController()

            entryViewModel.addData(
                track?.id ?: 0,
                binding.name.text.toString(),
                when (binding.excerciseGroup.checkedRadioButtonId) {
                    R.id.bike_button -> getString(R.string.track_bike)
                    R.id.run_button -> getString(R.string.track_run)
                    R.id.walk_button -> getString(R.string.track_walk)
                    else -> "Undefined"
                },
                binding.userHeight.text.toString(),
                binding.userWeight.text.toString(),
                binding.userCal.text.toString(),
                binding.userDistance.text.toString()






            ){ actualId ->
                val arg = TrackEntryFragmentArgs(actualId).toBundle()
                val pendingIntent = navController
                    .createDeepLink()
                    .setDestination(R.id.entry_dialog)
                    .setArguments(arg)
                    .createPendingIntent()

                Notifier.postNotification(actualId, context, pendingIntent)
            }
            doneButton.findNavController().navigate(
                TrackEntryFragmentDirections.actionEntryToList()
            )
        }

        // User clicked the Cancel button; just exit the dialog without saving the data
        binding.cancelButton.setOnClickListener {
            cancelButton.findNavController().navigate(
               TrackEntryFragmentDirections.actionEntryToList()
            )
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return TrackEntryfragmentBinding.inflate(inflater, container, false).root
    }
}
