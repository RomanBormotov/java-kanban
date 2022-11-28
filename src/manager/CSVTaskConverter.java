package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskType;

import java.util.ArrayList;
import java.util.List;

public class CSVTaskConverter {

    public static String toString(Task task) { //превращает таск в строку, можно сделать статическим
        return task.getId() + "," +
                task.getTaskType() + "," +
                task.getName() + "," +
                task.getStatus() + "," +
                task.getDescription() + "," +
                task.getEpicId()
                ;
    }

    public static Task fromString(String task) {
        String[] lines = task.split(",");
        int id = Integer.parseInt(lines[0]);
        TaskType taskType = TaskType.valueOf(lines[1]);
        String name = lines[2];
        String status = lines[3];
        String description = lines[4];
        int epicId = -1;
        if (!lines[5].equals("null")) {
            epicId = Integer.parseInt(lines[5]);
        }
        switch (taskType) {
            case EPIC:
                Epic epic = new Epic(name, description);
                epic.setId(id);
                epic.setTaskType(taskType);
                epic.setStatus(status);
                epic.setEpicId(epicId);
                return epic;
            case TASK:
                Task task1 = new Task(name, description);
                task1.setId(id);
                task1.setTaskType(taskType);
                task1.setStatus(status);
                task1.setEpicId(epicId);
                return task1;
            case SUBTASK:
                Subtask subtask = new Subtask(name, description);
                subtask.setId(id);
                subtask.setTaskType(taskType);
                subtask.setStatus(status);
                subtask.setEpicId(epicId);
                return subtask;
        }
        return null;
    }

    public static List<Integer> historyFromString(String value){
        List<Integer> history = new ArrayList<>();
        String [] splitString = value.split(",");
        for (String s : splitString) {
            history.add(Integer.valueOf(s));
        }
        return history;
    }
    public static String historyToString(HistoryManager historyManager){
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < historyManager.getHistory().size(); i++) {
            result.append(historyManager.getHistory().get(i).getId()).append(" ");
        }
        String resultString = result.toString().trim();

        return resultString.replace(" ", ",");
    }

}
