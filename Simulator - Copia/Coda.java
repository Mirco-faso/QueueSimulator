public class Coda
{ public Coda()
{ v = new Job[INITSIZE];
contatore=0;
makeEmpty();
}
public void makeEmpty()
{ front = back = 0;
contatore=0;
}
public boolean isEmpty()
{ return (back == front);
}
public void enqueue(Job obj)
{ contatore++;
    if (increment(back) == front) //back e front distano 1
{ v = resize(2*v.length);
//se si ridimensiona v e la zona utile della coda e`
//attorno alla sua fine (cioe` back < front) la seconda
//meta` del nuovo array rimane vuota e provoca un
//malfunzionamento della coda, che si risolve spostando
//la parte di coda che si trova all’inizio dell’array
if (back < front)
{ System.arraycopy(v, 0, v, v.length/2, back);
back += v.length/2;
}
}
v[back] = obj;
back = increment(back);
}
public Job getFront()
{ if (isEmpty()) return null;
return v[front];
}
public Job dequeue() 
{ contatore--;
    Job obj = getFront();
front = increment(front);
return obj;
}
public int size(){
    return contatore;
}
protected Job[] resize(int newLength) //solita tecnica
{ if (newLength < v.length)
throw new IllegalArgumentException();
Job[] newv = new Job[newLength];
System.arraycopy(v, 0, newv, 0, v.length);
return newv;
}
// il metodo increment fa avanzare un indice di una
// posizione, tornando all’inizio se si supera la fine.
// Attenzione: non aggiorna direttamente i campi front,back
protected int increment(int index)
{ return (index + 1) % v.length;
}

//campi di esemplare e variabili statiche
protected Job[] v;
private int contatore;
protected int front, back;
public static final int INITSIZE = 100;
}


