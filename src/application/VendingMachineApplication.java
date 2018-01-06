package application;

import impl.SodaCompanyDirectory;
import impl.VendingMachineImpl;
import interfaces.VendingMachine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import domain.Coin;
import domain.Dime;
import domain.Employee;
import domain.Money;
import domain.Nickel;
import domain.Quarter;
import domain.SodaCompany;
import domain.SodaProduct;
import domain.User;

public class VendingMachineApplication
{    
    VendingMachine testMachine;
    Employee employee; 
    Employee imposter;
    User userA;
    User userB;
    BufferedReader userInput;    
    
    public VendingMachineApplication()  {
        SodaCompanyDirectory companies = SodaCompanyDirectory.getSodaCompanyDirectory();
        SodaCompany abcCompany = new SodaCompany("ABC");
        companies.add(abcCompany);
        employee = new Employee("John", abcCompany.getName());
        abcCompany.addEmployee(employee);
        testMachine = VendingMachineImpl.getInstance();
        userInput = new BufferedReader(new InputStreamReader(System.in));
    }

    public void showIntroduction() {
        StringBuilder intro = new StringBuilder("=========================================================\n");
        intro.append("=========== Orbitz Vending Demo Application =============\n");
        intro.append("The demo application already has some pre-populated soda company\n");
        intro.append("and employee records. There is one soda company named \"ABC\" with\n");
        intro.append("one employee named \"John\". Use the Vending Machine Actions below\n");
        intro.append("to add more soda companies and/or employees. Whereever the user prompt\n");
        intro.append("asks for price value, use a positive maximum two-decimal.\n");
        intro.append("\n");
        System.out.println(intro.toString());
    }
    
    public int getUserSelection()  {
        int userSelection = 0;
        System.out.println("=========== Vending Machine Actions ================");
        System.out.println("1 -> Employee adds product");
        System.out.println("2 -> User inserts money");
        System.out.println("3 -> User makes selection");
        System.out.println("4 -> User request coin return");
        System.out.println("5 -> Get soda product inventory");
        System.out.println("6 -> Add authorized employe and company");
        System.out.println("7 -> Exit");
        System.out.print("\nSelect -> ");      
        try {
            String userInputStr = userInput.readLine();
            userSelection = Integer.parseInt(userInputStr);            
        }
        catch (IOException ioe) {
            System.out.println("IO error when reading user input. Exiting");
            System.exit(1);
        }
        return userSelection;
    }
        
    public void addProducts() {
        String employeeName = "";
        String productName = "";
        String companyName = "";
        int count;
        String productPrice = "";
        Employee emp;
        System.out.println("==== Add soda products to vending machine. ====");
        try {
            System.out.print("Enter company name -> ");
            companyName = userInput.readLine();
            System.out.print("Enter employee name -> ");
            employeeName = userInput.readLine();
            System.out.print("Enter soda product name -> ");
            productName = userInput.readLine();
            System.out.print("Enter soda product sale price (ex: 1.00, 0.50) -> ");
            productPrice = userInput.readLine();
            System.out.print("Enter product count to add -> ");
            count = Integer.parseInt(userInput.readLine());
            emp = new Employee(employeeName, companyName);
            SodaProduct soda = new SodaProduct(productName, productPrice);
            testMachine.addSodaProduct(emp, soda, count);
            System.out.println("Products added");
        }
        catch (IOException ioe) {
            System.out.println("IO error when reading user input. Exiting");
            System.exit(1);
        }
        catch (Exception e) {
            System.out.println("Failed to add. Reason: " + e.getMessage());
        }
    }
    
    public void userInsertsMoney() {
        String userName = "";
        String amount = "";
        System.out.println("==== User inserts money ====");
        try {
            System.out.print("Enter user name -> ");
            userName = userInput.readLine();
            System.out.print("Enter amount to insert (ex: 1.00, 0.50) -> ");
            amount = userInput.readLine();
            User user = new User(userName);
            Money money = new Money(amount);
            testMachine.insertMoney(user, money);
            System.out.println("Money deposited");
        }
        catch (IOException ioe) {
            System.out.println("IO error when reading user input. Exiting");
            System.exit(1);
        }
        catch (Exception e) {
            System.out.println("Failed to deposit money. Reason: " + e.getMessage());
        }
    }
    
