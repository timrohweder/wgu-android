package android.example.rohwedercoursescheduler;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "course_table",
        foreignKeys = {
            @ForeignKey(entity = Term.class, parentColumns = "id", childColumns = "termId"),
            @ForeignKey(entity = Mentor.class, parentColumns = "id", childColumns = "mentorId")
        },
        indices = {@Index("termId"), @Index("mentorId")})

public class Course {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int termId;
    String title;
    String startDate;
    String anticipatedEndDate;
    String status;
    private int mentorId;
    String notes;

    public Course(int termId, String title, String startDate, String anticipatedEndDate, String status, int mentorId, String notes) {
        this.termId = termId;
        this.title = title;
        this.startDate = startDate;
        this.anticipatedEndDate = anticipatedEndDate;
        this.status = status;
        this.mentorId = mentorId;
        this.notes = notes;
    }

    @NonNull
    @Override
    public String toString() {
        return this.title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTermId() {
        return termId;
    }

    public String getTitle() {
        return title;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getAnticipatedEndDate() {
        return anticipatedEndDate;
    }

    public String getStatus() {
        return status;
    }

    public int getMentorId() {
        return mentorId;
    }

    public String getNotes() {
        return notes;
    }
}
