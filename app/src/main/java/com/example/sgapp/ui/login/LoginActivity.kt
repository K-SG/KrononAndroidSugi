package com.example.sgapp.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.retrofit2_kotlin.Retrofit2.KrononService
import com.example.sgapp.MainButtomNavigationActivity
import com.example.sgapp.NewUserCreateActivity

import com.example.sgapp.R
import com.example.sgapp.Retrofit2.CreateUserErrorResponse
import com.example.sgapp.Retrofit2.LoginUserResponse
import com.example.sgapp.Retrofit2.LoginUser
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel

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
        val retrofit = Retrofit.Builder()
            .baseUrl(BaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(KrononService::class.java)
        val user = LoginUser(email,password)
        val call = service.login(user)
        call.enqueue(object : Callback<LoginUserResponse> {
            override fun onResponse(call : Call<LoginUserResponse>, response: Response<LoginUserResponse>){
                if(response.code() == 200){
                    val userResponse = response.body()
//                    Toast.makeText(this@MainActivity, weatherResponse!!.sys!!.country, Toast.LENGTH_LONG).show() }
                    Toast.makeText(this@LoginActivity, userResponse!!.success.toString()
                            +"\n"+
                            userResponse!!.code.toString()
                            +"\n"+
                            userResponse!!.data?.token.toString()
                        , Toast.LENGTH_LONG).show()
                    val intent = Intent(this@LoginActivity, MainButtomNavigationActivity::class.java)
                    startActivity(intent)
                }
                if(response.code() == 400){
                    val responseError = response.errorBody()
                    //GsonでKotlinクラスに型を変えてもらえる。
                    val exceptionBody = Gson().fromJson(responseError?.string(), CreateUserErrorResponse::class.java)
                    exceptionBody.message?.name?.forEach { element-> Log.i("nameError",element) }
                    exceptionBody.message?.email?.forEach { element-> Log.i("emailError",element) }
//                    Jsonのまま受け取る
//                    val jsonObj = JSONObject(responseError?.charStream()?.readText())
//                    val message = jsonObj.getJSONObject("message").get("email")
//                    JSONObject jObjError = new JSONObject(response.errorBody()?.string());
//                    Toast.makeText(this@NewUserCreateActivity, jsonObj.getString("message"), Toast.LENGTH_LONG).show()

                    AlertDialog.Builder(this@LoginActivity) // FragmentではActivityを取得して生成
                        .setTitle("エラー")
                        .setMessage("何かが間違っているよ")
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

    companion object {

        //        var BaseUrl = "http://api.openweathermap.org/"
        var BaseUrl = "http://54.199.202.205/"
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName
        // TODO : initiate successful logged in experience
//        Toast.makeText(
//                applicationContext,
//                "$welcome $displayName",
//                Toast.LENGTH_LONG
//        ).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}