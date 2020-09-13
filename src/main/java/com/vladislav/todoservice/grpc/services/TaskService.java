package com.vladislav.todoservice.grpc.services;

import com.proto.todo.*;
import com.vladislav.todoservice.documents.TaskDocument;
import com.vladislav.todoservice.pojo.User;
import com.vladislav.todoservice.repositories.TaskRepository;
import com.vladislav.todoservice.utils.mappers.PojoMapper;
import io.grpc.Context;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.vladislav.todoservice.utils.Utils.isAdmin;
import static com.vladislav.todoservice.utils.Utils.permissionDeniedException;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TaskService extends TaskServiceGrpc.TaskServiceImplBase {

    private final TaskRepository taskRepository;
    private final PojoMapper<TaskDocument, Task> taskMapper;
    private final Context.Key<User> userKey;

    @Override
    public void getAllUserTasks(
            GetAllUserTasksRequest request, StreamObserver<GetAllUserTasksResponse> responseObserver
    ) {
        final User user = userKey.get();
        if (request.getUserId().equals(user.getId().toString()) || isAdmin(user)) {
            final Set<GetAllUserTasksResponse> tasks = taskRepository.findAllByUserId(user.getId()).stream()
                    .map(taskMapper::toDto)
                    .map(task -> GetAllUserTasksResponse.newBuilder().setTask(task).build())
                    .collect(Collectors.toUnmodifiableSet());

            if (tasks.isEmpty()) {
                responseObserver.onError(Status.OK.withDescription("The user has no tasks").asRuntimeException());
            } else {
                tasks.forEach(responseObserver::onNext);
                responseObserver.onCompleted();
            }
        } else {
            responseObserver.onError(permissionDeniedException());
        }
    }

    @Override
    public void getTask(GetTaskRequest request, StreamObserver<GetTaskResponse> responseObserver) {
        final User user = userKey.get();
        final Optional<TaskDocument> optionalTask = taskRepository.findById(UUID.fromString(request.getTaskId()));
        if (optionalTask.isPresent()) {
            final TaskDocument taskDocument = optionalTask.get();
            if (taskDocument.getUserId().equals(user.getId()) || isAdmin(user)) {
                final GetTaskResponse response = GetTaskResponse.newBuilder()
                        .setTask(taskMapper.toDto(taskDocument))
                        .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            } else {
                responseObserver.onError(permissionDeniedException());
            }
        } else {
            final StatusRuntimeException exception = notFoundTaskException(request.getTaskId());
            responseObserver.onError(exception);
        }
    }

    @Override
    public void createTask(CreateTaskRequest request, StreamObserver<CreateTaskResponse> responseObserver) {
        final User user = userKey.get();

        final Task requestTask = request.getTask();
        TaskDocument taskDocument = taskMapper.toDocument(requestTask)
                .setUserId(user.getId()).setId(UUID.randomUUID());

        taskDocument = taskRepository.save(taskDocument);
        final Task task = taskMapper.toDto(taskDocument);
        final CreateTaskResponse taskResponse = CreateTaskResponse.newBuilder().setTask(task).build();

        responseObserver.onNext(taskResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void updateTask(UpdateTaskRequest request, StreamObserver<UpdateTaskResponse> responseObserver) {
        final User user = userKey.get();

        final Task task = request.getTask();
        if (task.getUserId().equals(user.getId().toString())) {
            final TaskDocument taskDocument = taskMapper.toDocument(task);
            final Optional<TaskDocument> optionalTaskDocument = taskRepository.findById(taskDocument.getId());
            if (optionalTaskDocument.isPresent()) {
                taskRepository.save(taskDocument);
                responseObserver.onNext(UpdateTaskResponse.newBuilder().build());
                responseObserver.onCompleted();
            } else {
                final StatusRuntimeException exception = notFoundTaskException(task.getId());
                responseObserver.onError(exception);
            }
        } else {
            responseObserver.onError(permissionDeniedException());
        }
    }

    @Override
    public void deleteTask(DeleteTaskRequest request, StreamObserver<DeleteTaskResponse> responseObserver) {
        final User user = userKey.get();
        final String taskId = request.getTaskId();
        final Optional<TaskDocument> optionalTaskDocument = taskRepository.findById(UUID.fromString(taskId));
        if (optionalTaskDocument.isPresent()) {
            final TaskDocument taskDocument = optionalTaskDocument.get();
            if (taskDocument.getUserId().equals(user.getId())) {
                taskRepository.deleteById(taskDocument.getId());
                responseObserver.onNext(DeleteTaskResponse.newBuilder().build());
                responseObserver.onCompleted();
            } else {
                responseObserver.onError(permissionDeniedException());
            }
        } else {
            responseObserver.onError(notFoundTaskException(taskId));
        }
    }

    private static StatusRuntimeException notFoundTaskException(String taskId) {
        return Status.NOT_FOUND
                .withDescription(String.format("Not found task with id: %s", taskId))
                .asRuntimeException();
    }
}
