import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import java.lang.Math;
import java.io.File;

public class CoreService {
    // Initialize shopping cart and userID for further reference, taking Scanner from Main
    private ArrayList<Product> cart = new ArrayList<Product>();
    private Scanner input;
    private String userID;

    public CoreService(Scanner input, String userID)
    {
        this.input = input;
        this.userID = userID;
    }

    // View account info
    protected void viewAccount() throws IOException {
        try (BufferedReader accountReader = new BufferedReader(new FileReader("./users.csv")))
        {
            String row;

            System.out.println(String.format("%-20s%-20s%-20s%-20s%-20s%-20s", "ID", "Full name", "Phone number", "Credit card number", "Address", "Total spent"));
            while ((row = accountReader.readLine()) != null) {
                String[] result = row.split(",");
                if (userID.equals(result[0])) {
                    System.out.printf("%-20s%-20s%-20s%-20s%-20s%-20s", result[0], result[3], result[4], result[5], result[6], result[7]);
                    System.out.println("");
                }
            }
        }
    }

    // Listing all products, unsorted
    protected void printAllProducts() throws IOException {
        try (BufferedReader productReader = new BufferedReader(new FileReader("./product.csv")))
        {
            String row;

            System.out.println(String.format("%-20s%-20s%-20s%-20s%-20s", "ID", "Name", "Price", "Category", "Description"));
            while ((row = productReader.readLine()) != null) {
                String[] result = row.split(",");
                for (String i : result) {
                    System.out.printf("%-20s", i);
                }
                System.out.println("");
            }
        }
    }

    // Search for products
    // Choosing search method, cases are seen below
    protected void searchProducts() throws IOException{
        System.out.print("\nHow would you like to search? \n"
                + "1) By ID \n"
                + "2) By name \n"
                + "3) By category \n");
        int choice = input.nextInt();
        input.nextLine();
        switch(choice){
            case 1:
                searchProductsById();
                break;

            case 2:
                searchProductsByName();
                break;
            
            case 3: 
                searchProductsByCategory();
                break;

            default:
                System.out.print("Invalid choice, please try again.");
        }
    }

    // Case 1: By ID
    private void searchProductsById() throws IOException{
        try (BufferedReader productByIdReader = new BufferedReader(new FileReader("./product.csv")))
        {
            String row;

            System.out.print("\nEnter keyword: ");
            String keyword = input.nextLine();

            System.out.println(String.format("%-20s%-20s%-20s%-20s%-20s", "ID", "Name", "Price", "Category", "Description"));
            while ((row = productByIdReader.readLine()) != null) {
                String[] splitRow = row.split(",");
                if (splitRow[0].equals(keyword)) {
                    for (String i : splitRow) {
                        System.out.printf("%-20s", i);
                    }
                    System.out.println("");
                }
            }
        }
    }

    // Case 2: By name (case-sensitive)
    private void searchProductsByName() throws IOException{
        try (BufferedReader productByNameReader = new BufferedReader(new FileReader("./product.csv"))) 
        {
            String row;

            System.out.print("\n" + "Enter keyword: ");
            String keyword = input.nextLine();

            System.out.println(String.format("%-20s%-20s%-20s%-20s%-20s", "ID", "Name", "Price", "Category", "Description"));
            while ((row = productByNameReader.readLine()) != null) {
                String[] splitRow = row.split(",");
                if (splitRow[1].toLowerCase().contains(keyword.toLowerCase())) {
                    for (String i : splitRow) {
                        System.out.printf("%-20s", i);
                    }
                    System.out.println("");
                }
            }
        }
    }
    
    // Case 3: By category (case-sensitive)
    private void searchProductsByCategory() throws IOException{
        try (BufferedReader productByCategoryReader = new BufferedReader(new FileReader("./product.csv"))) 
        {
            String row;
    
            System.out.print("\n" + "Enter keyword: ");
            String keyword = input.nextLine();

            System.out.println(String.format("%-20s%-20s%-20s%-20s%-20s", "ID", "Name", "Price", "Category", "Description"));
            while ((row = productByCategoryReader.readLine()) != null) {
                String[] splitRow = row.split(",");
                if (splitRow[3].toLowerCase().contains(keyword.toLowerCase())) {
                    for (String i : splitRow) {
                        System.out.printf("%-20s", i);
                    }
                    System.out.println("");
                }
            }            
        }
    }

