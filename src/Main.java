import Manager.Manager;
import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;

public class Main {

    public static void main(String[] args) {

        Manager manager = new Manager();

        Task task1 = new Task("Собрать вещи", "Уложить в коробки");
        Task task2 = new Task("Собрать вещи", "Заклеить коробки");
        Epic epic1 = new Epic("Переезд", "В другую квартиру");
        Subtask subtask1 = new Subtask("Вызвать перевозчика", "Позвонить по номеру 000");
        manager.createTask(task1);
        manager.createTask(task2);
        manager.createEpic(epic1);

        manager.getEpicOnID(0);

        manager.updateTask(task1.getId(), task1);

        manager.getEpicSubtasks(0);
    }
}