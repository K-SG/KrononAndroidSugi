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
        var logoutButton = root?.findViewById<ImageButton>(R.id.logout_button)
        logoutButton?.setOnClickListener{
            val intent = Intent(activity, TopActivity::class.java)
            startActivity(intent)
        }
        return root
    }
}