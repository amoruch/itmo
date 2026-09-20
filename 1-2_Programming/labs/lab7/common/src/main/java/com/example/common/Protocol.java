package com.example.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.example.common.Model.SpaceMarine;

/**
 * Simple messages exchanged by the client and the server.
 */
public final class Protocol {

    private Protocol() {
    }

    public enum CommandType {
        REGISTER, LOGIN,
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

    /** Login and password supplied with every request made by an authorized client. */
    public static class Credentials implements Serializable {

        private static final long serialVersionUID = 1L;
        private final String login;
        private final String password;

        public Credentials(String login, String password) {
            if (login == null || login.isBlank() || login.length() > 64
                    || password == null || password.isBlank()) {
                throw new IllegalArgumentException("Login and password must not be blank");
            }
            this.login = login;
            this.password = password;
        }

        public String login() {
            return login;
        }

        public String password() {
            return password;
        }
    }

    /** Text returned by a command that has no structured result. */
    public static class Text extends Payload {

        private static final long serialVersionUID = 1L;
        private final String text;

        public Text(String text) {
            this.text = text == null ? "" : text;
        }

        public String text() {
            return text;
        }
    }

    /** A collection of marines returned by collection-reading commands. */
    public static class Marines extends Payload {

        private static final long serialVersionUID = 1L;
        private final List<SpaceMarine> marines;

        public Marines(List<SpaceMarine> marines) {
            if (marines == null || marines.stream().anyMatch(marine -> marine == null)) {
                throw new IllegalArgumentException("marines must not contain null values");
            }
            this.marines = new ArrayList<>(marines);
        }

        public List<SpaceMarine> marines() {
            return Collections.unmodifiableList(marines);
        }
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
        private final Credentials credentials;

        public Request(CommandType command, Payload payload) {
            this(command, payload, null);
        }

        public Request(CommandType command, Payload payload, Credentials credentials) {
            if (command == null) {
                throw new IllegalArgumentException("command is required");
            }
            this.command = command;
            this.payload = payload == null ? new Empty() : payload;
            this.credentials = credentials;
        }

        public CommandType command() {
            return command;
        }

        public Payload payload() {
            return payload;
        }

        public Credentials credentials() {
            return credentials;
        }
    }

    public static class Response implements Serializable {

        private static final long serialVersionUID = 1L;
        private final boolean ok;
        private final Payload payload;

        public Response(boolean ok, Payload payload) {
            this.ok = ok;
            this.payload = payload == null ? new Empty() : payload;
        }

        public boolean ok() {
            return ok;
        }

        public Payload payload() {
            return payload;
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
