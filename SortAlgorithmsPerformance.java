/* SortAlgorithmsPerformance.java
 * Illustrates a number of sort algorithms using integers.
 * The program reads the numbers from a file which you can make yourself
 *   or generate randomly using the program g option. 
 *
 * The time taken by each algorithm reported in microseconds on the console.
 * The sorted numbers are written to a file.
 *
 * Run the program at a command prompt:
 *  EITHER java SortAlgorithmsPerformance g <num>   
 *           to generate a file of <num> radom integers;
 *  OR     java SortAlgorithmsPerformance [b|s|i|m|q]
 *           to sort with bubble/selection/insertion/merge/quick
 */
import java.util.*;
import java.io.*;
public class SortAlgorithmsPerformance {

  public static void main(String[] args) {
    int[] numsToSort;
    int[] outputArray;
    long tm, elapsedTm;

    if (args.length == 0) {
      System.err.println("Usage: java SortAlgorithmsPerformance g <num>");
      System.err.println("    or java SortAlgorithmsPerformance [b|s|i|m|q]");
      return;
    }

    char opt = args[0].charAt(0);

    if (opt == 'g') {
      int size = 1000;
      try {
        size = Integer.parseInt(args[1]);
      } catch (ArrayIndexOutOfBoundsException x) {
        System.err.println("Warning: missing arg 2: using default size");
      } catch (NumberFormatException x) {
        System.err.println("Warning: bad arg 2: using default size");
      }
      makeRandNums(size, "randomNums.txt");
      return;
    }
    //else
    numsToSort = readNums("randomNums.txt");
    System.err.printf("%d numbers read\n", numsToSort.length);
    //System.out.println("input"); list(numsToSort);

    System.err.print("Sorting ....");
    tm = System.nanoTime();
    if (opt == 'b') 
      bubbleSort(numsToSort);
    else if (opt == 's')
      selectionSort(numsToSort);
    else if (opt == 'i')
      insertionSort(numsToSort);
    else if (opt == 'm')
      mergeSort(numsToSort);
    else if (opt == 'q')
      quickSort(numsToSort);
    elapsedTm = System.nanoTime() - tm;
    System.err.printf(" done in %d microseconds\n", elapsedTm/1000); 
    System.out.println("sorted"); 
    writeNums(numsToSort, "sortedNums.txt");
  }


  //Utility methods

  //Swap two entries in an array of integers
  public static void swap(int[] arr, int i, int j) {
   int tmp = arr[i]; 
   arr[i] = arr[j]; arr[j] = tmp;
  }

  //Make a file of n random integers less than n
  public static void makeRandNums(int n, String path) {
    Random randGen = new Random();
    try {
      PrintWriter pw = new PrintWriter(new BufferedWriter(
        new FileWriter(path)));
      for (int k=0; k<n; k++) {
        pw.printf("%d ", randGen.nextInt(n));
        if (k % 500 == 0) randGen = new Random(); //reseed occasionally
      }
      pw.close();
      System.err.printf("%d numbers written\n", n);
    } catch (IOException x) {
      System.err.println("Could not make numbers file");
    }
  }

  //Read numbers from file into array
  public static int[] readNums(String path) {
    try {
      Scanner s = new Scanner(new BufferedReader(new FileReader(path)));
      ArrayList<Integer> numList = new ArrayList<Integer>();
      while (s.hasNextInt())
        numList.add(s.nextInt());
      s.close();
      int[] nums = new int[numList.size()];
      int i = 0;
      for (int n: numList) {
        nums[i] = n; i++;
      }
      return nums;
    } catch (IOException x) {
      System.err.println("Could not read numbers from file");
      System.exit(0);
    }
    return null;
  }

  //Write array if intgers to a file
  public static void writeNums(int[] arr, String path) {
    try {
      PrintWriter pw = new PrintWriter(new BufferedWriter(
        new FileWriter(path)));
      for (int k: arr) {
        pw.printf("%d ", k);
      }
      pw.close();
      System.err.printf("%d numbers written\n", arr.length);
    } catch (IOException x) {
      System.err.println("Could not make numbers file");
    }
  }

