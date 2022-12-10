package Test;

import manager.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Task;
import tasks.TaskManagerType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static manager.Managers.getDefaultHistory;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTasksManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    public static final Path path = Path.of("tasks.csv");
    File file = new File(String.valueOf(path));

    @BeforeEach
    public void shouldRunBeforeEach() {
        manager = (FileBackedTasksManager) Managers.getDefault(TaskManagerType.FILE_BACKEND);
    }

    @AfterEach
    public void shouldRunAfterEach() {
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void shouldCheckThatFilesSaveAndLoadSuccessful() {
        Task task = new Task(
                "Test name",
                "Test description",
                Status.NEW,
                LocalDateTime.now(),
                0);
        manager.createTask(task);
        Epic epic = new Epic(
                "Name",
                "Title",
                Status.NEW,
                LocalDateTime.now(),
                0);
        manager.createEpic(epic);
        FileBackedTasksManager.loadFromFile(file);
        assertEquals(0, manager.getTasks().size());
        assertEquals(0, manager.getEpics().size());
    }

    @Test
    public void shouldCheckThatAllEmptyFilesSaveAndLoadSuccessful() {
        FileBackedTasksManager fileManager = (FileBackedTasksManager) Managers.getDefault(TaskManagerType.FILE_BACKEND);
        assert fileManager != null;
        fileManager.save();
        FileBackedTasksManager.loadFromFile(file);
        assertEquals(0, manager.getTasks().size());
        assertEquals(0, manager.getEpics().size());
        assertEquals(0, manager.getSubtasks().size());
    }

    @Test
    public void shouldSaveAndLoadEmptyHistory() {
        FileBackedTasksManager fileManager = (FileBackedTasksManager) Managers.getDefault(TaskManagerType.FILE_BACKEND);
        assert fileManager != null;
        fileManager.save();
        FileBackedTasksManager.loadFromFile(file);
        assertEquals(Collections.EMPTY_LIST, manager.getHistory());
    }

}
