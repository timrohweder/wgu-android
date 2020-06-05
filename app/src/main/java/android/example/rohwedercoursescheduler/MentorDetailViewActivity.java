package android.example.rohwedercoursescheduler;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MentorDetailViewActivity extends AppCompatActivity {
    
    private int mentorId;

    public static final String EXTRA_ID = "android.example.rohwedercoursescheduler.EXTRA_ID";
    public static final String EXTRA_NAME = "android.example.rohwedercoursescheduler.EXTRA_NAME";
    public static final String EXTRA_PHONE = "android.example.rohwedercoursescheduler.EXTRA_PHONE";
    public static final String EXTRA_EMAIL = "android.example.rohwedercoursescheduler.EXTRA_EMAIL";

    SchedulerViewModel schedulerViewModel;

    private TextView nameTextView;
    private TextView phoneTextView;
    private TextView emailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor_detail_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        schedulerViewModel = ViewModelProviders.of(this).get(SchedulerViewModel.class);

        Intent intent = getIntent();
        
        mentorId = intent.getIntExtra(EXTRA_ID, -1);

        nameTextView = findViewById(R.id.mentorNameTextView);
        phoneTextView = findViewById(R.id.mentorPhoneTextView);
        emailTextView = findViewById(R.id.mentorEmailTextView);

        nameTextView.setText(intent.getStringExtra(EXTRA_NAME));
        phoneTextView.setText(intent.getStringExtra(EXTRA_PHONE));
        emailTextView.setText(intent.getStringExtra(EXTRA_EMAIL));
    }

    public void handleDeleteClick(View view) {
        final List<Course> courseList = new ArrayList<>();
        schedulerViewModel.getAllCourses().observe(this, new Observer<List<Course>>() {
            @Override
            public void onChanged(@Nullable List<Course> courses) {
                for (Course course : courses) {
                    if (course.getMentorId() == mentorId) {
                        courseList.add(course);
                    }
                }
                Mentor mentorToDelete = new Mentor("", "", "");
                mentorToDelete.setId(mentorId);
                if (courseList.size() > 0) {
                    Toast.makeText(MentorDetailViewActivity.this, "This mentor cannot be deleted while they have courses assigned to them.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mentorToDelete.getId() > 0) {
                    schedulerViewModel.delete(mentorToDelete);
                    Intent intent = new Intent(MentorDetailViewActivity.this, MentorsActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    public void handleEditClick(View view) {
        Intent intent = new Intent(this, AddEditMentorActivity.class);
        intent.putExtra(AddEditMentorActivity.EXTRA_ID, getIntent().getIntExtra(EXTRA_ID, -1));
        intent.putExtra(AddEditMentorActivity.EXTRA_NAME, getIntent().getStringExtra(EXTRA_NAME));
        intent.putExtra(AddEditMentorActivity.EXTRA_PHONE, getIntent().getStringExtra(EXTRA_PHONE));
        intent.putExtra(AddEditMentorActivity.EXTRA_EMAIL, getIntent().getStringExtra(EXTRA_EMAIL));
        startActivity(intent);
    }

}
