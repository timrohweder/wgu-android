package android.example.rohwedercoursescheduler;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface TermDao {

    @Insert
    void insert(Term term);

    @Update
    void update(Term term);

    @Delete
    void delete(Term term);

    @Query("SELECT * FROM term_table WHERE id = :id")
    LiveData<List<Term>> getTermById (int id);

    @Query("SELECT * FROM term_table")
    LiveData<List<Term>> getAllTerms();

}
