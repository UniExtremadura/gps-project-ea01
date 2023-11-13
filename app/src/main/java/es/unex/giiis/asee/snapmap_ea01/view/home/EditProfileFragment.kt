package es.unex.giiis.asee.snapmap_ea01.view.home

import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import es.unex.giiis.asee.snapmap_ea01.R
import es.unex.giiis.asee.snapmap_ea01.data.model.User
import es.unex.giiis.asee.snapmap_ea01.database.SnapMapDatabase
import es.unex.giiis.asee.snapmap_ea01.databinding.FragmentEditProfileBinding
import es.unex.giiis.asee.snapmap_ea01.utils.CredentialCheck
import kotlinx.coroutines.launch

class EditProfileFragment : Fragment() {
    private lateinit var binding: FragmentEditProfileBinding
    private lateinit var user : User
    private lateinit var navController : NavController
    private val USER_INFO = "USER_INFO"
    private lateinit var db: SnapMapDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = SnapMapDatabase.getInstance(requireContext())!! // Recuperamos el contexto a través de requireContext
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)

        setUpUI()

        setUpListeners()

        return binding.root
    }
    private fun setUpUI(){
        user = requireActivity().intent.getSerializableExtra(USER_INFO) as User
        if(user != null){
            binding.etUsername.setText(user.username)
            binding.etAboutMe.setText(user.aboutMe)
            binding.etEmail.setText(user.email)
            binding.etPassword.setText(user.password)
        }
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
    private fun edit(){
        with(binding){
            val check = CredentialCheck.edit(
                etUsername.text.toString(),
                etEmail.text.toString(),
                etPassword.text.toString(),
                etAboutMe.text.toString()
            )
            if (check.fail) notifyInvalidCredentials(check.msg)
            else {
                lifecycleScope.launch {
                    val user = User(
                        user.userId,
                        etUsername.text.toString(),
                        etAboutMe.text.toString(),
                        etEmail.text.toString(),
                        etPassword.text.toString()
                    )

                    // Actualización de los datos de Usuario
                    val userDao = db.userDao()
                    userDao.updateUser(user)

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