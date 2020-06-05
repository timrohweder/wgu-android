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

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AddEditTermActivity extends AppCompatActivity {

    private SchedulerViewModel schedulerViewModel;

    public static final String EXTRA_ID = "android.example.rohwedercoursescheduler.EXTRA_ID";
    public static final String EXTRA_TITLE = "android.example.rohwedercoursescheduler.EXTRA_TITLE";
    public static final String EXTRA_START_DATE = "android.example.rohwedercoursescheduler.EXTRA_START_DATE";
    public static final String EXTRA_END_DATE = "android.example.rohwedercoursescheduler.EXTRA_END_DATE";

    private EditText titleEditText;
    private TextView startDateTextView;
    private TextView endDateTextView;

    private boolean editMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_term);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        titleEditText = findViewById(R.id.title_editText);
        startDateTextView = findViewById(R.id.startDate_textView);
        endDateTextView = findViewById(R.id.endDate_textView);

        if (intent.getExtras() != null) {
            editMode = true;
            setTitle("Edit a Term");
            titleEditText.setText(intent.getStringExtra(EXTRA_TITLE));
            startDateTextView.setText(intent.getStringExtra(EXTRA_START_DATE));
            endDateTextView.setText(intent.getStringExtra(EXTRA_END_DATE));
        }
    }

    public void showStartDatePicker(View view) {
        DialogFragment newFragment = new DatePickerFragment("startDate");
        newFragment.show(getSupportFragmentManager(),"datePicker");
    }

    public void showEndDatePicker(View view) {
        DialogFragment newFragment = new DatePickerFragment("endDate");
        newFragment.show(getSupportFragmentManager(),"datePicker");
    }

    public void handleSaveClick(View view) {
        String title = titleEditText.getText().toString();
        String startDate = startDateTextView.getText().toString();
        String endDate = endDateTextView.getText().toString();

        if (title.trim().isEmpty() || startDate.trim().isEmpty() || endDate.trim().isEmpty()) {
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



        schedulerViewModel = ViewModelProviders.of(this).get(SchedulerViewModel.class);

        Term termToSave = new Term(title, startDate, endDate);

        if (editMode) {
            termToSave.setId(getIntent().getIntExtra(EXTRA_ID, -1));
            if (termToSave.getId() > 0) {
                schedulerViewModel.update(termToSave);
            }
        } else {
            schedulerViewModel.insert(new Term(title, startDate, endDate));
        }

        Intent intent = new Intent(this, TermsActivity.class);
        startActivity(intent);
    }

    public void handleCancelClick(View view) {
        Intent intent = new Intent(this, TermsActivity.class);
        startActivity(intent);
    }

    public void processDatePickerResult(int year, int month, int day, String tag) {
        String month_string = Integer.toString(month+1);
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

        if(dateToSet != null) {
            dateToSet.setText(dateMessage);
        }

    }

}
