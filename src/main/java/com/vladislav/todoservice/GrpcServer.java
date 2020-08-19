package com.vladislav.todoservice;

import com.vladislav.todoservice.manufacture.annotations.InjectLogger;
import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.ServerInterceptor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GrpcServer implements ApplicationRunner {

    @InjectLogger
    private Logger logger;

    @Value("${app.grpc.server.port}")
    private Integer port;

    private final List<BindableService> services;
    private final List<ServerInterceptor> interceptors;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("Starting gRPC server...");

        final Server server = createServer();

        server.start();
        logger.info("gRPC server started");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Received shutdown request");
            server.shutdown();
            logger.info("Successfully stopped the server");
        }));
        server.awaitTermination();
    }

    private Server createServer() {
        final ServerBuilder<?> serverBuilder = ServerBuilder.forPort(port);
        interceptors.forEach(serverBuilder::intercept);
        services.forEach(serverBuilder::addService);
        return serverBuilder.build();
    }
}
