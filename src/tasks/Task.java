package tasks;

import java.util.Objects;

public class Task {
    //поля
    private int epicID;
    private String name;
    private String description;
    //(!) по идее поле ниже (id) можно вообще удалить, так как все манипуляции
    // с id по сути завязаны на одноименном ключе в классе InMemoryTaskManager,
    // в ключах соответствующих коллекций.
    private int id;
    private Status status;

    private TaskType taskType = TaskType.TASK;
    //конструтор
    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
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

    //методы
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
                ", id=" + id +
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
        return null; //
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