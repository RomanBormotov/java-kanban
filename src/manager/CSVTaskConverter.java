package manager;

import tasks.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CSVTaskConverter {

    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public static String toString(Task task) { //превращает таск в строку, можно сделать статическим
        return task.getId() + "," +
                task.getTaskType() + "," +
                task.getName() + "," +
                task.getStatus() + "," +
                task.getDescription() + "," +
                task.getEpicId() + "," +
                task.getStartTime().format(dtf) + "," +
                task.getDuration().toMinutes();
    }

    public static Task fromString(String task) {
        String[] lines = task.split(",");
        int id = Integer.parseInt(lines[0]);
        TaskType taskType = TaskType.valueOf(lines[1]);
        String name = lines[2];
        String statusString = lines[3];
        Status status = Status.valueOf(statusString);
        String description = lines[4];
        int epicId = Integer.parseInt(lines[5]);
        LocalDateTime startTime = LocalDateTime.parse(lines[6], dtf);
        int duration = Integer.parseInt(lines[7]);
        switch (taskType) {
            case EPIC:
                Epic epic = new Epic(name, description);
                epic.setId(id);
                epic.setTaskType(taskType);
                epic.setStatus(status);
                epic.setEpicId(epicId);
                epic.setStartTime(startTime);
                epic.setDuration(Duration.ofMinutes(duration));
                return epic;
            case TASK:
                Task task1 = new Task(name, description);
                task1.setId(id);
                task1.setTaskType(taskType);
                task1.setStatus(status);
                task1.setEpicId(epicId);
                task1.setStartTime(startTime);
                task1.setDuration(Duration.ofMinutes(duration));
                task1.setEpicId(-1);
                return task1;
            case SUBTASK:
                Subtask subtask = new Subtask(name, description);
                subtask.setId(id);
                subtask.setTaskType(taskType);
                subtask.setStatus(status);
                subtask.setEpicId(epicId);
                subtask.setStartTime(startTime);
                subtask.setDuration(Duration.ofMinutes(duration));
                subtask.setEpicId(-1);
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
    public static String historyToString(List<Task> history){
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < history.size(); i++) {
            result.append(history.get(i).getId()).append(" ");
        }
        String resultString = result.toString().trim();

        return resultString.replace(" ", ",");
    }

}
