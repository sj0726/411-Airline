import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

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

    private void addUser(String name, String username, String password, CardInfo user_card) {
        UserAccount new_user = new UserAccount(name,username,password,user_card);
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

    public static void modifyUserPlane(UserAccount user, Planes p) { // add a plane ticket to a specific user
        UserAccount u = searchUser(user);
        if (u.name.equals("invalid") && u.username.equals("invalid") && u.password.equals("invalid")) { // if dummy user (user not found)
            return;
        }
        else {
            u.modifyUser_planes(p);
        }
    }

    public static void updateUserPlane(UserAccount user, Planes original, Planes changed) { // add a plane ticket to a specific user
        UserAccount u = searchUser(user);
        if (u.name.equals("invalid") && u.username.equals("invalid") && u.password.equals("invalid")) { // if dummy user (user not found)
            return;
        }
        else {
            u.modifyUser_planes(original, changed);
        }
    }

    public static void menu(UserAccount user){
        if(loggedIn){//-------------------Page when logged in------------------------
            Scanner s = new Scanner(System.in);
            System.out.println("How can we help today?");
            String choice;
            while(true) {
                System.out.printf("\nPlease select from the following: \n");
                System.out.print("Book a ticket (Type \"Book\") / Change a seat (Type \"Change\") / View your flights (Type \"View\") / Exit (Type \"Exit\"): ");
                choice = s.nextLine();
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
                            if(user.card!=null) {
                                int length = user.card.getCnumber().length();
                                assert length>4;
                                System.out.println("Confirm that you want to use this card *******" + user.getCardNum().substring(length - 4) + " to purchase this ticket? (y/n)");
                                String confirm_card = s.nextLine();
                                if (confirm_card.equalsIgnoreCase("y")) {
                                    ticketing.bookSeat(p.origin, p.dest, p.date);
                                    User.modifyUserPlane(user, p);
                                    user.printUserPlanes();
                                    break;
                                }
                            }else {
                                ticketing.bookSeat(p.origin, p.dest, p.date);
                                User.modifyUserPlane(user, p);
                                user.printUserPlanes();
                                break;
                            }

                        }
                        else {
                            continue;
                        }
                    }
                }
                else if (choice.equalsIgnoreCase("Change")) {
                    System.out.println("Please choose the plane ticket you'd like to change seats for: ");
                    user.printUserPlanes();
                    System.out.print("Ticket #: ");
                    int ticket = s.nextInt() - 1;
                    String[] data = user.user_planes.get(ticket);
                    Planes[] matching = ticketing.searchAllPlane(data[1], data[2]);
                    Planes[] planesToChange = new Planes[matching.length -1]; // contains all matching planes except the one identical
                    Planes identical = ticketing.searchPlane(data[1], data[2], ticketing.dateFormat(data[0]));
                    int count = 0;
                    System.out.println("\n----------------------------------------------------------------------\n");
                    System.out.println("Current plane\n====================\n" + identical + "====================\n");
                    System.out.println("Here are the planes you can change the seats to:\n");
                    for (int i = 0; i < matching.length; i++) {
                        if (matching[i].date.compareTo(ticketing.dateFormat(data[0])) != 0) { // if it has different dates (which means not an identical plane)
                            System.out.println(matching[i]);
                            planesToChange[count] = matching[i];
                            count++;
                        }
                    }
                    System.out.println("----------------------------------------------------------------------");
                    System.out.print("Plane #: ");
                    int planeNum = s.nextInt();

                    DateFormat dateFormat = new SimpleDateFormat("HH:mm MMM dd yyyy");
                    for (int i = 0; i < planesToChange.length; i++) {
                        if (matching[i].planeNum == planeNum) {
                            boolean check = ticketing.changeSeat(identical.origin, identical.dest, dateFormat.format(identical.date), dateFormat.format(matching[i].date));
                            if (check) {
                                User.updateUserPlane(user, identical, matching[i]);
                            }
                            else {
                                System.out.println("Failed to change seats!");
                            }
                            break;
                        }
                    }
                    break;
                }
                else if(choice.equalsIgnoreCase("View")){
                    user.printUserPlanes();
                }
                else if(choice.equalsIgnoreCase("Exit")) {
                    System.out.println("Thank you for using 411 Airlines!");
                    return;
                }
                else {
                    System.out.printf("Invalid input. (%s) Please try again.\n", choice);
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
        System.out.print("Login or Register?(type 'Login' or 'Register): ");
        choice = input.nextLine();
        if(choice.equalsIgnoreCase("Login")) {
            System.out.println("\nEnter your username and password to login to your account.");
            System.out.print("Username: ");
            username = input.nextLine();
            System.out.print("Password: ");
            password = input.nextLine();
            UserAccount u = User.searchUser(new UserAccount("dummy", username, password));
            if (u.name.equals("invalid")) {
                System.out.println("This account information is not on file. Do you want to create an account? (Type Yes/No)");
                String create_acc = input2.nextLine();
                if(create_acc.equalsIgnoreCase("Yes")){
                    wants_to_register = true;

                }else {
                    System.out.println("Session expired");
                }
            }
            else {
                System.out.println("Logged in. Welcome, " + u.name + "!" );
                loggedIn = true;
                menu(u);
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
            user.addUser(name,username,password,user_card);
            UserAccount u = new UserAccount(name,username,password,user_card);



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

    public CardInfo card;


    public UserAccount(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.active = true;
        this.user_planes = new ArrayList<String[]>();
    }
    public UserAccount(String name, String username, String password,CardInfo card_) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.active = true;
        this.user_planes = new ArrayList<String[]>();
        this.card = card_;
    }

    public UserAccount(String name, String username, String password, ArrayList<String[]> user_planes) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.active = true;
        this.user_planes = user_planes;
    }

    public void modifyUser_planes(Planes p){
        DateFormat dateFormat = new SimpleDateFormat("HH:mm MMM dd yyyy");
        String[] plane = {dateFormat.format(p.date), p.origin, p.dest};
        this.user_planes.add(plane);
        updateUser_planes(this, plane);
    }

    public void modifyUser_planes(Planes original, Planes changed){
        DateFormat dateFormat = new SimpleDateFormat("HH:mm MMM dd yyyy");
        String[] plane = {dateFormat.format(original.date), dateFormat.format(changed.date), changed.origin, changed.dest};
        this.user_planes.add(plane);
        updateUser_planes(this, plane);
    }

    public void printUserPlanes(){
        System.out.println("Your current flights are: ");
        for(int i= 0; i< this.user_planes.size();i++){
            System.out.println("=======================");
            System.out.println("Ticket #" + Integer.toString(i + 1));
            String[] p = this.user_planes.get(i);
            System.out.printf("Origin: %s\nDestination: %s\nDate: %s\n", p[1], p[2], p[0]);
        }
        System.out.println("=======================");
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
                    if (plane.length == 4) { // has 4 components, so update
                        result += data[0] + "/" + data[1] + "/" + data[2] + "/" + data[3] + "/"; // restore the user info other the planes list
                        String[] p = data[4].split(","); // data[4] stores planes
                        for (int i = 0; i < p.length; i++) {
                            String[] schedule = p[i].split("/");
                            if (schedule[0].equals(plane[0]) && schedule[1].equals(plane[2]) && schedule[2].equals(plane[3])) { // if it has a matching date, origin & dest as the original
                                result += plane[1] + "/" + plane[2] + "/" + plane[3] + ",";
                            }
                            else { // not a matching plane scheudle, move on
                                result += p[i] + ",";
                            }
                        }
                    }
                    else { // has 3 components, so append at the end of line
                        result += raw + "," + plane[0] + "/" + plane[1] + "/" + plane[2];
                    }
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
    public String getCardNum(){
        return this.card.getCnumber();
    }


}
