package tasks;

import java.util.ArrayList;

public class Epic extends Task {

    private int epicID;
    ArrayList<Integer> subtasks = new ArrayList<>();

    private TaskType taskType = TaskType.EPIC;

    public ArrayList<Integer> getSubtasks() {
        return subtasks;
    }

    public void addSubtasks(Subtask subtask) {
        subtasks.add(subtask.getId());
    }

    public void removeSubtasks(Subtask subtask) {

        subtasks.remove((Integer) (subtask.getId()));
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
                '}';
    }

    //конструктор
    public Epic(String name, String description) {
        super(name, description);
    }

    public Epic(String name, String description, Status status) {
        super(name, description, status);
    }
    public Integer getEpicId() {
        return null;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    @Override
    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }
    public void setEpicId(int epicID) {
        this.epicID = epicID;
    }
}
