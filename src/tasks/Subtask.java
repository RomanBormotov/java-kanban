package tasks;

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
                '}';
    }

    public Subtask(String name, String description) {
        super(name, description);
    }

    public Subtask(String name, String description, Status status) {
        super(name, description, status);
    }

    public Integer getEpicId() {
        return epicID;
    }

    public void setEpicId(int epicID) {
        this.epicID = epicID;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    @Override
    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }
}

