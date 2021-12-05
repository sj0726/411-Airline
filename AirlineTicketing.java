import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;
import java.io.*;

public class AirlineTicketing {
    ArrayList<Planes> allPlanes = new ArrayList<>();
    public AirlineTicketing() {
        this.readPlanes("planes.txt"); // read previously-stored planes
    }

    private void readPlanes(String file) throws IllegalArgumentException {
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

    private Date dateFormat (String date) throws IllegalArgumentException {
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

    private void writePlane(String file, int maxSeats, String dest, String date) throws IllegalArgumentException {
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

    public void addPlane(int maxSeats, String dest, String date) {
        Date d = this.dateFormat(date);
        Planes p = new Planes(maxSeats, dest, d);
        this.allPlanes.add(p);
        this.writePlane("planes.txt", maxSeats, dest, date);
    }

    public ArrayList<Planes> getPlanes() {
        return this.allPlanes;
    }

    public static void main(String[] args) {
        // Scanner sc = new Scanner(System.in);
        // String result = "";
        // try {
        //     File p = new File("planes.txt");
        //     Scanner fileReader = new Scanner(p);
        //     while (fileReader.hasNextLine()) {
        //         result += fileReader.nextLine();
        //         if (fileReader.hasNextLine()) {
        //             result += "\n";
        //         }
        //     }
        // } catch (Exception e) {
        //     System.out.println("File not found");
        // }
        // String[] planes = result.split("\n");
        // System.out.println("Please select from the following:");
        // System.out.println("Book a Ticket (1) / Change a Ticket (2)");
        // int s = sc.nextInt();
        // System.out.println("Selection: " + s);


        // SimpleDateFormat formatter = new SimpleDateFormat("HH:mm MMM dd yyyy", Locale.ENGLISH);
        // Date date;
        // try {
        //     for (int i = 0; i < planes.length; i++) {
        //         String d = planes[i];
        //         String[] details = d.split("/");
        //         date = formatter.parse(details[0]);
        //         Planes plane = new Planes(Integer.parseInt(details[1]), details[2], date);
        //         System.out.println(plane.toString());
        //     }
        // } catch (Exception e) {
        //     System.out.println("Wrong date format! (HH:mm MMM dd yyyy)");
        // }

        /////////////////////////////////////////////////////////////////////////////////

        AirlineTicketing a = new AirlineTicketing();
        ArrayList<Planes> x = a.getPlanes();
        System.out.println(x);
        x.get(0).addSeat();
        System.out.println(Arrays.toString(x.get(0).seats));
        System.out.println(x.get(0).current);
        x.get(0).removeSeat();
        System.out.println(Arrays.toString(x.get(0).seats));
        System.out.println(x.get(0).current);
    }
}