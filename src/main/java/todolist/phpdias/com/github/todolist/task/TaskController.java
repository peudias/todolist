package todolist.phpdias.com.github.todolist.task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import todolist.phpdias.com.github.todolist.util.Util;

import org.springframework.web.bind.annotation.PutMapping;


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

    @GetMapping("/")
    public List<TaskModel> list(HttpServletRequest request){
        var userId = request.getAttribute("userId");

        var tasksList = this.taskRepository.findByUserId((UUID)userId);
        return tasksList;   
    }

    //http://localhost:8080/tasks/23129831892-cdndc-1238123
    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody TaskModel taskModel, HttpServletRequest request, @PathVariable UUID id){
        var userId = request.getAttribute("userId");

        var taskTarget = this.taskRepository.findById(id).orElse(null);

        if(taskTarget == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Task not found.");
        }

        if(!taskTarget.getUserId().equals(userId)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User can't update this task.");
        }

        Util.copyNonNullProperties(taskModel, taskTarget);

        var taskUpdated = this.taskRepository.save(taskTarget);

        return ResponseEntity.ok().body(taskUpdated);
    }
}
