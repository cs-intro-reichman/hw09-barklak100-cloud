/** A linked list of character data objects. */
public class List {

    private Node first;
    private int size;

    public List() {
        first = null;
        size = 0;
    }

    public int getSize() {
        return size;
    }

    public void addFirst(char chr) {
        CharData cd = new CharData(chr);
        Node newNode = new Node(cd, first);
        first = newNode;
        size++;
    }

    public void addLast(char chr) {
        CharData cd = new CharData(chr);
        Node newNode = new Node(cd);
        if (first == null) {
            first = newNode;
        } else {
            Node current = first;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        size++;
    }

    public void update(char chr) {
        Node current = first;
        while (current != null) {
            if (current.cp.chr == chr) {
                current.cp.count++;
                return;
            }
            current = current.next;
        }
        addLast(chr); // חשוב: הוספה לסוף
    }

    public CharData get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        Node current = first;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.cp;
    }

    public int indexOf(char chr) {
        Node current = first;
        int index = 0;
        while (current != null) {
            if (current.cp.chr == chr) {
                return index;
            }
            current = current.next;
            index++;
        }
        return -1;
    }

    public String toString() {
        if (size == 0) return "()";
        StringBuilder str = new StringBuilder("(");
        Node current = first;
        while (current != null) {
            str.append(current.cp.toString());
            if (current.next != null) {
                str.append(" ");
            }
            current = current.next;
        }
        str.append(")");
        return str.toString();
    }
}