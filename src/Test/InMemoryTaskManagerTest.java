package Test;

import manager.*;
import org.junit.jupiter.api.BeforeEach;
import tasks.TaskManagerType;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void shouldRunBeforeEach() {
        manager = (InMemoryTaskManager) Managers.getDefault(TaskManagerType.INNER);
    }

}
