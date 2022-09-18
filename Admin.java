import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Admin {
    private Scanner input;
    public Admin(Scanner input)
    {
        this.input = input;
    }

    // View all products
    protected void printAllProducts() throws IOException{
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

    // View all orders
    protected void printAllOrders() throws IOException{
        try (BufferedReader orderReader = new BufferedReader(new FileReader("./orders.csv"))) 
        {
            String row;

            System.out.println(String.format("%-20s%-20s%-20s%-20s%-20s%-20s%-20s", "Order ID", "Customer ID", "Full name", "Phone number", "Address", "Total price", "Status")); 
            while ((row = orderReader.readLine()) != null) {
                String[] result = row.split(",");
                for (String i : result) {
                    System.out.printf("%-20s", i);
                }
                System.out.println("");
            }
        }
    }

    // View all users
    protected void printAllUsers() throws IOException{
        try (BufferedReader userReader = new BufferedReader(new FileReader("./users.csv"))) 
        {
            String row;

            System.out.println(String.format("%-20s%-20s%-20s%-20s%-20s%-20s%-20s%-20s", "User ID", "Username", "Password", "Full Name", "Phone Number", "Credit Card Number", "Address", "Total spent"));
            while ((row = userReader.readLine()) != null) {
                String[] result = row.split(",");
                for (String i : result) {
                    System.out.printf("%-20s", i);
                }
                System.out.println("");
            }
        }
    }

    // Add products
    protected void addProduct() throws IOException{
        System.out.println("Enter product info:");

        String id = Integer.toString( (int) (Math.random() * (99999 - 1000)) + 1000);

        System.out.print("Name: ");
        String name = input.nextLine();

        System.out.print("Price: ");
        String price = input.nextLine();

        System.out.print("Category: ");
        String category = input.nextLine();

        System.out.print("Description: ");
        String description = input.nextLine();

        try (BufferedReader usernameReader = new BufferedReader(new FileReader("./product.csv"))) {
            String row;

            while ((row = usernameReader.readLine()) != null) {
                String[] result = row.split(",");
                if (name.toLowerCase().equals(result[1].toLowerCase())) {
                    System.out.println("Product already existed, cannot add product.");
                    return;
                }
            }
        }
    
        addProductToList(id, name, price, category, description);
        System.out.println("New product added!");
    }

    // for AddProduct()
    private void addProductToList(String id, String name, String price, String category, String description) throws IOException {
        try (FileWriter output = new FileWriter(new File("./product.csv"), true))
        {
            output.write(String.format("%s,%s,%s,%s,%s\n", id, name, "VND" + price, category, description));
        }
    }

    protected void removeProduct() throws IOException {
        ArrayList<String[]> temp = new ArrayList<>();
        boolean found = false;
        int productToRemove = -1;

        try (BufferedReader Reader = new BufferedReader(new FileReader("./product.csv"))) 
        {
            String row;

            while ((row = Reader.readLine()) != null) {
                String[] splitRow = row.split(",");
                temp.add(splitRow);
            }
        }
        try (FileWriter Writer = new FileWriter(new File("./product.csv"))) 
        {
            System.out.println("Choose a product to remove (ID): ");
            String keyword = input.nextLine();
        
            for (String[] rowInTemp : temp) {
                if (rowInTemp[0].equals(keyword)) {
                    productToRemove = temp.indexOf(rowInTemp);
                    found = true;
                }
            }

            if (productToRemove != -1) {
                temp.remove(productToRemove);
            }

            for (String[] rowInTemp : temp) {
                Writer.write(String.format("%s,%s,%s,%s,%s\n", rowInTemp[0], rowInTemp[1], rowInTemp[2], rowInTemp[3], rowInTemp[4]));
            }
        }
        if (found == true) {
            System.out.println("Successfully removed product.");
        }
        else {
            System.out.println("No such product found, try again.");
        }
    }

    // update product info. Cases seen below
    protected void updateProductInfo() throws IOException {

        System.out.print("\nChoose product metadata to update: \n"
                + "1) Price \n"
                + "2) Category \n"
                + "3) Description \n");
        int choice = input.nextInt();
        input.nextLine();

        switch (choice) {
            case 1:
                updateProductPrice();
                break;
            case 2:
                updateProductCategory();
                break;
            case 3:
                updateProductDesc();
                break;
            default:
                System.out.println("Invalid choice, please try again.");
        }
    }

    // Case 1: price
    private void updateProductPrice() throws IOException {
        ArrayList<String[]> temp = new ArrayList<>();
        String newProductPrice = "";
        boolean found = false;

        try (BufferedReader Reader = new BufferedReader(new FileReader("./product.csv"))) 
        {
            String row;

            while ((row = Reader.readLine()) != null) {
                String[] splitRow = row.split(",");
                temp.add(splitRow);
            }
        }
        try (FileWriter Writer = new FileWriter(new File("./product.csv"))) 
        {
            System.out.println("Choose a product to update info (ID): ");
            String productToChange = input.nextLine();

            while (true) {
                System.out.println("Enter new product price: (Format: VNDXXXXXX, replace XXXXXX with price, e.g. VND50000) ");
                newProductPrice = input.nextLine();
                if (newProductPrice.matches("^VND[0-9]+$")) {
                    break;
                }

                System.out.println("Invalid price value, please try again below.");
            }
        
            for (String[] rowInTemp : temp) {
                if (rowInTemp[0].equals(productToChange)) {
                    rowInTemp[2] = newProductPrice;
                    found = true;
                }
                Writer.write(String.format("%s,%s,%s,%s,%s\n", rowInTemp[0], rowInTemp[1], rowInTemp[2], rowInTemp[3], rowInTemp[4]));
            }
        }
        if (found == true) {
            System.out.println("Successfully changed product price.");
        }
        else {
            System.out.println("No such product found, try again.");
        }
    }

    // case 2: category
    private void updateProductCategory() throws IOException {
        ArrayList<String[]> temp = new ArrayList<>();
        boolean found = false;

        try (BufferedReader Reader = new BufferedReader(new FileReader("./product.csv"))) {
            String row;

            while ((row = Reader.readLine()) != null) {
                String[] splitRow = row.split(",");
                temp.add(splitRow);
            }
        }
        try (FileWriter Writer = new FileWriter(new File("./product.csv")))
        {
            System.out.println("Choose a product to update info (ID): ");
            String productToChange = input.nextLine();

            System.out.println("Enter new product category: ");
            String newProductCategory = input.nextLine();

            for (String[] rowInTemp : temp) {
                if (rowInTemp[0].equals(productToChange)) {
                    rowInTemp[3] = newProductCategory;
                    found = true;
                }
                Writer.write(String.format("%s,%s,%s,%s,%s\n", rowInTemp[0], rowInTemp[1], rowInTemp[2], rowInTemp[3], rowInTemp[4]));
            }
        }
        if (found == true) {
            System.out.println("Successfully changed product category.");
        }
        else {
            System.out.println("No such product found, try again.");
        }
    }

    // case 3: description
    private void updateProductDesc() throws IOException {
        ArrayList<String[]> temp = new ArrayList<>();
        boolean found = false;

        try (BufferedReader Reader = new BufferedReader(new FileReader("./product.csv"))) {
            String row;

            while ((row = Reader.readLine()) != null) {
                String[] splitRow = row.split(",");
                temp.add(splitRow);
            }
        }
        try (FileWriter Writer = new FileWriter(new File("./product.csv")))
        {
            System.out.println("Choose a product to update info (ID): ");
            String productToChange = input.nextLine();

            System.out.println("Enter new product description: ");
            String newProductDesc = input.nextLine();

            for (String[] rowInTemp : temp) {
                if (rowInTemp[0].equals(productToChange)) {
                    rowInTemp[4] = newProductDesc;
                    found = true;
                }
                Writer.write(String.format("%s,%s,%s,%s,%s\n", rowInTemp[0], rowInTemp[1], rowInTemp[2], rowInTemp[3], rowInTemp[4]));
            }
        }
        if (found == true) {
            System.out.println("Successfully changed product description.");
        }
        else {
            System.out.println("No such product found, try again.");
        }
    }

    // find an order's status, basically the same as customer's
    protected void checkOrderStatus() throws IOException{
        try (BufferedReader orderReader = new BufferedReader(new FileReader("./orders.csv")))
        {
            String row;

            System.out.print("\n" + "Enter customer ID: ");
            String customerID = input.nextLine();

            System.out.println(String.format("%-20s%-20s%-20s%-20s%-20s%-20s%-20s", "Order ID", "Customer ID", "Full name", "Phone number", "Address", "Total price", "Status"));
            while ((row = orderReader.readLine()) != null) {
                String[] splitRow = row.split(",");
                if (splitRow[1].equals(customerID)) {
                    for (String i : splitRow) {
                        System.out.printf("%-20s", i);
                    }
                    System.out.println("");
                }
            }
        }
    }

    protected void changeOrderStatus() throws IOException {
        ArrayList<String[]> temp = new ArrayList<>();
        boolean found = false;
        try (BufferedReader Reader = new BufferedReader(new FileReader("./orders.csv"))) 
        {
            String row;

            while ((row = Reader.readLine()) != null) {
                String[] splitRow = row.split(",");
                temp.add(splitRow);
            }
        }
        try (FileWriter Writer = new FileWriter(new File("./orders.csv")))
        {
            System.out.println("Choose an order to update status (ID): ");
            String orderToChange = input.nextLine();

            System.out.println("Enter new order status: ");
            String newStatus = input.nextLine();

            for (String[] rowInTemp : temp) {
                if (rowInTemp[0].equals(orderToChange)) { 
                    rowInTemp[6] = newStatus;
                    found = true;
                }
                Writer.write(String.format("%s,%s,%s,%s,%s,%s,%s\n", rowInTemp[0], rowInTemp[1], rowInTemp[2], rowInTemp[3], rowInTemp[4], rowInTemp[5], rowInTemp[6]));
            }
        }
        if (found == true) {
            System.out.println("Successfully changed order status.");
        }
        else {
            System.out.println("No such order found, try again.");
        }
    }

    // For reference in Main
    protected int totalSold() throws IOException {
        int total = 0;
        try (BufferedReader Reader = new BufferedReader(new FileReader("./orders.csv"))) 
        {
            String row;

            while ((row = Reader.readLine()) != null) {
                String[] result = row.split(",");
                if (result[6].equals("paid")) {
                    total += Integer.parseInt(result[5].substring(3), 10);
                }
            }
        }

        return total;
    }
}
