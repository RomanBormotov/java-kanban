package Tasks;

import java.util.ArrayList;

public class Epic extends Task {

    ArrayList<Integer> subtasks = new ArrayList<>();

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
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
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



}
