package com.example.tutorhub;

import android.content.Intent;
import android.os.Bundle;
import android.util.Range;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

// to implement: Save Filters, subject addition, sorting strategy
public class Filter extends AppCompatActivity {
    String sortingCriteria = "DISTANCE";
    List courses = new ArrayList();
    double rangeValue;
    double ratingValue;

    final double MAX_RANGE = 100000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter);

        TextView cancel = findViewById(R.id.cancel_button);
        TextView reset = findViewById(R.id.reset_button);
        ToggleButton distance = findViewById(R.id.distance_button);
        ToggleButton rating = findViewById(R.id.rating_button);
        TextView progress = findViewById(R.id.range_progress);
        SeekBar range = findViewById(R.id.range_bar);
        ToggleButton math = findViewById(R.id.math_button);
        ToggleButton english = findViewById(R.id.english_button);
        ToggleButton physics = findViewById(R.id.physics_button);
        ToggleButton chemistry = findViewById(R.id.chemistry_button);
        ToggleButton data_structures = findViewById(R.id.data_structures_button);
        Button saveFilters = findViewById(R.id.save_filters_button);
        //Button dm = findViewById(R.id.dm_button);
        //Button home = findViewById(R.id.home_button);
        //Button map = findViewById(R.id.map_button);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (distance.isChecked()) {
                    distance.toggle();
                }
                if (rating.isChecked()) {
                    rating.toggle();
                }

                rangeValue = MAX_RANGE;
                ratingValue = 0;
                sortingCriteria = "";
                courses.clear();
                startActivity(new Intent(getApplicationContext(), Home.class));
                finish();
            }
        });


        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (distance.isChecked()) {
                    distance.toggle();
                }
                if (rating.isChecked()) {
                    rating.toggle();
                }
                if (math.isChecked()) {
                    math.toggle();
                }
                if (english.isChecked()) {
                    english.toggle();
                }
                if (physics.isChecked()) {
                    physics.toggle();
                }
                if (chemistry.isChecked()) {
                    chemistry.toggle();
                }
                if (data_structures.isChecked()) {
                    data_structures.toggle();
                }

                range.setProgress(50);
                rangeValue = MAX_RANGE;
                ratingValue = 0;
                sortingCriteria = "DISTANCE";
                courses.clear();
            }
        });

        distance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortingCriteria = "DISTANCE";
                if (rating.isChecked()) {
                    rating.toggle();
                    progress.setText("");
                }
            }
        });

        rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortingCriteria = "RATING";
                if (distance.isChecked()) {
                    distance.toggle();
                    progress.setText("");
                }
            }
        });

        progress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {}
        });

        range.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                DecimalFormat df = new DecimalFormat("##.##");
                df.setRoundingMode(RoundingMode.DOWN);
                if (sortingCriteria == "DISTANCE") {
                    progress.setText(String.valueOf(df.format(i * 0.1)) + " km");
                    rangeValue = i * 0.1;
                } else if (sortingCriteria == "RATING") {
                    progress.setText(String.valueOf(df.format(i * 0.05)));
                    ratingValue = i * 0.2;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });


        math.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (math.isChecked()) {
                    courses.add("MATH");
                } else {
                    courses.remove("MATH");
                }
            }
        });

        english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (english.isChecked()) {
                    courses.add("ENGLISH");
                } else {
                    courses.remove("ENGLISH");
                }
            }
        });

        physics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (physics.isChecked()) {
                    courses.add("PHYSICS");
                } else {
                    courses.remove("PHYSICS");
                }
            }
        });

        chemistry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chemistry.isChecked()) {
                    courses.add("CHEMISTRY");
                } else {
                    courses.remove("CHEMISTRY");
                }
            }
        });

        data_structures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (data_structures.isChecked()) {
                    courses.add("DATA_STRUCTURES");
                } else {
                    courses.remove("DATA_STRUCTURES");
                }
            }
        });


        saveFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Home.class);
                intent.putExtra("sortingCriteria", sortingCriteria);
                intent.putExtra("rangeValue", rangeValue);
                intent.putExtra("ratingValue", ratingValue);
                intent.putExtra("mathIsPresent", math.isChecked());
                intent.putExtra("physicsIsPresent", physics.isChecked());
                intent.putExtra("chemistryIsPresent", chemistry.isChecked());
                intent.putExtra("dataStructuresIsPresent", data_structures.isChecked());
                intent.putExtra("englishIsPresent", english.isChecked());
                startActivity(intent);
                finish();
            }
        });

        /*dm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), DM.class));
            }
        });*/

        /*home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Home.class));
            }
        });*/

        /*map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Map.class));
            }
        });*/
    }
}
