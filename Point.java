import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Point {

    private double x;
    private double y;
    private int cluster;
    private  double distence;
    private boolean visit ;
    private  double threshold;
    private  boolean flag;
    private  double time;
    private  int indexp;
   

    
    public Point(){

    }

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
        this.visit =false;
        this.indexp=-1;
        this.flag =false;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public int getCluster() {
        return cluster;
    }

    public void setCluster(int cluster) {
        this.cluster = cluster;
    }
     
    public double getdistence() {
        return distence;
    }

    public void setdistence(double distence) {
        this.distence = distence;
    }
    public boolean getvisit() {
        return visit;
    }
    public void setvisit(boolean visited) {
        this.visit = visited;
    }

    public int getIndexp() {
        return indexp;
    }

    public void setIndexp(int index) {
        this.indexp = index;
    }
    
    public double getThreshold() {
        return threshold;
    }

    public void setTthreshold(double threshold) {
        this.threshold = threshold;
    }

     public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }
     public boolean getflag() {
        return flag;
    }
    public void setflag(boolean flag) {
        this.flag = flag;
    }
    public String toString(){
        return "X: " +x+" Y: "+y + " cluster: " +cluster+" distence: "+distence+" visit: "+flag+" second indexlloo: "+indexp+" time: "+time ;
    }

}
