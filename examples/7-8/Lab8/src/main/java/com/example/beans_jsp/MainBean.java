package com.example.beans_jsp;

import java.util.ArrayList;
import java.util.Collections;
public class MainBean {

    private int countPageRefresh;
    private boolean trigger = false;
    ArrayList<Integer> al;


    public MainBean() {

    }

    public void solve(String args) {
        try {
            String[] newArgs = args.split(" ");
            ArrayList<Integer> al = new ArrayList<>();
            for (String x : newArgs) {
                al.add((Integer.parseInt(x)));
            }
            Collections.sort(al, Collections.reverseOrder());
            setAl(al);
        } catch (Exception ignored) {
            setAl(null);
        }
    }

    public int getCountPageRefresh() {
        return countPageRefresh;
    }

    public void setCountPageRefresh(int countPageRefresh) {
        this.countPageRefresh = countPageRefresh;
    }

    public ArrayList<Integer> getAl() {
        return al;
    }

    public void setAl(ArrayList<Integer> al) {
        this.al = al;
    }

    public boolean isTrigger() {
        return trigger;
    }

    public void setTrigger(boolean trigger) {
        this.trigger = trigger;
    }
}
