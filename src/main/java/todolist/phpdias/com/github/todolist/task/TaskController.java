package todolist.phpdias.com.github.todolist.task;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private InterfaceTaskRepository taskRepository;
    
    @PostMapping("/")
    public ResponseEntity createTask(@RequestBody TaskModel taskModel, HttpServletRequest request){
        var userId = request.getAttribute("userId");
        taskModel.setUserId((UUID) userId);

        var currentDate = LocalDateTime.now();
        if(currentDate.isAfter(taskModel.getDateInit())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Init date needs be bigger than current date.");
        } else if (currentDate.isAfter(taskModel.getDateEnd())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("End date needs be bigger than current date.");            
        }

        if(taskModel.getDateInit().isAfter(taskModel.getDateEnd())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Init date needs be less than end date.");
        }

        var taskCreated = this.taskRepository.save(taskModel);
        return ResponseEntity.status(HttpStatus.OK).body(taskCreated);
    }
}