    // Print all products in ascending price
    protected void printAllProductsAscPrice() throws IOException{
        try (BufferedReader productByPriceReader = new BufferedReader(new FileReader("./product.csv"))) 
        {
            ArrayList<String[]> products = new ArrayList<>();
            String row;
    
            while ((row = productByPriceReader.readLine()) != null) {
                String[] result = row.split(",");
                products.add(result);
            }
    
            Collections.sort(products, new Comparator<String[]>() {
                @Override
                public int compare(String[] lhs, String[] rhs) {
                    int priceA = Integer.parseInt(lhs[2].substring(3), 10);
                    int priceB = Integer.parseInt(rhs[2].substring(3), 10);
                    return Integer.compare(priceA, priceB);
                }
            });
    
            System.out.println(String.format("%-20s%-20s%-20s%-20s%-20s", "ID", "Name", "Price", "Category", "Description"));
            for (String[] rowAfterSort : products) {
                for (String i : rowAfterSort) {
                    System.out.printf(String.format("%-20s", i));
                }
                System.out.println("");
            }   
        }
    }

    // Add product to cart
    protected void addToCart() throws IOException {
        try (BufferedReader Reader = new BufferedReader(new FileReader("./product.csv"))) 
        {
            String row;
            String itemID;
            int itemQuantity;
            boolean found = false;
            boolean alreadyInCart = false;

            System.out.println("Enter ID of your desired item: ");
            itemID = input.nextLine();

            System.out.println("Enter quantity: ");
            itemQuantity = input.nextInt();
            input.nextLine();

            while ((row = Reader.readLine()) != null) {
                String[] splitRow = row.split(",");
                if (splitRow[0].equals(itemID)) {
                    for (Product product : cart) {
                        if (product.id == Integer.parseInt(itemID)) {
                            product.quantity += itemQuantity;
                            product.price = "VND" + (Integer.parseInt(product.price.substring(3), 10) + (Integer.parseInt(splitRow[2].substring(3), 10) * itemQuantity));
                            found = true;
                            alreadyInCart = true;
                            break;
                        }
                    }

                    if (alreadyInCart == false) {
                        Product product = new Product(Integer.parseInt(splitRow[0]), splitRow[1], itemQuantity, "VND" + Integer.parseInt(splitRow[2].substring(3), 10) * itemQuantity);
                        cart.add(product);
                        found = true;
                        break;
                    }
                }
            }
            
            if (found == true) {
                System.out.println("New product added!");
                viewCart();
            }
            else {
                System.out.println("Product not found, cannot add to cart.");
            }
        }
    }

    // Remove product from cart
    protected void removeFromCart() throws IOException {
        ArrayList<String[]> temp = new ArrayList<>();
        int itemID;
        int itemQuantity;
        int productToRemove = -1;
        boolean found = false;

        try (BufferedReader Reader = new BufferedReader(new FileReader("./product.csv"))) 
        {
            String row;

            while ((row = Reader.readLine()) != null) {
                String[] splitRow = row.split(",");
                temp.add(splitRow);
            }
        }

        if (cart.isEmpty()) {
            System.out.println("Cart is empty, cannot remove items.");
            return;
        }

        System.out.println("Enter ID of the item you want to remove: ");
        itemID = input.nextInt();
        input.nextLine();

        System.out.println("Enter quantity to remove: ");
        itemQuantity = input.nextInt();
        input.nextLine();

        for (Product product : cart) {
            if (product.id == itemID) {
                if (product.quantity <= itemQuantity) {
                    productToRemove = cart.indexOf(product);
                }
                else {
                    product.quantity -= itemQuantity;
                    for (String[] i : temp) {
                        if (String.valueOf(itemID).equals(i[0])) {
                            product.price = "VND" + (Integer.parseInt(product.price.substring(3), 10) - (Integer.parseInt(i[2].substring(3), 10) * itemQuantity));
                        }
                    }
                }

                found = true;
            }
        }

        if (productToRemove != -1) {
            cart.remove(productToRemove);
        }

        if (found == true) {
            System.out.println("Removal success!");
            viewCart();
        }
        else {
            System.out.println("No such product found in cart, cannot remove.");
            viewCart();
        }
    }

