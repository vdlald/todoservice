package com.vladislav.todoservice.grpc.services;

import com.proto.todo.*;
import com.vladislav.todoservice.documents.ProjectDocument;
import com.vladislav.todoservice.pojo.User;
import com.vladislav.todoservice.repositories.ProjectRepository;
import com.vladislav.todoservice.utils.readers.DocumentReader;
import com.vladislav.todoservice.utils.writers.DocumentWriter;
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
public class ProjectService extends ProjectServiceGrpc.ProjectServiceImplBase {

    private final Context.Key<User> userKey;
    private final ProjectRepository projectRepository;
    private final DocumentWriter<ProjectDocument, Project> projectDocumentWriter;
    private final DocumentReader<ProjectDocument, Project> projectDocumentReader;

    @Override
    public void getAllUserProjects(
            GetAllUserProjectsRequest request, StreamObserver<GetAllUserProjectsResponse> responseObserver
    ) {
        final User user = userKey.get();
        if (request.getUserId().equals(user.getId().toString()) || user.getRoles().contains(User.Role.ADMIN)) {
            final Set<GetAllUserProjectsResponse> projectsResponses = projectRepository.findAllByUserId(user.getId())
                    .stream()
                    .map(projectDocumentWriter::write)
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

    @Override
    public void getProject(GetProjectRequest request, StreamObserver<GetProjectResponse> responseObserver) {
        final User user = userKey.get();
        final String projectId = request.getProjectId();
        final Optional<ProjectDocument> optionalProject = projectRepository.findById(UUID.fromString(projectId));
        if (optionalProject.isPresent()) {
            final ProjectDocument projectDocument = optionalProject.get();
            if (projectDocument.getUserId().equals(user.getId()) || isAdmin(user)) {
                final GetProjectResponse response = GetProjectResponse.newBuilder()
                        .setProject(projectDocumentWriter.write(projectDocument))
                        .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            } else {
                responseObserver.onError(permissionDeniedException());
            }
        } else {
            responseObserver.onError(notFoundProjectException(projectId));
        }
    }

    @Override
    public void createProject(CreateProjectRequest request, StreamObserver<CreateProjectResponse> responseObserver) {
        final User user = userKey.get();
        final Project requestProject = request.getProject();
        ProjectDocument projectDocument = projectDocumentReader.read(requestProject)
                .setId(UUID.randomUUID()).setUserId(user.getId());

        projectDocument = projectRepository.save(projectDocument);
        final Project project = projectDocumentWriter.write(projectDocument);
        responseObserver.onNext(CreateProjectResponse.newBuilder().setProject(project).build());
        responseObserver.onCompleted();
    }

    @Override
    public void updateProject(UpdateProjectRequest request, StreamObserver<UpdateProjectResponse> responseObserver) {
        final User user = userKey.get();

        final Project project = request.getProject();
        if (project.getUserId().equals(user.getId().toString())) {
            final ProjectDocument projectDocument = projectDocumentReader.read(project);
            final var optionalProjectDocument = projectRepository.findById(projectDocument.getId());
            if (optionalProjectDocument.isPresent()) {
                projectRepository.save(projectDocument);
                responseObserver.onNext(UpdateProjectResponse.newBuilder().build());
                responseObserver.onCompleted();
            } else {
                responseObserver.onError(notFoundProjectException(project.getId()));
            }
        } else {
            responseObserver.onError(permissionDeniedException());
        }
    }

    @Override
    public void deleteProject(DeleteProjectRequest request, StreamObserver<DeleteProjectResponse> responseObserver) {
        final User user = userKey.get();
        final String projectId = request.getProjectId();
        final Optional<ProjectDocument> optionalProjectDocument = projectRepository.findById(UUID.fromString(projectId));
        if (optionalProjectDocument.isPresent()) {
            final ProjectDocument projectDocument = optionalProjectDocument.get();
            if (projectDocument.getUserId().equals(user.getId())) {
                projectRepository.deleteById(projectDocument.getId());
                responseObserver.onNext(DeleteProjectResponse.newBuilder().build());
                responseObserver.onCompleted();
            } else {
                responseObserver.onError(permissionDeniedException());
            }
        } else {
            responseObserver.onError(notFoundProjectException(projectId));
        }
    }

    private static StatusRuntimeException notFoundProjectException(String projectId) {
        return Status.NOT_FOUND
                .withDescription(String.format("Not found project with id: %s", projectId))
                .asRuntimeException();
    }
}
