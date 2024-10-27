

import java.util.Date;
import java.util.Random;
import java.util.Scanner;

/**
 * Describe briefly your program in steps.
 *
 * @author Name - Nahid Alam
 */
public class Main {

    public static final int ITEM_ID = 0;
    public static final int ITEM_COUNT = 1;
    public static final int ITEM_PRICE = 2;
    public static final int ITEM_COLUMN_SIZE = 3;
    public static final int INITIAL_ITEM_SIZE = 10;
    public static final int SALE_ITEM_ID = 0;
    public static final int SALE_ITEM_COUNT = 1;
    public static final int SALE_ITEM_PRICE = 2;
    public static final int SALE_COLUMN_SIZE = 3;
    public static final int MAX_SALES = 1000;
    public static final int INITIAL_ITEM_NUMBER = 1000;
    public static final int MENU_ITEM_Q = -1;

    // Menu constants
    private static final int ADD_ITEMS = 1;
    private static final int REMOVE_ITEM = 2;
    private static final int PRINT_ITEMS = 3;
    private static final int SELL_ITEM = 4;
    private static final int PRINT_SALES = 5;
    private static final int SORTED_TABLE = 6;

    private static final int MAX_RANDOM_ITEM_COUNT = 10;
    private static final int MAX_ITEM_PRICE = 500;
    private static final int MIN_ITEM_PRICE = 100;

    private static Scanner userInputScanner = new Scanner(System.in);
    private static Scanner testScanner = null;  // For injected test input
    private static Random random = new Random();

    public static void main(final String[] args) {
        int[][] items = new int[INITIAL_ITEM_SIZE][ITEM_COLUMN_SIZE];
        int[][] sales = new int[MAX_SALES][SALE_COLUMN_SIZE];
        Date[] salesDates = new Date[MAX_SALES];
        int lastItemNumber = INITIAL_ITEM_NUMBER;

        System.out.println("This is a simple cash register system.");

        while (true) {
            int userSelection = menu();
            switch (userSelection) {
                case ADD_ITEMS:
                    System.out.print("Enter number of items to add: ");
                    int noOfItems = input();
                    if (noOfItems > 0) {
                        items = insertItems(items, lastItemNumber, noOfItems);
                        lastItemNumber += noOfItems; // Update last item number
                        System.out.println(noOfItems + " items added.");
                    } else {
                        System.out.println("Invalid number of items.");
                    }
                    break;
                case REMOVE_ITEM:
                    System.out.print("Enter the item ID to remove: ");
                    int itemIdToRemove = input();
                    if (removeItem(items, itemIdToRemove) == 0) {
                        System.out.println("Item removed successfully.");
                    } else {
                        System.out.println("Could not find the item.");
                    }
                    break;
                case PRINT_ITEMS:
                    printItems(items);
                    break;
                case SELL_ITEM:
                    System.out.print("Enter item ID to sell: ");
                    int itemIdToSell = input();
                    if (itemIdToSell < 0) {
                        break; // Handle 'q' to quit
                    }
                    System.out.print("Enter quantity to sell: ");
                    int amountToSell = input();
                    if (amountToSell < 0) {
                        break; // Handle 'q' to quit
                    }
                    int sellResult = sellItem(sales, salesDates,
                                              items, itemIdToSell,
                                              amountToSell);
                    if (sellResult == -1) {
                        System.out.println("Could not find the item.");
                    } else if (sellResult > 0) {
                        System.out.println("Failed to sell specified amount."
                                           + " Remaining stock: "
                                           + sellResult);
                    } else {
                        System.out.println("Sale successful.");
                    }
                    break;
                case PRINT_SALES:
                    printSales(sales, salesDates);
                    break;
                case SORTED_TABLE:
                    sortedTable(sales, salesDates);
                    break;
                case MENU_ITEM_Q:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid selection. Try again.");
            }
        }
    }

    /**
     * Injects a custom Scanner instance for testing purposes.
     *
     * @param scanner the Scanner instance to use as input
     */
    public static void injectInput(final Scanner scanner) {
        testScanner = scanner;  // Assign injected scanner for test input
    }

    /**
     * Displays the menu options to the user and returns their selection.
     *
     * @return The user's menu selection.
     */
    public static int menu() {
        System.out.println("\nMenu:");
        System.out.println("1. Insert items");
        System.out.println("2. Remove an item");
        System.out.println("3. Display a list of items");
        System.out.println("4. Register a sale");
        System.out.println("5. Display sales history");
        System.out.println("6. Sort and display sales history table");
        System.out.println("q. Quit");
        System.out.print("Your selection: ");
        return input();
    }

