package Test;

import manager.HistoryManager;
import util.Managers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import constants.Status;
import tasks.Task;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryHistoryManagerTest {
    HistoryManager manager;
    private int id = 0;

    public int generateId() {
        return id++;
    }

    protected Task createTask() {
        return new Task(
                "Test name",
                "Test description",
                Status.NEW,
                LocalDateTime.now(),
                0);
    }

    @BeforeEach
    public void shouldRunBeforeEach() {
        manager = Managers.getDefaultHistory();
    }

    @Test
    public void shouldAddTasksToHistory() {
        Task task1 = createTask();
        int testTaskId1 = generateId();
        task1.setId(testTaskId1);

        Task task2 = createTask();
        int testTaskId2 = generateId();
        task2.setId(testTaskId2);

        Task task3 = createTask();
        int testTaskId3 = generateId();
        task3.setId(testTaskId3);

        manager.add(task1);
        manager.add(task2);
        manager.add(task3);

        assertEquals(List.of(task1, task2, task3), manager.getHistory());
    }

    @Test
    public void shouldRemoveTaskFromHistory() {
        Task task1 = createTask();
        int testTaskId1 = generateId();
        task1.setId(testTaskId1);

        Task task2 = createTask();
        int testTaskId2 = generateId();
        task2.setId(testTaskId2);

        Task task3 = createTask();
        int testTaskId3 = generateId();
        task3.setId(testTaskId3);

        manager.add(task1);
        manager.add(task2);
        manager.add(task3);
        manager.remove(task1.getId());
        manager.remove(task2.getId());
        manager.remove(task3.getId());

        assertEquals(Collections.EMPTY_LIST, manager.getHistory());
    }

    @Test
    public void shouldRemoveOnlyOneTask() {
        Task task1 = createTask();
        int testTaskId1 = generateId();
        task1.setId(testTaskId1);

        Task task2 = createTask();
        int testTaskId2 = generateId();
        task2.setId(testTaskId2);

        Task task3 = createTask();
        int testTaskId3 = generateId();
        task3.setId(testTaskId3);

        manager.add(task1);
        manager.add(task2);
        manager.add(task3);
        manager.remove(task1.getId());

        assertEquals(List.of(task2, task3), manager.getHistory());
    }

    @Test
    public void shouldCheckThatHistoryIsEmpty() {
        Task task1 = createTask();
        int testTaskId1 = generateId();
        task1.setId(testTaskId1);

        Task task2 = createTask();
        int testTaskId2 = generateId();
        task2.setId(testTaskId2);

        Task task3 = createTask();
        int testTaskId3 = generateId();
        task3.setId(testTaskId3);

        manager.remove(task1.getId());
        manager.remove(task2.getId());
        manager.remove(task3.getId());

        assertEquals(Collections.EMPTY_LIST, manager.getHistory());
    }

    @Test
    public void shouldNotRemoveTaskWithIncorrectId() {
        Task task1 = createTask();
        int testTaskId1 = generateId();

        task1.setId(testTaskId1);
        manager.add(task1);
        manager.remove(999);

        assertEquals(List.of(task1), manager.getHistory());
    }
}
