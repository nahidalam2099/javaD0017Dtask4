Task

You must create a program that works as a simple cash register system. In the program, you should be able to add/remove items, sell items, and present sales history.

The user can choose what they want to do via a text-based menu. The menu should have the following options:

1. Insert items
2. Remove an item
3. Display a list of items
4. Register a sale
5. Display sales history
6. Sort and display sales history table
q. Quit
Your Selection:

Data structure / Database:

You need to store data in several data structures/databases. You need an array to store items (integer) [item number, quantity, price], an array to store sales (integer) [item number, number, sum], and an array (Date) that stores time/date for each sale. Dates must be on the same index as the corresponding sales. (Please see the assignment overview video to understand this better)

The initial size should hold 10 items. Before adding new items, the program should ask the user how many items to add and test if there is enough space in the data structure to hold the desired number of items. Otherwise, the data structure should be expanded to accommodate the new items. The item number should start at 1000 and increase by 1 for each new item (for more on this, please see the task description for insertItem). The number of sales can be fixed (at least 1000).

Also, please make sure to check that the entered values are valid.

You may be able to find alternative solutions, but the task is formulated to test certain important steps, so it should be solved in the manner listed below. You will practice using arrays/matrices and sending/receiving variables to/from methods.

Begin the task by constructing a flow chart and program logic on paper. Then describe your structure in text form (pseudocode) in the comment header of the program and in the method comments so that an outsider can understand what the program does and how your logic/algorithm works.

It is not allowed inbuilt classes such as ArrayList or similar classes, nor can global variables (variables created outside the methods) be used in the program except for the Scanner object to be created in the class body (see Main.java)
The signatures of the methods must be:

public static int menu()
public static int input()
public static boolean checkFull(final int[][] items, final int noOfItems)
public static int[][] extendArray(final int[][]items, final int noOfItems)
public static int[][] insertItems(final int[][] items, final int lastItemId, final int noOfItems)
public static int removeItem(final int[][] items, final int itemId)
public static void printItems(final int[][] items)
public static int sellItem(final int[][] sales, final Date[] salesDate, final int[][] items, final int itemIdToSell, final int amountToSell)
public static void printSales(final int[][] sales, final Date[] salesDate)
public static void sortedTable(final int[][] sales,  final Date[] salesDate)

The Datastructures must be in this order only
items[0] = itemId
items[1] = itemCount
items[2] = itemPrice

sales[0] = itemId
sales[1] = numberOfItems
sales[2] = sum, here sum = numberOfItems *itemPrice

Tasks of the methods:

menu()

    Presents the menu, loads, and returns the user's selection.

input()

    All input from the user must be handled by this method. The method waits for input and returns the integer entered by the user or -1 if user entered q. The method must also handle entries that are not an integer, and as long as the user enters something other than a valid integer, they will have a new chance to enter an integer.

checkFull (final int[][] items, final int noOfItems)

    Check if the array cannot hold the specified number of new items. Return true if full or cannot hold the requested number. This method is called from inside the insertItems method. If checkfull returns true, then extendArray below is called to extend the array.

public static int[][] extendArray(int[][]items, int noOfItems)

    This method is called from checkFull if there is a need to extend array. This method allocated required amount of memory, copy existing contents and return the new array,

insertItems(final int[][] items, final int lastItemId, final int noOfItems)

    Adds the specified number of items in the array after first checking that the number of free spaces is sufficient. The item number is based on input parameters (last used item number) and increases by one for each new item. So if the previously used lastItemId is 1010, then the new item number will be 1011. What it means is, that the last used itemId must be passed as the second argument. For example, when I first run the program, I add 5 items, and then I add 7 more items, function will be called as inesrtItem(items, 1004, 7) and not insertItems(items, 1005, 7). The number of units of items is randomly selected (1-10 pcs), and the item price (SEK 100-1000 / pc) is also chosen randomly. The method returns the new array (if the size increases)) or the original one.

removeItem (final int[][] items, final int itemId)

    Ask the user for the item number to be removed. Then call this method. Inside the methods, all fields for this item are set to 0. It returns 0 if the item is removed successfully, it returns -1 if the item is not found. 

printItems (final int[][] items)

    Prints item number, number, and price for all items that have an item number. The printout must be sorted into ascending item numbers.

sellItem(final int[][] sales, final Date[] salesDate, final int[][] items, final int itemIdToSell, final int amountToSell)

    Inside the method, If there are enough items in stock, the number of items is reduced and a sale is registered in the intended array (item number, number, sum). The date and time of the transaction are saved in the Date array at the corresponding position.  It returns 0 if the item is sold successfully, it returns -1 if the item is not found. Returns > 0 if the number of items available is less than what the user requested. So if user requested 5 items and only 4 items were available, then it returns 4.

public static void printSales (final int[][] sales, final Date[] salesDate)

    Prints all sales transactions with date, item number, number of items sold, and the total amount of the sale.

public static void sortedTable(final int[][]sales, final Date[] salesDate)

    sort the selling table by item number, in ascending order. The program shall print the sorted table, that keeps the correct information about the time and price of the sold items.

You may create additional methods if justified (eg functionality that reappears in several places in the program)
Error checks

All inputs in the program should be integers. However, entering anything other than integers should not crash the program, incorrect entry should result in the user having to enter a new selection.

You should not be able to add, delete or sell 0 items. Likewise, the price should not be able to be SEK 0.

The number of an individual item should never be less than 0, the program should not allow residual listing.
Tips

When you submit an array as an input parameter to a method, it refers to the memory location that is copied over to the method. All changes we make to this copy (e.g., adding new values) are "visible" even for the original reference in the main method as long as we do not create a new memory space (change the reference to memory space). If we need to create a new memory space, the new reference needs to be returned to the main and this replaces the previous reference.

To copy values between two arrays, you can create a loop and copy one value at a time or use Java's built-in function System.arraycopy()

Note: You are not allowed to use Arrays.sort() function to sort the array, you must use your own code (you can use the sorting algorithm and code used as an example in the course.)
