import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;
import java.io.*;

public class AirlineTicketing {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String result = "";
        try {
            File p = new File("planes.txt");
            Scanner fileReader = new Scanner(p);
            while (fileReader.hasNextLine()) {
                result += fileReader.nextLine();
                if (fileReader.hasNextLine()) {
                    result += "\n";
                }
            }
        } catch (Exception e) {
            System.out.println("File not found");
        }
        String[] planes = result.split("\n");
        // System.out.println("Please select from the following:");
        // System.out.println("Book a Ticket (1) / Change a Ticket (2)");
        // int s = sc.nextInt();
        // System.out.println("Selection: " + s);


        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm MMM dd yyyy", Locale.ENGLISH);
        Date date;
        try {
            for (int i = 0; i < planes.length; i++) {
                String d = planes[i];
                String[] details = d.split("/");
                date = formatter.parse(details[0]);
                Planes plane = new Planes(Integer.parseInt(details[1]), details[2], date);
                System.out.println(plane.toString());
            }
        } catch (Exception e) {
            System.out.println("Wrong date format! (HH:mm MMM dd yyyy)");
        }
    }
}