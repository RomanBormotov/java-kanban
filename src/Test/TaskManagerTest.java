package Test;

import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {

    protected T manager;

    protected Task createTask() {
        return new Task(
                "Test name",
                "Test description",
                Status.NEW,
                LocalDateTime.now(),
                10);
    }

    protected Epic createEpic() {

        return new Epic(
                "Description",
                "Title",
                Status.NEW,
                LocalDateTime.now(),
                0);
    }

    protected Subtask createSubtask(Epic epic) {
        return new Subtask("Test name",
                "Test description",
                Status.NEW,
                LocalDateTime.now(),
                0);
    }

    @Test
    public void shouldCreateTask() {
        Task task = createTask();
        manager.createTask(task);
        Map<Integer, Task> tasks = manager.getTasks();
        assertNotNull(tasks.get(task.getId()));
        assertEquals(Status.NEW.name(), task.getStatus());
    }

    @Test
    public void shouldCreateEpic() {
        Epic epic = createEpic();
        manager.createEpic(epic);
        Map<Integer, Epic> epics = manager.getEpics();
        assertNotNull(epics.get(epic.getId()));
        assertEquals(Status.NEW.name(), epic.getStatus());
    }

    @Test
    public void shouldCreateSubtask() {
        Epic epic = createEpic();
        manager.createEpic(epic);
        Subtask subtask = createSubtask(epic);
        manager.createSubtask(epic.getId(),subtask);
        Map<Integer, Subtask> subtasks = manager.getSubtasks();
        assertNotNull(subtask.getStatus());
        assertNotNull(subtasks.get(epic.getId()));
        assertEquals(epic.getId(), subtask.getEpicId());
        assertEquals(Status.NEW.name(), subtask.getStatus());
    }

    @Test
    void shouldReturnNullWhenTaskCreatedNull() {
        Task task = createTask();
        manager.createTask(null);
        assertNull(task);
    }

    @Test
    void shouldReturnNullWhenEpicCreatedNull() {
        Epic epic = createEpic();
        manager.createEpic(null);
        assertNull(epic);
    }

    @Test
    void shouldReturnNullWhenSubtaskCreatedNull() {
        Subtask subtask = createSubtask(null);
        manager.createSubtask(0, subtask);
        assertNull(subtask);
    }

    @Test
    public void shouldUpdateTaskStatusToInProgress() {
        Task task = createTask();
        manager.createTask(task);
        task.setStatus(Status.IN_PROGRESS);
        manager.updateTask(task.getId(), task);
        assertEquals(Status.IN_PROGRESS.name(), manager.getTaskOnID(task.getId()).getStatus());
    }

    @Test
    public void shouldUpdateEpicStatusToInProgress() {
        Epic epic = createEpic();
        manager.createEpic(epic);
        epic.setStatus(Status.IN_PROGRESS);
        manager.updateTask(epic.getId(), epic);
        assertEquals(Status.IN_PROGRESS.name(), manager.getEpicOnID(epic.getId()).getStatus());
    }

    @Test
    public void shouldUpdateSubtaskStatusToInProgress() {
        Epic epic = createEpic();
        manager.createEpic(epic);
        Subtask subtask = createSubtask(epic);
        manager.createSubtask(epic.getId(),subtask);
        subtask.setStatus(Status.IN_PROGRESS);
        manager.updateSubtask(epic.getId(),subtask);
        assertEquals(Status.IN_PROGRESS.name(), manager.getSubtaskOnID(subtask.getId()).getStatus());
        assertEquals(Status.IN_PROGRESS.name(), manager.getEpicOnID(epic.getId()).getStatus());
    }

    @Test
    public void shouldUpdateTaskStatusToDone() {
        Task task = createTask();
        manager.createTask(task);
        task.setStatus(Status.DONE);
        manager.updateTask(task.getId(), task);
        assertEquals(Status.DONE.name(), manager.getTaskOnID(task.getId()).getStatus());
    }

    @Test
    public void shouldUpdateEpicStatusToDone() {
        Epic epic = createEpic();
        manager.createEpic(epic);
        epic.setStatus(Status.DONE);
        manager.updateTask(epic.getId(), epic);
        assertEquals(Status.DONE.name(), manager.getEpicOnID(epic.getId()).getStatus());
    }

    @Test
    public void shouldUpdateSubtaskStatusToDone() {
        Epic epic = createEpic();
        manager.createEpic(epic);
        Subtask subtask = createSubtask(epic);
        manager.createSubtask(epic.getId(),subtask);
        subtask.setStatus(Status.DONE);
        manager.updateSubtask(epic.getId(),subtask);
        assertEquals(Status.DONE.name(), manager.getSubtaskOnID(subtask.getId()).getStatus());
        assertEquals(Status.DONE.name(), manager.getEpicOnID(epic.getId()).getStatus());
    }

    @Test
    public void shouldNotUpdateIfTaskNull() {
        Task task = createTask();
        manager.createTask(task);
        manager.updateTask(task.getId(),null);
        assertEquals(task, manager.getTaskOnID(task.getId()));
    }

    @Test
    public void shouldNotUpdateIfEpicNull() {
        Epic epic = createEpic();
        manager.createEpic(epic);
        manager.updateEpic(epic.getId(),null);
        assertEquals(epic, manager.getEpicOnID(epic.getId()));
    }

    @Test
    public void shouldNotUpdateIfSubtaskNull() {
        Epic epic = createEpic();
        manager.createEpic(epic);
        Subtask subtask = createSubtask(epic);
        manager.createSubtask(epic.getId(), subtask);
        manager.updateSubtask(epic.getId(),null);
        assertEquals(subtask, manager.getSubtaskOnID(subtask.getId()));
    }

    @Test
    public void shouldDeleteAllTasks() {
        Task task = createTask();
        manager.createTask(task);
        manager.removeTasks();
        assertEquals(Collections.EMPTY_LIST, manager.getTasks());
    }

    @Test
    public void shouldDeleteAllEpics() {
        Epic epic = createEpic();
        manager.createEpic(epic);
        manager.removeEpics();
        assertEquals(Collections.EMPTY_LIST, manager.getEpics());
    }

    @Test
    public void shouldDeleteAllSubtasks() {
        Epic epic = createEpic();
        manager.createEpic(epic);
        Subtask subtask = createSubtask(epic);
        manager.createSubtask(epic.getId(), subtask);
        manager.removeSubtasks();
        assertTrue(epic.getSubtasks().isEmpty());
        assertTrue(manager.getSubtaskOnID(subtask.getId()).toString().isEmpty());
    }

    @Test
    public void shouldDeleteAllSubtasksFromEpic() {
        Epic epic = createEpic();
        manager.createEpic(epic);
        Subtask subtask = createSubtask(epic);
        manager.createSubtask(epic.getId(), subtask);
        manager.removeSubtaskOnID(subtask.getId());
        assertTrue(epic.getSubtasks().isEmpty());
        assertTrue(manager.getSubtaskOnID(subtask.getId()).toString().isEmpty());
    }

    @Test
    public void shouldDeleteAllTasksById() {
        Task task = createTask();
        manager.createTask(task);
        manager.removeTaskOnID(task.getId());
        assertEquals(Collections.EMPTY_LIST, manager.getTasks());
    }

    @Test
    public void shouldDeleteAllEpicsById() {
        Epic epic = createEpic();
        manager.createEpic(epic);
        manager.removeEpicOnID(epic.getId());
        assertEquals(Collections.EMPTY_LIST, manager.getEpics());
    }

    @Test
    public void shouldNotDeleteTasksWithIncorrectId() {
        Task task = createTask();
        manager.createTask(task);
        manager.removeTaskOnID(9999);
        assertEquals(List.of(task), manager.getTasks());
    }

    @Test
    public void shouldNotDeleteEpicsWithIncorrectId() {
        Epic epic = createEpic();
        manager.createEpic(epic);
        manager.removeEpicOnID(9999);
        assertEquals(List.of(epic), manager.getEpics());
    }

    @Test
    public void shouldNotDeleteSubtasksWithIncorrectId() {
        Epic epic = createEpic();
        manager.createEpic(epic);
        Subtask subtask = createSubtask(epic);
        manager.createSubtask(epic.getId(), subtask);
        manager.removeSubtaskOnID(9999);
        assertEquals(List.of(subtask), manager.getSubtasks());
        List<Integer> list = new ArrayList<>();
        for (Subtask subtask1: manager.getEpicSubtasks(epic.getId())) {
            list.add(subtask1.getId());
        }
        assertEquals(List.of(subtask.getId()), list); // ???
    }

    @Test
    public void shouldNotToDoSomethingWithEmptyHashMapOfTasks() {
        manager.removeTasks();
        manager.removeTaskOnID(9999);
        assertEquals(0, manager.getTasks().size());
    }

    @Test
    public void shouldNotToDoSomethingWithEmptyHashMapOfEpics() {
        manager.removeEpics();
        manager.removeEpicOnID(9999);
        assertEquals(0, manager.getEpics().size());
    }

    @Test
    public void shouldNotToDoSomethingWithEmptyHashMapOfSubtasks() {
        manager.removeSubtasks();
        manager.removeSubtaskOnID(9999);
        assertEquals(0, manager.getSubtasks().size());
    }

    @Test
    void shouldCheckEmptyListWhenSubtasksAtEpicAreEmpty() {
        Epic epic = createEpic();
        manager.createEpic(epic);
        List<Subtask> subtasks = manager.getEpicSubtasks(epic.getId());
        assertTrue(subtasks.isEmpty());
    }

    @Test
    public void shouldReturnEmptyTasksIfTasksAreNotCreated() {
        assertTrue(manager.getTasks().isEmpty());
    }

    @Test
    public void shouldReturnEmptyEpicsIfEpicsAreNotCreated() {
        assertTrue(manager.getEpics().isEmpty());
    }

    @Test
    public void shouldReturnEmptySubtasksIfSubtasksAreNotCreated() {
        assertTrue(manager.getSubtasks().isEmpty());
    }

    @Test
    public void shouldReturnNullIfTaskDoesNotExist() {
        assertNull(manager.getTaskOnID(9999));
    }

    @Test
    public void shouldReturnNullIfEpicDoesNotExist() {
        assertNull(manager.getEpicOnID(9999));
    }

    @Test
    public void shouldReturnNullIfSubtaskDoesNotExist() {
        assertNull(manager.getSubtaskOnID(9999));
    }

    @Test
    public void shouldReturnEmptyHistory() {
        assertEquals(Collections.EMPTY_LIST, manager.getHistory());
    }

    @Test
    public void shouldReturnEmptyHistoryIfTasksAreNotExist() {
        manager.getTaskOnID(9999);
        manager.getSubtaskOnID(9999);
        manager.getEpicOnID(9999);
        assertTrue(manager.getHistory().isEmpty());
    }

    @Test
    public void shouldReturnHistoryWithAllInfo() {
        Epic epic = createEpic();
        manager.createEpic(epic);
        Subtask subtask = createSubtask(epic);
        manager.createSubtask(epic.getId(), subtask);
        manager.getEpicOnID(epic.getId());
        manager.getSubtaskOnID(subtask.getId());
        List<Task> info = manager.getHistory();
        assertEquals(2, info.size());
        assertTrue(info.contains(epic));
        assertTrue(info.contains(subtask));
    }
}