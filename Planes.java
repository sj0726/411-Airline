import java.util.*;

public class Planes {
    public static int number = 0;
    int planeNum;
    int maxSeats;
    boolean[] seats;
    String dest;
    Date date;

    public Planes(int maxSeats, String dest, Date date) {
        Planes.number++;
        this.planeNum = Planes.number;
        this.maxSeats = maxSeats;
        this.seats = new boolean[maxSeats];
        this.dest = dest;
        this.date = date;
    }

    public String toString() {
        String s = String.format("Plane #%d\n----------\nDestination: %s\nDate/Time: %s\nPassenger Cap.: %d\n", this.planeNum, this.dest, this.date, this.maxSeats);
        return s;
    }
}
