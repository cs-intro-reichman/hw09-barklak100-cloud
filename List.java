/** A linked list of character data objects.
 *  (Actually, a list of Node objects, each holding a reference to a character data object.
 *  However, users of this class are not aware of the Node objects. As far as they are concerned,
 *  the class represents a list of CharData objects. Likwise, the API of the class does not
 *  mention the existence of the Node objects). */
public class List 
{
    // Points to the first node in this list
    private Node first;

    // The number of elements in this list
    private int size;
	
    /** Constructs an empty list. */
    public List() 
    {
        this.first = null;
        this.size = 0;
    }
    
    /** Returns the number of elements in this list. */
    public int getSize() 
    {
 	    return this.size;
    }

    /** Returns the CharData of the first element in this list. */
    public CharData getFirst() 
    {
        // If empty list return null because wont have first.cp if the value is null
        if (this.first == null)
            return null;

        return this.first.cp;
    }

    /** GIVE Adds a CharData object with the given character to the beginning of this list. */
    public void addFirst(char chr) 
    {
        // Creating new chardata elemnt to put in the first node and creating the first node
        CharData newChar = new CharData(chr);
        Node newFirst = new Node(newChar, this.first);

        // Setting all the current values after the newFirst then changing the first value
        this.first = newFirst;
        this.size++; 
    }
    
    /** GIVE Textual representation of this list. */
    public String toString() 
    {
        if (this.size == 0) // If empty list
            return "()";

        String toReturn = "(";
        Node tempHead = this.first;

        // Adding each node value from the list into toReturn then returns it
        while (tempHead != null)
        {
            toReturn += tempHead.toString() + " ";
            tempHead = tempHead.next;
        }
        
        return toReturn.substring(0, toReturn.length() - 1) + ")"; // removing last space and adding )
    }

    /** Returns the index of the first CharData object in this list
     *  that has the same chr value as the given char,
     *  or -1 if there is no such object in this list. */
    public int indexOf(char chr) 
    {
        int index = 0;
        Node newFirst = this.first;

        while (newFirst != null)
        {
            // If found char return current index if not add 1 to index and move to next node
            if (newFirst.cp.chr == chr)  
                return index;

            newFirst = newFirst.next;
            index++;
        }

        return -1;
    }

    /** If the given character exists in one of the CharData objects in this list,
     *  increments its counter. Otherwise, adds a new CharData object with the
     *  given chr to the beginning of this list. */
    public void update(char chr) 
    {
        Node newFirst = this.first;
        while (newFirst != null)
        {
            if (newFirst.cp.chr == chr)
            {
                newFirst.cp.count++;
                return;
            }

            newFirst = newFirst.next;
        }

        // If char doesnt exist in the list add it to the beggining
        addFirst(chr);
    }

    /** GIVE If the given character exists in one of the CharData objects
     *  in this list, removes this CharData object from the list and returns
     *  true. Otherwise, returns false. */
    public boolean remove(char chr) 
    {
        Node newFirst = this.first;

        if (indexOf(chr) == 0) // If need to remove the head
        {
            newFirst = this.first.next;
            this.first = newFirst;
            this.size--;
            return true;
        }

        while (newFirst.next != null)
        {
            if (newFirst.next.cp.chr == chr)
            {
                newFirst.next = newFirst.next.next;
                this.size--;
                return true;
            }
            newFirst = newFirst.next;
        }

        return false;        
    }

    /** Returns the CharData object at the specified index in this list. 
     *  If the index is negative or is greater than the size of this list, 
     *  throws an IndexOutOfBoundsException. */
    public CharData get(int index) 
    {
        if (index < 0 || index >= this.size) // if index biger then size or negative throw exception
            throw new IndexOutOfBoundsException();

        // Creating new pointer and moving to the index of the correct value in the list
        Node newFirst = first;
        for (int i = 0; i < index; i++)
        {
            newFirst = newFirst.next;
        }
        
        return newFirst.cp;
    }

    /** Returns an array of CharData objects, containing all the CharData objects in this list. */
    public CharData[] toArray() 
    {
	    CharData[] arr = new CharData[size];
	    Node current = this.first;
	    int i = 0;
        
        while (current != null) 
        {
    	    arr[i++]  = current.cp;
    	    current = current.next;
        }
        return arr;
    }

    /** Returns an iterator over the elements in this list, starting at the given index. */
    public ListIterator listIterator(int index) 
    {
	    // If the list is empty, there is nothing to iterate   
	    if (size == 0) 
            return null;

	    // Gets the element in position index of this list
	    Node current = this.first;
	    int i = 0;
        while (i < index) 
        {
            current = current.next;
            i++;
        }
        // Returns an iterator that starts in that element
	    return new ListIterator(current);
    }
}