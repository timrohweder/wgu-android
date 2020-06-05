package android.example.rohwedercoursescheduler;

import android.content.Context;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Term.class, Mentor.class, Course.class, Assessment.class}, version = 9, exportSchema = false)
public abstract class SchedulerDatabase extends RoomDatabase {

    private static SchedulerDatabase instance;

    public abstract TermDao termDao();
    public abstract MentorDao mentorDao();
    public abstract CourseDao courseDao();
    public abstract AssessmentDao assessmentDao();

    public static synchronized SchedulerDatabase getInstance(Context context) {
//        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    SchedulerDatabase.class, "scheduler_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
//        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private TermDao termDao;
        private MentorDao mentorDao;
        private CourseDao courseDao;
        private AssessmentDao assessmentDao;

        private PopulateDbAsyncTask(SchedulerDatabase db) {
            termDao = db.termDao();
            mentorDao = db.mentorDao();
            courseDao = db.courseDao();
            assessmentDao = db.assessmentDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            termDao.insert(new Term("Term 1", "1/1/2020", "6/30/2020"));
            termDao.insert(new Term("Term 2", "7/1/2020", "12/31/2020"));
            termDao.insert(new Term("Term 3", "1/1/2021", "6/30/2021"));
            termDao.insert(new Term("Term 4", "1/1/2022", "6/30/2022"));
            mentorDao.insert(new Mentor("John Doe", "123-456-7890", "john@john.com"));
            mentorDao.insert(new Mentor("Jane Johnson", "555-555-5555", "jane@jane.com"));
            mentorDao.insert(new Mentor("Hank Hill", "777-777-7777", "ilovepropane@aol.com"));
            courseDao.insert(new Course(1, "Woodworking 101", "1/1/2020", "6/1/2020", "In Progress", 1, "These are my course notes"));
            courseDao.insert(new Course(1, "Home Economics 101", "1/1/2020", "6/1/2020", "In Progress", 1, "These are my course notes"));
            assessmentDao.insert(new Assessment("Assessment #1", "6/1/2020", "objective", 1));
            assessmentDao.insert(new Assessment("Assessment #2", "6/1/2020", "performance", 2));
            return null;
        }
    }
}

// note: this code is inspired from the YouTube tutorial by CodingInFlow
