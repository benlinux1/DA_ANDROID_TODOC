package com.cleanup.todoc.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.repositories.TaskDataRepository;

import java.util.List;
import java.util.concurrent.Executor;

public class TaskViewModel  extends ViewModel {

    private LiveData<List<Task>> currentTasks;

    // REPOSITORIES
    private final TaskDataRepository taskDataSource;
    private final Executor executor;

    // DATA
    public TaskViewModel(TaskDataRepository taskDataSource, Executor executor) {

        this.taskDataSource = taskDataSource;
        this.executor = executor;
    }

    // INIT TASKS LIST
    public void init() {

        if (this.currentTasks != null) {
            return;
        }

        currentTasks = taskDataSource.getTasks();

    }


    // GET TASKS LIST
    public LiveData<List<Task>> getTasks() {
        return taskDataSource.getTasks();
    }

    // CREATE A NEW TASK
    public void createTask(Task task) {
        executor.execute(() -> {
            taskDataSource.createTask(task);
        });
    }

    // DELETE A GIVEN TASK
    public void deleteTask(long taskId) {
        executor.execute(() -> taskDataSource.deleteTask(taskId));
    }

}
