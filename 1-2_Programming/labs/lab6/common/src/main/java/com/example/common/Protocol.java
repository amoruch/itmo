package com.example.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.example.common.Model.SpaceMarine;

/**
 * Simple messages exchanged by the client and the server.
 */
public final class Protocol {

    private Protocol() {
    }

    public enum CommandType {
        HELP, INFO, SHOW, ADD, UPDATE, REMOVE_BY_ID, CLEAR, REMOVE_FIRST,
        ADD_IF_MAX, REMOVE_LOWER, MAX_BY_HEALTH, GROUP_COUNTING_BY_CREATION_DATE,
        PRINT_DESCENDING, HISTORY
    }

    public abstract static class Payload implements Serializable {

        private static final long serialVersionUID = 1L;
    }

    public static class Empty extends Payload {

        private static final long serialVersionUID = 1L;
    }

    public static class Id extends Payload {

        private static final long serialVersionUID = 1L;
        private final int id;

        public Id(int id) {
            if (id <= 0) {
                throw new IllegalArgumentException("id must be > 0");
            }
            this.id = id;
        }

        public int id() {
            return id;
        }
    }

    public static class Marine extends Payload {

        private static final long serialVersionUID = 1L;
        private final SpaceMarine marine;

        public Marine(SpaceMarine marine) {
            if (marine == null) {
                throw new IllegalArgumentException("marine is required");
            }
            this.marine = marine;
        }

        public SpaceMarine marine() {
            return marine;
        }
    }

    public static class Update extends Payload {

        private static final long serialVersionUID = 1L;
        private final int id;
        private final SpaceMarine marine;

        public Update(int id, SpaceMarine marine) {
            if (id <= 0 || marine == null) {
                throw new IllegalArgumentException("Invalid update");
            }
            this.id = id;
            this.marine = marine;
        }

        public int id() {
            return id;
        }

        public SpaceMarine marine() {
            return marine;
        }
    }

    public static class Request implements Serializable {

        private static final long serialVersionUID = 1L;
        private final CommandType command;
        private final Payload payload;

        public Request(CommandType command, Payload payload) {
            if (command == null) {
                throw new IllegalArgumentException("command is required");
            }
            this.command = command;
            this.payload = payload == null ? new Empty() : payload;
        }

        public CommandType command() {
            return command;
        }

        public Payload payload() {
            return payload;
        }
    }

    public static class Response implements Serializable {

        private static final long serialVersionUID = 1L;
        private final boolean ok;
        private final String message;

        public Response(boolean ok, String message) {
            this.ok = ok;
            this.message = message;
        }

        public boolean ok() {
            return ok;
        }

        public String message() {
            return message;
        }
    }

    public static byte[] serialize(Serializable value) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        ObjectOutputStream output = new ObjectOutputStream(bytes);
        output.writeObject(value);
        output.close();
        return bytes.toByteArray();
    }

    public static Object deserialize(byte[] data, int length)
            throws IOException, ClassNotFoundException {
        ObjectInputStream input = new ObjectInputStream(new ByteArrayInputStream(data, 0, length));
        Object value = input.readObject();
        input.close();
        return value;
    }
}
