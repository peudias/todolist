package todolist.phpdias.com.github.todolist.task;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InterfaceTaskRepository extends JpaRepository<TaskModel, UUID> {
    List<TaskModel> findByUserId(UUID userId);
}
