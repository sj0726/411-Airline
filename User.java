import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class User
{
    private static boolean loggedIn;
    public static ArrayList<UserAccount> accounts = new ArrayList<>();

    public User() {
        this.readUsers();
    }

    private void readUsers() throws IllegalArgumentException{
        String result = "";
        String file = "user_info.txt";
        try {
            File p = new File(file);
            Scanner fileReader = new Scanner(p);
            while (fileReader.hasNextLine()) {
                result += fileReader.nextLine();
                if (fileReader.hasNextLine()) {
                    result += "\n";
                }
            }
            fileReader.close();
        } catch (Exception e) {
            System.out.println("File not found");
            throw new IllegalArgumentException(e);
        }
        UserAccount user;
        String[] users = result.split("\n");
        for (int i = 0; i < users.length; i++) {
            String[] data = users[i].split("/", 5);
            String[] planes = data[4].split(",");
            if (planes.length > 0) { // user has a list of purchased tickets, use modified constructor consisting of planes ArrayList
                ArrayList<String[]> user_planes = new ArrayList<String[]>();
                for (int j = 0; j < planes.length; j++) {
                    String[] schedule = planes[j].split("/");
                    user_planes.add(schedule);
                }
                user = new UserAccount(data[0], data[1], data[2], user_planes); // name, username, password, user_planes
            }
            else { // user has no plane tickets, use default constructor without ArrayList of planes
                user = new UserAccount(data[0], data[1], data[2]); // name, username, password, user_planes
            }
            User.accounts.add(user);
        }
    }

    private void addUser(String name, String username, String password) {
        UserAccount new_user = new UserAccount(name,username,password);
        accounts.add(new_user);
        try {
            UserAccount.storeUser(new_user);
        } catch (Exception e) {
            System.out.println("Invalid User! Please try");
        }
    }

    public static UserAccount searchUser(UserAccount user) {
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).username.equals(user.username) && accounts.get(i).password.equals(user.password)) { // if there exists a matching username & password
                return accounts.get(i);
            }
        }
        System.out.println("The account does not exist!");
        UserAccount dummy = new UserAccount("invalid", "invalid", "invalid");
        return dummy;
    }

    public static void addUserPlane(UserAccount user, Planes p) { // add a plane ticket to a specific user
        UserAccount u = searchUser(user);
        if (u.name.equals("invalid") && u.username.equals("invalid") && u.password.equals("invalid")) { // if dummy user (user not found)
            return;
        }
        else {
            u.addUser_planes(p);
        }
    }


    public static void menu(UserAccount user){
        if(loggedIn){//-------------------Page when logged in------------------------
            Scanner s = new Scanner(System.in);
            System.out.println("How can we help today?");
            String choice;
            while(true) {
                System.out.print("Book a ticket (Type \"Book\") / Change a seat (Type \"Change\") / View your flights (Type \"View\") : ");
                choice = s.nextLine();
                if (choice.equalsIgnoreCase("Book")) {
                    break;
                }
                else if (choice.equalsIgnoreCase("Change")) {
                    break;
                }
                else if(choice.equalsIgnoreCase("View")){
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
            if (choice.equalsIgnoreCase("Book")) {
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
                        User.addUserPlane(user, p);
                        System.out.println(Arrays.toString(user.printUserPlanes()));
                        break;
                    }
                    else {
                        continue;
                    }
                }
            }
            if(choice.equalsIgnoreCase("Change")) { // seat change




            }
            if(choice.equalsIgnoreCase("View")) {
                for(int i= 1; i< user.user_planes.size();i++){
                    System.out.println(Arrays.toString(user.user_planes.get(i)));

                }



            }
        }
    }

    public static void main(String[] args) throws IOException {
        User user = new User();


        Scanner input = new Scanner (System.in);
        Scanner input2 = new Scanner(System.in);

        String username;
        String password;
        String choice;


        boolean wants_to_register = false;


        System.out.println("Welcome to New England Express Airlines!");
        System.out.println("Login or Register?(type 'Login' or 'Register):\n");
        choice = input.nextLine();
        if(choice.equalsIgnoreCase( "Login")) {

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

                System.out.println("Logged in. Welcome, " + username + "!" );
                UserAccount u = User.searchUser(new UserAccount("dummy", username, password));
                if (u.name.equals("invalid")) {
                    System.out.println("Username/Password does not match!");
                }
                else {
                    menu(u);
                }


            }else {
                System.out.println("This account information is not on file. Do you want to create an account? (Type Yes/No)");
                String create_acc = input2.nextLine();
                if(create_acc.equalsIgnoreCase("Yes")){
                    wants_to_register = true;

                }else {
                    System.out.println("Session expired");
                }
            }
        }
        if(choice.equalsIgnoreCase("Register")||wants_to_register) {
            //---------------------------------Registration-----------------------------------------------
            System.out.println("Create an account so you can book your ticket with us!");
            System.out.print("Name: ");
            String name = input.nextLine();
            System.out.print("User: ");
            username = input.nextLine();
            System.out.print("Password:");
            password = input.nextLine();
            System.out.print("Email:");
            String email = input.nextLine();
            System.out.println("Enter Card Info: ");
            System.out.print("Card name: ");
            String cardName = input.nextLine();
            System.out.print("Card number: ");
            String cardNum = input.nextLine();
            System.out.print("Card expiration date: ");
            String expDate = input.nextLine();
            System.out.print("Security code: ");
            String secCode = input.nextLine();
            CardInfo user_card = new CardInfo(cardName,cardNum,expDate,secCode);
            user_card.storeCard();
            user.addUser(name,username,password);
            UserAccount u = new UserAccount(name,username,password);

            loggedIn = true;
            menu(u);

        }

    }

}


