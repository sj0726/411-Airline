import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CardInfo {
    private final String Cname;
    private final String Cnumber;
    private final String CexpDate;
    private final String CsecCode;

    public CardInfo(String name, String number, String expDate, String secCode){
        this.Cname = name;
        this.Cnumber = number;
        this.CexpDate = expDate;
        this.CsecCode = secCode;



    }

    public void storeCard() throws IOException {
        FileWriter fw = null;
        BufferedWriter bw = null;
        PrintWriter pw = null;

        try{
            fw = new FileWriter("card_info.txt",true);
            bw = new BufferedWriter(fw);
            pw = new PrintWriter(bw);


            pw.println(this.Cname+ ", "+ this.Cnumber+ ", "+this.CexpDate+ ", "+this.CsecCode);
            System.out.println("Card added to system");
            pw.flush();



        }finally {
            try {
                pw.close();
                bw.close();
                fw.close();

            }catch (IOException io){
        }

    }}

}
