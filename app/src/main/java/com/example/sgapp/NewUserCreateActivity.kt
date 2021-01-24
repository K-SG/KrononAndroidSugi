package com.example.sgapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.retrofit2_kotlin.Retrofit2.KrononRespose
import com.example.retrofit2_kotlin.Retrofit2.KrononService
import com.example.retrofit2_kotlin.Retrofit2.User
import org.json.JSONObject
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
        val call = service.createUser(name, email, password)
        call.enqueue(object : Callback<KrononRespose> {
            override fun onResponse(call : Call<KrononRespose>, response: Response<KrononRespose>){
                if(response.code() == 200){
                    val weatherResponse = response.body()
//                    Toast.makeText(this@MainActivity, weatherResponse!!.sys!!.country, Toast.LENGTH_LONG).show() }
                    Toast.makeText(this@NewUserCreateActivity, weatherResponse!!.success.toString()
                            +"\n"+
                            weatherResponse!!.code.toString()
                            +"\n"+
                            weatherResponse!!.message.toString()
                        , Toast.LENGTH_LONG).show()

                    val intent = Intent(this@NewUserCreateActivity, MainButtomNavigationActivity::class.java)
                    startActivity(intent)
                }
                if(response.code() == 201){
                    val weatherResponse = response.body()

//                    Toast.makeText(this@MainActivity, weatherResponse!!.sys!!.country, Toast.LENGTH_LONG).show() }
                    Toast.makeText(this@NewUserCreateActivity, weatherResponse!!.success.toString()
                            +"\n"+
                            weatherResponse!!.code.toString()
                            +"\n"+
                            weatherResponse!!.user?.token.toString()
                        , Toast.LENGTH_LONG).show() }
                if(response.code() == 400){
                    val weatherResponseEroor = response.errorBody()
                    val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
//                    JSONObject jObjError = new JSONObject(response.errorBody()?.string());
//                    Toast.makeText(this@NewUserCreateActivity, jsonObj.getString("message"), Toast.LENGTH_LONG).show()
                    AlertDialog.Builder(this@NewUserCreateActivity) // FragmentではActivityを取得して生成
                        .setTitle("エラー")
                        .setMessage(jsonObj.getString("message"))
                        .setPositiveButton("OK", { dialog, which ->
                            // TODO:Yesが押された時の挙動

                        })
                        .show()
                }
            }

            override fun onFailure(calll: Call<KrononRespose>, t: Throwable){
                Toast.makeText(this@NewUserCreateActivity, "Fail", Toast.LENGTH_LONG)
            }
        })
    }

    companion object {

        //        var BaseUrl = "http://api.openweathermap.org/"
        var BaseUrl = "http://54.199.202.205/"
//        var AppId = "2e65127e909e178d0af311a81f39948c"
//        var lat = "35"
//        var lon = "139"
//        var name = "sgsgsg"
//        var email = "sugi@kmail.com"
//        var password = "Kronon1122"
    }
}