class UserAccount
{

    public String username;
    public String password;
    public String name;
    private boolean active;
    public ArrayList<String[]> user_planes;


    public UserAccount(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.active = true;
        this.user_planes = new ArrayList<String[]>();
    }

    public UserAccount(String name, String username, String password, ArrayList<String[]> user_planes) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.active = true;
        this.user_planes = user_planes;
    }

    public void addUser_planes(Planes p){
        DateFormat dateFormat = new SimpleDateFormat("HH:mm MMM dd yyyy");
        String[] plane = {dateFormat.format(p.date), p.origin, p.dest};
        this.user_planes.add(plane);
        updateUser_planes(this, plane);
    }

    public String[] printUserPlanes(){
        String[] result = new String[user_planes.size()];
        for (int i = 0; i < this.user_planes.size(); i++) {
            result[i] = Arrays.toString(this.user_planes.get(i));
        }
        return result;
    }

    private static void updateUser_planes(UserAccount user, String[] plane) {
        File file = new File("user_info.txt");
        FileWriter writer = null;
        String result = "";
        try {
            Scanner fileReader = new Scanner(file);
            while (fileReader.hasNextLine()) {
                String raw = fileReader.nextLine();
                String[] data = raw.split("/", 5);
                if (data[0].equals(user.name) && data[1].equals(user.username) && data[2].equals(user.password)) { // found a matching user
                    result += raw + "," + plane[0] + "/" + plane[1] + "/" + plane[2];
                }
                else { // not a matching user, continue
                    result += raw;
                }
                if (fileReader.hasNextLine()) {
                    result += "\n";
                }
            }
            writer = new FileWriter(file);
            writer.write(result);
            fileReader.close();
            writer.close();
        } catch (Exception e) {
            System.out.println("File not found");
            throw new IllegalArgumentException(e);
        }
    }


    public static void storeUser(UserAccount user) throws IOException{
        FileWriter fw = null;
        BufferedWriter bw = null;
        PrintWriter pw = null;
        String planes = "";
        ArrayList<String[]> p = user.user_planes;
        for (int i = 0; i < p.size(); i++) {
            planes = p.get(i)[0] + ", " + p.get(i)[1] + ", " + p.get(i)[2]; // "date, origin, dest"
        }

        try {
            fw = new FileWriter("user_info.txt", true);
            bw = new BufferedWriter(fw);
            pw = new PrintWriter(bw);
            pw.printf("%s/%s/%s/%s/%s\n", user.name, user.username, user.password, user.active, planes);
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