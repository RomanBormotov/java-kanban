package util;

import manager.*;
import constants.TaskManagerType;

import java.io.File;

public class Managers {

    private static File taskStorageFile = new File("tasks.csv");

    public static TaskManager getDefault(TaskManagerType type) {
        switch (type) {
            case INNER:
                return new InMemoryTaskManager();
            case FILE_BACKEND:
                return new FileBackedTasksManager(taskStorageFile);
        }
        return null;
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    /* public static FileBackedTasksManager loadFromFile(){
        FileBackedTasksManager manager = getDefaultFileBackedTasksManager();
        manager.loadFromFile(taskStorageFile);
        return manager;
    } */

}
