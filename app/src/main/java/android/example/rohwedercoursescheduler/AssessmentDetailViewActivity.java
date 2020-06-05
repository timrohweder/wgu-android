package android.example.rohwedercoursescheduler;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;



import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Calendar;
import java.util.List;

public class AssessmentDetailViewActivity extends AppCompatActivity {

    // alarm and notification code is inspired by the official Android tutorials

    // Notification ID.
    private static int START_NOTIFICATION_ID;
    // Notification channel ID.
    private static final String PRIMARY_CHANNEL_ID =
            "primary_notification_channel";
    private NotificationManager mNotificationManager;
    boolean startAlarmUp;
    Intent startNotifyIntent;
    PendingIntent startNotifyPendingIntent;
    int assessmentId;

    SchedulerViewModel schedulerViewModel;

    private TextView titleTextView;
    private TextView dueDateTextView;
    private TextView typeTextView;
    private TextView courseTextView;

    public static final String EXTRA_ID = "android.example.rohwedercoursescheduler.EXTRA_ID";
    public static final String EXTRA_TITLE = "android.example.rohwedercoursescheduler.EXTRA_TITLE";
    public static final String EXTRA_DUE_DATE = "android.example.rohwedercoursescheduler.EXTRA_DUE_DATE";
    public static final String EXTRA_TYPE = "android.example.rohwedercoursescheduler.EXTRA_TYPE";
    public static final String EXTRA_COURSE_ID = "android.example.rohwedercoursescheduler.EXTRA_COURSE_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_detail_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mNotificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        Intent intent = getIntent();

        ToggleButton startAlarmToggle = findViewById(R.id.assessmentStartAlarmToggle);

        startNotifyIntent = new Intent(this, AlarmReceiver.class);

        assessmentId = intent.getIntExtra(AssessmentDetailViewActivity.EXTRA_ID, -1);
        startNotifyIntent.putExtra("title", intent.getStringExtra(EXTRA_TITLE));

        START_NOTIFICATION_ID = assessmentId + 10000;

        startNotifyIntent.putExtra("notification_id", START_NOTIFICATION_ID);

        startAlarmUp = (PendingIntent.getBroadcast(this, START_NOTIFICATION_ID,
                startNotifyIntent, PendingIntent.FLAG_NO_CREATE) != null);

        startAlarmToggle.setChecked(startAlarmUp);

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
                                    (AssessmentDetailViewActivity.this, START_NOTIFICATION_ID, startNotifyIntent,
                                            PendingIntent.FLAG_UPDATE_CURRENT);

                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(System.currentTimeMillis());
                            calendar.set(Calendar.HOUR_OF_DAY, 8);
                            calendar.set(Calendar.MINUTE, 0);
                            calendar.set(Calendar.SECOND, 0);
                            String[] dateArray = dueDateTextView.getText().toString().split("/");
                            calendar.set(Calendar.MONTH, Integer.parseInt(dateArray[0]) - 1);
                            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateArray[1]));
                            calendar.set(Calendar.YEAR, Integer.parseInt(dateArray[2]));

                            if (alarmManager != null) {
                                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                                        calendar.getTimeInMillis(),
                                        startNotifyPendingIntent);
                            }
                            // Set the toast message for the "on" case.
                            toastMessage = "You will be notified at 8am on the assessment's goal date.";

                        } else {
                            startNotifyPendingIntent = PendingIntent.getBroadcast
                                    (AssessmentDetailViewActivity.this, START_NOTIFICATION_ID, startNotifyIntent,
                                            PendingIntent.FLAG_NO_CREATE);
                            // Cancel notification if the alarm is turned off.
                            startNotifyPendingIntent.cancel();

                            if (alarmManager != null) {
                                alarmManager.cancel(startNotifyPendingIntent);
                            }
                            // Set the toast message for the "off" case.
                            toastMessage = "The assessment's goal date notification is off.";

                        }

                        // Show a toast to say the alarm is turned on or off.
                        Toast.makeText(AssessmentDetailViewActivity.this, toastMessage,
                                Toast.LENGTH_LONG).show();
                    }
                });

        // Create the notification channel.
        createNotificationChannel();

        titleTextView = findViewById(R.id.assessmentTitleTextView);
        dueDateTextView = findViewById(R.id.assessmentDueDateTextView);
        typeTextView = findViewById(R.id.assessmentTypeTextView);
        courseTextView = findViewById(R.id.assessmentCourseTextView);

        schedulerViewModel = ViewModelProviders.of(this).get(SchedulerViewModel.class);

        if (intent.getExtras() != null) {
            titleTextView.setText(intent.getStringExtra(EXTRA_TITLE));
            dueDateTextView.setText(intent.getStringExtra(EXTRA_DUE_DATE));
            String unformattedType = intent.getStringExtra(EXTRA_TYPE);
            String formattedType = unformattedType.substring(0, 1).toUpperCase() + unformattedType.substring(1);
            typeTextView.setText(formattedType + " Assessment");
            final int courseId = intent.getIntExtra(EXTRA_COURSE_ID, -1);

            if (courseId > 0) {
                schedulerViewModel.getAllCourses().observe(this, new Observer<List<Course>>() {
                    @Override
                    public void onChanged(@Nullable List<Course> courses) {
                        for (Course course : courses) {
                            if (course.getId() == courseId) {
                                courseTextView.setText(course.getTitle());
                            }
                        }
                    }
                });

            }
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

    public void handleDeleteClick(View view) {
        Assessment assessmentToDelete = new Assessment("", "", "",-1);
        assessmentToDelete.setId(getIntent().getIntExtra(EXTRA_ID, -1));
        if (assessmentToDelete.getId() > 0) {
            schedulerViewModel.delete(assessmentToDelete);
        }
        Intent intent = new Intent(this, AssessmentsActivity.class);
        startActivity(intent);
    }

    public void handleEditClick(View view) {
        Intent intent = new Intent(this, AddEditAssessmentActivity.class);
        intent.putExtra(AddEditAssessmentActivity.EXTRA_ID, getIntent().getIntExtra(EXTRA_ID, -1));
        intent.putExtra(AddEditAssessmentActivity.EXTRA_TITLE, getIntent().getStringExtra(EXTRA_TITLE));
        intent.putExtra(AddEditAssessmentActivity.EXTRA_DUE_DATE, getIntent().getStringExtra(EXTRA_DUE_DATE));
        intent.putExtra(AddEditAssessmentActivity.EXTRA_TYPE, getIntent().getStringExtra(EXTRA_TYPE));
        intent.putExtra(AddEditAssessmentActivity.EXTRA_COURSE_ID, getIntent().getIntExtra(EXTRA_COURSE_ID, -1));
        startActivity(intent);
    }
}
