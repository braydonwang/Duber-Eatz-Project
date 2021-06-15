package DMOJ;
import java.util.Scanner;
import java.io.File;
import java.util.*;
public class DuberEatz {
	static int row;
	static int column;
	static Queue<Integer> q;
	static int[][] dis;
	static char[][] map;
	static ArrayList<String> allPermutations = new ArrayList<String>();
	public static void main(String[] args) throws Exception {
	    Scanner sc = new Scanner(System.in);
	    
	    /*//Getting the name of the file
	    System.out.println("What is the name of the file?");
	    String nameOfFile = sc.next();
	    File file = new File(nameOfFile);
	    Scanner input = new Scanner(file);
	    
	    //Taking the number of rows and columns
	    int row = input.nextInt();
	    int column = input.nextInt();
	    int count = 1;
	    char[][] map = new char[row][column];
	    input.nextLine();
	    String str = input.nextLine();
	    for (int i = 0; i < column; i++){
	      map[0][i] = str.charAt(i);
	    }
	    while (input.hasNextLine()){
	      str = input.nextLine();
	      for (int i = 0; i < column; i++){
	        map[count][i] = str.charAt(i);
	      }
	      count++;
	    }*/
	    
	    row = sc.nextInt();
	    column = sc.nextInt();
	    int startX = 0;
	    int startY = 0;
	    int distance = 0;
	    int numberOfDelivery = 0;
	    int minimumDistance = Integer.MAX_VALUE;
	    int[][] preX = new int[row][column];
	    int[][] preY = new int[row][column];
	    map = new char[row][column];
	    char[][] originalMap = new char[row][column];
	    char[][] newMap = new char[row][column];
	    char[][] coolMap = new char[row][column];
	    sc.nextLine();
	    for (int i = 0; i < row; i++){
	    	String str = sc.nextLine();
		    for (int j = 0; j < column; j++){
		    	map[i][j] = str.charAt(j);
		    	originalMap[i][j] = str.charAt(j);
		    	newMap[i][j] = str.charAt(j);
		    	if (map[i][j] == 'S') {
		    		startX = j;
		    		startY = i;
		    	} else if (map[i][j] - '0' >= 0 && map[i][j] - '0' <= 9) {
		    		numberOfDelivery++;  //Counting the number of delivery locations
		    	}
		    }
		}
	    
	    //Displaying the original map that is inputted
	    System.out.println("The original map looks like this:\n");
	    for (int i = 0; i < row; i++) {
	    	for (int j = 0; j < column; j++) {
	    		System.out.print(map[i][j]);
	    	}
	    	System.out.println();
	    }
	    
	    //Keeping the coordinates of every delivery location in an array
	    int[] deliveryX = new int[numberOfDelivery];
	    int[] deliveryY = new int[numberOfDelivery];
	    for (int i = 0, count = 0; i < row; i++) {
	    	for (int j = 0; j < column; j++) {
	    		if (map[i][j] - '0' >= 0 && map[i][j] - '0' <= 9) {
	    			deliveryX[count] = j;
	    			deliveryY[count] = i;
	    			count++;
	    		}
	    	}
	    }
	    
	    //To figure out all the different types of possible permutations with the given number of delivery locations
	    String temporary = "";
	    for (int i = 1; i <= numberOfDelivery; i++) {
	    	temporary += Integer.toString(i);  //Assigning every delivery location a number to easily identify each one (the starting location will be assigned the number 0)
	    }
	    permutation(temporary);
	    
	    //Looping for every possible permutation to find the shortest path
	    for (int i = 0; i < allPermutations.size(); i++) {
	    	int totalDistance = 0;
	    	String permute = allPermutations.get(i);
	    	//Resetting the new map to the original design
    		for (int k = 0; k < row; k++) {
    			for (int l = 0; l < column; l++) {
    				newMap[k][l] = originalMap[k][l];
    			}
    		}
    	       //Looping through every pair of locations of each possible path
    	       for (int j = 0; j < numberOfDelivery; j++) {
    	    	 dis = new int[row][column];
    	    	 q = new LinkedList<Integer>();
    	         //Resetting the map to the original design
    	         for (int k = 0; k < row; k++) {
    	           for (int l = 0; l < column; l++) {
    	             map[k][l] = originalMap[k][l];
    	           }
    	         }
    	         int firstLocation;
    	         int secondLocation;
    	         if (j == 0) {  //If it is the very beginning of the path, use the starting location as the first location
    	           firstLocation = 0;
    	           secondLocation = Integer.parseInt(permute.substring(0,1));
    	         } else {
    	           firstLocation = Integer.parseInt(permute.substring(j-1,j));
    	           secondLocation = Integer.parseInt(permute.substring(j,j+1));
    	         }
    	         //Setting the starting positions and ending positions to include the pair of locations in the permutation
    	         if (firstLocation == 0) {
    	           totalDistance += pathDistance(distance,startX,startY,deliveryX[secondLocation-1],deliveryY[secondLocation-1],preX,preY);
    	         } else {
    	           totalDistance += pathDistance(distance,deliveryX[firstLocation-1],deliveryY[firstLocation-1],deliveryX[secondLocation-1],deliveryY[secondLocation-1],preX,preY);
    	         }
    	         //Updating the new map to hold the possible path that it can travel
    	         int xx = deliveryX[secondLocation-1];
    	         int yy = deliveryY[secondLocation-1];
    	         newMap[yy][xx] = 'X';
    	         while (dis[yy][xx] != 1) {
    	        	 if (dis[yy+1][xx] == dis[yy][xx] - 1) {
    	        		 newMap[yy+1][xx] = 'x';
    	        		 yy++;
    	        	 } else if (dis[yy-1][xx] == dis[yy][xx] - 1) {
    	        		 newMap[yy-1][xx] = 'x';
    	        		 yy--;
    	        	 } else if (dis[yy][xx+1] == dis[yy][xx] - 1) {
    	        		 newMap[yy][xx+1] = 'x';
    	        		 xx++;
    	        	 } else {
    	        		 newMap[yy][xx-1] = 'x';
    	        		 xx--;
    	        	 }
    	         }
    	       }
    	       //Updating the minimum distance if it finds a shorter path
    	       if (totalDistance < minimumDistance) {
    	    	   minimumDistance = totalDistance;
    	    	   for (int j = 0; j < row; j++) {
    	    		   for (int k = 0; k < column; k++) {
    	    			   coolMap[j][k] = newMap[j][k];
    	    		   }
    	    	   }
    	       }
    	     }
    	     System.out.println("\nThe minimum distance that passes every delivery location is " + minimumDistance + " steps!\n");
    	     
    	     //Displaying the new map with the shortest path represented with 'x'
    	     System.out.println("The new map with the shortest possible path looks like this:\n");
    	     for (int i = 0; i < row; i++){
    	       for (int k = 0; k < 2; k++){
    	         //System.out.print(coolMap[i][j]);
    	    	 for (int j = 0; j < column; j++) {
    	    		 if (coolMap[i][j] == '#'){
    	    			 for (int l = 0; l < 2; l++) {
    	    				 System.out.print("0 0 0 ");
    	    			 }
    	    		 } else if (coolMap[i][j] == 'x'){
    	    			 for (int l = 0; l < 2; l++) {
    	    				 System.out.print("255 0 0 ");
    	    			 }
        	         } else if (coolMap[i][j] == 'S'){
        	        	 for (int l = 0; l < 2; l++) {
        	        		 System.out.print("0 255 0 ");
        	        	 }
        	         } else if (coolMap[i][j] == 'X'){
        	        	 for (int l = 0; l < 2; l++) {
        	        		 System.out.print("0 0 255 ");
        	        	 }
        	         } else{
        	        	 for (int l = 0; l < 2; l++) {
        	        		 System.out.print("255 255 255 ");
        	        	 }
        	         }
    	    	 }
    	    	 System.out.println();
    	       }
    	     }
    	     sc.close();
    	 }
	public static int pathDistance(int distance, int startX, int startY, int deliveryX, int deliveryY, int[][] preX, int[][] preY) {
		//Updating the map to contain the path that it will travel
		map[startY][startX] = 'x';
		dis[startY][startX] = distance;
		//The base condition that checks if the current location is at the ending location
		if (startX == deliveryX && startY == deliveryY) {
			return dis[startY][startX];
		}
		//
		if (startX + 1 < column && map[startY][startX+1] != '#' && map[startY][startX+1] != 'x') {
			preX[startY][startX+1] = startX;
			preY[startY][startX+1] = startY;
			q.add(startX+1); q.add(startY); map[startY][startX+1] = 'x';
		} 
		if (startY + 1 < row && map[startY+1][startX] != '#' && map[startY+1][startX] != 'x') {
			preX[startY+1][startX] = startX;
			preY[startY+1][startX] = startY;
			q.add(startX); q.add(startY+1); map[startY+1][startX] = 'x';
		}
		if (startX - 1 >= 0 && map[startY][startX-1] != '#' && map[startY][startX-1] != 'x') {
			preX[startY][startX-1] = startX;
			preY[startY][startX-1] = startY;
			q.add(startX-1); q.add(startY); map[startY][startX-1] = 'x';
		}
		if (startY - 1 >= 0 && map[startY-1][startX] != '#' && map[startY-1][startX] != 'x') {
			preX[startY-1][startX] = startX;
			preY[startY-1][startX] = startY;
			q.add(startX); q.add(startY-1); map[startY-1][startX] = 'x';
		}
		int x = q.poll();
		int y = q.poll();
		return pathDistance(dis[preY[y][x]][preX[y][x]]+1,x,y,deliveryX,deliveryY,preX,preY);
	}
	public static void permutation(String str) {
		permutation2("", str);
	}
	private static void permutation2(String prefix, String str) {
		int n = str.length();
		if (n == 0) {
			allPermutations.add(prefix);
		} else {
			for (int i = 0; i < n; i++) {
				permutation2(prefix + str.charAt(i), str.substring(0, i) + str.substring(i+1, n));
			}
		}
	}
}
