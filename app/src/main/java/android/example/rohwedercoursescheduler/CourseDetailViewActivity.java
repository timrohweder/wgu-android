package android.example.rohwedercoursescheduler;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CourseDetailViewActivity extends AppCompatActivity {

    // alarm and notification code is inspired by the official Android tutorials

    // Notification ID.
    private static int START_NOTIFICATION_ID;
    private static int END_NOTIFICATION_ID;
    // Notification channel ID.
    private static final String PRIMARY_CHANNEL_ID =
            "primary_notification_channel";
    private NotificationManager mNotificationManager;
    boolean startAlarmUp;
    boolean endAlarmUp;
    Intent startNotifyIntent;
    Intent endNotifyIntent;
    PendingIntent startNotifyPendingIntent;
    PendingIntent endNotifyPendingIntent;

    int courseId;
    Course course;

    SchedulerViewModel schedulerViewModel;
    public static final String EXTRA_ID = "android.example.rohwedercoursescheduler.EXTRA_ID";

    private TextView titleTextView;
    private TextView startDateTextView;
    private TextView endDateTextView;
    private TextView termTextView;
    private TextView statusTextView;
    private TextView assessmentsTextView;
    private TextView mentorTextView;
    private TextView notesTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mNotificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        ToggleButton startAlarmToggle = findViewById(R.id.startAlarmToggle);
        ToggleButton endAlarmToggle = findViewById(R.id.endAlarmToggle);
        
        startNotifyIntent = new Intent(this, AlarmReceiver.class);
        endNotifyIntent = new Intent(this, AlarmReceiver.class);

        Intent intent = getIntent();
        courseId = intent.getIntExtra(EXTRA_ID, -1);
        
        START_NOTIFICATION_ID = courseId;
        END_NOTIFICATION_ID = courseId + 1000;
        
        startNotifyIntent.putExtra("notification_id", START_NOTIFICATION_ID);
        endNotifyIntent.putExtra("notification_id", END_NOTIFICATION_ID);

        startAlarmUp = (PendingIntent.getBroadcast(this, START_NOTIFICATION_ID,
                startNotifyIntent, PendingIntent.FLAG_NO_CREATE) != null);

        endAlarmUp = (PendingIntent.getBroadcast(this, END_NOTIFICATION_ID,
                endNotifyIntent, PendingIntent.FLAG_NO_CREATE) != null);

        startAlarmToggle.setChecked(startAlarmUp);
        endAlarmToggle.setChecked(endAlarmUp);

        final AlarmManager alarmManager = (AlarmManager) getSystemService
                (ALARM_SERVICE);
        
        startAlarmToggle.setOnCheckedChangeListener
                (new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged
                            (CompoundButton buttonView, boolean isChecked) {
                        String toastMessage;
                        if (isChecked) {

                            startNotifyPendingIntent = PendingIntent.getBroadcast
                                    (CourseDetailViewActivity.this, START_NOTIFICATION_ID, startNotifyIntent,
                                            PendingIntent.FLAG_UPDATE_CURRENT);

                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(System.currentTimeMillis());
                            calendar.set(Calendar.HOUR_OF_DAY, 8);
                            calendar.set(Calendar.MINUTE, 0);
                            calendar.set(Calendar.SECOND, 0);
                            String[] dateArray = startDateTextView.getText().toString().split("/");
                            calendar.set(Calendar.MONTH, Integer.parseInt(dateArray[0]) - 1);
                            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateArray[1]));
                            calendar.set(Calendar.YEAR, Integer.parseInt(dateArray[2]));

                            if (alarmManager != null) {
                                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                                        calendar.getTimeInMillis(),
                                        startNotifyPendingIntent);
                            }
                            // Set the toast message for the "on" case.
                            toastMessage = "You will be notified at 8am on the course's start date.";

                        } else {
                            startNotifyPendingIntent = PendingIntent.getBroadcast
                                    (CourseDetailViewActivity.this, START_NOTIFICATION_ID, startNotifyIntent,
                                            PendingIntent.FLAG_NO_CREATE);
                            // Cancel notification if the alarm is turned off.
                            startNotifyPendingIntent.cancel();

                            if (alarmManager != null) {
                                alarmManager.cancel(startNotifyPendingIntent);
                            }
                            // Set the toast message for the "off" case.
                            toastMessage = "The course start notification is off.";

                        }

                        // Show a toast to say the alarm is turned on or off.
                        Toast.makeText(CourseDetailViewActivity.this, toastMessage,
                                Toast.LENGTH_LONG).show();
                    }
                });
        
        endAlarmToggle.setOnCheckedChangeListener
                (new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged
                            (CompoundButton buttonView, boolean isChecked) {
                        String toastMessage;
                        if (isChecked) {

                            endNotifyPendingIntent = PendingIntent.getBroadcast
                                    (CourseDetailViewActivity.this, END_NOTIFICATION_ID, endNotifyIntent,
                                            PendingIntent.FLAG_UPDATE_CURRENT);

                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(System.currentTimeMillis());
                            calendar.set(Calendar.HOUR_OF_DAY, 8);
                            calendar.set(Calendar.MINUTE, 0);
                            calendar.set(Calendar.SECOND, 0);
                            String[] dateArray = endDateTextView.getText().toString().split("/");
                            calendar.set(Calendar.MONTH, Integer.parseInt(dateArray[0]) - 1);
                            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateArray[1]));
                            calendar.set(Calendar.YEAR, Integer.parseInt(dateArray[2]));

                            if (alarmManager != null) {
                                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                                        calendar.getTimeInMillis(),
                                        endNotifyPendingIntent);
                            }
                            // Set the toast message for the "on" case.
                            toastMessage = "You will be notified at 8am on the course's end date.";

                        } else {
                            endNotifyPendingIntent = PendingIntent.getBroadcast
                                    (CourseDetailViewActivity.this, END_NOTIFICATION_ID, endNotifyIntent,
                                            PendingIntent.FLAG_NO_CREATE);
                            // Cancel notification if the alarm is turned off.
                            endNotifyPendingIntent.cancel();

                            if (alarmManager != null) {
                                alarmManager.cancel(endNotifyPendingIntent);
                            }
                            // Set the toast message for the "off" case.
                            toastMessage = "The course end notification is off.";

                        }

                        // Show a toast to say the alarm is turned on or off.
                        Toast.makeText(CourseDetailViewActivity.this, toastMessage,
                                Toast.LENGTH_LONG).show();
                    }
                });
        
        // Create the notification channel.
        createNotificationChannel();

        titleTextView = findViewById(R.id.courseTitleTextView);
        startDateTextView = findViewById(R.id.courseStartDateTextView);
        endDateTextView = findViewById(R.id.courseEndDateTextView);
        termTextView = findViewById(R.id.courseTermTextView);
        statusTextView = findViewById(R.id.courseStatusTextView);
        mentorTextView = findViewById(R.id.courseMentorTextView);
        notesTextView = findViewById(R.id.courseNotesTextView);
        assessmentsTextView = findViewById(R.id.courseAssessmentsTextView);

        schedulerViewModel = ViewModelProviders.of(this).get(SchedulerViewModel.class);

        if (courseId > 0) {
            schedulerViewModel.getAllCourses().observe(this, new Observer<List<Course>>() {
                @Override
                public void onChanged(@Nullable List<Course> courses) {
                    for (Course courseToCheck : courses) {
                        if (courseToCheck.getId() == courseId) {
                            course = courseToCheck;
                            titleTextView.setText(course.getTitle());
                            startDateTextView.setText(course.getStartDate());
                            endDateTextView.setText(course.getAnticipatedEndDate());
                            statusTextView.setText(course.getStatus());
                            notesTextView.setText(course.getNotes());
                            populateTermField();
                            populateMentorField();
                            assessmentsTextView.setText("");
                            populateAssessmentList();

                            startNotifyIntent.putExtra("title", course.getTitle());
                            endNotifyIntent.putExtra("title", course.getTitle());
                        }
                    }
                }
            });
        }
    }

    public void createNotificationChannel() {

        mNotificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O) {

            NotificationChannel notificationChannel = new NotificationChannel
                    (PRIMARY_CHANNEL_ID,
                            "WGU Scheduler Channel",
                            NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Alerts you to important dates for WGU courses and assessments.");
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private void populateTermField() {
        schedulerViewModel.getAllTerms().observe(this, new Observer<List<Term>>() {
            @Override
            public void onChanged(@Nullable List<Term> terms) {
                for (Term term : terms) {
                    if (course.getTermId() == term.getId()) {
                        String termName = term.getTitle();
                        termTextView.setText(termName);
                    }
                }
            }
        });
    }

    private void populateMentorField() {
        schedulerViewModel.getAllMentors().observe(this, new Observer<List<Mentor>>() {
            @Override
            public void onChanged(@Nullable List<Mentor> mentors) {
                for (Mentor mentor : mentors) {
                    if (mentor.getId() == course.getMentorId()) {
                        StringBuilder formattedMentorList = new StringBuilder();
                        formattedMentorList.append(mentor.getName());
                        formattedMentorList.append("\n");
                        formattedMentorList.append(mentor.getEmail());
                        formattedMentorList.append("\n");
                        formattedMentorList.append(mentor.getPhone());
                        mentorTextView.setText(formattedMentorList.toString());
                    }
                }
            }
        });
    }

    private void populateAssessmentList() {
        final List<Assessment> assessmentList = new ArrayList<>();
        schedulerViewModel.getAllAssessments().observe(this, new Observer<List<Assessment>>() {
            @Override
            public void onChanged(@Nullable List<Assessment> assessments) {
                for (Assessment assessment : assessments) {
                    if (assessment.getCourseId() == courseId) {
                        assessmentList.add(assessment);
                    }
                }
                StringBuilder formattedAssessmentList = new StringBuilder();
                for (Assessment assessment : assessmentList) {
                    String formattedType;
                    if (assessment.getType().equals("performance")) {
                        formattedType = " (PA)";
                    } else {
                        formattedType = " (OA)";
                    }
                    formattedAssessmentList.append(assessment.getTitle() + formattedType);
                    formattedAssessmentList.append("\n");
                }
                if (formattedAssessmentList.toString().isEmpty()) {
                    formattedAssessmentList.append("No assessments assigned to this course.");
                }
                assessmentsTextView.setText(formattedAssessmentList.toString());
            }
        });
    }

    public void handleEditClick(View view) {
        Intent intent = new Intent(this, AddEditCourseActivity.class);
        intent.putExtra(CourseDetailViewActivity.EXTRA_ID, course.getId());
        intent.putExtra(AddEditCourseActivity.EXTRA_TITLE, course.getTitle());
        intent.putExtra(AddEditCourseActivity.EXTRA_START_DATE, course.getStartDate());
        intent.putExtra(AddEditCourseActivity.EXTRA_END_DATE, course.getAnticipatedEndDate());
        intent.putExtra(AddEditCourseActivity.EXTRA_STATUS, course.getStatus());
        intent.putExtra(AddEditCourseActivity.EXTRA_MENTOR_ID, course.getMentorId());
        intent.putExtra(AddEditCourseActivity.EXTRA_NOTES, course.getNotes());
        intent.putExtra(AddEditCourseActivity.EXTRA_TERM_ID, course.getTermId());
        startActivity(intent);

    }

    public void handleDeleteClick(View view) {
        if (assessmentsTextView.getText().toString().equals("No assessments assigned to this course.")) {
            Course courseToDelete = new Course(-1, "", "", "", "", -1, "");
            courseToDelete.setId(getIntent().getIntExtra(EXTRA_ID, -1));
            if (courseToDelete.getId() > 0) {
                schedulerViewModel = ViewModelProviders.of(this).get(SchedulerViewModel.class);
                schedulerViewModel.delete(courseToDelete);
            }
            Intent intent = new Intent(this, CoursesActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "The course cannot be deleted while it has assessments assigned to it.", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public void handleShareClick(View view) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, course.getNotes());
        intent.setType("text/plain");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

}
