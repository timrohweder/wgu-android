package android.example.rohwedercoursescheduler;

import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class TermDetailViewActivity extends AppCompatActivity {
    private int termId;
    private Term term;
    SchedulerViewModel schedulerViewModel;
    public static final String EXTRA_ID = "android.example.rohwedercoursescheduler.EXTRA_ID";

    private TextView titleTextView;
    private TextView startDateTextView;
    private TextView endDateTextView;
    private TextView coursesTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_detail_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        schedulerViewModel = ViewModelProviders.of(this).get(SchedulerViewModel.class);

        titleTextView = findViewById(R.id.textViewTitle);
        startDateTextView = findViewById(R.id.textViewStartDate);
        endDateTextView = findViewById(R.id.textViewEndDate);
        coursesTextView = findViewById(R.id.textViewCourses);

        Intent intent = getIntent();
        termId = intent.getIntExtra(EXTRA_ID, -1);
        populateCourseList();

        if (termId > 0) {
            schedulerViewModel.getAllTerms().observe(this, new Observer<List<Term>>() {
                @Override
                public void onChanged(@Nullable List<Term> terms) {
                    for (Term termToCheck : terms) {
                        if (termToCheck.getId() == termId) {
                            term = termToCheck;
                        }
                    }
                    titleTextView.setText(term.getTitle());
                    startDateTextView.setText(term.getStartDate());
                    endDateTextView.setText(term.getEndDate());
                }
            });
        }
    }

    private void populateCourseList() {
        final List<Course> courseList = new ArrayList<>();
        schedulerViewModel.getAllCourses().observe(this, new Observer<List<Course>>() {
            @Override
            public void onChanged(@Nullable List<Course> courses) {
                for (Course course : courses) {
                    if (course.getTermId() == termId) {
                        courseList.add(course);
                    }
                }
                StringBuilder formattedCourseList = new StringBuilder();
                for (Course course : courseList) {
                    formattedCourseList.append(course.getTitle());
                    formattedCourseList.append("\n");
                }
                if(formattedCourseList.toString().isEmpty()) {
                    formattedCourseList.append("No courses assigned to this term.");
                }
                coursesTextView.setText(formattedCourseList.toString());
            }
        });
    }

    public void handleEditClick(View view) {
        Intent intent = new Intent(this, AddEditTermActivity.class);
        intent.putExtra(TermDetailViewActivity.EXTRA_ID, term.getId());
        intent.putExtra(AddEditTermActivity.EXTRA_TITLE, term.getTitle());
        intent.putExtra(AddEditTermActivity.EXTRA_START_DATE, term.getStartDate());
        intent.putExtra(AddEditTermActivity.EXTRA_END_DATE, term.getEndDate());
        startActivity(intent);
    }

    public void handleDeleteClick(View view) {
        if (coursesTextView.getText().toString().trim().equals("No courses assigned to this term.")) {
            Term termToDelete = new Term("", "", "");
            termToDelete.setId(getIntent().getIntExtra(EXTRA_ID, -1));
            if (termToDelete.getId() > 0) {
                schedulerViewModel.delete(termToDelete);
            }
            Intent intent = new Intent(TermDetailViewActivity.this, TermsActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "The term cannot be deleted while it has courses assigned to it.", Toast.LENGTH_SHORT).show();
            return;
        }
    }
}
