package com.example.sgapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.retrofit2_kotlin.Retrofit2.*
import com.example.sgapp.api.CreateUser
import com.example.sgapp.api.CreateUserErrorResponse
import com.example.sgapp.api.CreateUserResponse
import com.example.sgapp.api.KrononClient.Companion.BaseUrl
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NewUserCreateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_user)
        val returnButton = findViewById<ImageButton>(R.id.back_button)
        val returnLogin = findViewById<TextView>(R.id.new_login)
        val createUserButton = findViewById<Button>(R.id.create_button)
        val name = findViewById<EditText>(R.id.login_email)
        val email = findViewById<EditText>(R.id.user_mail)
        val password = findViewById<EditText>(R.id.login_password)
        val passwordConfirm = findViewById<EditText>(R.id.password_confirm)


        returnButton.setOnClickListener{
            finish()
        }
        returnLogin.setOnClickListener{
            finish()
        }
        createUserButton?.setOnClickListener{
            val nameStr = name.text.toString()
            val emailStr = email.text.toString()
            val passwordStr = password.text.toString()
            var passwordConfirmStr = passwordConfirm.text.toString()

            if(passwordStr == passwordConfirmStr || passwordStr == "" || passwordConfirmStr == "" ){
                getAPI(nameStr,emailStr,passwordStr)
            }else{
                AlertDialog.Builder(this) // FragmentではActivityを取得して生成
                    .setTitle("エラー")
                    .setMessage("passwordが異なっているよ")
                    .setPositiveButton("OK", { dialog, which ->
                        // TODO:Yesが押された時の挙動

                    })
                    .show()
            }

        }
    }

    fun getAPI(name:String,email:String,password:String){
        val retrofit = Retrofit.Builder()
            .baseUrl(BaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(KrononService::class.java)
        val user = CreateUser(name,email,password)
        val call = service.createUser(user)
        call.enqueue(object : Callback<CreateUserResponse> {
            override fun onResponse(call : Call<CreateUserResponse>, response: Response<CreateUserResponse>){
                if(response.code() == 201){
                    val userResponse = response.body()
                    getSharedPreferences("user_data", Context.MODE_PRIVATE).edit().apply {
                        putString("name", userResponse!!.data?.name.toString())
                        putString("email", userResponse!!.data?.email.toString())
                        commit()
                    }
                    val intent = Intent(this@NewUserCreateActivity, MainButtomNavigationActivity::class.java)
                    startActivity(intent)
                }else {
                    val responseError = response.errorBody()
                    //GsonでKotlinクラスに型を変えてもらえる。
                    val exceptionBody = Gson().fromJson(responseError?.string(), CreateUserErrorResponse::class.java)
                    AlertDialog.Builder(this@NewUserCreateActivity) // FragmentではActivityを取得して生成
                        .setTitle("エラー")
                        .setMessage(exceptionBody.message.toString())
                        .setPositiveButton("OK", { dialog, which ->
                            // TODO:Yesが押された時の挙動
                        })
                        .show()
                }
            }

            override fun onFailure(calll: Call<CreateUserResponse>, t: Throwable){
//                Toast.makeText(this@NewUserCreateActivity, "Fail", Toast.LENGTH_LONG)
                AlertDialog.Builder(this@NewUserCreateActivity) // FragmentではActivityを取得して生成
                    .setTitle("ネットワークエラー")
                    .setMessage("ネットワークの接続が悪いです")
                    .setPositiveButton("OK", { dialog, which ->
                        // TODO:Yesが押された時の挙動
                    })
                    .show()
            }

        })
    }

}