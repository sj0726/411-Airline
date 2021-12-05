import java.text.SimpleDateFormat;
import java.util.*;
import java.io.*;

public class AirlineTicketing {
    ArrayList<Planes> allPlanes = new ArrayList<>();

//***************************************** Basic Methods *****************************************//

    public AirlineTicketing() {
        this.readPlanes("planes.txt"); // read previously-stored planes
    }

    private void readPlanes(String file) throws IllegalArgumentException { // reads a text file of planes with delimiter "/" to parse data and loads them to its allPlanes attribute
        String result = "";
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
        String[] planes = result.split("\n");

        Date date;
        for (int i = 0; i < planes.length; i++) {
            String d = planes[i];
            String[] details = d.split("/");
            date = this.dateFormat(details[0]);
            Planes plane = new Planes(Integer.parseInt(details[1]), details[2], date);
            this.allPlanes.add(plane);
        }
    }

    private Date dateFormat (String date) throws IllegalArgumentException { // formats incoming string as a valid Date object
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm MMM dd yyyy", Locale.ENGLISH);
        Date d;
        try {
            d = formatter.parse(date);
            return d;
        } catch (Exception e) {
            System.out.println("Wrong date format! (HH:mm MMM dd yyyy)");
            throw new IllegalArgumentException(e);
        }
    }

    private void writePlane(String file, int maxSeats, String dest, String date) throws IllegalArgumentException { // writes down the inputted plane value to a text file (only accesible via addPlanes() method)
        try {
            FileWriter fr = new FileWriter(file, true);
            fr.write("\n");
            String data = date + "/" + Integer.toString(maxSeats) + "/" + dest;
            fr.write(data);
            fr.close();
        } catch (Exception e) {
            System.out.println("File not found");
            throw new IllegalArgumentException(e);
        }
    }

    public ArrayList<Planes> getPlanes() {
        return this.allPlanes;
    }

//***************************************** Functional Methods *****************************************//

    public void addPlane(int maxSeats, String dest, String date) {
        Date d = this.dateFormat(date);
        Planes p = new Planes(maxSeats, dest, d);
        this.allPlanes.add(p);
        this.writePlane("planes.txt", maxSeats, dest, date);
    }

    public Planes searchPlane(String dest, String date) {
        Date d = this.dateFormat(date);
        for (int i = 0; i < this.allPlanes.size(); i++) {
            Planes p = this.allPlanes.get(i);
            if (p.date.compareTo(d) == 0 && p.dest.equals(dest)) { // found a matching plane
                return p;
            }
        }
        System.out.println("No matching plane going to " + dest + " on " + date + "! Please try again.");
        Planes dummy = new Planes(0, "invalid", this.dateFormat("00:00 Jan 1 0000"));
        return dummy;
    }

    public void bookSeat(String dest, String date) {
        Planes p = this.searchPlane(dest, date);
        if (p.dest == "invalid") {
            return;
        }
        else {
            if (p.addSeat()) {
                System.out.println("Successfully booked a seat on plane " + p.planeNum + " going to " + dest + " at " + date);
            }
        }
    }

    public void changeSeat(String dest, String originDate, String newDate) { // seat change can only happen on planes with identical destinations
        Planes originPlane = this.searchPlane(dest, originDate);
        Planes newPlane = this.searchPlane(dest, newDate);
        if (newPlane.dest == "invalid") {
            return;
        }
        else {
            if (originPlane.removeSeat()) {
                this.bookSeat(dest, newDate);
            }
        }
    }

    public static void main(String[] args) {
        // Scanner sc = new Scanner(System.in);
        // System.out.println("Please select from the following:");
        // System.out.println("Book a Ticket (1) / Change a Ticket (2)");
        // int s = sc.nextInt();
        // System.out.println("Selection: " + s);

        AirlineTicketing a = new AirlineTicketing();
        System.out.println(Arrays.toString(a.getPlanes().get(2).seats));
        a.bookSeat("Boston", "12:30 Apr 1 2022");
        System.out.println(Arrays.toString(a.getPlanes().get(2).seats));

        System.out.println(a.getPlanes());
        a.bookSeat("New York", "12:30 Apr 1 2022");
        System.out.println(a.getPlanes());

        a.getPlanes().get(2).current = 50;
        a.bookSeat("Boston", "12:30 Apr 1 2022");

        System.out.println(a.getPlanes().get(0).planeNum);
        System.out.println(a.getPlanes().get(1).planeNum);
        System.out.println(a.getPlanes().get(2).planeNum);
    }
}