package kmeans;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import edu.rit.pj2.Loop;
import edu.rit.pj2.Task;
import edu.rit.pj2.Schedule;


public class KMeans extends Task {
    public static double Speed=80;
public static double DistanceOfDronse=150;
public static boolean cc;
public static double totalX = 0;
public static double totalY = 0;
public static double minDistance;
public static int minIndex = 0;
public static ArrayList<Integer>[] RemoveCluster;
public static int z=0;


public static ArrayList<Integer>[] finalClster;
      
	public void main(String args[]) throws IOException {
		Scanner sc = new Scanner(System.in);
		//String filePath = "";
		//System.out.print("Enter the name of the CSV file: ");
		//String fileName = sc.nextLine();
		
		//int NumOfPoints = getPoints(filePath, fileName);// *Open the file just to count the number of Records
                int NumOfPoints = getPoints();// *Open the file just to count the number of Records
		
                int X = 0;
		int Y = 1;
      //double speeed;
      ArrayList<Point> dataPoint = new ArrayList<Point>();
      //storePoints(filePath, fileName, dataPoint, X, Y);// *Open file again to store the Records//done  
      storePoints(dataPoint, X, Y);// *Open file again to store the Records//done
      

		int maxIterations = 100;//done
        
		System.out.print("Enter the number of drones to form: ");// *Input number of clusters
		int clusters = sc.nextInt();
                                     
		// *Calculate initial centroid_
	   double[][] centroid = new double[clusters][2];
      
      
      
      finalClster = new ArrayList[clusters];
		for(int i=0; i<clusters; i++) {// intilaize 
			finalClster[i] = new ArrayList<Integer>();
		}
//schedule(leapfrog).
     parallelFor (0,centroid.length-1).schedule(leapfrog).exec( new Loop(){//
     //for(int i=0;i<centroid.length;i++){//PARALLEL******
     public void run(int i) throws Exception {
         int index=(int) (Math.floor((NumOfPoints*1.0/clusters)/2) + i*NumOfPoints/clusters); 
			centroid[i][0] = dataPoint.get(index).getX();
			centroid[i][1] = dataPoint.get(index).getY();
      }});
      
		// *Create array to save the clusters
		ArrayList<Integer>[] oldClusters = new ArrayList[clusters];//******** */
		ArrayList<Integer>[] newClusters = new ArrayList[clusters];//********** */

		for(int i=0; i<clusters; i++) {// intilaize //parallel or not ?!
			oldClusters[i] = new ArrayList<Integer>();
			newClusters[i] = new ArrayList<Integer>();
		}
      
		distanceFormClusters(oldClusters, centroid, dataPoint);// *Make the initial clusters
      int iterations = 0;

		// *Run---
         while(true) {
			updateMeans(oldClusters, centroid, dataPoint);
			distanceFormClusters(newClusters, centroid, dataPoint);

			iterations++;

			if(iterations > maxIterations || clustersEquality(oldClusters, newClusters))
				break;
			else
				resetClusters(oldClusters, newClusters);
		}//end while
      
      for(int i=0;i<centroid.length;i++){//adding the centroid in dataPointList
       Point Centroid_Point = new Point(centroid[i][0],centroid[i][1]);
       dataPoint.add(Centroid_Point);
      }    
      
      int[] arrayOfCentroid = new int [clusters];

      for(int x=0;x<centroid.length;x++){
         for(int s=0;s<dataPoint.size();s++){
            if(dataPoint.get(s).getX()== centroid[x][0]&& dataPoint.get(s).getY()== centroid[x][1]){
                 oldClusters[x].add(s);   
                 arrayOfCentroid[x]=s;          
            }
         }
      }                    
    // computeDistance( oldClusters,arrayOfCentroid, dataPoint);
     //computeThreshold(oldClusters, speeed,dataPoint);
     
     System.out.println("shortest path");//print the indexP for check
     for(int i=0; i<oldClusters.length;i++){
     System.out.print("indexp for cluster"+i+":");
     for(  int index2: oldClusters[i]) {
           System.out.print("**"+dataPoint.get(index2).getIndexp());
       }System.out.println();
     }
     // computeTime(oldClusters,dataPoint);
      
      // *the output
		System.out.println("\nThe final clusters are:");
		displayOutput(oldClusters, centroid, dataPoint);
		System.out.println("\nIterations taken = " + iterations);
     
 //    
    
		sc.close();
	}//End Main
//___________________________________________________________
   static void storePoints(ArrayList<Point> dataPoints, int X, int Y) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("NewData.csv"));
		String line;
		int i = 0;
		while ((line = br.readLine()) != null) {
                String[] values = line.split(",");

               // if(!values[0].equals("X") && !values[1].equals("Y")){
                    double x = Double.parseDouble(values[0]);
                    double y = Double.parseDouble(values[1]);

                    Point p = new Point(x,y);
                    dataPoints.add(p);
                //}
            }
        //terminate(1);
		br.close();

	}
       // terminate(1);
//___________________________________________________________
   //static int getPoints(String filePath, String fileName) throws IOException {   
    static int getPoints() throws IOException {
		int NumOfPoints = 0;
		BufferedReader br = new BufferedReader(new FileReader("NewData.csv"));
		while (br.readLine() != null)
			NumOfPoints++;
		br.close();
		return NumOfPoints;
    }  
