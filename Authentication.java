import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.lang.Math;

public class Authentication {

    private Scanner input;
    public Authentication(Scanner input)
    {
        this.input = input;
    }
    
    // Login
    protected String Login() throws IOException {
        try (BufferedReader accountReader = new BufferedReader(new FileReader("./users.csv")))
        {
            String row;

            System.out.println("Enter username:");
            String username = input.nextLine();
            System.out.println("Enter password: ");
            String password = input.nextLine();

            if (username.equals("admin") && password.equals("admin")) {
                System.out.println("Admin login successful.");
                return "admin";
            }

            while ((row = accountReader.readLine()) != null) {
                String[] result = row.split(",");
                if (username.equals(result[1]) && password.equals(result[2])) {
                    System.out.println("Login successful!");
                    return result[0];
                }
            }
        
            return "error";
        }
    }

    // Register
    protected void Register() throws IOException {
        try (
            BufferedReader usernameReader = new BufferedReader(new FileReader("./users.csv"));
            FileWriter accountWriter = new FileWriter(new File("./users.csv"), true);
        )
        {
            String row;
            String fullName;
            String phoneNumber;
            String creditCardNumber;
            String deliveryAddress;

            System.out.println("Enter username:");
            String username = input.nextLine();
            System.out.println("Enter password: ");
            String password = input.nextLine();
            System.out.println("Confirm password: ");
            String passwordConfirm = input.nextLine();

            if (!passwordConfirm.equals(password)) {
                System.out.println("Password mismatch, please try again.");
                return;
            }

            while ((row = usernameReader.readLine()) != null) {
                String[] result = row.split(",");
                if (username.equals(result[1])) {
                    System.out.println("Username already existed, please try again.");
                    return;
                }
            }

            System.out.println("Account verification OK, please enter your billing info below. \n");

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

            System.out.println("Enter delivery address:");
            deliveryAddress = input.nextLine();

            String customerID = Integer.toString( (int) (Math.random() * (99999 - 1000)) + 1000);

            accountWriter.write(String.format("%s,%s,%s,%s,%s,%s,%s,%s\n", customerID, username, password, fullName, phoneNumber, creditCardNumber, deliveryAddress, "0"));
            accountWriter.flush();
            System.out.println("Account successfully registered!");
        }
    }
}
