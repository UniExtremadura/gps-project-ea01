package es.unex.giiis.asee.snapmap_ea01.view.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getString
import androidx.recyclerview.widget.RecyclerView
import es.unex.giiis.asee.snapmap_ea01.R
import es.unex.giiis.asee.snapmap_ea01.data.model.User
import es.unex.giiis.asee.snapmap_ea01.databinding.SearchListItemBinding
import es.unex.giiis.asee.snapmap_ea01.databinding.UserListItemBinding

class FollowingAdapter(
    var users: List<User>,
    private val context: Context?,
    private val actualUser: User
) : RecyclerView.Adapter<FollowingAdapter.ShowViewHolder>() {

    interface OnFollowButtonClickListener {
        fun onFollowButtonClick(user: User, flag: Boolean)
    }

    // Propiedad para almacenar el escuchador del clic del botón
    private var followButtonClickListener: OnFollowButtonClickListener? = null

    class ShowViewHolder(
        private val binding: SearchListItemBinding,
        private val adapter: FollowingAdapter,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            with(binding) {
                val isCurrentUser = user.userId == adapter.actualUser.userId
                val isFollowing = adapter.users.any { it.userId == user.userId }

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

    fun setOnFollowButtonClickListener(listener: OnFollowButtonClickListener) {
        followButtonClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowViewHolder {
        val binding =
            SearchListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShowViewHolder(binding, this)
    }

    override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount() = users.size

    fun updateUsers(updatedUsers: List<User>) {
        users = updatedUsers
        notifyDataSetChanged()
    }
}