//___________________________________________________________
	public void updateMeans(ArrayList<Integer>[] clusterList, double[][] centroid, ArrayList<Point> dataPoints) throws IOException{//done
	 //double totalX = 0;
	 //double totalY = 0;
		parallelFor (0,clusterList.length).schedule(leapfrog).exec(new Loop(){//
               // for(int i=0; i<clusterList.length; i++) {
      public void run(int i)throws Exception{
			totalX = 0;
			totalY = 0;
			for(int index: clusterList[i]) { 
				totalX += dataPoints.get(index).getX();
				totalY += dataPoints.get(index).getY();
			}
			centroid[i][0] = totalX/clusterList[i].size(); 
			centroid[i][1] = totalY/clusterList[i].size();
      }});
   }
//___________________________________________________________
	public void distanceFormClusters(ArrayList<Integer>[] clusterList,double[][] centroid,ArrayList<Point> dataPoints ) throws IOException {//Done
		double distance[] = new double[centroid.length];
	 //double minDistance;
		//int minIndex = 0;
	   parallelFor (0,dataPoints.size()).schedule(leapfrog).exec(new Loop(){//
          //for(int i=0;i<dataPoints.size();i++){
      public void run(int i)throws Exception{
         //double distance[] = new double[centroid.length];
			 minDistance = Double.MAX_VALUE; 
		  // int minIndex = 0;//erorr
			parallelFor (0,centroid.length).schedule(leapfrog).exec(new Loop(){// 
                       // for(int j=0; j<centroid.length; j++) {
         public void run(int j)throws Exception{
             		  // int minIndex = 0;//error
				distance[j] = Math.sqrt(Math.pow((dataPoints.get(i).getX() - centroid[j][0]), 2) + Math.pow((dataPoints.get(i).getY() - centroid[j][1]), 2));
				if(distance[j] < minDistance) {
					minDistance = distance[j];
					minIndex = j;
				}
			}});
         dataPoints.get(i).setCluster(minIndex);
			clusterList[minIndex].add(i);
		}});
   }
   //___________________________________
     public void computeShortPath (ArrayList<Integer>[] clusterList,int[] centroid,ArrayList<Integer>[] finalCluster,ArrayList<Point> dataPoints){
      
      double distance[] = new double[centroid.length+1];
      parallelFor(0,centroid.length).schedule(leapfrog).exec(new Loop(){
      public void run(int x)
      {
         finalCluster[x].add(centroid[x]);
         clusterList[x].remove(clusterList[x].indexOf(centroid[x]));
      }
      });
        
      try {
         parallelFor(0,clusterList.length).schedule(leapfrog).exec(new Loop(){
          public void run(int i){
            for(; z<=clusterList[i].size(); z++) {
               minDistance=Double.MAX_VALUE;
                  for(int index2: clusterList[i]) {  
                     dataPoints.get(index2).setdistence(Math.sqrt(Math.pow((dataPoints.get(index2).getX() - dataPoints.get(finalCluster[i].get(finalCluster[i].size()-1)).getX()), 2) + Math.pow((dataPoints.get(index2).getY() - dataPoints.get(finalCluster[i].get(finalCluster[i].size()-1)).getY()), 2)));
                     if((dataPoints.get(index2).getdistence() < minDistance )){  
                        minDistance = dataPoints.get(index2).getdistence();
                        minIndex = index2;
                        distance[i]=minIndex;
                     }
                  }
               finalCluster[i].add(minIndex);
               clusterList[i].remove(clusterList[i].indexOf(minIndex));
              // z=0;
           }}
        });
      } catch (IndexOutOfBoundsException e) {
               e.printStackTrace();
               System.out.println("Invalid indexes or empty string");
             
}
}//end method computeShortPath

//______________________________________________________
	public boolean clustersEquality(ArrayList<Integer>[] oldClusters, ArrayList<Integer>[] newClusters) throws IOException {
		parallelFor (0,oldClusters.length).schedule(leapfrog).exec(new Loop(){//
               // for(int i=0; i<oldClusters.length; i++) {
    public void run(int i)throws Exception{
      
          // Check only lengths first
			if(oldClusters[i].size() == newClusters[i].size()){
				//cc= false;
                       
			// Check individual values if lengths are equal
			for(int j=0; j<oldClusters[i].size(); j++){
				if(oldClusters[i].get(j) == newClusters[i].get(j))
					cc= true;}}
		
                else{
		cc= false;}}});
                return cc;
	}
//___________________________________________________________
	public void resetClusters(ArrayList<Integer>[] oldClusters, ArrayList<Integer>[] newClusters) throws IOException {
      parallelFor (0,newClusters.length).schedule(leapfrog).exec(new Loop(){//
      //for(int i=0; i<newClusters.length; i++) {
      public void run(int i)throws Exception{
			// Copy newClusters to oldClusters
			oldClusters[i].clear();
			for(int index: newClusters[i])
				oldClusters[i].add(index);
			// Clear newClusters
			newClusters[i].clear();
		}});
      
   }
//___________________________________________________________
   static void displayOutput(ArrayList<Integer>[] clusterList, double[][] centroid, ArrayList<Point> dataPoints) {
   int i=0;
   int j=0;
   int x=0;
    LOOP: for(; j<centroid.length; j++)
    {
      if(x==3){
         try{ // thread to sleep for 1000 milliseconds 
         Thread.sleep(1000);} 
      
         catch (Exception e){ 
         System.out.println(e);}
         x=0;
      }
      
       System.out.print("centroid"+i+": (" + centroid[j][0] + "," + centroid[j][1] + ") >> ");
             
       for(;i<clusterList.length;) 
       {
         System.out.print("[");
			for(int index: clusterList[i])
         	System.out.print("("+dataPoints.get(index).getX()+","+dataPoints.get(index).getY()+ ")");
         System.out.print("]\n");
         i++;
         x++;
         continue LOOP;
            }
             
    }
	}
//___________________________________________________________
}//end class


