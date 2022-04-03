package com.example.tutorhub;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class Home extends AppCompatActivity {



    private RecyclerView courseRV;
    /*private FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();*/

    private ArrayList<CourseModel> courseModelArrayList;

    DatabaseReference tutorsRef = mAuth.getReference("tutors");

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        courseRV = findViewById(R.id.idRVCourse);


        // here we have created new array list and added data to it.
        courseModelArrayList = new ArrayList<>();
        courseModelArrayList.add(new CourseModel("DSA in Java", 4, R.drawable.logo_login));
        courseModelArrayList.add(new CourseModel("Java Course", 3, R.drawable.logo_login));
        courseModelArrayList.add(new CourseModel("C++ COurse", 4, R.drawable.logo_login));
        courseModelArrayList.add(new CourseModel("DSA in C++", 4, R.drawable.logo_login));
        courseModelArrayList.add(new CourseModel("Kotlin for Android", 4, R.drawable.logo_login));
        courseModelArrayList.add(new CourseModel("Java for Android", 4, R.drawable.logo_login));
        courseModelArrayList.add(new CourseModel("HTML and CSS", 4, R.drawable.logo_login));


        // we are initializing our adapter class and passing our arraylist to it.
        CourseAdapter courseAdapter = new CourseAdapter(this, courseModelArrayList);

        // below line is for setting a layout manager for our recycler view.
        // here we are creating vertical list so we will provide orientation as vertical
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        // in below two lines we are setting layoutmanager and adapter to our recycler view.
        courseRV.setLayoutManager(linearLayoutManager);
        courseRV.setAdapter(courseAdapter);
        //setContentView(R.layout.home);

        /*Button info = findViewById(R.id.info_button);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Info.class));
            }
        });*/

        /*Button filter = findViewById(R.id.filter_button);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Filter.class));
            }
        });*/

        /*Button dm = findViewById(R.id.dm_button);
        dm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), DM.class));
            }
        });*/

        /*Button home = findViewById(R.id.home_button);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Home.class));
            }
        });*/

        /*Button map = findViewById(R.id.map_button);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Map.class));
            }
        });*/

    }
}