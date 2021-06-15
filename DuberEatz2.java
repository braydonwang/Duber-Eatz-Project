import java.io.PrintWriter;
import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.LinkedList; 
import java.util.Queue; 

/*
 * [Level4Plus.java]
 * This program is the solution to the Duber Eatz Assignment for level 4+
 * Authour: Braydon Wang
 * Date: 11/06/2019
*/

public class Level4Plus {
 static int row;
 static int column;
 static Queue<Integer> allCoordinates;
 static int[][] allDistances;
 static char[][] map;
 static ArrayList<String> allPermutations = new ArrayList<String>();
 public static void main(String[] args) throws Exception {
     Scanner sc = new Scanner(System.in);
     
     //Getting the name of the file
     System.out.println("What is the name of the file?");
     String nameOfFile = sc.next();
     File file = new File(nameOfFile);
     Scanner input = new Scanner(file);
     
     //Creating the image/PPM file
     File imageFile = new File(nameOfFile.substring(0,nameOfFile.length()-4) + ".ppm");
     PrintWriter output = new PrintWriter(imageFile);
     output.println("P3");
     
     //Taking the number of rows and columns
     row = input.nextInt();
     column = input.nextInt();
     output.println(row*30 + " " + column*30);
     output.println("255");
     
     //Initializing all variables
     int count = 1;
     map = new char[row][column];
     char[][] originalMap = new char[row][column];
     char[][] newMap = new char[row][column];
     char[][] finalMap = new char[row][column];
     int startX = 0;
     int startY = 0;
     int distance = 0;
     int numberOfDelivery = 0;
     int maximumTip = Integer.MIN_VALUE;
     int[][] preX = new int[row][column];
     int[][] preY = new int[row][column];
     int firstLocation;
     int secondLocation;
     int coordinateX;
     int coordinateY;
     int totalDistance;
     int totalTip;
     
     //Taking data from the file (map)
     input.nextLine();
     String str = input.nextLine();
     for (int i = 0; i < column; i++) {
       map[0][i] = str.charAt(i);
       originalMap[0][i] = str.charAt(i);
       newMap[0][i] = str.charAt(i);
     }
     while (input.hasNextLine()) {
       str = input.nextLine();
       for (int i = 0; i < column; i++) {
         map[count][i] = str.charAt(i);
         originalMap[count][i] = str.charAt(i);
         newMap[count][i] = str.charAt(i);
         //Keeping the coordinate of the starting location in two variables
         if (map[count][i] == 'S') {
           startX = i;
           startY = count;
         } else if (map[count][i] - '0' >= 0 && map[count][i] - '0' <= 9) {
           //Counting the number of delivery locations
           numberOfDelivery++;
         }
       }
       count++;
     }
     
     //Displaying the original map that is inputed
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
     for (int i = 0, counter = 0; i < row; i++) {
      for (int j = 0; j < column; j++) {
       if (map[i][j] - '0' >= 0 && map[i][j] - '0' <= 9) {
        deliveryX[counter] = j;
        deliveryY[counter] = i;
        counter++;
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
       totalDistance = 0;
       totalTip = 0;
       String permute = allPermutations.get(i);
       //Resetting the new map to the original design
       for (int k = 0; k < row; k++) {
         for (int l = 0; l < column; l++) {
           newMap[k][l] = originalMap[k][l];
         }
       }
       //Looping through every pair of locations of each possible path
       for (int j = 0; j < numberOfDelivery; j++) {
         //Resetting the map to the original design
         for (int k = 0; k < row; k++) {
           for (int l = 0; l < column; l++) {
             map[k][l] = originalMap[k][l];
           }
         }
         //Resetting the global distance array and the queue that holds all the reachable coordinates
         allDistances = new int[row][column];
         allCoordinates = new LinkedList<Integer>();
         //If it is the very beginning of the path, use the starting location as the first location
         if (j == 0) { 
           firstLocation = 0;
           secondLocation = Integer.parseInt(permute.substring(0,1));
         } else {
           firstLocation = Integer.parseInt(permute.substring(j-1,j));
           secondLocation = Integer.parseInt(permute.substring(j,j+1));
         }
         //Setting the starting positions and ending positions to include the pair of locations in the permutation and updating the accumulated distance
         if (firstLocation == 0) {
           totalDistance += pathDistance(distance,startX,startY,deliveryX[secondLocation-1],deliveryY[secondLocation-1],preX,preY);
         } else {
           totalDistance += pathDistance(distance,deliveryX[firstLocation-1],deliveryY[firstLocation-1],deliveryX[secondLocation-1],deliveryY[secondLocation-1],preX,preY);
         }
         //Updating the total tip received based on the formula
         if ((originalMap[deliveryY[secondLocation-1]][deliveryX[secondLocation-1]] - '0') - totalDistance > 0) {
             totalTip += ((originalMap[deliveryY[secondLocation-1]][deliveryX[secondLocation-1]] - '0') - totalDistance) * 10;
         } else {
           totalTip += (originalMap[deliveryY[secondLocation-1]][deliveryX[secondLocation-1]] - '0') - totalDistance;
         }
         //Updating the new map to hold the possible path that it can travel
         coordinateX = deliveryX[secondLocation-1];
         coordinateY = deliveryY[secondLocation-1];
         //Starting from the delivery location, backtrack using the global distance array to find the previous place that has the current distance minus one
         newMap[coordinateY][coordinateX] = 'X';
         while (allDistances[coordinateY][coordinateX] != 1) {
           if ((allDistances[coordinateY+1][coordinateX] == allDistances[coordinateY][coordinateX] - 1)) {
             newMap[coordinateY+1][coordinateX] = 'x';
             coordinateY++;
           } else if (allDistances[coordinateY-1][coordinateX] == allDistances[coordinateY][coordinateX] - 1) {
             newMap[coordinateY-1][coordinateX] = 'x';
             coordinateY--;
           } else if (allDistances[coordinateY][coordinateX+1] == allDistances[coordinateY][coordinateX] - 1) {
             newMap[coordinateY][coordinateX+1] = 'x';
             coordinateX++;
           } else {
             newMap[coordinateY][coordinateX-1] = 'x';
             coordinateX--;
           }
         }
       }
       //Updating the maximum tip and the final map if it finds a larger tip amount
       if (totalTip > maximumTip) {
         maximumTip = totalTip;
         for (int j = 0; j < row; j++) {
           for (int k = 0; k < column; k++) {
             finalMap[j][k] = newMap[j][k];
           }
         }
       }
     }
     System.out.println("\nThe maximum amount of tip possible is $" + maximumTip + "!\n");
     
     //Displaying the new map with the shortest path represented with 'x' as well as creating/updating the PPM image file that displays the path
     System.out.println("The new map with the maximum amount of tip looks like this:\n");
     for (int i = 0; i < row; i++) {
       for (int j = 0; j < column; j++) {
         System.out.print(finalMap[i][j]);
       }
       System.out.println();
     }
     
     for (int i = 0; i < row; i++) {
       for (int j = 0; j < 30; j++) {
         for (int k = 0; k < column; k++) {
           if (finalMap[i][k] == '#') {
             for (int l = 0; l < 30; l++) {
               //Black for walls
               output.print("0 0 0 ");
             }
           } else if (finalMap[i][k] == 'x') {
             for (int l = 0; l < 30; l++) {
               //Red for path
               output.print("255 0 0 ");
             }
           } else if (finalMap[i][k] == 'S') {
             for (int l = 0; l < 30; l++) {
               //Green for starting location
               output.print("0 255 0 ");
             }
           } else if (finalMap[i][k] == 'X') {
             for (int l = 0; l < 30; l++) {
               //Blue for delivery location
               output.print("0 0 255 ");
             }
           } else {
             for (int l = 0; l < 30; l++) {
               //White for empty space
               output.print("255 255 255 ");
             }
           }
         }
         output.println();
       }
     }
     //Closing the scanner class, file input and file output
     sc.close();
     input.close();
     output.close();
 }
 public static int pathDistance(int distance, int startX, int startY, int deliveryX, int deliveryY, int[][] preX, int[][] preY) {
   //Placing an 'x' to represent all visited coordinates and updating the global distance array to hold the value of the distance it took to get there
   map[startY][startX] = 'x';
   allDistances[startY][startX] = distance;
   //The base condition that checks if the current location is at the ending location
   if (startX == deliveryX && startY == deliveryY) {
     return allDistances[startY][startX];
   }
   //Checking if there is an empty space to the right of the current location
   if (startX + 1 < column && map[startY][startX+1] != '#' && map[startY][startX+1] != 'x') {
     //Updating the previous array to hold the location that it came from
     preX[startY][startX+1] = startX;
     preY[startY][startX+1] = startY;
     //Adding the location to the queue and marking it as visited
     allCoordinates.add(startX+1); 
     allCoordinates.add(startY); 
     map[startY][startX+1] = 'x';
   } 
   //Checking if there is an empty space below the current location
   if (startY + 1 < row && map[startY+1][startX]  != '#' && map[startY+1][startX] != 'x') {
     preX[startY+1][startX] = startX;
     preY[startY+1][startX] = startY;
     allCoordinates.add(startX); 
     allCoordinates.add(startY+1); 
     map[startY+1][startX] = 'x';
   }
   //Checking if there is an empty space to the left of the current location
   if (startX - 1 >= 0 && map[startY][startX-1] != '#' && map[startY][startX-1] != 'x') {
     preX[startY][startX-1] = startX;
     preY[startY][startX-1] = startY;
     allCoordinates.add(startX-1); 
     allCoordinates.add(startY); 
     map[startY][startX-1] = 'x';
   }
   //Checking if there is an empty space above the current location
   if (startY - 1 >= 0 && map[startY-1][startX] != '#' && map[startY-1][startX] != 'x') {
     preX[startY-1][startX] = startX;
     preY[startY-1][startX] = startY;
     allCoordinates.add(startX); 
     allCoordinates.add(startY-1); 
     map[startY-1][startX] = 'x';
   }
   //Selecting the next location at the front of the queue and recursively following the same process
   int nextX = allCoordinates.poll();
   int nextY = allCoordinates.poll();
   return pathDistance(allDistances[preY[nextY][nextX]][preX[nextY][nextX]]+1,nextX,nextY,deliveryX,deliveryY,preX,preY);
 }
 //The main method that calls the sub method for generating all different permutations
 public static void permutation(String str) {
   permutation2("", str);
 }
 //The sub method that finds all possible permutations through recursion
 private static void permutation2(String prefix, String str) {
   int stringLength = str.length();
   //The base condition that returns the permutation after reaching the end of the string
   if (stringLength == 0) {
     allPermutations.add(prefix);
   } else {
     //For each iteration, swap each pair of terms with each other
     for (int i = 0; i < stringLength; i++) {
       permutation2(prefix + str.charAt(i), str.substring(0, i) + str.substring(i+1, stringLength));
     }
   }
 }
}







