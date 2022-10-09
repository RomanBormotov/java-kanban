package Tasks;

public class Subtask extends Task {

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

    private int epicID; //указывает к какому эпику относится

    public Subtask(String name, String description) {
        super(name, description);
    }

    public Subtask(String name, String description, String status) {
        super(name, description, status);
    }

    public int getEpicId() {
        return epicID;
    }

    public void setEpicId(int epicID) {
        this.epicID = epicID;
    }
}

