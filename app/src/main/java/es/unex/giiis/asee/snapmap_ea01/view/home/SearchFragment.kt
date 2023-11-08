package es.unex.giiis.asee.snapmap_ea01.view.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import es.unex.giiis.asee.snapmap_ea01.data.dummyUsers
import es.unex.giiis.asee.snapmap_ea01.databinding.FragmentSearchBinding
import es.unex.giiis.asee.snapmap_ea01.model.User

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
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
        setUpRecyclerView()
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

    private fun filterUsers(query: String) {
        filteredUsers.clear()
        if (query.isNotEmpty()) {
            for (user in dummyUsers) {
                if (user.name.contains(query, ignoreCase = true)) {
                    filteredUsers.add(user)
                }
            }
        }
        adapter.updateUsers(filteredUsers)
    }


    private fun setUpRecyclerView() {
        adapter = SearchAdapter(users = mutableListOf(), requireContext())
        with(binding) {
            rVSearch.layoutManager = LinearLayoutManager(requireContext())
            rVSearch.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // avoid memory leaks
    }
}