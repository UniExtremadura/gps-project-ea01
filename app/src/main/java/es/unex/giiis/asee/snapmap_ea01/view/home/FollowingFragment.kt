package es.unex.giiis.asee.snapmap_ea01.view.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import es.unex.giiis.asee.snapmap_ea01.adapters.FollowingAdapter
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
    private lateinit var db: SnapMapDatabase
    private val usersList = mutableListOf<User>()
    private lateinit var actualUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = SnapMapDatabase.getInstance(requireContext())!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)
        setUpRecyclerView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter.setOnFollowButtonClickListener(object : FollowingAdapter.OnFollowButtonClickListener {
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

    private fun setUpRecyclerView() {
        actualUser = requireActivity().intent.getSerializableExtra(HomeActivity.USER_INFO) as User
        adapter = FollowingAdapter(users = usersList, requireContext(), actualUser)
        with(binding) {
            rVFollowing.layoutManager = LinearLayoutManager(requireContext())
            rVFollowing.adapter = adapter
        }
    }

    private fun loadData() {
        lifecycleScope.launch {
            val userFollowRefList = actualUser.userId?.let { db.userUserFollowRefDao().getFollowing(it) }

            if (userFollowRefList != null) {
                for (userFollow in userFollowRefList) {
                    val id = userFollow.user2
                    usersList.add(db.userDao().getUserById(id))
                }
            }

            adapter.updateUsers(usersList)
        }
    }
}