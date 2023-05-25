public class MemoryWord {
    String name;
    Object value;

    public MemoryWord() {
        this.name = "";
        this.value = null;
    }

    public MemoryWord(String name, Object value) {
        this.name = name;
        this.value = value;
    }
}
