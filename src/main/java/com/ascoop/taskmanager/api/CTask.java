package com.ascoop.taskmanager.api;

import com.ascoop.taskmanager.dto.TaskDTO;
import com.ascoop.taskmanager.model.Task;
import com.ascoop.taskmanager.services.TaskServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/task")
public class CTask {
    private final TaskServices taskServices;

    @PostMapping("/addTask")
    public ResponseEntity<?> addTask(@RequestBody TaskDTO task) {

        System.out.println("-----------------");
        System.out.println(task);
        System.out.println("-----------------");

        Task newTask = new Task();
        newTask.setId(task.getId());
        newTask.setCompleted(task.isCompleted());
        newTask.setDescription(task.getDescription());
        newTask.setTitle(task.getTitle());
        newTask.setUser(task.getUser());

        taskServices.addTask(newTask);
        return ResponseEntity.ok("Task created successfully");
    }

    @PutMapping("/updateTask/{taskId}")
    public ResponseEntity<?> updateTask(@RequestBody TaskDTO task, @PathVariable int taskId) {
        Task optionalTask = taskServices.getTaskById(taskId);
        if (optionalTask==null) {
            return ResponseEntity.notFound().build();
        }

        optionalTask.setCompleted(task.isCompleted());
        optionalTask.setDescription(task.getDescription());
        optionalTask.setTitle(task.getTitle());
        optionalTask.setUser(task.getUser());
        taskServices.addTask(optionalTask);

        return ResponseEntity.ok("Updating task successfully");

    }

    @GetMapping("/getTaskByUser/{userID}")
    public ResponseEntity<?> getTaskByUser(@PathVariable long userID) {
        List<TaskDTO> tasksDto  = taskServices.getTaskByUserId(userID).stream().map(TaskDTO::new).toList();
        System.out.println(tasksDto);
        return ResponseEntity.ok(tasksDto);
    }
    @GetMapping("/getTaskById/{taskId}")
    public ResponseEntity<?> getTaskById(@PathVariable int taskId) {
        System.out.println(taskId);
        return ResponseEntity.ok(taskServices.getTaskById(taskId));
    }
    @DeleteMapping("/deleteTask/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable int taskId) {
        taskServices.deleteTask(taskId);
        return ResponseEntity.ok("Deleting task successfully");
    }
}
