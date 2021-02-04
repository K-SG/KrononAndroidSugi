package com.example.sgapp.ui.account

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.retrofit2_kotlin.Retrofit2.KrononService
import com.example.sgapp.R
import com.example.sgapp.TopActivity
import com.example.sgapp.api.*
import com.example.sgapp.api.KrononClient.Companion.BaseUrl
import com.example.sgapp.ui.login.LoginActivity
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


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
        val logoutButton = root?.findViewById<ImageButton>(R.id.logout_button)
        val nameText = root?.findViewById<TextView>(R.id.account_name_text)
        val emailText = root?.findViewById<TextView>(R.id.account_mail_text)
        logoutButton?.setOnClickListener{
            val intent = Intent(activity, TopActivity::class.java)
            startActivity(intent)
        }
        val pref = activity?.getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val name = pref?.getString("name", "")
        val email = pref?.getString("email", "")
        nameText?.text = name
        emailText?.text = email

        logoutButton?.setOnClickListener {
            getAPI()
        }
        return root
    }
    fun getAPI(){
        val pref = activity?.getSharedPreferences("user_data", Context.MODE_PRIVATE)
        var token = pref?.getString("token", "")

        val retrofit = Retrofit.Builder()
            .baseUrl(BaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        token = "Bearer $token"
        val service = retrofit.create(KrononService::class.java)
        val call = service.logout(token)
        call.enqueue(object : Callback<LogoutUserResponse> {
            override fun onResponse(
                call: Call<LogoutUserResponse>,
                response: Response<LogoutUserResponse>
            ) {
                if (response.code() == 200) {
                    deleteUserInfo()
                    val intent = Intent(activity, LoginActivity::class.java)
                    startActivity(intent)
                } else {
                    val responseError = response.errorBody()
                    val exceptionBody = Gson().fromJson(responseError?.string(), LoginUserErrorResponse::class.java)
                    activity?.let {
                        AlertDialog.Builder(it) // FragmentではActivityを取得して生成
                            .setTitle("エラー")
                            .setMessage(exceptionBody.message.toString())
                            .setPositiveButton("OK", { dialog, which ->
                                // TODO:Yesが押された時の挙動
                            })
                            .show()
                    }

                }
            }

            override fun onFailure(calll: Call<LogoutUserResponse>, t: Throwable) {
//                Toast.makeText(this@NewUserCreateActivity, "Fail", Toast.LENGTH_LONG)
                activity?.let {
                    AlertDialog.Builder(it) // FragmentではActivityを取得して生成
                        .setTitle("ネットワークエラー")
                        .setMessage("ネットワークの接続が悪いです")
                        .setPositiveButton("OK", { dialog, which ->
                            // TODO:Yesが押された時の挙動
                        })
                        .show()
                }
            }

        })
    }

    private fun deleteUserInfo() {
        val pref: SharedPreferences? = activity?.getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val userData = pref?.edit()
        userData?.remove("name")
        userData?.remove("email")
        userData?.remove("token")
        //commit()は同期、apply()は非同期
        userData?.commit()
    }
}
