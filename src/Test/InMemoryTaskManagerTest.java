package Test;

import manager.*;
import org.junit.jupiter.api.BeforeEach;
import constants.TaskManagerType;
import util.Managers;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void shouldRunBeforeEach() {
        manager = (InMemoryTaskManager) Managers.getDefault(TaskManagerType.INNER);
    }

}
