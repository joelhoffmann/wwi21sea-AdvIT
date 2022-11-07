package Testate.Aufgabe3;

public class Testat3_Monitor {

    int[] states = new int[5];
    // 0 -> nix
    // 1 -> will lesen -> eigentlich unnötig
    // 2 -> will schreiben
    // 3 -> liest
    // 4 -> schreibt

    public Testat3_Monitor() {

        for (int i = 0; i < states.length; i++) {
            states[i] = 0;
        }

    }

    public synchronized void startWrite(int id) {
        try {

            states[id] = 2;
            //someone want reading or writing ? -> wait
            while (containsButNotMe(states, 3, id) || containsButNotMe(states, 4, id)) {
                wait();
            }
            //block and start writing
            states[id] = 4;

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public synchronized void endWrite(int id) {
        //release state
        states[id] = 0;
        notifyAll();
    }

    public synchronized void startRead(int id) {
        try {
            states[id] = 1;
            //someone writing or wants to write ? -> wait
            while (containsButNotMe(states, 4, id) || containsButNotMe(states, 2, id)) {
                wait();
            }
            states[id] = 3;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void endRead(int id) {
        //release state
        states[id] = 0;
        notifyAll();
    }

    //helper function
    public boolean containsButNotMe(int[] array, int number, int id) {
        boolean result = false;
        for (int i = 0; i < array.length; i++) {
            if (i != id && array[i] == number) result = true;
        }
        return result;
    }

}
