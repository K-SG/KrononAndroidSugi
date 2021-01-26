package com.example.sgapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.retrofit2_kotlin.Retrofit2.*
import com.example.sgapp.Retrofit2.CreateUserErrorResponse
import com.example.sgapp.Retrofit2.CreateUserResponse
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
        val name = findViewById<EditText>(R.id.user_name)
        val email = findViewById<EditText>(R.id.user_mail)
        val password = findViewById<EditText>(R.id.password)
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

            if(passwordStr == passwordConfirmStr){
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
//                    Toast.makeText(this@MainActivity, weatherResponse!!.sys!!.country, Toast.LENGTH_LONG).show() }
                    Toast.makeText(this@NewUserCreateActivity, userResponse!!.success.toString()
                            +"\n"+
                            userResponse!!.code.toString()
                            +"\n"+
                            userResponse!!.data?.token.toString()
                        , Toast.LENGTH_LONG).show()
                    val intent = Intent(this@NewUserCreateActivity, MainButtomNavigationActivity::class.java)
                    startActivity(intent)
                }
                if(response.code() == 400){
                    val responseError = response.errorBody()
                    //GsonでKotlinクラスに型を変えてもらえる。
                    val exceptionBody = Gson().fromJson(responseError?.string(), CreateUserErrorResponse::class.java)
                    exceptionBody.message?.name?.forEach { element-> Log.i("nameError",element) }
                    exceptionBody.message?.email?.forEach { element-> Log.i("emailError",element) }
                    exceptionBody.message?.password?.forEach { element-> Log.i("passwordError",element) }
//                    Jsonのまま受け取る
//                    val jsonObj = JSONObject(responseError?.charStream()?.readText())
//                    val message = jsonObj.getJSONObject("message").get("email")
//                    JSONObject jObjError = new JSONObject(response.errorBody()?.string());
//                    Toast.makeText(this@NewUserCreateActivity, jsonObj.getString("message"), Toast.LENGTH_LONG).show()

                    AlertDialog.Builder(this@NewUserCreateActivity) // FragmentではActivityを取得して生成
                        .setTitle("エラー")
                        .setMessage("何かが間違っているよ")
                        .setPositiveButton("OK", { dialog, which ->
                            // TODO:Yesが押された時の挙動
                        })
                        .show()
                }
            }

            override fun onFailure(calll: Call<CreateUserResponse>, t: Throwable){
                Toast.makeText(this@NewUserCreateActivity, "Fail", Toast.LENGTH_LONG)
            }
        })
    }

    companion object {

        //        var BaseUrl = "http://api.openweathermap.org/"
        var BaseUrl = "http://54.199.202.205/"
    }
}