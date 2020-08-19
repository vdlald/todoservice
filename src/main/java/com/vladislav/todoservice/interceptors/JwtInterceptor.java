package com.vladislav.todoservice.interceptors;

import com.vladislav.todoservice.pojo.User;
import io.grpc.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JwtInterceptor implements ServerInterceptor {

    private final JwtParser jwtParser;
    private final Context.Key<User> userKey;

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next
    ) {
        final String jwt = headers.get(Metadata.Key.of("jwt", Metadata.ASCII_STRING_MARSHALLER));
        try {
            final Jws<Claims> jws = jwtParser.parseClaimsJws(jwt);
            final Claims claims = jws.getBody();

            final User user = new User()
                    .setId(UUID.fromString(claims.get("userId", String.class)))
                    .setUsername(claims.get("username", String.class))
                    .setRoles(claims.get("roles", (Class<List<User.Role>>) (Class) List.class));

            final Context context = Context.current().withValue(userKey, user);

            return Contexts.interceptCall(context, call, headers, next);
        } catch (Exception e) {  // don't trust the JWT !!
            call.close(Status.UNAUTHENTICATED, new Metadata());
            return new ServerCall.Listener<>() {
            };
        }
    }
}
