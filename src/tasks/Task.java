package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {
    //поля
    private int epicID = -1;
    private String name;
    private String description;
    private int id;
    private Status status;
    private Duration duration;
    private LocalDateTime startTime;
    private TaskType taskType = TaskType.TASK;
    protected DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    //конструторы
    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
    }

    public Task(String name, String description, String startTime, int duration) {
        this.name = name;
        this.description = description;
        this.startTime = LocalDateTime.parse(startTime, dtf);
        this.duration = Duration.ofMinutes(duration);
        this.status = Status.NEW;
    }

    //конструктор для тестов
    public Task(String name, String description, Status status, LocalDateTime startTime, int duration) {
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.duration = Duration.ofMinutes(duration);
        this.status = status;
    }

    public Task(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task(String name, String description, String status) {
        this.name = name;
        this.description = description;
        if (status.equals("NEW")||(status.equals("IN_PROGRESS"))||(status.equals("DONE"))) {
            this.status = Status.valueOf(status);
        } else {
            this.status = Status.NEW;
        }
    }

    public Task(String name, String description, Status status, String startTime, int duration) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = LocalDateTime.parse(startTime, dtf);
        this.duration = Duration.ofMinutes(duration);
    }

    //методы

    public LocalDateTime getEndTime() {
        return startTime.plusMinutes(duration.toMinutes());
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name) && Objects.equals(description, task.description) && Objects.equals(status, task.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, status, id);
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", id=" + id +'\'' +
                ", supposed start time=" + startTime.format(dtf) + '\'' +
                ", supposed duration(min)=" + duration.toMinutes() + '\'' +
                ", expected end time=" + getEndTime().format(dtf) +
                '}';
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getStatus() {
        return status.name();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getEpicId() { // нужен для получения ID в случае когда у эпика нет ID
        return epicID; //
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public void setEpicId(int epicID) {
        this.epicID = epicID;
    }

}