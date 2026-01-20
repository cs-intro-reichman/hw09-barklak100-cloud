/** A linked list of CharData objects. */
public class List {
    private Node first; // Pointer to the first node
    private int size;   // Number of elements

    /** Constructs an empty list. */
    public List() {
        first = null;
        size = 0;
    }

    /** Returns the number of elements in this list. */
    public int getSize() {
        return size;
    }

    /** Returns the CharData object at the first node. */
    public CharData getFirst() {
        if (first == null) return null;
        return first.cp;
    }

    /** Adds a CharData object with the given character to the beginning of this list. */
    public void addFirst(char chr) {
        CharData cd = new CharData(chr);
        first = new Node(cd, first);
        size++;
    }

    /** Returns the CharData object at the specified index. */
    public CharData get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        Node current = first;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.cp; // Field name is 'cp' based on architecture
    }

    /** Returns the index of the first node containing the given character. */
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

    /** Updates the counter if character exists, or adds to beginning if not. */
    public void update(char chr) {
        int index = indexOf(chr);
        if (index != -1) {
            get(index).count++; // Increment count
        } else {
            addFirst(chr); // Use addFirst for new characters
        }
    }

    /** Removes the node with the given character. */
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

    /** Returns a string representation of the list. */
    public String toString() {
        if (size == 0) return "()";
        StringBuilder sb = new StringBuilder("(");
        Node current = first;
        while (current != null) {
            sb.append(current.cp.toString()); // Uses CharData's toString
            if (current.next != null) sb.append(" ");
            current = current.next;
        }
        sb.append(")");
        return sb.toString();
    }

    /** Returns an iterator over the list elements. */
    public ListIterator listIterator(int index) {
        Node current = first;
        for (int i = 0; i < index && current != null; i++) {
            current = current.next;
        }
        return new ListIterator(current);
    }
}