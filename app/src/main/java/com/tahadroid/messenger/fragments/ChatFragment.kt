package com.tahadroid.messenger.fragments


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.tahadroid.messenger.ChatActivity
import com.tahadroid.messenger.ProfileActivity
import com.tahadroid.messenger.R
import com.tahadroid.messenger.model.User
import com.tahadroid.messenger.recyclerview.ChatItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.OnItemClickListener
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.fragment_chat.*

/**
 * A simple [Fragment] subclass.
 */
class ChatFragment : Fragment() {
    private lateinit var chatSection: Section
    private val firebaseStore: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_chat, container, false)
        val title = activity?.findViewById<TextView>(R.id.title_text_view)
        title?.text = "Chats"
        val circleImageVieww = activity?.findViewById<ImageView>(R.id.circle_image_view_profile)
        circleImageVieww?.setOnClickListener {
            startActivity(
                Intent(
                    activity,
                    ProfileActivity::class.java
                )
            )
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addChatListner(::setupRecyclerView)
    }

    private fun setupRecyclerView(item: List<Item>) {
        chat_rv.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = GroupAdapter<GroupieViewHolder>().apply {
                chatSection = Section(item)
                add(chatSection)
            setOnItemClickListener(onclickListner)
            }
        }
    }
val onclickListner = OnItemClickListener{item, view ->
    if(item is ChatItem) {
        val intent = Intent(activity, ChatActivity::class.java)
        intent.putExtra("name", item.user.name)
        intent.putExtra("image", item.user.profileImage)
        intent.putExtra("uid", item.uid)
        activity!!.startActivity(intent)
    }
}
    private fun addChatListner(onListner: (List<Item>) -> Unit) {
        firebaseStore.collection("users")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    return@addSnapshotListener
                }
                val items = mutableListOf<Item>()
                querySnapshot!!.documents.forEach {document->
                    items.add(ChatItem(document.id,document.toObject(User::class.java)!!, activity!!))
                }
                onListner(items)
            }
    }
}
