import java.io.PrintWriter;
import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.LinkedList; 
import java.util.Queue; 
import java.util.Arrays;

/*
 * [DuberEatz3.java]
 * This program is the solution to the Duber Eatz Assignment for level 4++
 * Authour: Braydon Wang
 * Date: 11/06/2019
*/

public class DuberEatz3 {
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
     char[][] adjustedMap = new char[row][column];
     char[][] newMap = new char[row][column];
     char[][] finalMap = new char[row][column];
     char[][] firstToSecondMap = new char[row][column];
     int startX = 0;
     int startY = 0;
     int distance = 0;
     int numberOfDelivery = 0;
     int numberOfMicrowaves = 0;
     int numberOfOriginalMicrowaves = 0;
     int maximumTip = Integer.MIN_VALUE;
     int[][] preX = new int[row][column];
     int[][] preY = new int[row][column];
     int firstLocation;
     int secondLocation;
     int coordinateX;
     int coordinateY;
     int totalDistance;
     int totalTip;
     int microwaveToSecond;
     int distanceFirstToSecond;
     int closeMicrowaveX = 0;
     int closeMicrowaveY = 0;
     char[][] microwavePath = new char[row][column];
     
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
         } else if (map[count][i] == 'M'){
           //Counting the number of microwaves
           numberOfMicrowaves++;
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
     
     //Keeping the coordinates of every delivery and microwave location in an array
     int[] deliveryX = new int[numberOfDelivery];
     int[] deliveryY = new int[numberOfDelivery];
     int[] microwaveX = new int[numberOfMicrowaves];
     int[] microwaveY = new int[numberOfMicrowaves];
     int[] microwaveDistances = new int[numberOfMicrowaves];
     numberOfOriginalMicrowaves = numberOfMicrowaves;
     for (int i = 0, counter = 0, counter2 = 0; i < row; i++) {
      for (int j = 0; j < column; j++) {
       if (map[i][j] - '0' >= 0 && map[i][j] - '0' <= 9) {
        deliveryX[counter] = j;
        deliveryY[counter] = i;
        counter++;
       } else if (map[i][j] == 'M'){
         microwaveX[counter2] = j;
         microwaveY[counter2] = i;
         counter2++;
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
       //Resetting the new map and the adjusted map to the original design
       for (int k = 0; k < row; k++) {
         for (int l = 0; l < column; l++) {
           newMap[k][l] = originalMap[k][l];
           adjustedMap[k][l] = originalMap[k][l];
         }
       }
       //Resetting the number of original microwaves from the original map
       numberOfMicrowaves = numberOfOriginalMicrowaves;
       //Looping through every pair of locations of each possible path
       for (int j = 0; j < numberOfDelivery; j++) {
         //Resetting the map to the original design
         for (int k = 0; k < row; k++) {
           for (int l = 0; l < column; l++) {
             map[k][l] = adjustedMap[k][l];
           }
         }
         //Resetting the global distance array, the queue that holds all the reachable coordinates, the microwave distance array and the microwave path map
         allDistances = new int[row][column];
         allCoordinates = new LinkedList<Integer>();
         microwaveDistances = new int[numberOfMicrowaves];
         microwavePath = new char[row][column];
         //If it is the very beginning of the path, use the starting location as the first location
         if (j == 0) { 
           firstLocation = 0;
           secondLocation = Integer.parseInt(permute.substring(0,1));
         } else {
           firstLocation = Integer.parseInt(permute.substring(j-1,j));
           secondLocation = Integer.parseInt(permute.substring(j,j+1));
         }
         //Setting the starting positions and ending positions to include the pair of locations in the permutation
         if (firstLocation == 0) {
           pathDistance(distance,startX,startY,deliveryX[secondLocation-1],deliveryY[secondLocation-1],preX,preY);
         } else {
           pathDistance(distance,deliveryX[firstLocation-1],deliveryY[firstLocation-1],deliveryX[secondLocation-1],deliveryY[secondLocation-1],preX,preY);
         }
         //Adding the distance from the first location to the second location to the accumulated distance
         totalDistance += allDistances[deliveryY[secondLocation-1]][deliveryX[secondLocation-1]];
         distanceFirstToSecond = allDistances[deliveryY[secondLocation-1]][deliveryX[secondLocation-1]];
         //Store the distances to every microwave in the map from the first location, including the distance it took to get to the first location
         for (int k = 0; k < numberOfMicrowaves; k++){
           microwaveDistances[k] = allDistances[microwaveY[k]][microwaveX[k]] + (totalDistance - distanceFirstToSecond);
         }
         //Find the closest nearby microwave by sorting it
         Arrays.sort(microwaveDistances);
         for (int k = 0; k < numberOfMicrowaves; k++){
           if (allDistances[microwaveY[k]][microwaveX[k]] + (totalDistance - distanceFirstToSecond) == microwaveDistances[0]){
             closeMicrowaveX = microwaveX[k];
             closeMicrowaveY = microwaveY[k];
           }
         }
         coordinateX = closeMicrowaveX;
         coordinateY = closeMicrowaveY;
         //Storing the path from the first location to the microwave in an array
         microwavePath[coordinateY][coordinateX] = 'x';
         while (allDistances[coordinateY][coordinateX] != 1) {
           if ((allDistances[coordinateY+1][coordinateX] == allDistances[coordinateY][coordinateX] - 1)) {
             microwavePath[coordinateY+1][coordinateX] = 'x';
             coordinateY++;
           } else if (allDistances[coordinateY-1][coordinateX] == allDistances[coordinateY][coordinateX] - 1) {
             microwavePath[coordinateY-1][coordinateX] = 'x';
             coordinateY--;
           } else if (allDistances[coordinateY][coordinateX+1] == allDistances[coordinateY][coordinateX] - 1) {
             microwavePath[coordinateY][coordinateX+1] = 'x';
             coordinateX++;
           } else {
             microwavePath[coordinateY][coordinateX-1] = 'x';
             coordinateX--;
           }
         }
         //Storing the path from the first location to the second location in an array
         coordinateX = deliveryX[secondLocation-1];
         coordinateY = deliveryY[secondLocation-1];
         //Starting from the delivery location, backtrack using the global distance array to find the previous place that has the current distance minus one
         firstToSecondMap = new char[row][column];
         firstToSecondMap[coordinateY][coordinateX] = 'X';
         while (allDistances[coordinateY][coordinateX] != 1) {
           if ((allDistances[coordinateY+1][coordinateX] == allDistances[coordinateY][coordinateX] - 1)) {
             firstToSecondMap[coordinateY+1][coordinateX] = 'x';
             coordinateY++;
           } else if (allDistances[coordinateY-1][coordinateX] == allDistances[coordinateY][coordinateX] - 1) {
             firstToSecondMap[coordinateY-1][coordinateX] = 'x';
             coordinateY--;
           } else if (allDistances[coordinateY][coordinateX+1] == allDistances[coordinateY][coordinateX] - 1) {
             firstToSecondMap[coordinateY][coordinateX+1] = 'x';
             coordinateX++;
           } else {
             firstToSecondMap[coordinateY][coordinateX-1] = 'x';
             coordinateX--;
           }
         }
         //Finding the distance from the closest microwave to the second location in order to see if it is worth going to
         allDistances = new int[row][column];
         for (int k = 0; k < row; k++) {
           for (int l = 0; l < column; l++) {
             map[k][l] = originalMap[k][l];
           }
         }
         pathDistance(distance,closeMicrowaveX,closeMicrowaveY,deliveryX[secondLocation-1],deliveryY[secondLocation-1],preX,preY);
         microwaveToSecond = allDistances[deliveryY[secondLocation-1]][deliveryX[secondLocation-1]];
         //Calculating whether or not the microwave should be used
         if (microwaveDistances.length > 0 && microwaveDistances[0] / 2 + microwaveToSecond < totalDistance){
           totalDistance = (microwaveDistances[0] / 2) + microwaveToSecond;
           //Updating the new map to hold the path from the first location to the microwave to the second location
           coordinateX = deliveryX[secondLocation-1];
           coordinateY = deliveryY[secondLocation-1];
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
           for (int k = 0; k < row; k++){
             for (int l = 0; l < column; l++){
               if (microwavePath[k][l] == 'x'){
                 newMap[k][l] = 'x';
               }
             }
           }
           //Updating the adjusted map and the number of microwaves to remove the microwave that was used
           adjustedMap[closeMicrowaveY][closeMicrowaveX] = ' ';
           numberOfMicrowaves--;
         } else {
           //If the microwave is not optimal to use, update the new map with the path from the first location to the second location
           for (int k = 0; k < row; k++){
             for (int l = 0; l < column; l++){
               if (firstToSecondMap[k][l] == 'x' || firstToSecondMap[k][l] == 'X'){
                 newMap[k][l] = firstToSecondMap[k][l];
               }
             }
           }
         }
         //Updating the total tip received based on the formula
         if ((adjustedMap[deliveryY[secondLocation-1]][deliveryX[secondLocation-1]] - '0') - totalDistance > 0) {
           totalTip += ((adjustedMap[deliveryY[secondLocation-1]][deliveryX[secondLocation-1]] - '0') - totalDistance) * 10;
         } else {
           totalTip += (adjustedMap[deliveryY[secondLocation-1]][deliveryX[secondLocation-1]] - '0') - totalDistance;
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
     
     System.out.println("\nThe maximum amount of tip possible including microwaves is $" + maximumTip + "!\n");
     
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
 public static void pathDistance(int distance, int startX, int startY, int deliveryX, int deliveryY, int[][] preX, int[][] preY) {
   //Placing an 'x' to represent all visited coordinates and updating the global distance array to hold the value of the distance it took to get there
   map[startY][startX] = 'x';
   allDistances[startY][startX] = distance;
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
   //If there are no more coordinates to visit, exit from the method
   if (allCoordinates.size() == 0) {
     return;
   }
   //Selecting the next location at the front of the queue and recursively following the same process
   int nextX = allCoordinates.poll();
   int nextY = allCoordinates.poll();
   pathDistance(allDistances[preY[nextY][nextX]][preX[nextY][nextX]]+1,nextX,nextY,deliveryX,deliveryY,preX,preY);
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










