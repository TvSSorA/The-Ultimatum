import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner input = new Scanner(System.in);
        Authentication Auth = new Authentication(input); // Passing the Scanner around

        System.out.print("COSC2081 GROUP ASSIGNMENT \n"
                        + "STORE ORDER MANAGEMENT SYSTEM \n" 
                        + "Instructor: Mr. Minh Vu \n"
                        + "Group: Ultimatum \n"
                        + "S3925948, Tran Pham Quoc Duy \n"
                        + "S3926652, Hua Lam Anh \n"
                        + "S3926245, Nguyen Viet Long\n\n");

        // Loop until someone calls System.exit(0) with choice == 9
        while (true) {
            System.out.print("===================================== \n"
                        + "Welcome to The Ultimatum! Please login to continue, or register if you don't have an account yet.\n"
                        + "1. Login \n"
                        + "2. Register \n"
                        + "Input your desired choice: ");
            
            int loginChoice = input.nextInt();
            input.nextLine();

            switch (loginChoice) {
                case 1:
                    String user_type = Auth.Login(); 

                    if (user_type == "error") {
                        System.out.println("Invalid username or password, please try again.");
                    }

                    // Admin UI
                    else if (user_type == "admin") {
                        Admin adminService = new Admin(input);

                        System.out.println("Current total revenue: " + adminService.totalSold() + "VND" + "\n");

                        while (true) {
                            System.out.print("===================================== \n"
                                    + "[MENU] \n"
                                    + "1. Add product \n"
                                    + "2. Remove product \n"
                                    + "3. List all products \n"
                                    + "4. List all orders \n"
                                    + "5. List all users \n"
                                    + "6. Update product info \n"
                                    + "7. Get order by customer ID \n"
                                    + "8. Change order status \n"
                                    + "9. Exit \n"
                                    + "Input your desired choice: ");
                            int choice = input.nextInt();
                            input.nextLine();
                            
                            switch(choice){
                                case 1:
                                    adminService.addProduct();
                                    break;
                
                                case 2: 
                                    adminService.removeProduct();
                                    break;

                                case 3:
                                    adminService.printAllProducts();
                                    break;
                
                                case 4:
                                    adminService.printAllOrders();
                                    break;
                
                                case 5:
                                    adminService.printAllUsers();
                                    break;
                
                                case 6:
                                    adminService.updateProductInfo();
                                    break;
                
                                case 7:
                                    adminService.checkOrderStatus();
                                    break;
                
                                case 8:
                                    adminService.changeOrderStatus();
                                    break;
                
                                case 9:
                                    System.exit(0);
                                
                                default:
                                    System.out.println("Invalid choice, please try again.");
                            }
                        }
                    }

                    // Customer UI
                    else {
                        CoreService Service = new CoreService(input, user_type); // user_type is the userID in this case, see CoreService()

                        System.out.println("Current membership: " + Service.getCurrentMembership() + "\n");
                        
                        while (true) {

                            System.out.print("===================================== \n"
                                    + "[MENU] \n"
                                    + "1. View my account info \n"
                                    + "2. List all products \n"
                                    + "3. List products by category \n"
                                    + "4. List all products by ascending price \n"
                                    + "5. Add product to cart \n"
                                    + "6. Remove product from cart \n"
                                    + "7. Place order \n"
                                    + "8. Check an order's status \n"
                                    + "9. Exit \n"
                                    + "Input your desired choice: ");
                            int choice = input.nextInt();
                            input.nextLine();

                            switch(choice){
                                case 1:
                                    Service.viewAccount();
                                    break;
                
                                case 2:
                                    Service.printAllProducts();
                                    break;
                
                                case 3:
                                    Service.searchProducts();
                                    break;
                
                                case 4:
                                    Service.printAllProductsAscPrice();
                                    break;
                
                                case 5:
                                    Service.addToCart();
                                    break;
                
                                case 6:
                                    Service.removeFromCart();
                                    break;
                
                                case 7:
                                    Service.checkout();
                                    break;
                
                                case 8:
                                    Service.checkOrderStatus();
                                    break;
                                case 9:
                                    System.out.println("\nThank you for shopping with us. We hope to see you again!");
                                    Thread.sleep(5000);
                                    System.exit(0);
                                default:
                                    System.out.println("Invalid choice, please try again.");
                            }
                        }
                    }
                    break;
                
                case 2:
                    Auth.Register();
                    break;
                
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }
}

// Thanks Nguyen Long <3 (Duy's programming fellow saved us once again)
