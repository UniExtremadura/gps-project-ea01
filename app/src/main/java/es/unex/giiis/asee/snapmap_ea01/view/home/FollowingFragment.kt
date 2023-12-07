package es.unex.giiis.asee.snapmap_ea01.view.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import es.unex.giiis.asee.snapmap_ea01.adapters.FollowingAdapter
import es.unex.giiis.asee.snapmap_ea01.adapters.SearchAdapter
import es.unex.giiis.asee.snapmap_ea01.api.getNetworkService
import es.unex.giiis.asee.snapmap_ea01.data.Repository
import es.unex.giiis.asee.snapmap_ea01.data.model.User
import es.unex.giiis.asee.snapmap_ea01.data.model.UserUserFollowRef
import es.unex.giiis.asee.snapmap_ea01.database.SnapMapDatabase
import es.unex.giiis.asee.snapmap_ea01.databinding.FragmentFollowingBinding
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [FollowersFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FollowingFragment : Fragment() {

    private lateinit var adapter: FollowingAdapter
    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FollowingViewModel by viewModels { FollowingViewModel.Factory }
    private val homeViewModel: HomeViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.user.observe(viewLifecycleOwner) { user ->
            viewModel.user = user
            setUpRecyclerView()
            subscribeUi(adapter)
        }
    }

    private fun setUpRecyclerView() {
        adapter = viewModel.user?.let { FollowingAdapter(users = mutableListOf(), requireContext(), it) }!!

        adapter.setOnFollowButtonClickListener(object : FollowingAdapter.OnFollowButtonClickListener {
            override fun onFollowButtonClick(user: User, flag: Boolean) {
                val userFollowRef = viewModel.user!!.userId?.let { UserUserFollowRef(it, user.userId!!) }
                if (userFollowRef != null) {
                    if (!flag)
                        viewModel.unfollowUser(userFollowRef)
                }
            }
        })

        with(binding) {
            rVFollowing.layoutManager = LinearLayoutManager(requireContext())
            rVFollowing.adapter = adapter
        }
    }

    private fun subscribeUi(adapter: FollowingAdapter) {
        viewModel.following.observe(viewLifecycleOwner) { usersList ->
            adapter.updateUsers(usersList)
        }
    }

}