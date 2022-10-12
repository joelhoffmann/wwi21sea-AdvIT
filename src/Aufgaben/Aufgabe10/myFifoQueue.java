package Aufgaben.Aufgabe10;

import groovy.util.Eval;

import java.util.concurrent.Semaphore;

public class myFifoQueue {

    public Node knoten = null;
    public static Semaphore mutex = new Semaphore(1, true);

    public void add(String input){

        while (true){
            try {
                mutex.acquire();

                if(knoten == null){
                    knoten = new Node(input);
                    return;
                }

                Node localNode = knoten;

                while (localNode.getNextString() != null) localNode = localNode.getNextString();

                localNode.setNextString(new Node(input));

                mutex.release();
                return;

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String get(){

        while (true) {

            try {
                mutex.acquire();

                if (knoten == null) return null;

                String value = knoten.getCurrentString();

                Node localKnoten = knoten.getNextString();

                knoten = localKnoten;

                mutex.release();
                return value;

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void print (){

        System.out.println("Queue FIFO: ");

        Node localNode = knoten;

        while (localNode.getNextString() != null) {

            System.out.print(localNode.getCurrentString() + ", ");

            localNode = localNode.getNextString();
        }
        System.out.print(localNode.getCurrentString());
    }

    public class Node {

        private String currentString;
        private Node nextString;

        public Node (String currentString){
            this.currentString = currentString;
            nextString = null;
        }

        public String getCurrentString (){
            return this.currentString;
        }

        public Node getNextString(){
            return this.nextString;
        }

        public void setNextString(Node input){
            this.nextString = input;
        }

    }


    public static void main(String[] args) {
        myFifoQueue queue = new myFifoQueue();
        queue.add("Hallo1");
        queue.add("Hallo1");
        queue.add("Hallo1");
        queue.add("Hallo1");
        queue.add("Hallo1");

        Runnable r = new Runnable() {
            @Override
            public void run() {
                queue.get();
                queue.add("Hallo2");
            }
        };

        for (int i = 0; i < 2; i++){
            new Thread(r).start();
        }

        queue.print();


    }

}
