package es.unex.giiis.asee.snapmap_ea01.view.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import es.unex.giiis.asee.snapmap_ea01.data.model.User
import es.unex.giiis.asee.snapmap_ea01.data.model.UserUserFollowRef
import es.unex.giiis.asee.snapmap_ea01.database.SnapMapDatabase
import es.unex.giiis.asee.snapmap_ea01.databinding.FragmentFollowersBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A simple [Fragment] subclass.
 * Use the [FollowersFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FollowersFragment : Fragment() {

    private var _binding: FragmentFollowersBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: SnapMapDatabase
    private lateinit var adapter: FollowersAdapter
    private lateinit var actualUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = SnapMapDatabase.getInstance(requireContext())!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFollowersBinding.inflate(inflater, container, false)
        setUpRecyclerView()
        return binding.root
    }

    private fun setUpRecyclerView() {
        actualUser = requireActivity().intent.getSerializableExtra(HomeActivity.USER_INFO) as User
        adapter = FollowersAdapter(mutableListOf(), requireContext(), mutableListOf(), actualUser)
        with(binding) {
            rVFollowers.layoutManager = LinearLayoutManager(requireContext())
            rVFollowers.adapter = adapter
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter.setOnFollowButtonClickListener(object : FollowersAdapter.OnFollowButtonClickListener {
            override fun onFollowButtonClick(user: User, flag: Boolean) {
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

    private fun loadData() {
        lifecycleScope.launch {
            val userFollowRefList = actualUser.userId?.let { db.userUserFollowRefDao().getFollowers(it) }
            val usersList = mutableListOf<User>()
            val usersFollowersList = mutableListOf<User>()

            if (userFollowRefList != null) {
                for (userFollow in userFollowRefList) {
                    val id = userFollow.user1
                    usersList.add(db.userDao().getUserById(id))
                }
            }

            val userFollowRefList2 = actualUser.userId?.let { db.userUserFollowRefDao().getFollowing(it) }

            if (userFollowRefList2 != null) {
                for (userFollow in userFollowRefList2) {
                    val id = userFollow.user2
                    usersFollowersList.add(db.userDao().getUserById(id))
                }
            }

            adapter.updateData(usersList, usersFollowersList)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
