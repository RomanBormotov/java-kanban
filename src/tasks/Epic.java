package tasks;

import constants.Status;
import constants.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private ArrayList<Subtask> subtasks = new ArrayList<>();

    private TaskType taskType = TaskType.EPIC;

    //конструкторы
    public Epic(String name, String description) {
        super(name, description);
    }

    public Epic(String name, String description, String startTime, int duration) {
        super(name, description, startTime, duration);
    }

    public Epic(String name, String description, Status status, LocalDateTime startTime, int duration) {
        super(name, description, status, startTime, duration);
    }

    public Epic(String name, String description, Status status) {
        super(name, description, status);
    }

    public Epic(String name, String description, Status status, String startTime, int duration) {
        super(name, description, status, startTime, duration);
    }

    public void setStartTime() {
        LocalDateTime resultStartTime = LocalDateTime.now();
        for (Subtask s : subtasks) {
            LocalDateTime subStartTime = s.getStartTime();
            if (resultStartTime.isAfter(subStartTime))
                resultStartTime = subStartTime;
        }
        setStartTime(resultStartTime);
    }

    public void setDuration() {
        int resultDuration = 0;
        for (Subtask s : subtasks) {
            resultDuration += s.getDuration().toMinutes();
        }
        setDuration(Duration.ofMinutes(resultDuration));
    }

    public ArrayList<Subtask> getSubtasks() {
        /*ArrayList<Integer> tempList = new ArrayList<>();
        for (Subtask s : subtasks) {
            tempList.add(s.getId());
        }*/
        return subtasks;
    }

    public void setSubtasks(List<Subtask> subtasks) {
        this.subtasks = new ArrayList<>(subtasks);
    }

    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask);
    }

    public void removeSubtask(Subtask subtask) {
        subtasks.remove(subtask);
    }

    public void removeSubtasks() {
        subtasks.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Epic epic = (Epic) o;
        return this.subtasks == epic.subtasks;
    }

    @Override
    public int hashCode() {
        int hash = 17;
        if (subtasks != null) {
            hash = subtasks.hashCode();
        }
        return hash;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status='" + getStatus() + '\'' +
                ", id=" + getId() + '\'' +
                ", SubtasksID='" + subtasks + '\'' +
                ", supposed start time=" + getStartTime().format(dtf) + '\'' +
                ", supposed duration(min)=" + getDuration().toMinutes() + '\'' +
                ", expected end time=" + getEndTime().format(dtf) +
                '}';
    }

    public TaskType getTaskType() {
        return taskType;
    }

    @Override
    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }
}
