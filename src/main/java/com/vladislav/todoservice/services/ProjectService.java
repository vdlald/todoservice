package com.vladislav.todoservice.services;

import com.proto.todo.GetAllUserProjectsRequest;
import com.proto.todo.GetAllUserProjectsResponse;
import com.proto.todo.ProjectServiceGrpc;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProjectService extends ProjectServiceGrpc.ProjectServiceImplBase {

    @Override
    public void getAllUserProjects(
            GetAllUserProjectsRequest request, StreamObserver<GetAllUserProjectsResponse> responseObserver
    ) {
        responseObserver.onError(Status.UNIMPLEMENTED.asRuntimeException());
    }
}