  //list array on stdout
  public static void list(int arr[]) {
    int ct = 0;
    for (int n: arr) {
      System.out.printf("%8d ", n);
      ct++;
      if (ct == 10) { //new line every 10 entries
        System.out.println();
        ct = 0;
      }
    }
    System.out.println();
  }

  //Sort entries in an array using bubble sort algorithm
  public static void bubbleSort(int arr[]) {
    int out, in, tmp;

    for (out = arr.length - 1; out > 1; out--) {     // outer loop (backward)
      for (in = 0; in < out; in++) {
      // inner loop (forward)
        if (arr[in] > arr[in + 1]) {// out of order?
          swap(arr, in, in + 1); // swap them
        }
      }
    }
  }

  //Sort entries in an array using bubble sort algorithm
  public static void selectionSort(int arr[]) {
    int out, in, min;
    for (out = 0; out < arr.length - 1; out++) { // outer loop
      min = out;
      for (in = out + 1; in < arr.length; in++) { // inner loop
        if (arr[in] < arr[min]) 
          min = in;
      } // end for (in)
      swap(arr, out, min);
    } // end for(out)
  } // end selectionSort()

  public static void insertionSort(int arr[]) {
    int in, out;
    for (out = 1; out < arr.length; out++) { // out is dividing line
      int temp = arr[out]; // remove marked item
      in = out; // start shifts at out
      while (in > 0 && arr[in - 1] >= temp) { // until one is smaller,
        arr[in] = arr[in - 1]; // shift item right,
        in--; // go left one position
      }
      arr[in] = temp; // insert marked item
    } // end for
  } // end insertionSort()

  //Merge sort: main routine and two helper functions
  //(See separate demo program)
  public static void mergeSort(int[] a) {
    int[] tmpArray = new int[a.length];
    mergeSort(a, tmpArray, 0, a.length - 1);
  }

  //mergeSort calls the following helper ...
  private static void mergeSort(int[] a, int[] tmpArray,
       int left, int right) {
    if (left < right) {
      int center = (left + right) / 2;
      mergeSort(a, tmpArray, left, center);
      mergeSort(a, tmpArray, center + 1, right);
      merge(a, tmpArray, left, center + 1, right);
    }
  }

  //Merge routine used by merge-sort helper
  private static void merge(int[] a, int[] tmpArray,
         int leftPos, int rightPos, int rightEnd) {
    int leftEnd = rightPos - 1; 
    int tmpPos = leftPos;
    int numElements = rightEnd - leftPos + 1;
    // Main loop
    while (leftPos <= leftEnd && rightPos <= rightEnd)
      if (a[leftPos] <= (a[rightPos]))
        tmpArray[tmpPos++] = a[leftPos++];
      else
        tmpArray[tmpPos++] = a[rightPos++];

    while (leftPos <= leftEnd)
      // Copy rest of first half
      tmpArray[tmpPos++] = a[leftPos++];
    while (rightPos <= rightEnd)
      // Copy rest of right half
      tmpArray[tmpPos++] = a[rightPos++];

    // Copy tmpArray back
    for (int i = 0; i < numElements; i++, rightEnd--)
      a[rightEnd] = tmpArray[rightEnd];
  }

  public static void quickSort(int arr[]) {
    quickSort(arr, 0, arr.length - 1);
  }

  public static void quickSort(int arr[], int start, int end) {
    int i = start; // index of left-to-right scan
    int k = end; // index of right-to-left scan

    if (end - start >= 1) {
      int pivot = arr[start]; 

      while (k > i) { //scan indices from left and right have not met
        while (arr[i] <= pivot && i <= end && k > i)
          i++;        // from the left, look for first elt > pivot
        
        while (arr[k] > pivot && k >= start && k >= i)
          k--;        // from the right, look for first elt not > pivot
        if (k > i)    // if right seek index still > left index, 
           swap(arr, i, k);  //swap the corresponding elements
      } 
      // after indices have crossed
      swap(arr, start, k); //swap last element in left partition with pivot
      quickSort(arr, start, k - 1); // recursively quicksort left partition
      quickSort(arr, k + 1, end);   // recursively quicksort right partition
    }
    else {
      // if only one element in the partition, do no sorting
      return; // the array is sorted, so exit
    }
  } //end quicksort

} //end class

