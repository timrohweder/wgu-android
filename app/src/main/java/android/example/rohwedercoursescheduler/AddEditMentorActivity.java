package android.example.rohwedercoursescheduler;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddEditMentorActivity extends AppCompatActivity {

    private SchedulerViewModel schedulerViewModel;

    private int mentorId;
    
    public static final String EXTRA_ID = "android.example.rohwedercoursescheduler.EXTRA_ID";
    public static final String EXTRA_NAME = "android.example.rohwedercoursescheduler.EXTRA_NAME";
    public static final String EXTRA_PHONE = "android.example.rohwedercoursescheduler.EXTRA_PHONE";
    public static final String EXTRA_EMAIL = "android.example.rohwedercoursescheduler.EXTRA_EMAIL";

    private EditText nameEditText;
    private EditText phoneEditText;
    private EditText emailEditText;


    private boolean editMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_mentor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nameEditText = findViewById(R.id.mentorNameTextEdit);
        phoneEditText = findViewById(R.id.mentorPhoneTextEdit);
        emailEditText = findViewById(R.id.mentorEmailTextEdit);

        Intent intent = getIntent();

        schedulerViewModel = ViewModelProviders.of(this).get(SchedulerViewModel.class);

        if (intent.getExtras() != null) {
            editMode = true;
            setTitle("Edit Mentor Information");
            nameEditText.setText(intent.getStringExtra(EXTRA_NAME));
            phoneEditText.setText(intent.getStringExtra(EXTRA_PHONE));
            emailEditText.setText(intent.getStringExtra(EXTRA_EMAIL));

        }
    }

    public void handleSaveClick(View view) {
        String title = nameEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String email = emailEditText.getText().toString();


        if ( title.trim().isEmpty() || phone.trim().isEmpty() || email.trim().isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Mentor mentorToSave = new Mentor(title, phone, email);

        if (editMode) {
            mentorToSave.setId(getIntent().getIntExtra(EXTRA_ID, -1));
            if (mentorToSave.getId() > 0) {
                schedulerViewModel.update(mentorToSave);
            }
        } else {
            schedulerViewModel.insert(mentorToSave);
        }

        Intent intent = new Intent(this, MentorsActivity.class);
        startActivity(intent);
    }

    public void handleCancelClick(View view) {
        Intent intent = new Intent(this, MentorsActivity.class);
        startActivity(intent);
    }
}
