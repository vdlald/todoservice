package com.vladislav.todoservice.utils;

import com.vladislav.todoservice.pojo.User;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;

public class Utils {

    public static boolean isAdmin(User user) {
        return user.getRoles().contains(User.Role.ADMIN);
    }

    public static StatusRuntimeException permissionDeniedException() {
        return Status.PERMISSION_DENIED.asRuntimeException();
    }
}
