record Task(String name, int priority) implements Comparable<Task> {

    @Override
    public int compareTo(Task other) {
        return name.compareTo(other.name);
    }
}