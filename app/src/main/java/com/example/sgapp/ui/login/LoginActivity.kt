package com.example.sgapp.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.retrofit2_kotlin.Retrofit2.KrononService
import com.example.sgapp.DateManager
import com.example.sgapp.MainButtomNavigationActivity
import com.example.sgapp.NewUserCreateActivity

import com.example.sgapp.R
import com.example.sgapp.api.KrononClient
import com.example.sgapp.api.LoginUserResponse
import com.example.sgapp.api.LoginUser
import com.example.sgapp.api.LoginUserErrorResponse
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {

    private var KrononClient: KrononClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        val loginEmail = findViewById<EditText>(R.id.login_email)
        val loginPassword = findViewById<EditText>(R.id.login_password)
        val newUser = findViewById<TextView>(R.id.new_login)
        newUser.setOnClickListener{
            val intent = Intent(this, NewUserCreateActivity::class.java)
            startActivity(intent)
        }
        val loginButton = findViewById<Button>(R.id.login)
        loginButton.setOnClickListener {
            val userMail = loginEmail.text.toString()
            val passwordStr = loginPassword.text.toString()
            getAPI(userMail,passwordStr)
        }

    }
    fun getAPI(email:String,password:String){
        //APIクラスでやったほうがよい
        val service = KrononClient?.retrofitBuilder()
        val user = LoginUser(email,password)
        //ここをトライキャッチ　オフライン　と　タイムアウト（スリープ関数で処理を止める）
        val call = service?.login(user)
        call?.enqueue(object : Callback<LoginUserResponse> {
            override fun onResponse(call : Call<LoginUserResponse>, response: Response<LoginUserResponse>){
                if(response.code() == 200){
                    val userResponse = response.body()
//                    Toast.makeText(this@MainActivity, weatherResponse!!.sys!!.country, Toast.LENGTH_LONG).show() }
                    Toast.makeText(this@LoginActivity, userResponse!!.success.toString()
                            +"\n"+
                            userResponse!!.code.toString()
                            +"\n"+
                            //プリファレンスに保存
                            userResponse!!.data?.token.toString()
                        , Toast.LENGTH_LONG).show()
                    val intent = Intent(this@LoginActivity, MainButtomNavigationActivity::class.java)
                    startActivity(intent)
                }
                if(response.code() == 400){
                    val responseError = response.errorBody()
                    //GsonでKotlinクラスに型を変えてもらえる。
                    val exceptionBody = Gson().fromJson(responseError?.string(), LoginUserErrorResponse::class.java)
//                    exceptionBody.message?.name?.forEach { element-> Log.i("nameError",element) }
//                    exceptionBody.message?.email?.forEach { element-> Log.i("emailError",element) }
//                    Jsonのまま受け取る
//                    val jsonObj = JSONObject(responseError?.charStream()?.readText())
//                    val message = jsonObj.getJSONObject("message").get("email")
//                    JSONObject jObjError = new JSONObject(response.errorBody()?.string());
//                    Toast.makeText(this@NewUserCreateActivity, jsonObj.getString("message"), Toast.LENGTH_LONG).show()

                    AlertDialog.Builder(this@LoginActivity) // FragmentではActivityを取得して生成
                        .setTitle("エラー")
                        .setMessage(exceptionBody.message.toString())
                        .setPositiveButton("OK", { dialog, which ->
                            // TODO:Yesが押された時の挙動
                        })
                        .show()
                }
            }

            override fun onFailure(calll: Call<LoginUserResponse>, t: Throwable){
                Toast.makeText(this@LoginActivity, "Fail", Toast.LENGTH_LONG)
            }
        })
    }
}