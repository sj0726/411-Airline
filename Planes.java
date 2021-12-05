import java.util.*;

// class for individual planes

public class Planes {
    public static int number = 0;
    int planeNum;
    int maxSeats;
    int current;
    boolean[] seats;
    String dest;
    Date date;

    public Planes(int maxSeats, String dest, Date date) {
        Planes.number++;
        this.planeNum = Planes.number;
        this.maxSeats = maxSeats;
        this.seats = new boolean[maxSeats];
        this.current = 0;
        this.dest = dest;
        this.date = date;
    }

    public String toString() {
        String s = String.format("Plane #%d\n----------\nDestination: %s\nDate/Time: %s\nPassenger Cap.: %d\n", this.planeNum, this.dest, this.date, this.maxSeats);
        return s;
    }

    public void addSeat() {
        if (this.current < this.maxSeats) {
            this.seats[this.current] = true;
            this.current++;
        }
        else {
            System.out.println("The plane is fully booked!");
        }
    }

    public void removeSeat() {
        if (this.current > 0) {
            this.current--;
            this.seats[this.current] = false;
        }
        else {
            System.out.println("The plane is already empty!");
        }
    }
}
