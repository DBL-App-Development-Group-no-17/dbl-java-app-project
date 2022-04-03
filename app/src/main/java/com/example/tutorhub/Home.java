package com.example.tutorhub;

import android.app.DownloadManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Home extends AppCompatActivity {



    private RecyclerView courseRV;
    /*private FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();*/

    private ArrayList<CourseModel> courseModelArrayList;

//    Task<DataSnapshot> database = FirebaseDatabase.getInstance().getReference().child("users").get().addOnCompleteListener(
//            new OnCompleteListener<DataSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<DataSnapshot> task) {
//                    if (task.isSuccessful()) {
//                        String s = task.getResult().getValue().toString();
//                        String[] ss = s.split(", ");
//                    }
//                }
//            });

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    ValueEventListener query = reference.child("users").orderByChild("tutor").equalTo(true)
            .addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                // dataSnapshot is the "issue" node with all children with id 0
                for (DataSnapshot user : dataSnapshot.getChildren()) {
                    User curUser = user.getValue(User.class);

                    System.out.println(curUser.getUsername());
                }
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            int x = 1;
        }
    });





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