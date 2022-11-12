package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.HashMap;
import java.util.List;

  public interface TaskManager {

      HashMap<Integer, Task> getTasks();

      HashMap<Integer, Subtask> getSubtasks();
      HashMap<Integer, Epic> getEpics();
      void removeTasks();
      void removeSubtasks();
      void removeEpics();

    //2.3 Получение по идентификатору
      Task getTaskOnID(int ID);
      Subtask getSubtaskOnID(int ID);
      Epic getEpicOnID(int ID);

    //2.4 Создание
      void createTask(Task task);
      void createSubtask(int epicID, Subtask subtask);
      void createEpic(Epic epic);

    //2.5 Обновление
      void updateTask(int keyID, Task task);
      void updateSubtask(int keyID, Subtask subtask);
      void updateEpic(int keyID, Epic epic);

    //2.6 Удаление по идентификатору
      void removeTaskOnID(int ID);
      void removeSubtaskOnID(int ID);
      void removeEpicOnID(int ID);

    //3.1 Получение списка всех подзадач определённого эпика
      List<Subtask> getEpicSubtasks(int ID);

    //4 Управление статусами (делается приватным, согласно условию тз)
      void updateStatus(Epic epic);

     //История просмотров задач

      List<Task> getHistory();



    
}
