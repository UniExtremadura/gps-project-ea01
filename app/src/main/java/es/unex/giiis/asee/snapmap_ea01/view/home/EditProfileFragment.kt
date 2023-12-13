package es.unex.giiis.asee.snapmap_ea01.view.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import es.unex.giiis.asee.snapmap_ea01.R
import es.unex.giiis.asee.snapmap_ea01.data.model.User
import es.unex.giiis.asee.snapmap_ea01.databinding.FragmentEditProfileBinding
import es.unex.giiis.asee.snapmap_ea01.utils.CredentialCheck

class EditProfileFragment : Fragment() {

    private lateinit var binding: FragmentEditProfileBinding
    private lateinit var navController : NavController
    private val USER_INFO = "USER_INFO"

    private val homeViewModel: HomeViewModel by activityViewModels()
    private val viewModel: EditProfileViewModel by viewModels { EditProfileViewModel.Factory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)

        setUpListeners()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.user.observe(viewLifecycleOwner) { user ->
            viewModel.user = user
            setUpUI()
        }
    }

    private fun setUpUI(){

        if(homeViewModel.user != null){

            binding.etUsername.setText(homeViewModel.user.value!!.username)
            binding.etAboutMe.setText(homeViewModel.user.value!!.aboutMe)
            binding.etEmail.setText(homeViewModel.user.value!!.email)
            binding.etPassword.setText(homeViewModel.user.value!!.password)


        }
        else Log.d("EditProfileFragment", "User is null")
    }

    private fun setUpListeners(){
        with(binding){
            btnEdit.setOnClickListener {
                edit()
            }
            btnBack.setOnClickListener {
                navController.navigateUp()
            }
        }
    }

    private fun edit() {
        with(binding) {
            val check = CredentialCheck.edit(
                etUsername.text.toString(),
                etEmail.text.toString(),
                etPassword.text.toString(),
                etAboutMe.text.toString()
            )
            if (check.fail) {
                notifyInvalidCredentials(check.msg)
            } else {
                viewModel.user?.let { currentUser ->
                    val user = User(
                        currentUser.userId,
                        etUsername.text.toString().trim(),
                        etAboutMe.text.toString(),
                        etEmail.text.toString(),
                        etPassword.text.toString()
                    )

                    viewModel.saveUser(user)
                    homeViewModel.user.value = user

                    Toast.makeText(requireContext(), "Datos actualizados con éxito", Toast.LENGTH_SHORT).show()

                    requireActivity().intent.putExtra(USER_INFO, user)

                    navController.navigateUp()
                }
            }
        }
    }

    private fun notifyInvalidCredentials(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }
}