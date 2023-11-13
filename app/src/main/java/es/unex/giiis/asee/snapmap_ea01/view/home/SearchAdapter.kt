package es.unex.giiis.asee.snapmap_ea01.view.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import es.unex.giiis.asee.snapmap_ea01.databinding.SearchListItemBinding
import es.unex.giiis.asee.snapmap_ea01.data.model.User

class SearchAdapter(
    var users: List<User>,
    private val context: Context?
) : RecyclerView.Adapter<SearchAdapter.ShowViewHolder>() {

    // Interfaz para gestionar el clic del botón
    interface OnFollowButtonClickListener {
        fun onFollowButtonClick(user: User)
    }

    // Propiedad para almacenar el escuchador del clic del botón
    private var followButtonClickListener: OnFollowButtonClickListener? = null

    // Clase interna ViewHolder
    class ShowViewHolder(
        private val binding: SearchListItemBinding,
        private val adapter: SearchAdapter,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            with(binding) {
                tVUser.text = user.username
                btnSeguir.setOnClickListener {
                    adapter.followButtonClickListener?.onFollowButtonClick(user)
                }
            }
        }
    }

    // Método para establecer el escuchador del clic del botón desde fuera del adaptador
    fun setOnFollowButtonClickListener(listener: OnFollowButtonClickListener) {
        followButtonClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowViewHolder {
        val binding =
            SearchListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShowViewHolder(binding, this)
    }

    override fun getItemCount() = users.size

    override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {
        holder.bind(users[position])
    }

    // Método para actualizar la lista de usuarios
    fun updateUsers(updatedUsers: List<User>) {
        users = updatedUsers
        notifyDataSetChanged()
    }
}
