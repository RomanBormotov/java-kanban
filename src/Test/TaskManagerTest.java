package Test;

import manager.TaskManager;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import constants.Status;
import tasks.Subtask;
import tasks.Task;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {

    T manager;

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
        Subtask subtask = new Subtask("Test name",
                "Test description",
                Status.NEW,
                LocalDateTime.now(),
                0);
        subtask.setEpicId(epic.getId());
        return subtask;
    }

    @Test
    public void shouldCreateTask() {
        Task task = createTask();
        manager.createTask(task);
        int taskID = manager.getKeyID() - 1;
        Task returnedTask = manager.getTaskOnID(taskID);

        //проверка создания таска
        assertNotNull(returnedTask, "Задача не найдена");
        assertEquals(task, returnedTask, "Задачи не совпадают");

        //проверка мапы с тасками
        Map<Integer, Task> tasks = manager.getTasks();
        assertNotNull(tasks, "Задачи не возвращаются");
        assertEquals(1, tasks.size(), "Неверное количество задач");
        assertEquals(task, tasks.get(0), "Задачи не совпадают");
    }

    //изменил
    @Test
    public void shouldCreateEpic() {
        Epic epic = createEpic();
        manager.createEpic(epic);
        int taskID = manager.getKeyID() - 1;
        Epic returnedEpic = manager.getEpicOnID(taskID);

        //проверка создания эпика
        assertNotNull(returnedEpic, "Задача не найдена");
        assertEquals(epic, returnedEpic, "Задачи не совпадают");

        //проверка мапы с эпиками
        Map<Integer, Epic> epics = manager.getEpics();
        assertNotNull(epics, "Задачи не возвращаются");
        assertEquals(1, epics.size(), "Неверное количество задач");
        assertEquals(epic, epics.get(0), "Задачи не совпадают");
    }

    //изменил
    @Test
    public void shouldCreateSubtask() {
        Epic epic = createEpic();
        manager.createEpic(epic);

        Subtask subtask = createSubtask(epic);
        manager.createSubtask(epic.getId(), subtask);

        int taskID = manager.getKeyID() - 1;
        Subtask returnedSubtask = manager.getSubtaskOnID(taskID);

        //проверка создания сабтаска
        assertNotNull(returnedSubtask, "Задача не найдена");
        assertEquals(subtask, returnedSubtask, "Задачи не совпадают");

        //проверка мапы с сабтасками
        Map<Integer, Subtask> subtasks = manager.getSubtasks();
        assertNotNull(subtasks, "Задачи не возвращаются");
        assertEquals(1, subtasks.size(), "Неверное количество задач");
        assertEquals(subtask, subtasks.get(1), "Задачи не совпадают");

        //проверка привязки сабтаска к эпику
        Epic returnedEpic = manager.getEpicOnID(0);
        assertEquals(returnedSubtask, returnedEpic.getSubtasks().get(0),
                "Эпик не подтягивает созданный сабтаск");
    }

    //изменил
    @Test
    void shouldReturnNullWhenTaskCreatedNull() {
        Task task = manager.createTask(null);

        assertNull(task);
    }

    //изменил
    @Test
    void shouldReturnNullWhenEpicCreatedNull() {
        Epic epic = manager.createEpic(null);

        assertNull(epic);
    }

    //изменил
    @Test
    void shouldReturnNullWhenSubtaskCreatedNull() {
        Subtask subtask = manager.createSubtask(0, null);

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

    //изменил
    @Test
    public void shouldUpdateSubtaskStatusToInProgress() {
        Epic epic = createEpic();
        manager.createEpic(epic);

        Subtask subtask = createSubtask(epic);
        manager.createSubtask(epic.getId(), subtask);

        subtask.setStatus(Status.IN_PROGRESS);
        manager.updateSubtask(subtask.getId(), subtask);

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

    //изменил
    @Test
    public void shouldUpdateSubtaskStatusToDone() {
        Epic epic = createEpic();
        manager.createEpic(epic);

        Subtask subtask = createSubtask(epic);
        manager.createSubtask(epic.getId(), subtask);

        subtask.setStatus(Status.DONE);
        manager.updateSubtask(subtask.getId(), subtask);

        assertEquals(Status.DONE.name(), manager.getSubtaskOnID(subtask.getId()).getStatus());
        assertEquals(Status.DONE.name(), manager.getEpicOnID(epic.getId()).getStatus());
    }

    //изменил код
    @Test
    public void shouldNotUpdateIfTaskNull() {
        Task task = createTask();

        manager.createTask(task);
        manager.updateTask(task.getId(), null);

        assertEquals(task, manager.getTaskOnID(task.getId()));
    }

    //изменил код
    @Test
    public void shouldNotUpdateIfEpicNull() {
        Epic epic = createEpic();

        manager.createEpic(epic);
        manager.updateEpic(epic.getId(), null);

        assertEquals(epic, manager.getEpicOnID(epic.getId()));
    }

    //изменил код
    @Test
    public void shouldNotUpdateIfSubtaskNull() {
        Epic epic = createEpic();
        manager.createEpic(epic);

        Subtask subtask = createSubtask(epic);
        manager.createSubtask(epic.getId(), subtask);
        manager.updateSubtask(epic.getId(), null);

        assertEquals(subtask, manager.getSubtaskOnID(subtask.getId()));
    }

    //изменил
    @Test
    public void shouldDeleteAllTasks() {
        Task task = createTask();

        manager.createTask(task);
        manager.removeTasks();

        assertEquals(Collections.EMPTY_MAP, manager.getTasks());
    }

    //изменил
    @Test
    public void shouldDeleteAllEpics() {
        Epic epic = createEpic();
        manager.createEpic(epic);

        Subtask subtask = createSubtask(epic);
        manager.createSubtask(epic.getId(), subtask);
        manager.removeEpics();

        assertEquals(Collections.EMPTY_MAP, manager.getSubtasks());
        assertEquals(Collections.EMPTY_MAP, manager.getEpics());
    }

    //изменил
    @Test
    public void shouldDeleteAllSubtasks() {
        Epic epic = createEpic();
        manager.createEpic(epic);

        Subtask subtask = createSubtask(epic);
        manager.createSubtask(epic.getId(), subtask);
        manager.removeSubtasks();

        assertTrue(epic.getSubtasks().isEmpty());
        assertTrue(manager.getSubtasks().isEmpty());
    }

    //изменил
    @Test
    public void shouldDeleteSubtaskOnID() {
        Epic epic = createEpic();
        manager.createEpic(epic);

        Subtask subtask = createSubtask(epic);
        manager.createSubtask(epic.getId(), subtask);
        manager.removeSubtaskOnID(subtask.getId());

        assertTrue(epic.getSubtasks().isEmpty());
        assertTrue(manager.getSubtasks().isEmpty());
    }

    //изменил
    @Test
    public void shouldDeleteTaskOnId() {
        Task task = createTask();

        manager.createTask(task);
        manager.removeTaskOnID(task.getId());

        assertEquals(Collections.EMPTY_MAP, manager.getTasks());
    }

    //изменил
    @Test
    public void shouldDeleteEpicOnId() {
        Epic epic = createEpic();

        manager.createEpic(epic);
        manager.removeEpicOnID(epic.getId());

        assertEquals(Collections.EMPTY_MAP, manager.getEpics());
    }

    //
    @Test
    public void shouldNotDeleteTasksWithIncorrectId() {
        Task task = createTask();

        manager.createTask(task);
        manager.removeTaskOnID(9999);

        assertEquals(Map.of(task.getId(), task), manager.getTasks());
    }

    @Test
    public void shouldNotDeleteEpicsWithIncorrectId() {
        Epic epic = createEpic();

        manager.createEpic(epic);
        manager.removeEpicOnID(9999);

        assertEquals(Map.of(epic.getId(), epic), manager.getEpics());
    }

    @Test
    public void shouldNotDeleteSubtasksWithIncorrectId() {
        Epic epic = createEpic();
        manager.createEpic(epic);

        Subtask subtask = createSubtask(epic);
        manager.createSubtask(epic.getId(), subtask);
        manager.removeSubtaskOnID(9999);

        assertEquals(Map.of(subtask.getId(), subtask), manager.getSubtasks());
        assertEquals(List.of(subtask), manager.getEpicSubtasks(epic.getId()));
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