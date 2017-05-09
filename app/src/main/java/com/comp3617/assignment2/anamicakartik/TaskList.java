package com.comp3617.assignment2.anamicakartik;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jkart on 2016-06-22.
 */
public class TaskList {

    private static TaskList mInstance ;
    private List<Task> tasks = null;

    public static TaskList getInstance() {

        if(mInstance == null){
            synchronized (TaskList.class){
                mInstance = new TaskList();
            }
        }
        return mInstance;
    }

    public List<Task> getTaskList(){

        return tasks;
    }

    public void editTask(Task eTask,int pos){
        tasks.remove(pos);
        tasks.add(pos,eTask);
    }

    public void addTask(Task pTask){
        tasks.add(pTask);
    }

    public void deleteTask(int pos){
        tasks.remove(pos);
    }

    private TaskList() {
        tasks = new ArrayList<Task>();

        Task task1 = new Task();
        task1.setTitle("ICBC");
        task1.setDescription("Get Address changed");
        task1.setStatus("Pending");
        task1.setCategory("Work");
        task1.setPriority("High");
        task1.setImageResource(R.drawable.work);
        task1.setMyPositionInList(0);
        task1.setAddress(" 4820 Kingsway, Burnaby");

        Task task2 = new Task();
        task2.setTitle("Medicines");
        task2.setDescription("Get medicines");
        task2.setStatus("Pending");
        task2.setCategory("personal");
        task2.setPriority("medium");
        task2.setImageResource(R.drawable.personal);
        task2.setMyPositionInList(1);
        task2.setAddress("7488 Byrnapark walk,burnaby");

        //tk1.setAddressLocation();
        tasks.add(task1);
        tasks.add(task2);
    }
}
