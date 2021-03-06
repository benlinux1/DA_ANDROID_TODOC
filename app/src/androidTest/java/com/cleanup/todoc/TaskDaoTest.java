package com.cleanup.todoc;

import static org.junit.Assert.assertTrue;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.cleanup.todoc.database.dao.TodocDatabase;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)

public class TaskDaoTest {

    // FOR DATA
    private TodocDatabase database;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void initDb() throws Exception {

        this.database = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),
                        TodocDatabase.class)
                .allowMainThreadQueries()
                .build();
    }

    @After
    public void closeDb() throws Exception {
        database.close();
    }

    /**
     * DATA SET FOR TEST (Project & Task)
     */
    private static final int PROJECT_ID = 1;
    private static final Project PROJECT_DEMO = new Project(PROJECT_ID, "Pierpoljak", 0xFFe942f5);
    private static final Task TASK_DEMO = new Task(1, "Nettoyer les vitres de la salle de réunion", 1654684380);

    /**
     * Test that checks task insertion & data conformity in database
     */
    @Test
    public void insertAndGetTask() throws InterruptedException {

        // BEFORE : Creating a Project & adding a new task
        this.database.projectDao().createProject(PROJECT_DEMO);
        this.database.taskDao().createTask(TASK_DEMO);

        // TEST : checks if task data is in the database
        Task task = LiveDataTestUtil.getValue(this.database.taskDao().getTasks()).get(0);
        assertTrue(task.getName().equals(TASK_DEMO.getName())
                && task.getProjectId() == TASK_DEMO.getProjectId() && task.getCreationTimestamp() == TASK_DEMO.getCreationTimestamp());

    }

    /**
     * Test that checks task insertion & delete method in/from database
     */
    @Test
    public void insertAndDeleteItem() throws InterruptedException {

        // BEFORE : Adding a demo project & a demo task. Next, get the item added & delete it.
        this.database.projectDao().createProject(PROJECT_DEMO);
        this.database.taskDao().createTask(TASK_DEMO);
        Task taskAdded = LiveDataTestUtil.getValue(this.database.taskDao().getTasks()).get(0);
        this.database.taskDao().deleteTask(taskAdded.getId());

        //TEST : checks if task list is empty when task has been deleted
        List<Task> tasks = LiveDataTestUtil.getValue(this.database.taskDao().getTasks());
        assertTrue(tasks.isEmpty());

    }

}
