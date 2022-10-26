package com.example.ourstory.ui.page.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ourstory.R
import com.example.ourstory.databinding.FragmentHomeBinding
import com.example.ourstory.domain.params.StoryParams
import com.example.ourstory.session.SessionManager
import com.example.ourstory.ui.page.auth.AuthActivity
import com.example.ourstory.ui.page.detail.DetailActivity
import com.example.ourstory.ui.view.PopDialog
import com.example.ourstory.utils.Constants.STORY
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var storyAdapter: StoryPageAdapter
    private val viewModel: HomeViewModel by viewModels()

    @Inject
    lateinit var pop: PopDialog

    @Inject
    lateinit var session: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.myToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_exit -> {
                    showDelDialog()
                    true
                }
                else -> false
            }
        }

        storyAdapter = StoryPageAdapter(requireContext())
        binding.rvStories.apply {
            adapter = storyAdapter.withLoadStateHeaderAndFooter(
                footer = StoryStateAdapter { storyAdapter.retry() },
                header = StoryStateAdapter { storyAdapter.retry() }
            )
            layoutManager = LinearLayoutManager(requireContext())

        }
        getStories()

        storyAdapter.setOnItemClick { story ->
            val intent = Intent(requireActivity(), DetailActivity::class.java)
            intent.putExtra(STORY, story)
            startActivity(intent)
        }
    }

    private fun getStories() {
        val params = StoryParams(page = 5, size = 20)
        viewModel.getStories(params).observe(viewLifecycleOwner) { res ->
            storyAdapter.submitData(lifecycle, res)
        }
    }

    private fun showDelDialog() {
        pop.showDialog(requireContext(),
            positive = { _, _ ->
                session.logout()
                startActivity(Intent(requireActivity(), AuthActivity::class.java))
                requireActivity().finish()
            }, negative = { _, _ ->

            },
            title = getString(R.string.title_logout),
            subTitle = getString(R.string.sub_logout)
        )
    }


    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }

}