    // View cart
    protected void viewCart() {
        int total = 0;

        for (Product product : cart) {
            int filtered_price = Integer.parseInt(product.price.substring(3), 10);
            total += filtered_price;
        }

        System.out.println(String.format("%-20s%-20s%-20s%-20s", "Product ID", "Product name", "Quantity", "Price"));
        for (Product product : cart) {
            System.out.println(product);
        }
        System.out.println("\nCurrent subtotal: VND" + total);
    }

    //Get information of an Order by using Order ID. Doesn't return any info if no corresponding order found 
    protected void checkOrderStatus() throws IOException{
        try (BufferedReader orderReader = new BufferedReader(new FileReader("./orders.csv"))) 
        {
            String row;

            System.out.print("\n" + "Enter order ID: ");
            String orderID = input.nextLine();

            System.out.println(String.format("%-20s%-20s%-20s%-20s%-20s%-20s%-20s", "Order ID", "Customer ID", "Full name", "Phone number", "Address", "Total price", "Status"));
            while ((row = orderReader.readLine()) != null) {
                String[] splitRow = row.split(",");
                if (splitRow[0].equals(orderID)) {
                    for (String i : splitRow) {
                        System.out.printf("%-20s", i);
                    }
                    System.out.println("");
                }
            }
        }
    }

    // Checkout
    protected void checkout() throws IOException{
        int total = 0;

        for (Product product : cart) {
            int filtered_price = Integer.parseInt(product.price.substring(3), 10);
            total += filtered_price;
        }

        if (cart.isEmpty()) {
            System.out.println("Cart is empty, cannot proceed to checkout.");
            return;
        }

        System.out.println("\nDo you wish to proceed to checkout? (Y/N): ");
        String choice_checkout = input.nextLine();

        switch (choice_checkout) {
            case "Y":
                viewCart();
                System.out.println("");
                viewAccount();
                System.out.println("");

                System.out.println("Do you wish to checkout with this billing info? (Y/N): ");
                String choice_billing = input.nextLine();

                switch (choice_billing) {
                    case "Y":
                        String fullName = "";
                        String phoneNumber = "";
                        String creditCardNumber = "";
                        String deliveryAddress = "";

                        try (BufferedReader accountReader = new BufferedReader(new FileReader("./users.csv")))
                        {
                            String row;

                            while ((row = accountReader.readLine()) != null) {
                                String[] result = row.split(",");
                                if (userID.equals(result[0])) {
                                    fullName = result[3];
                                    phoneNumber = result[4];
                                    creditCardNumber = result[5];
                                    deliveryAddress = result[6];
                                }
                            }
                        }

                        if (getCurrentMembership() != "None") {
                            if (getCurrentMembership() == "Silver") {
                                total -= total * 0.05;
                                System.out.println(getCurrentMembership() + " Membership discount awarded! New total price: VND" + total);
                            }
                            if (getCurrentMembership() == "Gold") {
                                total -= total * 0.1;
                                System.out.println(getCurrentMembership() + " Membership discount awarded! New total price: VND" + total);
                            }
                            if (getCurrentMembership() == "Platinum") {
                                total -= total * 0.15;
                                System.out.println(getCurrentMembership() + " Membership discount awarded! New total price: VND" + total);
                            }
                        }

                        addOrderToCsv(total, fullName, phoneNumber, deliveryAddress);
                        addToTotalSpent(userID, total);
                        cart.clear();
                        System.out.println("Checkout complete, your order is on the way!");

                        break;
                    case "N":
                        System.out.println("Please fill in your billing info below: \n");

                        while (true) {
                            System.out.println("Enter your full name: ");
                            fullName = input.nextLine();
                            if (fullName.matches("^[a-zA-Z\\s]+$")) {
                                break;
                            }

                            System.out.println("Illegible name, please try again.");
                        }

                        while (true) {
                            System.out.println("Enter your phone number: ");
                            phoneNumber = input.nextLine();
                            if (phoneNumber.matches("^0[0-9]{9}$")) {
                                break;
                            }

                            System.out.println("Illegible phone number, please try again.");
                        }

                        while (true) {
                            System.out.println("Enter your credit card number: ");
                            creditCardNumber = input.nextLine();
                            if (creditCardNumber.matches("^[0-9]{16}$")) {
                                break;
                            }

                            System.out.println("Illegible credit card number, please try again.");
                        }

                        System.out.println("Enter your billing address: ");
                        deliveryAddress = input.nextLine();

                        System.out.printf(String.format("%-20s%-20s%-20s%-20s\n", "Full name", "Phone number", "Credit card number", "Address"));
                        System.out.printf(String.format("%-20s%-20s%-20s%-20s\n\n", fullName, phoneNumber, creditCardNumber, deliveryAddress));

                        System.out.println("Is this ok? (Y/N): ");
                        String choice_newinfo = input.nextLine();

                        switch (choice_newinfo) {
                            case "Y":
                                if (getCurrentMembership() != "None") {
                                    if (getCurrentMembership() == "Silver") {
                                        total -= total * 0.05;
                                        System.out.println(getCurrentMembership() + " Membership discount awarded! New total price: VND" + total);
                                    }
                                    if (getCurrentMembership() == "Gold") {
                                        total -= total * 0.1;
                                        System.out.println(getCurrentMembership() + " Membership discount awarded! New total price: VND" + total);
                                    }
                                    if (getCurrentMembership() == "Platinum") {
                                        total -= total * 0.15;
                                        System.out.println(getCurrentMembership() + " Membership discount awarded! New total price: VND" + total);
                                    }
                                }

                                addOrderToCsv(total, fullName, phoneNumber, deliveryAddress);
                                addToTotalSpent(userID, total);
                                cart.clear();
                                System.out.println("Checkout complete, your order is on the way!");
                                break;
                            case "N":
                                System.out.println("Checkout cancelled.");
                                break;
                            default: 
                                System.out.println("Invalid choice, cancelling checkout.");
                        }
                        break;
                    default: 
                        System.out.println("Invalid choice, cancelling checkout.");
                }
                break;
            case "N":
                System.out.println("Checkout cancelled.");
                break;
            default:
                System.out.println("Invalid choice, cancelling checkout.");
        }
    }

