package Aufgaben.Aufgabe12;

import javax.management.monitor.Monitor;

public class Aufgabe12_monitor{

    public int[] forks = new int[5];

    public Aufgabe12_monitor(){
        for(int i = 0; i< 5; i++){
            forks[i] = 0;
        }
    }

    public synchronized void eat (int id){
        //wenn gablen nicht frei
        try {
            while(forks[id] != 0 || forks[left_id(id)] != 0){
                wait();
            }
            forks[id] = 1;
            forks[left_id(id)] = 1;
            System.out.println(id + " isst jetzt");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public synchronized void finishedEat (int id){
        forks[id] = 0;
        forks[left_id(id)] = 0;
        System.out.println(id + " isst nicht mehr");
        notifyAll();
    }

    public int left_id(int id){
        return (id - 1 + 5 ) % 5;
    }

}
