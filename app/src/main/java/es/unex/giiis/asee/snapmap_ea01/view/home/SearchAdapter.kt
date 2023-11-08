package es.unex.giiis.asee.snapmap_ea01.view.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import es.unex.giiis.asee.snapmap_ea01.databinding.SearchListItemBinding
import es.unex.giiis.asee.snapmap_ea01.model.User

class SearchAdapter(
    var users: List<User>,
    private val context: Context?
) : RecyclerView.Adapter<SearchAdapter.ShowViewHolder>() {

    class ShowViewHolder(
        private val binding: SearchListItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            with(binding) {
                tVUser.text = user.name
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowViewHolder {
        val binding =
            SearchListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShowViewHolder(binding)
    }

    override fun getItemCount() = users.size

    override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {
        holder.bind(users[position])
    }

    fun updateUsers(updatedUsers: List<User>) {
        users = updatedUsers
        notifyDataSetChanged()
    }

}