package android.example.rohwedercoursescheduler;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AddEditCourseActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private SchedulerViewModel schedulerViewModel;

    private EditText titleEditText;
    private Spinner termSpinner;
    private TextView startDateTextView;
    private TextView endDateTextView;
    private Spinner statusSpinner;
    private Spinner mentorSpinner;
    private EditText notesEditText;
    private int termId;
    private int mentorId;

    private boolean editMode = false;

    public static final String EXTRA_ID = "android.example.rohwedercoursescheduler.EXTRA_ID";
    public static final String EXTRA_TITLE = "android.example.rohwedercoursescheduler.EXTRA_TITLE";
    public static final String EXTRA_START_DATE = "android.example.rohwedercoursescheduler.EXTRA_START_DATE";
    public static final String EXTRA_END_DATE = "android.example.rohwedercoursescheduler.EXTRA_END_DATE";
    public static final String EXTRA_TERM_ID = "android.example.rohwedercoursescheduler.EXTRA_TERM_ID";
    public static final String EXTRA_STATUS = "android.example.rohwedercoursescheduler.EXTRA_STATUS";
    public static final String EXTRA_MENTOR_ID = "android.example.rohwedercoursescheduler.EXTRA_MENTOR_ID";
    public static final String EXTRA_NOTES = "android.example.rohwedercoursescheduler.EXTRA_END_DATE_NOTES";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_course);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        titleEditText = findViewById(R.id.title_editText);
        termSpinner = findViewById(R.id.term_spinner);
        startDateTextView = findViewById(R.id.startDate_textView);
        endDateTextView = findViewById(R.id.endDate_textView);
        statusSpinner = findViewById(R.id.course_status_spinner);
        mentorSpinner = findViewById(R.id.mentor_spinner);
        notesEditText = findViewById(R.id.notes_edit_text);

        if (intent.getExtras() != null) {
            editMode = true;
            setTitle("Edit a Course");
            titleEditText.setText(intent.getStringExtra(EXTRA_TITLE));
            startDateTextView.setText(intent.getStringExtra(EXTRA_START_DATE));
            endDateTextView.setText(intent.getStringExtra(EXTRA_END_DATE));
            notesEditText.setText(intent.getStringExtra(EXTRA_NOTES));
        }

        // Set up term spinner

        if (termSpinner != null) {
            termSpinner.setOnItemSelectedListener(this);
        }

        schedulerViewModel = ViewModelProviders.of(this).get(SchedulerViewModel.class);
        final ArrayAdapter<Term> termSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        termSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        schedulerViewModel.getAllTerms().observe(this, new Observer<List<Term>>() {
            @Override
            public void onChanged(@Nullable List<Term> terms) {
                termSpinnerAdapter.addAll(terms);
                termSpinner.setAdapter(termSpinnerAdapter);
                if(getIntent().getExtras() != null) {
                    schedulerViewModel.getTermById(getIntent().getIntExtra(EXTRA_TERM_ID, -1)).observe(AddEditCourseActivity.this, new Observer<List<Term>>() {
                        @Override
                        public void onChanged(@Nullable List<Term> terms) {
                            Term termToFind = terms.get(0);
                            int termSpinnerPosition = termSpinnerAdapter.getPosition(termToFind);
                            termSpinner.setSelection(termSpinnerPosition);
                        }
                    });
                }

            }
        });

        // Set up status spinner

        if (statusSpinner != null) {
            statusSpinner.setOnItemSelectedListener(this);
        }
        String[] statusOptions = new String[]{"In Progress", "Completed", "Dropped", "Plan to Take"};
        final ArrayAdapter<String> statusSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, statusOptions);
        statusSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        if (statusSpinnerAdapter != null) {
            statusSpinner.setAdapter(statusSpinnerAdapter);
            if(getIntent().getExtras() != null) {
                int statusSpinnerPosition = statusSpinnerAdapter.getPosition(getIntent().getStringExtra(EXTRA_STATUS));
                statusSpinner.setSelection(statusSpinnerPosition);
            }
        }

        // Set up mentors spinner

        if (mentorSpinner != null) {
            mentorSpinner.setOnItemSelectedListener(this);
        }

        schedulerViewModel = ViewModelProviders.of(this).get(SchedulerViewModel.class);
        final ArrayAdapter<Mentor> mentorSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        mentorSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        schedulerViewModel.getAllMentors().observe(this, new Observer<List<Mentor>>() {
            @Override
            public void onChanged(@Nullable List<Mentor> mentors) {
                mentorSpinnerAdapter.addAll(mentors);
                mentorSpinner.setAdapter(mentorSpinnerAdapter);
                if(getIntent().getExtras() != null) {
                    schedulerViewModel.getAllMentors().observe(AddEditCourseActivity.this, new Observer<List<Mentor>>() {
                        @Override
                        public void onChanged(@Nullable List<Mentor> mentors) {
                            Mentor mentorToFind;
                            for (Mentor mentor : mentors) {
                                if (mentor.getId() == getIntent().getIntExtra(EXTRA_MENTOR_ID, -1)) {
                                    mentorToFind = mentor;
                                    int mentorSpinnerPosition = mentorSpinnerAdapter.getPosition(mentorToFind);
                                    mentorSpinner.setSelection(mentorSpinnerPosition);
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
        switch (parent.getItemAtPosition(position).getClass().getSimpleName()) {
            case "Term":
                termId = ((Term) parent.getItemAtPosition(position)).getId();
                break;
            case "Mentor":
                mentorId = ((Mentor) parent.getItemAtPosition(position)).getId();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void showStartDatePicker(View view) {
        DialogFragment newFragment = new DatePickerFragment("startDate");
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showEndDatePicker(View view) {
        DialogFragment newFragment = new DatePickerFragment("endDate");
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void handleSaveClick(View view) {
        String title = titleEditText.getText().toString();
        String startDate = startDateTextView.getText().toString();
        String endDate = endDateTextView.getText().toString();
        String notes = notesEditText.getText().toString();
        String status = statusSpinner.getSelectedItem().toString();

        if (
                title.trim().isEmpty()
                        || startDate.trim().isEmpty()
                        || endDate.trim().isEmpty()
        ) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
        LocalDate parsedStartDate = LocalDate.parse(startDate, formatter);
        LocalDate parsedEndDate = LocalDate.parse(endDate, formatter);

        if (parsedStartDate.isAfter(parsedEndDate)) {
            Toast.makeText(this, "The end date must be later than the start date", Toast.LENGTH_SHORT).show();
            return;
        }

        Course courseToSave = new Course(termId, title, startDate, endDate, status, mentorId, notes);

        if (editMode) {
            courseToSave.setId(getIntent().getIntExtra(EXTRA_ID, -1));
            if (courseToSave.getId() > 0) {
                schedulerViewModel.update(courseToSave);
            }
        } else {
            schedulerViewModel.insert(courseToSave);
        }

        Intent intent = new Intent(this, CoursesActivity.class);
        startActivity(intent);
    }

    public void handleCancelClick(View view) {
        Intent intent = new Intent(this, CoursesActivity.class);
        startActivity(intent);
    }


    public void processDatePickerResult(int year, int month, int day, String tag) {
        String month_string = Integer.toString(month + 1);
        String day_string = Integer.toString(day);
        String year_string = Integer.toString(year);
        String dateMessage = (month_string +
                "/" + day_string + "/" + year_string);

        TextView dateToSet;

        if (tag.equals("startDate")) {
            dateToSet = findViewById(R.id.startDate_textView);
        } else if (tag.equals("endDate")) {
            dateToSet = findViewById(R.id.endDate_textView);
        } else {
            dateToSet = null;
        }

        if (dateToSet != null) {
            dateToSet.setText(dateMessage);
        }

    }

}
