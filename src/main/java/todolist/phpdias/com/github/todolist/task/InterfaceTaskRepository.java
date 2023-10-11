package todolist.phpdias.com.github.todolist.task;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InterfaceTaskRepository extends JpaRepository<TaskModel, UUID> {
    
}
