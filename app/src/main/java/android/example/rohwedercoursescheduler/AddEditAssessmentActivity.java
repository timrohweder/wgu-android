package android.example.rohwedercoursescheduler;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AddEditAssessmentActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private SchedulerViewModel schedulerViewModel;

    private int courseId;

    public static final String EXTRA_ID = "android.example.rohwedercoursescheduler.EXTRA_ID";
    public static final String EXTRA_TITLE = "android.example.rohwedercoursescheduler.EXTRA_TITLE";
    public static final String EXTRA_DUE_DATE = "android.example.rohwedercoursescheduler.EXTRA_DUE_DATE";
    public static final String EXTRA_TYPE = "android.example.rohwedercoursescheduler.EXTRA_TYPE";
    public static final String EXTRA_COURSE_ID = "android.example.rohwedercoursescheduler.EXTRA_END_DATE";

    private EditText titleEditText;
    private TextView dueDateTextView;
    private Spinner courseSpinner;
    private RadioButton performanceRadioButton;
    private RadioButton objectiveRadioButton;

    String type = "performance";

    private boolean editMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_assessment);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        titleEditText = findViewById(R.id.assessmentTitleTextEdit);
        dueDateTextView = findViewById(R.id.assessmentDueDateTextView);
        courseSpinner = findViewById(R.id.assessmentCourseSpinner);
        performanceRadioButton = findViewById(R.id.performance);
        objectiveRadioButton = findViewById(R.id.objective);

        Intent intent = getIntent();

        schedulerViewModel = ViewModelProviders.of(this).get(SchedulerViewModel.class);

        if (intent.getExtras() != null) {
            editMode = true;
            setTitle("Edit an Assessment");
            titleEditText.setText(intent.getStringExtra(EXTRA_TITLE));
            dueDateTextView.setText(intent.getStringExtra(EXTRA_DUE_DATE));
            switch (intent.getStringExtra(EXTRA_TYPE)) {
                case "objective":
                    objectiveRadioButton.setChecked(true);
                    break;
                default:
                    performanceRadioButton.setChecked(true);
                    break;
            }
        }

        // Set up course spinner

        if (courseSpinner != null) {
            courseSpinner.setOnItemSelectedListener(this);
        }

        schedulerViewModel = ViewModelProviders.of(this).get(SchedulerViewModel.class);
        final ArrayAdapter<Course> courseSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        courseSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        schedulerViewModel.getAllCourses().observe(this, new Observer<List<Course>>() {
            @Override
            public void onChanged(@Nullable List<Course> courses) {
                courseSpinnerAdapter.addAll(courses);
                courseSpinner.setAdapter(courseSpinnerAdapter);
                if(getIntent().getExtras() != null) {
                    schedulerViewModel.getAllCourses().observe(AddEditAssessmentActivity.this, new Observer<List<Course>>() {
                        @Override
                        public void onChanged(@Nullable List<Course> courses) {
                            Course courseToFind;
                            for (Course course: courses) {
                                if (course.getId() == getIntent().getIntExtra(EXTRA_COURSE_ID, -1)) {
                                    courseToFind = course;
                                    int courseSpinnerPosition = courseSpinnerAdapter.getPosition(courseToFind);
                                    courseSpinner.setSelection(courseSpinnerPosition);
                                }
                            }
                        }
                    });
                }

            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        courseId = ((Course) parent.getItemAtPosition(position)).getId();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void handleDueDateClick(View view) {
        DialogFragment newFragment = new DatePickerFragment("dueDate");
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void handleSaveClick(View view) {
        String title = titleEditText.getText().toString();
        String dueDate = dueDateTextView.getText().toString();
        String type = this.type;

        if ( title.trim().isEmpty() || dueDate.trim().isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Assessment assessmentToSave = new Assessment(title, dueDate, type, courseId);

        if (editMode) {
            assessmentToSave.setId(getIntent().getIntExtra(EXTRA_ID, -1));
            if (assessmentToSave.getId() > 0) {
                schedulerViewModel.update(assessmentToSave);
            }
        } else {
            schedulerViewModel.insert(assessmentToSave);
        }

        Intent intent = new Intent(this, AssessmentsActivity.class);
        startActivity(intent);
    }

    public void handleCancelClick(View view) {
        Intent intent = new Intent(this, AssessmentsActivity.class);
        startActivity(intent);
    }

    public void processDatePickerResult(int year, int month, int day, String tag) {
        String month_string = Integer.toString(month + 1);
        String day_string = Integer.toString(day);
        String year_string = Integer.toString(year);
        String dateMessage = (month_string +
                "/" + day_string + "/" + year_string);

        TextView dateToSet;

        if (tag.equals("dueDate")) {
            dateToSet = findViewById(R.id.assessmentDueDateTextView);
        } else {
            dateToSet = null;
        }

        if (dateToSet != null) {
            dateToSet.setText(dateMessage);
        }

    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.performance:
                if (checked)
                    this.type = "performance";
                    break;
            case R.id.objective:
                if (checked)
                    this.type = "objective";
                    break;
        }
    }
}
