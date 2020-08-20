package com.vladislav.todoservice.utils;

import com.vladislav.todoservice.pojo.User;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Utils {

    public boolean isAdmin(User user) {
        return user.getRoles().contains(User.Role.ADMIN);
    }

    public StatusRuntimeException permissionDeniedException() {
        return Status.PERMISSION_DENIED.asRuntimeException();
    }
}
