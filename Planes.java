import java.util.*;
import java.text.SimpleDateFormat;

// class for individual planes

public class Planes {
    public static int number = 0;
    int planeNum;
    int maxSeats;
    int current;
    boolean[] seats;
    String origin;
    String dest;
    Date date;

    public Planes(int maxSeats, String origin, String dest, Date date) { // constructor using Date object for dates
        if (maxSeats == -1) {
            this.planeNum = -1;
            this.seats = new boolean[0];
        }
        else {
            Planes.number++;
            this.planeNum = Planes.number;
            this.seats = new boolean[maxSeats];
        }
        this.maxSeats = maxSeats;
        this.current = 0;
        this.origin = origin;
        this.dest = dest;
        this.date = date;
    }

    public Planes(int maxSeats, String origin, String dest, String date) { // constructor using String object for dates
        if (maxSeats == -1) {
            this.planeNum = -1;
            this.seats = new boolean[0];
        }
        else {
            Planes.number++;
            this.planeNum = Planes.number;
            this.seats = new boolean[maxSeats];
        }
        this.maxSeats = maxSeats;
        this.current = 0;
        this.origin = origin;
        this.dest = dest;
        this.date = this.dateFormat(date);
    }

    public Date dateFormat (String date) throws IllegalArgumentException { // formats incoming string as a valid Date object
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

    public String toString() {
        String s = String.format("Plane #%d\n----------\nOrigin: %s\nDestination: %s\nDate/Time: %s\nPassenger Cap.: %d\n", this.planeNum, this.origin, this.dest, this.date, this.maxSeats);
        return s;
    }

    public boolean addSeat() { // increments seat count "current" and corresponding seats index to true
        if (this.current < this.maxSeats) {
            this.seats[this.current] = true;
            this.current++;
            return true;
        }
        else {
            System.out.println("The plane is fully booked! Please try again.");
            return false;
        }
    }

    public boolean removeSeat() { // decrements seat count "current" and corresponding seats index to false
        if (this.current > 0) {
            this.current--;
            this.seats[this.current] = false;
            return true;
        }
        else {
            System.out.println("The plane is already empty! Please try again.");
            return false;
        }
    }
}
