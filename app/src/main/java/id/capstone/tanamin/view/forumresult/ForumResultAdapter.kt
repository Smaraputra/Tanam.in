package id.capstone.tanamin.view.forumresult

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import id.capstone.tanamin.R
import id.capstone.tanamin.data.remote.response.DataItem

class ForumResultAdapter(private val message: List<DataItem>, private val userID: Int) : RecyclerView.Adapter<ForumResultAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val layout = when (viewType) {
            TYPE_ME -> R.layout.rv_forum_chat_me
            TYPE_OTHER -> R.layout.rv_forum_chat_other
            else -> throw IllegalArgumentException("Unsupported type")
        }
        val view = LayoutInflater
            .from(parent.context)
            .inflate(layout, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(message[position], userID)
    }

    override fun getItemCount(): Int = message.size

    override fun getItemViewType(position: Int): Int {
        val data = message[position]
        return when {
            data.usersId==userID -> {
                0
            }
            data.usersId>userID || data.usersId<userID -> {
                1
            }
            else -> {
                throw IllegalArgumentException("Unsupported type")
            }
        }
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private fun bindMe(data: DataItem) {
            itemView.findViewById<TextView>(R.id.text_gchat_message_me).text = data.messege
        }

        private fun bindOther(data: DataItem) {
            val name = data.name.substring(data.name.lastIndexOf(" ")+1)
            itemView.findViewById<TextView>(R.id.text_gchat_message_other).text = data.messege
            itemView.findViewById<TextView>(R.id.text_gchat_user_other).text = name
        }

        fun bind(message: DataItem, userID: Int) {
            when {
                message.usersId==userID -> {
                    bindMe(message)
                }
                message.usersId>userID || message.usersId<userID -> {
                    bindOther(message)
                }
                else -> {
                    throw IllegalArgumentException("Unsupported type")
                }
            }
        }
    }

    companion object {
        private const val TYPE_ME = 0
        private const val TYPE_OTHER = 1
    }
}