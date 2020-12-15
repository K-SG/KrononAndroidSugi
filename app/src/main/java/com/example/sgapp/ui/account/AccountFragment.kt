package com.example.sgapp.ui.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.sgapp.R
import com.example.sgapp.TopActivity

class AccountFragment : Fragment() {

    private lateinit var notificationsViewModel: AccountViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
            ViewModelProvider(this).get(AccountViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_account, container, false)
//        val textView: TextView = root.findViewById(R.id.text_notifications)
//        notificationsViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
        var logoutButton = root?.findViewById<ImageButton>(R.id.logout_button)
        logoutButton?.setOnClickListener{
            val intent = Intent(activity, TopActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
        return root
    }
}