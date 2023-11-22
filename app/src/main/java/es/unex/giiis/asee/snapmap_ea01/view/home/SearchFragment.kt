package es.unex.giiis.asee.snapmap_ea01.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import es.unex.giiis.asee.snapmap_ea01.adapters.SearchAdapter
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
    private var filteredUsers = mutableListOf<User>()
    private lateinit var actualUser : User
    private lateinit var db: SnapMapDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = SnapMapDatabase.getInstance(requireContext())!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        setUpRecyclerView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterUsers(newText.orEmpty())
                return true

            }
        })

        adapter.setOnFollowButtonClickListener(object : SearchAdapter.OnFollowButtonClickListener {
            override fun onFollowButtonClick(user: User, flag: Boolean) {
                val actualUser = requireActivity().intent.getSerializableExtra(HomeActivity.USER_INFO) as User
                lifecycleScope.launch {
                    val userFollowRef = actualUser.userId?.let { UserUserFollowRef(it, user.userId!!) }
                    if (userFollowRef != null) {
                        if (flag)
                            db.userUserFollowRefDao().insertUserUserFollowRef(userFollowRef)
                        else
                            db.userUserFollowRefDao().deleteUserFollowRef(userFollowRef)
                    }
                }
            }
        })

        loadData()

    }

    private fun filterUsers(query: String) {
        filteredUsers.clear()
        if (query.isNotEmpty()) {
            lifecycleScope.launch {
                for (user in db.userDao().getUsers()) {
                    if (user.username.contains(query, ignoreCase = true)) {
                        filteredUsers.add(user)
                    }
                }
            }
        }
        adapter.updateUsers(filteredUsers, null)
    }

    private fun setUpRecyclerView() {
        actualUser = requireActivity().intent.getSerializableExtra(HomeActivity.USER_INFO) as User
        adapter = SearchAdapter(users = mutableListOf(), requireContext(), mutableListOf(), actualUser)
        with(binding) {
            rVSearch.layoutManager = LinearLayoutManager(requireContext())
            rVSearch.adapter = adapter
        }
    }

    private fun loadData() {
        lifecycleScope.launch {
            val userFollowRefList = actualUser.userId?.let { db.userUserFollowRefDao().getFollowing(it) }
            val usersList = mutableListOf<User>()

            if (userFollowRefList != null) {
                for (userFollow in userFollowRefList) {
                    val id = userFollow.user2
                    usersList.add(db.userDao().getUserById(id))
                }
            }
            adapter.updateUsers(null, usersList)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}