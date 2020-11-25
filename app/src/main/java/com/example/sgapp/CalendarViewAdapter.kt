//package com.example.sgapp
//
//import android.R
//import android.content.Context
//import android.content.Intent
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ArrayAdapter
//import androidx.core.content.ContextCompat.startActivity
//
//
//internal class CalendarViewAdapter(
//    context: Context,
//    private val resource: Int,
////    emoUsers: List<EmoUser>
//    var days: ArrayList<String> = arrayListOf<String>("1","2","3","4")
//) :
//    ArrayAdapter<List<String>?>(context, resource,days) {
//    private val friendsList: List<String>
//    private val inflater: LayoutInflater
//    fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
//        val view: View
//        view = if (convertView != null) {
//            convertView
//        } else {
//            inflater.inflate(resource, null)
//        }
////        // リストビューに表示する要素を取得
////        val user: EmoUser = friendsList[position]
////        val friend = FriendItem(view, user)
////        setBtnProfileView(view, user)
//        return view
//    }
//
//    /**
//     * ボタンを押したらプロフィール表示
////     */
////    fun setBtnProfileView(view: View, user: EmoUser?) {
////        view.findViewById(R.id.friendImage).setOnClickListener(object : OnClickListener() {
////            fun onClick(v: View?) {
////                val profileViewIntent = Intent(context, ProfileViewActivity::class.java)
////                profileViewIntent.putExtra("profileData", user)
////                profileViewIntent.putExtra("MyData", me)
////                startActivity(profileViewIntent)
////            }
////        })
////    }
//
//    init {
//        friendsList = emoUsers
//        inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
//    }
//}