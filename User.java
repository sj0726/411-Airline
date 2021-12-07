import java.io.*;
import java.sql.PseudoColumnUsage;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
public class User
{
    private static boolean loggedIn;
    private static String name;



    public static void main(String[] args) throws IOException {

        Scanner input = new Scanner (System.in);
        Scanner input2 = new Scanner(System.in);

        String username;
        String password;
        String choice;

        boolean wants_to_register = false;


        System.out.println("Welcome to New England Express Airlines!");
        System.out.println("Login or Register?(type 'Login' or 'Register):\n");
        choice = input.nextLine();
        if(Objects.equals(choice, "Login")) {

            System.out.println("\nEnter your username and password to login to your account.");
            System.out.println("Username: ");
            username = input.nextLine();
            System.out.println("Password: ");
            password = input.nextLine();
            boolean valid = false;
            Scanner sc2 = new Scanner(new FileInputStream("user_info.txt"));
            while (sc2.hasNextLine()){
                String pass = sc2.nextLine();
                if(pass.contains(password)){
                    valid = true;

                }

            }
            if(valid&& password.length()>1){
                loggedIn = true;

                System.out.println("Logged in. Welcome, " + name + "!" );
                menu();


            }else {
                System.out.println("This account information is not on file. Do you want to create an account? (Type Yes/No)");
                String create_acc = input2.nextLine();
                if(Objects.equals(create_acc, "Yes")){
                    wants_to_register = true;

                }else {
                    System.out.println("Session expired");
                }
            }
        }
        if(Objects.equals(choice, "Register")||wants_to_register) {
            //---------------------------------Registration-----------------------------------------------
            System.out.println("Create an account so you can book your ticket with us!");
            System.out.println("Name: ");
            name= input.nextLine();
            System.out.println("User: ");
            username = input.nextLine();
            System.out.println("Password:");
            password = input.nextLine();
            System.out.println("Email:");
            String email = input.nextLine();
            System.out.println("Enter Card Info: ");
            System.out.println("Card name: ");
            String cardName = input.nextLine();
            System.out.println("Card number: ");
            String cardNum = input.nextLine();
            System.out.println("Card expiration date: ");
            String expDate = input.nextLine();
            System.out.println("Security code: ");
            String secCode = input.nextLine();
            CardInfo user_card = new CardInfo(cardName,cardNum,expDate,secCode);
            user_card.storeCard();
            UserAccount new_user = new UserAccount(name,username,password,user_card);
            new_user.storeUser();
        }








    }
    public static void menu(){
        if(loggedIn){//-------------------Page when logged in------------------------
            Scanner s = new Scanner(System.in);
            System.out.println("How can we help today?");
            String choice;
            while(true) {
                System.out.print("Book a ticket (Type \"Book\") / Change a seat (Type \"Change\"): ");
                choice = s.nextLine();
                if (choice.equals("Book")) {
                    break;
                }
                else if (choice.equals("Change")) {
                    break;
                }
                else {
                    System.out.printf("Invalid input. (%s) Please try again.\n", choice);
                }
            }
            // start ticketing process.. but must differentiate per airport? or nah
            AirlineTicketing ticketing = new AirlineTicketing();
            ArrayList<Planes> planeSchedule = ticketing.getPlanes();
            Planes p;
            if (choice.equals("Book")) {
                while (true) {
                    System.out.println("Please choose from the following planes:");
                    for (int i = 0; i < planeSchedule.size(); i++) {
                        System.out.println(planeSchedule.get(i));
                    }
                    System.out.print("Plane #?: ");
                    int planeNum = Integer.parseInt(s.nextLine()) - 1;
                    p = planeSchedule.get(planeNum);
                    System.out.print("\nSelected plane\n====================\n" + p + "====================\n\nPlease confirm the above is correct (y/n): ");
                    if (s.nextLine().equals("y")) {
                        ticketing.bookSeat(p.origin, p.dest, p.date);
                        break;
                    }
                    else {
                        continue;
                    }
                }
            }
            else { // seat change

            }
        }
    }



}
class UserAccount
{

    private final String username;
    private final String password;
    private final CardInfo cardInfo;
    private final String name;
    private boolean active;


    public UserAccount(String name, String username, String password,CardInfo card) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.cardInfo = card;
        this.active = true;

    }
    public void storeUser() throws IOException{
        FileWriter fw = null;
        BufferedWriter bw = null;
        PrintWriter pw = null;

        try {
            fw = new FileWriter("user_info.txt", true);
            bw = new BufferedWriter(fw);
            pw = new PrintWriter(bw);
            pw.println("---------------------User--------------------- ");
            pw.println("Name: ");
            pw.println(this.name);
            pw.println("Username: ");
            pw.println(this.username);
            pw.println("Password: ");
            pw.println(this.password);

            pw.println("Active account:" + this.active);
            pw.flush();
        }finally {
            try {
                pw.close();
                bw.close();
                fw.close();


            }catch (IOException io){
            }

        }

    }


    public void deactivateAccount()
    {
        this.active = false;

    }

    public String getUsername(){
        return this.username;
    }
    public String getPassword(){
        return this.password;
    }

}