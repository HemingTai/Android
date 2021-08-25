package com.example.mykotlin

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mykotlin.adapter.ItemAdapter
import com.example.mykotlin.data.Datasource

const val KEY_REVENUE = "revenue_key"
const val KEY_DESSERT_SOLD = "dessert_sold_key"
const val TAG = "AffirmationsActivity"

class AffirmationsActivity : AppCompatActivity() {

    //用于创建应用
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate called")
        setContentView(R.layout.activity_affirmations)

        val dataset = Datasource().loadAffirmations()
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.adapter = ItemAdapter(this, dataset)
        recyclerView.setHasFixedSize(true)

        println(savedInstanceState?.getInt(KEY_REVENUE) ?: 0)
        println(savedInstanceState?.getInt(KEY_DESSERT_SOLD) ?: 0)
    }

    //系统会在调用 onCreate() 之后立即调用 onStart()
    //onStart() 会使 activity 显示在屏幕上
    //onStart() 可能会被调用多次，类似于 viewWillAppear()
    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart called")
    }

    //onStart() 调用之后会立即调用 onResume()
    //onResume() 用于使相应 activity 成为焦点，并让用户能够与其互动
    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume called")
    }

    //调用 onPause()后，该应用不会再获得焦点
    //退到后台时会先调用 onPause()
    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause called")
    }

    //调用 onStop()后，该应用将不再显示在屏幕上
    //应用退到后台时先调用 onPause()后，再调用 onStop()
    //onStop() 可能会被调用多次，类似于 viewWillDisappear()
    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop called")
    }

    //应用销毁时会调用 onDestroy()
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy called")
    }

    //应用重新回到前台时会先调用 onRestart()，再调用 onStart()
    //onRestart() 方法用于放置仅在 activity 不是首次启动时才需要调用的代码
    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart called")
    }

    //onSaveInstanceState() 方法是一个回调，用于保存 Activity 被销毁后可能需要的任何数据。
    //在生命周期回调图中，系统会在相应 activity 停止后调用 onSaveInstanceState()
    //应用进入后台最后还会调用 onSaveInstanceState()
    //Bundle 是键值对的集合，其中的键始终为字符串
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, "onSaveInstanceState Called")
        outState.putInt(KEY_REVENUE, 2)
        outState.putInt(KEY_DESSERT_SOLD, 3)
    }
}