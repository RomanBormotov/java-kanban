import manager.InMemoryTaskManager;
import manager.Managers;
import manager.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

public class Main {

    public static void main(String[] args) {

        TaskManager InMemoryTaskManager = Managers.getDefault();
        //InMemoryTaskManager InMemoryTaskManager = new InMemoryTaskManager();

        Task task1 = new Task("Собрать вещи", "Уложить в коробки", "DONE");
        Task task2 = new Task("Собрать вещи", "Заклеить коробки", "DONE");
        Epic epic1 = new Epic("Переезд", "В другую квартиру");
        Subtask subtask1 = new Subtask("Вызвать перевозчика", "Позвонить по номеру 000", "DONE");
        Subtask subtask2 = new Subtask("Отдать ключи", "Вернуть ключи хозяину квартиры", "DONE");
        Subtask subtask3 = new Subtask("Рассчитаться за аренду квартиры", "Подсчитать месяц оплаты", "NEW");

        Epic epic2 = new Epic("Очистка квартиры", "Вызвать клининг");


        InMemoryTaskManager.createTask(task1);
        InMemoryTaskManager.createTask(task2);
        InMemoryTaskManager.createEpic(epic1);
        InMemoryTaskManager.createEpic(epic2);
        InMemoryTaskManager.createSubtask(epic1.getId(), subtask1);
        InMemoryTaskManager.createSubtask(epic1.getId(), subtask2);
        InMemoryTaskManager.createSubtask(epic1.getId(), subtask3);

        /* InMemoryTaskManager.updateTask(task1.getId(), task1);
        InMemoryTaskManager.updateTask(task2.getId(), task2);
        InMemoryTaskManager.updateEpic(epic1.getId(), epic1);
        InMemoryTaskManager.updateSubtask(subtask1.getId(), subtask1);
        InMemoryTaskManager.updateSubtask(subtask2.getId(), subtask2);

        System.out.println(InMemoryTaskManager.getSubtasks());
        System.out.println(InMemoryTaskManager.getTasks()); */

        InMemoryTaskManager.getEpicOnID(epic1.getId());

        InMemoryTaskManager.getTaskOnID(task1.getId());
        InMemoryTaskManager.getTaskOnID(task2.getId());
        InMemoryTaskManager.getTaskOnID(task1.getId());

        InMemoryTaskManager.getSubtaskOnID(subtask1.getId());
        InMemoryTaskManager.getSubtaskOnID(subtask3.getId());
        InMemoryTaskManager.getSubtaskOnID(subtask2.getId());

        InMemoryTaskManager.getEpicOnID(epic2.getId());
        InMemoryTaskManager.getEpicOnID(epic1.getId());


        System.out.println(InMemoryTaskManager.getHistory());

        InMemoryTaskManager.removeSubtaskOnID(subtask1.getId());
        InMemoryTaskManager.removeSubtaskOnID(subtask2.getId());
        InMemoryTaskManager.removeEpicOnID(epic1.getId());

        System.out.println(InMemoryTaskManager.getHistory());

        /* InMemoryTaskManager.removeSubtaskOnID(subtask1.getId());

        System.out.println(InMemoryTaskManager.getEpics());
        System.out.println(InMemoryTaskManager.getSubtasks());

        InMemoryTaskManager.removeEpicOnID(epic1.getId());
        InMemoryTaskManager.createEpic(epic2);
        InMemoryTaskManager.createSubtask(epic2.getId(), subtask3);

        System.out.println(InMemoryTaskManager.getEpics());
        System.out.println(InMemoryTaskManager.getTasks());
        System.out.println(InMemoryTaskManager.getSubtasks());

        System.out.println(InMemoryTaskManager.getEpicOnID(epic1.getId()));
        System.out.println(InMemoryTaskManager.getEpicOnID(epic2.getId())); */

    }
}