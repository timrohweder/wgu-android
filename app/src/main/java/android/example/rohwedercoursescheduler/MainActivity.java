package android.example.rohwedercoursescheduler;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SchedulerViewModel schedulerViewModel;
    TextView progressUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


//        Uncomment this to reset the database. Note that any changes made will be lost each time MainActivity is visited
//        while this uncommented.

        getApplicationContext().deleteDatabase("scheduler_database");


        progressUpdate = findViewById(R.id.progressTextView);

        schedulerViewModel = ViewModelProviders.of(this).get(SchedulerViewModel.class);
        schedulerViewModel.getAllCourses().observe(this, new Observer<List<Course>>() {
            @Override
            public void onChanged(@Nullable List<Course> courses) {
                StringBuilder progressString = new StringBuilder();
                int inProgress = 0;
                int completed = 0;
                int dropped = 0;
                int planToTake = 0;

                for (Course course : courses) {
                    switch(course.getStatus()) {
                        case "In Progress":
                            inProgress++;
                            break;
                        case "Completed":
                            completed++;
                            break;
                        case "Dropped":
                            dropped++;
                            break;
                        case "Plan to Take":
                            planToTake++;
                            break;
                    }
                }

                progressString.append(inProgress + " courses in progress.");
                progressString.append("\n");
                progressString.append(completed + " courses completed.");
                progressString.append("\n");
                progressString.append(dropped + " courses dropped.");
                progressString.append("\n");
                progressString.append(planToTake + " courses you plan to take.");

                progressUpdate.setText(progressString.toString());


                LinearLayout linearLayout = findViewById(R.id.MainActivityLinearLayout);
                TextView schedulerText = new TextView(MainActivity.this);
                schedulerText.setText("Scheduling functions are found in the detail pages for Courses and Assessments.");

                LinearLayout.LayoutParams schedulerTextParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                schedulerTextParams.setMargins(42,42,42,42);
                schedulerText.setLayoutParams(schedulerTextParams);

                linearLayout.addView(schedulerText);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_navigation) {
            Intent intent = new Intent(this, NavigationActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

}
