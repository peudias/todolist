package todolist.phpdias.com.github.todolist.user;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InterfaceUserRepository extends JpaRepository<UserModel, UUID> {
    
}
