package com.vladislav.todoservice.services;

import com.proto.todo.GetAllUserProjectsRequest;
import com.proto.todo.GetAllUserProjectsResponse;
import com.proto.todo.Project;
import com.proto.todo.ProjectServiceGrpc;
import com.vladislav.todoservice.documents.ProjectDocument;
import com.vladislav.todoservice.pojo.User;
import com.vladislav.todoservice.repositories.ProjectRepository;
import com.vladislav.todoservice.utils.mappers.PojoMapper;
import io.grpc.Context;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProjectService extends ProjectServiceGrpc.ProjectServiceImplBase {

    private final ProjectRepository projectRepository;
    private final PojoMapper<ProjectDocument, Project> projectMapper;
    private final Context.Key<User> userKey;

    @Override
    public void getAllUserProjects(
            GetAllUserProjectsRequest request, StreamObserver<GetAllUserProjectsResponse> responseObserver
    ) {
        final User user = userKey.get();
        if (request.getUserId().equals(user.getId().toString()) || user.getRoles().contains(User.Role.ADMIN)) {
            final Set<GetAllUserProjectsResponse> projectsResponses = projectRepository.findAllByUserId(user.getId()).stream()
                    .map(projectMapper::toDto)
                    .map(project -> GetAllUserProjectsResponse.newBuilder().setProject(project).build())
                    .collect(Collectors.toUnmodifiableSet());

            if (projectsResponses.isEmpty()) {
                responseObserver.onError(Status.OK.withDescription("The user has no projects.").asRuntimeException());
            } else {
                projectsResponses.forEach(responseObserver::onNext);
                responseObserver.onCompleted();
            }
        } else {
            responseObserver.onError(Status.PERMISSION_DENIED.asRuntimeException());
        }
    }
}