    /**
     * Reads user input and returns it as an integer.
     * If 'q' is entered, it returns a constant indicating quit.
     *
     * @return The user input as an integer, or MENU_ITEM_Q if 'q' is entered.
     */
    public static int input() {
        Scanner scanner = (testScanner != null)
                          ? testScanner : userInputScanner;
        while (true) {
            String input = scanner.next().trim();
            if (input.equalsIgnoreCase("q")) {
                return MENU_ITEM_Q;
            }
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Enter a number "
                                   + "or 'q' to quit.");
            }
        }
    }

    /**
     * Checks if there is enough space in the items array to add new items.
     *
     * @param items The current array of items.
     * @param noOfItems The number of items to be added.
     * @return true if there is insufficient space, false otherwise.
     */
    public static boolean checkFull(final int[][] items, final int noOfItems) {
        return getFreeSlots(items) < noOfItems;
    }

    /**
     * Extends the items array to accommodate new items.
     *
     * @param items the current items array
     * @param noOfItems the number of additional items needed
     * @return the extended items array with increased capacity
     */
    public static int[][] extendArray(final int[][] items,
                                       final int noOfItems) {
        int newSize = items.length + noOfItems;
        int[][] newItemsArray = new int[newSize][ITEM_COLUMN_SIZE];
        System.arraycopy(items, 0, newItemsArray, 0, items.length);
        return newItemsArray;
    }

    /**
     * Inserts new items into the inventory.
     *
     * @param items The current items array.
     * @param lastItemId The last used item ID.
     * @param noOfItems The number of items to insert.
     * @return The updated items array with new items.
     */
    public static int[][] insertItems(final int[][] items,
                                       final int lastItemId,
                                       final int noOfItems) {
        return insertItems(items, lastItemId, noOfItems, -1);
    }

    /**
     * Inserts new items with incrementing IDs and
     * either random counts/prices or a fixed price.
     * @param items the items array
     * @param lastItemId the last used item ID
     * @param noOfItems the number of items to add
     * @param fixedPrice the fixed price for all new items
     * @return the updated items array
     */
    public static int[][] insertItems(final int[][] items,
                                       final int lastItemId,
                                       final int noOfItems,
                                       final int fixedPrice) {
        int[][] workingArray = items;
        if (checkFull(items, noOfItems)) {
            workingArray = extendArray(items, noOfItems);
        }

        int startIndex = 0;
        while (startIndex < workingArray.length
              && workingArray[startIndex][ITEM_ID] != 0) {
            startIndex++;
        }

        for (int i = 0; i < noOfItems; i++) {
            workingArray[startIndex + i][ITEM_ID] = lastItemId + 1 + i;
            workingArray[startIndex + i][ITEM_COUNT] = random.nextInt(MAX_RANDOM_ITEM_COUNT) + 1;
            workingArray[startIndex + i][ITEM_PRICE] = (fixedPrice > 0)
                ? fixedPrice
                : random.nextInt(MAX_ITEM_PRICE) + MIN_ITEM_PRICE;
        }
        return workingArray;
    }

    /**
     * Removes an item from the inventory based on its ID.
     *
     * @param items The current items array.
     * @param itemId The ID of the item to remove.
     * @return 0 if the item was successfully removed, -1 if not found.
     */
    public static int removeItem(final int[][] items, final int itemId) {
        for (int i = 0; i < items.length; i++) {
            if (items[i][ITEM_ID] == itemId) {
                items[i][ITEM_ID] = 0;
                items[i][ITEM_COUNT] = 0;
                items[i][ITEM_PRICE] = 0;
                return 0;
            }
        }
        return -1; // Item not found
    }

    /**
     * Prints the list of items in inventory.
     *
     * @param items The current items array.
     */
    public static void printItems(final int[][] items) {
        System.out.println("Items in inventory:");
        for (int[] item : items) {
            if (item[ITEM_ID] != 0) {
                System.out.println("Item ID: " + item[ITEM_ID]
                                   + ", Count: " + item[ITEM_COUNT]
                                   + ", Price: " + item[ITEM_PRICE]);
            }
        }
    }

    /**
     * Sells an item from the inventory and records the sale.
     *
     * @param sales The current sales array.
     * @param salesDates The dates of the sales.
     * @param items The inventory items array.
     * @param itemId The ID of the item to sell.
     * @param amountToSell The quantity to sell.
     * @return 0 if sale successful,
     * amount remaining if not enough stock,
     * -1 if item not found.
     */
    public static int sellItem(final int[][] sales, final Date[] salesDates,
                                final int[][] items, final int itemId,
                                final int amountToSell) {
        int itemIndex = -1;
        for (int i = 0; i < items.length; i++) {
            if (items[i][ITEM_ID] == itemId) {
                itemIndex = i;
                break;
            }
        }

        if (itemIndex == -1 || items[itemIndex][ITEM_COUNT] < amountToSell) {
            return (itemIndex == -1) ? -1 : items[itemIndex][ITEM_COUNT];
        }

        items[itemIndex][ITEM_COUNT] -= amountToSell; // Deduct from stock

        // Record sale
        for (int j = 0; j < sales.length; j++) {
            if (sales[j][SALE_ITEM_ID] == 0) { // Find the first empty slot
                sales[j][SALE_ITEM_ID] = itemId;
                sales[j][SALE_ITEM_COUNT] = amountToSell;
                sales[j][SALE_ITEM_PRICE] = items[itemIndex][ITEM_PRICE] * amountToSell;
                salesDates[j] = new Date(); // Capture the current date
                break;
            }
        }
        return 0; // Sale successful
    }

    /**
     * Prints the sales history.
     *
     * @param sales The sales array.
     * @param salesDates The dates of the sales.
     */
    public static void printSales(final int[][] sales, final Date[] salesDates) {
        System.out.println("Sales history:");
        for (int i = 0; i < sales.length; i++) {
            if (sales[i][SALE_ITEM_ID] != 0) {
                System.out.println("Sale ID: " + sales[i][SALE_ITEM_ID]
                                   + ", Quantity: " + sales[i][SALE_ITEM_COUNT]
                                   + ", Price: " + sales[i][SALE_ITEM_PRICE]
                                   + ", Date: " + salesDates[i]);
            }
        }
    }

    /**
     * Sorts and displays the sales history in a formatted table.
     *
     * @param sales The sales array.
     * @param salesDates The dates of the sales.
     */
    public static void sortedTable(final int[][] sales,
                                   final Date[] salesDates) {
        // Implement sorting logic and display formatted table
    }

    /**
     * Gets the number of free slots in the items array.
     *
     * @param items The items array.
     * @return The number of free slots available.
     */
    public static int getFreeSlots(final int[][] items) {
        int count = 0;
        for (int[] item : items) {
            if (item[ITEM_ID] == 0) {
                count++;
            }
        }
        return count;
    }
}