    public void userMakesSelection() {
        String userName = "";
        String productName = "";
        System.out.println("==== User makes selection ====");
        SodaProduct soda = null;
        try {
            System.out.print("Enter user name -> ");
            userName = userInput.readLine();
            System.out.print("Enter soda product name -> ");
            productName = userInput.readLine();
            User user = new User(userName);
            soda = testMachine.makeSelection(user, productName);
            System.out.println("Selection made.");
            System.out.println("Soda product " + soda.getName() + " received");
        }
        catch (IOException ioe) {
            System.out.println("IO error when reading user input. Exiting");
            System.exit(1);
        }
        catch (Exception e) {
            System.out.println("Failed to make selection. Reason: " + e.getMessage());
        }
    }

    public void userRequestsCoinReturn() {
        String userName = "";
        System.out.println("==== User requests coin return ====");
        try {
            System.out.print("Enter user name -> ");
            userName = userInput.readLine();
            User user = new User(userName);
            Coin[] coins = testMachine.coinReturn(user);
            int qCount = 0;
            int dCount = 0;
            int nCount = 0;
            for (Coin aCoin : coins) {
                if (aCoin.getCoinType().equals(Quarter.QUARTER)) {
                    qCount++;
                }
                if (aCoin.getCoinType().equals(Dime.DIME)) {
                    dCount++;
                }
                if (aCoin.getCoinType().equals(Nickel.NICKEL)) {
                    nCount++;
                }
            }
            StringBuilder outputMessage = new StringBuilder("=== Coins returned ===\n");
            outputMessage.append("Quarters = ").append(qCount).append("\n");
            outputMessage.append("Dimes    = ").append(dCount).append("\n");
            outputMessage.append("Nickels  = ").append(nCount).append("\n");
            System.out.print(outputMessage.toString());
        }
        catch (IOException ioe) {
            System.out.println("IO error when reading user input. Exiting");
            System.exit(1);
        }
        catch (Exception e) {
            System.out.println("Failed to make selection. Reason: " + e.getMessage());
        }
    }

    public void getSodaProductInventory()
    {
        String productName = "";
        System.out.println("==== Get soda product inventory ====");
        try {
            System.out.print("Enter soda product name -> ");
            productName = userInput.readLine();
            int count = testMachine.getSodaProductCount(productName);
            System.out.println(productName + " count = " + count);
        }
        catch (IOException ioe) {
            System.out.println("IO error when reading user input. Exiting");
            System.exit(1);
        }
        catch (Exception e) {
            System.out.println("Failed to add. Reason: " + e.getMessage());
        }
    }
    
    public void addEmployee()  {
        String employeeName = "";
        String companyName = "";
        Employee emp;
        System.out.println("==== Add authorized employe and company ====");
        try {
            System.out.print("Enter company name -> ");
            companyName = userInput.readLine();
            System.out.print("Enter employee name -> ");
            employeeName = userInput.readLine();
            emp = new Employee(employeeName, companyName);
            SodaCompanyDirectory companies = SodaCompanyDirectory.getSodaCompanyDirectory();
            SodaCompany company = companies.find(companyName);
            if (company == null) {
                company = new SodaCompany(companyName);
            }
            company.addEmployee(emp);
            companies.add(company);
            System.out.println("Company and employee records updated");
        }
        catch (IOException ioe) {
            System.out.println("IO error when reading user input. Exiting");
            System.exit(1);
        }
        catch (Exception e) {
            System.out.println("Failed to add. Reason: " + e.getMessage());
        }

        
    }
    
    /**
     * @param args
     */
    public static void main(String[] args)
    {
        VendingMachineApplication app = new VendingMachineApplication();
        int choice = 0;
        app.showIntroduction();
        while (choice != 7) {
            choice = app.getUserSelection();
            switch (choice) {
                case 1:
                    app.addProducts();
                    break;
                case 2: 
                    app.userInsertsMoney();
                    break;
                case 3:
                    app.userMakesSelection();
                    break;
                case 4:
                    app.userRequestsCoinReturn();
                    break;
                case 5:
                    app.getSodaProductInventory();
                    break;
                case 6:
                    app.addEmployee();
                    break;
            }                        
        }
    }
    
   

}
