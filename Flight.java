import java.text.SimpleDateFormat;
import java.util.*;



public class Flight {
    public static int number = 0;
    private final int maxPassengers;


    boolean[] seats;
    String dest;
    Date date;
    double flightID;
    String depTime;
    String arrTime;
    String depLoc;
    String arrLoc;
    Boolean status;
    Airport depAirport;
    Airport getArrAirport;
    int flightCap;
    Passengers passengers;
    int gateNum;

    public Flight(Integer maxPassengers, String depTime, String origin, String dest, Date date) {
        this.maxPassengers = maxPassengers;
        this.seats = new boolean[maxPassengers];
        this.dest = dest;
        this.date = date;
        this.depTime = depTime;
        this.depLoc = origin;
        this.arrLoc = dest;
        this.flightID = (Math.random()) * 10;
    }

    public String toString() {
        String s = String.format("Flight #%d\n----------\nDestination: %s\nDate/Time: %s\nPassenger Cap.: %n\n", this.flightID, this.dest, this.date);
        return s;
    }

    public Flight(int maxPassengers,Date date, String origin, String dest) {
        this.maxPassengers = maxPassengers;
        this.flightID = (Math.random()) * 10;
        this.depTime = this.getDepTime();
        this.depLoc = this.getDepLoc();
        this.arrLoc = this.getArrLoc();
        this.status = this.getStatus();
        this.flightCap = this.getFlightCap();
        this.passengers = this.getPassengers();
        this.gateNum = this.getGateNum();

    }

    private Date getDate() {
        return this.date;
    }

    private Planes getPlane() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm MMM dd yyyy", Locale.ENGLISH);
        Date date;

        Planes flight_plane = new Planes(50, this.getArrLoc(), this.getDate());
        return flight_plane;


    }

    public String getDepTime() {
        return this.depTime;
    }

    public void setDepTime(String depTime) {

        this.depTime = depTime;
    }

    public String getDepLoc() {
        return this.depLoc;
    }

    public void setDepLoc(String depLoc) {

        this.depLoc = depLoc;
    }

    public String getArrLoc() {

        return this.arrLoc;
    }

    public void setArrLoc(String arrLoc) {
        this.arrLoc = arrLoc;
    }

    public Boolean getStatus() {

        return this.status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getFlightCap() {

        return this.flightCap;
    }

    public void setFlightCap(Integer flightCap) {

        this.flightCap = flightCap;
    }

    public Passengers getPassengers() {

        return this.passengers;
    }

    public void setPassengers(Passengers passengers) {
        this.passengers = passengers;
    }

    public int getGateNum() {

        return this.gateNum;
    }

    public void setGateNum(int gateNum) {
        this.gateNum = gateNum;
    }
}







