package es.unex.giiis.asee.snapmap_ea01.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import es.unex.giiis.asee.snapmap_ea01.R
import es.unex.giiis.asee.snapmap_ea01.databinding.SearchListItemBinding
import es.unex.giiis.asee.snapmap_ea01.data.model.User

class SearchAdapter(
    var users: List<User>,
    private val context: Context?,
    private var usersList: List<User> = mutableListOf(),
    private val actualUser: User
) : RecyclerView.Adapter<SearchAdapter.ShowViewHolder>() {

    // Interfaz para gestionar el clic del botón
    interface OnFollowButtonClickListener {
        fun onFollowButtonClick(user: User, flag: Boolean)
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
                val isCurrentUser = user.userId == adapter.actualUser.userId
                val isFollowing = adapter.usersList.any { it.userId == user.userId }

                btnSeguir.setBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        getColorResource(isCurrentUser, isFollowing)
                    )
                )

                btnSeguir.text = getItemText(isCurrentUser, isFollowing)
                btnSeguir.setOnClickListener {
                    adapter.followButtonClickListener?.onFollowButtonClick(user, !isFollowing)
                }

                tVUser.text = user.username
            }
        }

        private fun getColorResource(isCurrentUser: Boolean, isFollowing: Boolean): Int {
            return when {
                isCurrentUser -> R.color.backgroundApp
                isFollowing -> R.color.items2
                else -> R.color.items
            }
        }

        private fun getItemText(isCurrentUser: Boolean, isFollowing: Boolean): String {
            return when {
                isCurrentUser -> adapter.context?.getString(R.string.you) ?: ""
                isFollowing -> adapter.context?.getString(R.string.following) ?: ""
                else -> adapter.context?.getString(R.string.follow) ?: ""
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
    fun updateUsers(updatedUsers: List<User>?, followedUsers: List<User>?) {
        if (updatedUsers != null) {
            users = updatedUsers
        }
        if (followedUsers != null) {
            usersList = followedUsers
        }
        notifyDataSetChanged()
    }
}
