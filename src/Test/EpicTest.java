package Test;

import constants.Status;
import constants.TaskManagerType;
import manager.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import util.Managers;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    InMemoryTaskManager manager;

    protected Epic createEpic() {

        return new Epic(
                "Description",
                "Title",
                Status.NEW,
                LocalDateTime.now(),
                0);
    }

    protected Subtask createSubtask(Epic epic) {
        Subtask subtask = new Subtask("Test name"+Math.round(Math.random()*10),
                "Test description",
                Status.NEW,
                LocalDateTime.now(),
                0);
        subtask.setEpicId(epic.getId());
        return subtask;
    }

    @BeforeEach
    public void shouldRunBeforeEach() {
        manager = (InMemoryTaskManager) Managers.getDefault(TaskManagerType.INNER);
    }

    @Test
    public void shouldCreateEmptyEpic() {
        Epic epic = createEpic();
        manager.createEpic(epic);
        Epic returnedEpic = manager.getEpics().get(0);

        assertEquals(Status.NEW.toString(), returnedEpic.getStatus(), "Неверный статус");
    }

    @Test
    public void shouldCreateEpicWithNewSubtasks() {
        Epic epic = createEpic();
        manager.createEpic(epic);
        Subtask subtask = createSubtask(epic);
        Subtask subtask1 = createSubtask(epic);
        manager.createSubtask(epic.getId(), subtask);
        manager.createSubtask(epic.getId(), subtask1);
        Epic returnedEpic = manager.getEpics().get(0);
        assertEquals(Status.NEW.toString(), returnedEpic.getStatus(), "Неверный статус");
    }

    @Test
    public void shouldCreateEpicWithDONESubtasks() {
        Epic epic = createEpic();
        manager.createEpic(epic);
        Subtask subtask = createSubtask(epic);
        Subtask subtask1 = createSubtask(epic);
        manager.createSubtask(epic.getId(), subtask);
        manager.createSubtask(epic.getId(), subtask1);
        subtask.setStatus(Status.DONE);
        subtask1.setStatus(Status.DONE);
        manager.updateSubtask(subtask.getId(), subtask);
        manager.updateSubtask(subtask1.getId(), subtask1);
        Epic returnedEpic = manager.getEpics().get(0);
        assertEquals(Status.DONE.toString(), returnedEpic.getStatus(), "Неверный статус");
    }

    @Test
    public void shouldCreateEpicWithNEWandDONESubtasks() {
        Epic epic = createEpic();
        manager.createEpic(epic);
        Subtask subtask = createSubtask(epic);
        Subtask subtask1 = createSubtask(epic);
        manager.createSubtask(epic.getId(), subtask);
        manager.createSubtask(epic.getId(), subtask1);
        subtask.setStatus(Status.DONE);
        manager.updateSubtask(subtask.getId(), subtask);
        Epic returnedEpic = manager.getEpics().get(0);
        assertEquals(Status.IN_PROGRESS.toString(), returnedEpic.getStatus(), "Неверный статус");
    }

    @Test
    public void shouldCreateEpicWithINPROGRESSSubtasks() {
        Epic epic = createEpic();
        manager.createEpic(epic);
        Subtask subtask = createSubtask(epic);
        Subtask subtask1 = createSubtask(epic);
        manager.createSubtask(epic.getId(), subtask);
        manager.createSubtask(epic.getId(), subtask1);
        subtask.setStatus(Status.IN_PROGRESS);
        subtask1.setStatus(Status.IN_PROGRESS);
        manager.updateSubtask(subtask.getId(), subtask);
        manager.updateSubtask(subtask1.getId(), subtask1);
        Epic returnedEpic = manager.getEpics().get(0);
        assertEquals(Status.IN_PROGRESS.toString(), returnedEpic.getStatus(), "Неверный статус");
    }

}