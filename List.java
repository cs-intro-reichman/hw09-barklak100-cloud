/** A linked list of CharData objects. */
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

    public CharData getFirst() {
        if (first == null) return null;
        return first.cp;
    }

    public void addFirst(char chr) {
        CharData cd = new CharData(chr);
        first = new Node(cd, first);
        size++;
    }

    public CharData get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        Node current = first;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.cp; // שם השדה הוא cp לפי הארכיטקטורה [cite: 46-51]
    }

    public int indexOf(char chr) {
        Node current = first;
        int index = 0;
        while (current != null) {
            if (current.cp.chr == chr) return index;
            current = current.next;
            index++;
        }
        return -1;
    }

 public void update(char chr) {
    Node current = first; 
    boolean found = false;

    while (current != null) {
        if (current.cp.chr == chr) { 
            current.cp.count++; 
            found = true;
            return; 
        }
        current = current.next;
    }

    if (!found) {
        addFirst(chr); 
    }
}

    public boolean remove(char chr) {
        Node prev = null;
        Node current = first;
        while (current != null) {
            if (current.cp.chr == chr) {
                if (prev == null) {
                    first = first.next;
                } else {
                    prev.next = current.next;
                }
                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }
        return false;
    }

    public String toString() {
        if (size == 0) return "()";
        StringBuilder sb = new StringBuilder("(");
        Node current = first;
        while (current != null) {
            sb.append(current.cp.toString()); 
            if (current.next != null) sb.append(" ");
            current = current.next;
        }
        sb.append(")");
        return sb.toString();
    }
}