package com.example.client.commands;

/**
 * Result of executing one client command.
 */
public record ExecutionResult(ExecutionStatus status, String message) {

    public ExecutionResult {
        if (status == null) {
            throw new IllegalArgumentException("status is required");
        }
        message = message == null ? "" : message;
    }

    public static ExecutionResult success(String message) {
        return new ExecutionResult(ExecutionStatus.SUCCESS, message);
    }

    public static ExecutionResult failure(String message) {
        return new ExecutionResult(ExecutionStatus.FAILURE, message);
    }

    public static ExecutionResult exit(String message) {
        return new ExecutionResult(ExecutionStatus.EXIT, message);
    }
}
