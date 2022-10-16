import manager.InMemoryTaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

public class Main {

    public static void main(String[] args) {

        InMemoryTaskManager InMemoryTaskManager = new InMemoryTaskManager();

        Task task1 = new Task("Собрать вещи", "Уложить в коробки", "DONE");
        Task task2 = new Task("Собрать вещи", "Заклеить коробки", "DONE");
        Epic epic1 = new Epic("Переезд", "В другую квартиру");
        Subtask subtask1 = new Subtask("Вызвать перевозчика", "Позвонить по номеру 000", "DONE");
        Subtask subtask2 = new Subtask("Отдать ключи", "Вернуть ключи хозяину квартиры", "DONE");

        Epic epic2 = new Epic("Очистка квартиры", "Вызвать клининг");
        Subtask subtask3 = new Subtask("Оценить работу клининга", "Осмотр квартиры после уборки");

        InMemoryTaskManager.createTask(task1);
        InMemoryTaskManager.createTask(task2);
        InMemoryTaskManager.createEpic(epic1);
        InMemoryTaskManager.createSubtask(epic1.getId(), subtask1);
        InMemoryTaskManager.createSubtask(epic1.getId(), subtask2);

        InMemoryTaskManager.updateTask(task1.getId(), task1);
        InMemoryTaskManager.updateTask(task2.getId(), task2);
        InMemoryTaskManager.updateEpic(epic1.getId(), epic1);
        InMemoryTaskManager.updateSubtask(subtask1.getId(), subtask1);
        InMemoryTaskManager.updateSubtask(subtask2.getId(), subtask2);
        System.out.println(InMemoryTaskManager.getSubtasks());
        System.out.println(InMemoryTaskManager.getTasks());
        System.out.println(InMemoryTaskManager.getEpicOnID(epic1.getId()));
        InMemoryTaskManager.getTaskOnID(task1.getId());
        InMemoryTaskManager.getSubtaskOnID(subtask1.getId());

        System.out.println(InMemoryTaskManager.getHistory());



        InMemoryTaskManager.removeSubtaskOnID(subtask1.getId());

        System.out.println(InMemoryTaskManager.getEpics());
        System.out.println(InMemoryTaskManager.getSubtasks());

        InMemoryTaskManager.removeEpicOnID(epic1.getId());
        InMemoryTaskManager.createEpic(epic2);
        InMemoryTaskManager.createSubtask(epic2.getId(), subtask3);

        System.out.println(InMemoryTaskManager.getEpics());
        System.out.println(InMemoryTaskManager.getTasks());
        System.out.println(InMemoryTaskManager.getSubtasks());

        System.out.println(InMemoryTaskManager.getEpicOnID(epic1.getId()));
        System.out.println(InMemoryTaskManager.getEpicOnID(epic2.getId()));

    }
}