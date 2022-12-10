package tasks;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {

    private int epicID; //указывает к какому эпику относится

    private TaskType taskType = TaskType.SUBTASK;

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status='" + getStatus() + '\'' +
                ", id=" + getId() + '\'' +
                ", EpicID='" + epicID + '\'' +
                ", supposed start time=" + getStartTime().format(dtf) + '\'' +
                ", supposed duration(min)=" + getDuration().toMinutes() + '\'' +
                ", expected end time=" + getEndTime().format(dtf) +
                '}';
    }

    public Subtask(String name, String description) {
        super(name, description);
    }

    public Subtask(String name, String description, String startTime, int duration) {
        super(name, description, startTime, duration);
    }

    //конструктор для теста
    public Subtask(String name, String description, Status status, LocalDateTime startTime, int duration) {
        super(name, description, status, startTime, duration);
    }

    public Subtask(String name, String description, Status status) {
        super(name, description, status);
    }

    public Subtask(String name, String description, Status status, String startTime, int duration) {
        super(name, description, status, startTime, duration);
    }

    @Override
    public Integer getEpicId() {
        return epicID;
    }

    public void setEpicId(int epicID) {
        this.epicID = epicID;
    }

    public void removeEpicId() {
        this.epicID = -1;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    @Override
    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }
}

