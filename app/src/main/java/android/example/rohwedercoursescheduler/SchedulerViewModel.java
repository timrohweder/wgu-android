package android.example.rohwedercoursescheduler;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class SchedulerViewModel extends AndroidViewModel {
    private SchedulerRepository repository;
    private LiveData<List<Term>> allTerms;
    private LiveData<List<Mentor>> allMentors;
    private LiveData<List<Course>> allCourses;
    private LiveData<List<Assessment>> allAssessments;

    public SchedulerViewModel(@NonNull Application application) {
        super(application);
        repository = new SchedulerRepository(application);
        allTerms = repository.getAllTerms();
        allMentors = repository.getAllMentors();
        allCourses = repository.getAllCourses();
        allAssessments = repository.getAllAssessments();
    }

    public void insert(Term term) {
        repository.insert(term);
    }

    public void update(Term term) {
        repository.update(term);
    }

    public void delete(Term term) {
        repository.delete(term);
    }

    public void insert(Course course) {
        repository.insert(course);
    }

    public void update(Course course) {
        repository.update(course);
    }

    public void delete(Course course) {
        repository.delete(course);
    }

    public void insert(Mentor mentor) {
        repository.insert(mentor);
    }

    public void update(Mentor mentor) {
        repository.update(mentor);
    }

    public void delete(Mentor mentor) {
        repository.delete(mentor);
    }

    public void insert(Assessment assessment) {
        repository.insert(assessment);
    }

    public void update(Assessment assessment) {
        repository.update(assessment);
    }

    public void delete(Assessment assessment) {
        repository.delete(assessment);
    }

    public LiveData<List<Term>> getAllTerms() {
        return allTerms;
    }

    public LiveData<List<Term>> getTermById(int id) {
        return repository.getTermById(id);
    }

    public LiveData<List<Mentor>> getAllMentors() {
        return allMentors;
    }

    public LiveData<List<Course>> getAllCourses() {
        return allCourses;
    }

    public LiveData<List<Assessment>> getAllAssessments() { return allAssessments; }
}
