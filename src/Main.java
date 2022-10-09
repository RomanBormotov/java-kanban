import Manager.Manager;
import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;

public class Main {

    public static void main(String[] args) {

        Manager manager = new Manager();

        Task task1 = new Task("Собрать вещи", "Уложить в коробки", "DONE");
        Task task2 = new Task("Собрать вещи", "Заклеить коробки", "DONE");
        Epic epic1 = new Epic("Переезд", "В другую квартиру");
        Subtask subtask1 = new Subtask("Вызвать перевозчика", "Позвонить по номеру 000", "DONE");
        Subtask subtask2 = new Subtask("Отдать ключи", "Вернуть ключи хозяину квартиры", "DONE");

        Epic epic2 = new Epic("Очистка квартиры", "Вызвать клининг");
        Subtask subtask3 = new Subtask("Оценить работу клининга", "Осмотр квартиры после уборки");

        manager.createTask(task1);
        manager.createTask(task2);
        manager.createEpic(epic1);
        manager.createSubtask(epic1.getId(), subtask1);
        manager.createSubtask(epic1.getId(), subtask2);
        manager.updateTask(task1.getId(), task1);
        manager.updateTask(task2.getId(), task2);
        manager.updateEpic(epic1.getId(), epic1);
        manager.updateSubtask(subtask1.getId(), subtask1);
        manager.updateSubtask(subtask2.getId(), subtask2);
        System.out.println(manager.getSubtasks());
        System.out.println(manager.getTasks());
        System.out.println(manager.getEpicOnID(epic1.getId()));

        manager.removeSubtaskOnID(subtask1.getId());

        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtasks());

        manager.removeEpicOnID(epic1.getId());
        manager.createEpic(epic2);
        manager.createSubtask(epic2.getId(), subtask3);

        System.out.println(manager.getEpics());
        System.out.println(manager.getTasks());
        System.out.println(manager.getSubtasks());

        System.out.println(manager.getEpicOnID(epic1.getId()));
        System.out.println(manager.getEpicOnID(epic2.getId()));

    }
}