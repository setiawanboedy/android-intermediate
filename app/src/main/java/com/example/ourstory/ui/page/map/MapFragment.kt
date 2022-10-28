package com.example.ourstory.ui.page.map

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.ourstory.R
import com.example.ourstory.core.Status
import com.example.ourstory.databinding.FragmentMapBinding
import com.example.ourstory.domain.params.MapParams
import com.example.ourstory.ui.page.detail.DetailActivity
import com.example.ourstory.utils.Constants.STORY
import com.example.ourstory.utils.bitmapFromVector
import com.example.ourstory.utils.snackBar
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapFragment : Fragment(), OnMapReadyCallback {
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MapViewModel by viewModels()
    private lateinit var map: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.maps) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

    }

    private val boundsBuilder = LatLngBounds.Builder()
    private fun observeViewModel() {
        viewModel.getStoriesLocation(MapParams(1)).observe(viewLifecycleOwner) { res ->
            when (res.status) {
                Status.LOADING -> {}
                Status.ERROR -> {
                    binding.root.snackBar(resources.getString(R.string.not_found_story_location))
                }
                Status.SUCCESS -> {
                    res.data?.listStory?.forEach { story ->
                        if (story.lat != null && story.lon != null) {
                            val latLng = LatLng(story.lat, story.lon)

                            map.addMarker(
                                MarkerOptions()
                                    .position(latLng)
                                    .title(story.name)
                                    .icon(
                                        bitmapFromVector(
                                            requireContext(),
                                            R.drawable.ic_action_location
                                        )
                                    )
                            )?.showInfoWindow()
                            boundsBuilder.include(latLng)

                            val bounds: LatLngBounds = boundsBuilder.build()
                            map.animateCamera(
                                CameraUpdateFactory.newLatLngBounds(
                                    bounds,
                                    resources.displayMetrics.widthPixels,
                                    resources.displayMetrics.heightPixels,
                                    300
                                )
                            )

                        }
                        map.setOnInfoWindowClickListener {
                            res.data.listStory.asSequence()
                                .filter { a -> a.lat != null && a.lon != null }.filter { b ->
                                    val latLng = LatLng(b.lat!!, b.lon!!)
                                    it.title == b.name && it.position == latLng
                                }.forEach { story ->
                                    val intent =
                                        Intent(requireActivity(), DetailActivity::class.java)
                                    intent.putExtra(STORY, story)
                                    startActivity(intent)
                                }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.uiSettings.isIndoorLevelPickerEnabled = true
        map.uiSettings.isCompassEnabled = true


        getStoryLocation()
        observeViewModel()
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted)
            getStoryLocation()
    }

    private fun getStoryLocation() {
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION,
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            map.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

}
