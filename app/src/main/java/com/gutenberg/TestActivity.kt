package com.gutenberg

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        var list1 = mutableListOf<Person>()
        var list2 = mutableListOf<Person>()
        var p = Person()
        p.name = "XYZ"
        list1.add(p)
        list2.add(p)
        list1[0].name = "ABC"
        list2[0].name = "XYZ"
        print(list1[0].name)
        print(list2[0].name)
        print(p.name)
    }
}

class Person { var name ="" }