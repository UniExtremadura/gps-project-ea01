package es.unex.giiis.asee.snapmap_ea01.view.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import es.unex.giiis.asee.snapmap_ea01.adapters.FollowersAdapter
import es.unex.giiis.asee.snapmap_ea01.data.model.User
import es.unex.giiis.asee.snapmap_ea01.data.model.UserUserFollowRef
import es.unex.giiis.asee.snapmap_ea01.databinding.FragmentFollowersBinding

/**
 * A simple [Fragment] subclass.
 * Use the [FollowersFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FollowersFragment : Fragment() {

    private var _binding: FragmentFollowersBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: FollowersAdapter
    private val viewModel: FollowersViewModel by viewModels { FollowersViewModel.Factory }
    private val homeViewModel: HomeViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFollowersBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun setUpRecyclerView() {
        adapter = viewModel.user?.let {
            FollowersAdapter(users = mutableListOf(), requireContext(), mutableListOf(),
                it
            )
        }!!

        adapter.setOnFollowButtonClickListener(object : FollowersAdapter.OnFollowButtonClickListener {
            override fun onFollowButtonClick(user: User, flag: Boolean) {
                val userFollowRef = viewModel.user?.userId?.let { UserUserFollowRef(it, user.userId!!) }
                if (userFollowRef != null) {
                    if (flag)
                        viewModel.followUser(userFollowRef)
                    else
                        viewModel.unfollowUser(userFollowRef)
                }
            }
        })

        with(binding) {
            rVFollowers.layoutManager = LinearLayoutManager(requireContext())
            rVFollowers.adapter = adapter
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.user.observe(viewLifecycleOwner) { user ->
            viewModel.user = user
            setUpRecyclerView()
            subscribeUi(adapter)
        }

    }

    private fun subscribeUi(adapter: FollowersAdapter) {
        viewModel.followers.observe(viewLifecycleOwner) { usersList ->
            adapter.updateData(usersList, null)
        }
        viewModel.following.observe(viewLifecycleOwner) { usersList ->
            adapter.updateData(null, usersList)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