    // For checkout()
    private void addOrderToCsv(int total, String fullName, String phoneNumber, String address) throws IOException {
        String orderID = Integer.toString( (int) (Math.random() * (99999 - 1000)) + 1000);

        try (FileWriter orderWriter = new FileWriter(new File("./orders.csv"), true)) {
            orderWriter.write(String.format("%s,%s,%s,%s,%s,%s,%s\n", orderID, userID, fullName, phoneNumber, address, "VND" + total, "active"));
        }
    }

    // For checkout()
    private void addToTotalSpent(String userID, int total) throws IOException {
        ArrayList<String[]> temp = new ArrayList<>();

        try (BufferedReader accountReader = new BufferedReader(new FileReader("./users.csv")))
        {
            String row;

            while ((row = accountReader.readLine()) != null) {
                String[] result = row.split(",");
                temp.add(result);
            }
        }

        try (FileWriter Writer = new FileWriter(new File("./users.csv"))) 
        {
            for (String[] rowInTemp : temp) {
                if (rowInTemp[0].equals(userID)) {
                    rowInTemp[7] = String.valueOf(Integer.parseInt(rowInTemp[7]) + total);
                }
                Writer.write(String.format("%s,%s,%s,%s,%s,%s,%s,%s\n", rowInTemp[0], rowInTemp[1], rowInTemp[2], rowInTemp[3], rowInTemp[4], rowInTemp[5], rowInTemp[6], rowInTemp[7]));
            }
        }
    }

    // For reference in Main
    protected String getCurrentMembership() throws NumberFormatException, IOException {
        String currentMembership = "";

        try (BufferedReader accountReader = new BufferedReader(new FileReader("./users.csv")))
        {
            String row;

            while ((row = accountReader.readLine()) != null) {
                String[] result = row.split(",");
                if (result[0].equals(userID)) {
                    if (Integer.parseInt(result[7]) <= 5000000) {
                        currentMembership = "None";
                        return currentMembership;
                    }
                    if (Integer.parseInt(result[7]) > 5000000 && Integer.parseInt(result[7]) <= 10000000) {
                        currentMembership = "Silver";
                        return currentMembership;
                    }
                    if (Integer.parseInt(result[7]) > 10000000 && Integer.parseInt(result[7]) <= 25000000) {
                        currentMembership = "Gold";
                        return currentMembership;
                    }
                    if (Integer.parseInt(result[7]) > 25000000) {
                        currentMembership = "Platinum";
                        return currentMembership;
                    }
                }
            }
        }

        return "error";
    }
}

class Product {
    int id;
    String name;
    int quantity;
    String price;

    public Product(int id, String name, int quantity, String price) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public String toString() {
        return String.format("%-20s%-20s%-20s%-20s", id, name, quantity, price);
    }
}

