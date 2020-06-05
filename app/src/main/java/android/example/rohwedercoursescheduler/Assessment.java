package android.example.rohwedercoursescheduler;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "assessment_table",
        foreignKeys = {
                @ForeignKey(entity = Course.class, parentColumns = "id", childColumns = "courseId"),
        },
        indices = {@Index("courseId")})
public class Assessment {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;
    private String dueDate;
    private String type;
    private int courseId;

    public Assessment(String title, String dueDate, String type, int courseId) {
        this.title = title;
        this.dueDate = dueDate;
        this.type = type;
        this.courseId = courseId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getDueDate() {
        return dueDate;
    }

    public int getCourseId() {
        return courseId;
    }
}
