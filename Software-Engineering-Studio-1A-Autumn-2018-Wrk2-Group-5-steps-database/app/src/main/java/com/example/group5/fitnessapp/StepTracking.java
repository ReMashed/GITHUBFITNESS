package com.example.group5.fitnessapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
//GraphView objects
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.BarGraphSeries;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class StepTracking extends AppCompatActivity{
    //  BarChart barChart;
    GraphView graph;
    BarGraphSeries<DataPoint> series;
    //Calendar calendar = Calendar.getInstance(new Locale("en", "AU"));
    Calendar calendar = Calendar.getInstance(Locale.getDefault());

    TextView steps;
    private int todaySteps = 0;
    private int monday = 0;
    private int tuesday = 0;
    private int wednesday = 0;
    private int thursday = 0;
    private int friday = 0;
    private int satday = 0;
    private int sunday = 0;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_tracking);

        mAuth = FirebaseAuth.getInstance();
        //Get reference to database
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //If the user is not logged in, redirect them to the login page
        if (mAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        //Check for current date then figure out which axis to input it into.
        Date now = new Date();
        calendar.setTime(now);
        int dayIndex = calendar.get(Calendar.DAY_OF_WEEK);
        final String day = Integer.toString(dayIndex);

        //Implement value listener which executes a method every time something changes in the database
        databaseReference.child(mAuth.getCurrentUser().getUid()).child("steps").child(day).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FirebaseUser current = mAuth.getCurrentUser();
                //Since we added a valueEventListner for the specific user child,
                //We can simply user the datasnapshot to get the value of variable stored under their id
                String count = (String) dataSnapshot.child("step").getValue();
                if (count == null) {
                    //If variable doesn't yet exist
                    //However it should've of been created via sign up
                    //so this shouldn't be happening
                    System.out.println("null");
                } else if (Integer.parseInt(count) >= 0){
                    todaySteps = Integer.parseInt(count);
                    //Date labels
                    Date d1 = calendar.getTime();
                    calendar.add(Calendar.DATE, 1);
                    Date d2 = calendar.getTime();
                    calendar.add(Calendar.DATE, 1);
                    Date d3 = calendar.getTime();
                    calendar.add(Calendar.DATE, 1);
                    Date d4 = calendar.getTime();
                    calendar.add(Calendar.DATE, 1);
                    Date d5 = calendar.getTime();
                    calendar.add(Calendar.DATE, 1);
                    Date d6 = calendar.getTime();
                    calendar.add(Calendar.DATE, 1);
                    Date d7 = calendar.getTime();

                    int mon = calendar.MONDAY;
                    int tues = calendar.TUESDAY;
                    int wednes = calendar.WEDNESDAY;
                    int thurs = calendar.THURSDAY;
                    int fri = calendar.FRIDAY;
                    int sat = Calendar.DAY_OF_WEEK;
                    int sun = calendar.SUNDAY;


                    Intent intent = getIntent();
                    int total = intent.getIntExtra("steps",0);
                    steps = (TextView)  findViewById(R.id.todayStep);
                    steps.setText("Steps Taken Today: " + todaySteps);
                    graph = (GraphView) findViewById(R.id.graph);

                    //Need to grab data from the firebase database ;; for now we'll just grab the data from the main page
                    series = new BarGraphSeries<>(new DataPoint[] {
                           /* new DataPoint(1, 0), //Mon
                            new DataPoint(2, 0), //T
                            new DataPoint(3,0 ), //W
                            new DataPoint(4, 0), //Th
                            new DataPoint(5, 0), //FR
                            new DataPoint(6, 0), //Sat
                            new DataPoint(7,0 ), //Sun but with calendar it's actually 1 and mon = 2 so SAT = 7*/
                    });
                    graph.addSeries(series);


                    series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
                        @Override
                        public int get(DataPoint data) {
                            return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
                        }
                    });



                    Date now = new Date();
                    calendar.setTime(now);
                    int dayIndex = calendar.get(Calendar.DAY_OF_WEEK);
                    //int dayIndex = 1;
                    int index = 0;
                    //String day = Integer.toString(dayIndex);

                    switch(dayIndex) {
                        //1 is sunday, but we want monday so set it to 7
                        case 1:
                            index = 7;
                            series.appendData(new DataPoint(dayIndex, todaySteps ), false, 10000);
                        break;
                        //2 is monday set to one
                        case 2:
                            index = 1;

                            series.appendData(new DataPoint(dayIndex, todaySteps ), false, 10000);
                        break;
                        //3 is tues
                        case 3:
                            index = 2;

                            series.appendData(new DataPoint(dayIndex, todaySteps ), false, 10000);
                        break;
                        //4 is wed
                        case 4:
                            index = 3;

                            series.appendData(new DataPoint(dayIndex, todaySteps ), false, 10000);
                        break;
                        //5 is thur
                        case 5:
                            index = 4;

                            series.appendData(new DataPoint(dayIndex, todaySteps ), false, 10000);
                        break;
                        //6 is fri
                        case 6:
                            index = 5;
                            series.appendData(new DataPoint(dayIndex, todaySteps ), false, 10000);
                        break;
                        //7 is Sat
                        case 7:
                            index = 6;
                            series.appendData(new DataPoint(dayIndex, todaySteps ), false, 10000);
                        break;
                    }

                    graph.setTitle("This Weeks Step Count");
                    graph.getViewport().setScrollable(true); //enables scrolling and zooming
                    graph.getViewport().setScrollableY(true);
                    graph.getViewport().setXAxisBoundsManual(true);
                    graph.getViewport().setMinY(0);
                    graph.getViewport().setMinX(1);
                    graph.getViewport().setMaxX(7);

                    //graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this));
                    //StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
                    //staticLabelsFormatter.setHorizontalLabels(new String[] {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"});
                    //graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
                    graph.getGridLabelRenderer().setNumHorizontalLabels(7); // only 4 because of the space
                    graph.getGridLabelRenderer().setHumanRounding(true);
                    graph.getGridLabelRenderer().setPadding(64); //padding for axis labels to fit
                    series.setSpacing(2);
                    series.setDataWidth(1);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
