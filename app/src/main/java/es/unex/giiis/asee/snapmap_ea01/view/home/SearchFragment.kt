package es.unex.giiis.asee.snapmap_ea01.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import es.unex.giiis.asee.snapmap_ea01.adapters.SearchAdapter
import es.unex.giiis.asee.snapmap_ea01.api.getNetworkService
import es.unex.giiis.asee.snapmap_ea01.data.Repository
import es.unex.giiis.asee.snapmap_ea01.data.model.User
import es.unex.giiis.asee.snapmap_ea01.data.model.UserUserFollowRef
import es.unex.giiis.asee.snapmap_ea01.database.SnapMapDatabase
import es.unex.giiis.asee.snapmap_ea01.databinding.FragmentSearchBinding
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: SearchAdapter
    private val viewModel: SearchViewModel by viewModels { SearchViewModel.Factory }
    private val homeViewModel: HomeViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.user.observe(viewLifecycleOwner) { user ->
            viewModel.user = user
            setUpRecyclerView()
            setUpListeners()
            subscribeUi(adapter)
        }
    }

    private fun setUpListeners(){
        binding.etSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterUsers(newText.orEmpty())
                return true
            }
        })
    }

    private fun subscribeUi(adapter: SearchAdapter) {
        viewModel.following.observe(viewLifecycleOwner) { usersList ->
            adapter.updateUsers(null, usersList)
        }
    }

    private fun filterUsers(query: String) {
        val filteredUsers = if (query.isNotEmpty()) {
                                viewModel.getUsers().filter { it.username.contains(query, ignoreCase = true) }
                            } else {
                                emptyList()
                            }
        adapter.updateUsers(filteredUsers.toMutableList(), null)
    }

    private fun setUpRecyclerView() {
        adapter = viewModel.user?.let {
            SearchAdapter(users = mutableListOf(), requireContext(), mutableListOf(),
                it
            )

        }!!

        adapter.setOnFollowButtonClickListener(object : SearchAdapter.OnFollowButtonClickListener {
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
            rVSearch.layoutManager = LinearLayoutManager(requireContext())
            rVSearch.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}