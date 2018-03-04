import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.time.Year;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;


public class midterm {
    public static void main(String args[]) throws Exception {
        String website="https://aps2.missouriwestern.edu/schedule/default.asp?tck=201910";
        getData(website,"CSMP","mat");
    }



    public static java.sql.Connection getConnection() throws Exception{
        try{
            String dbFile="jdbc:sqlite:classes.db";
            java.sql.Connection conn= DriverManager.getConnection(dbFile);
            return conn;
        }catch (Exception e){
            System.out.println(e);
        }
        finally {
            System.out.println("Connected to data base");
        }
        return null;
    }
    public static void getData(String web,String department,String classes) throws Exception {
        ArrayList<Section> sections = new ArrayList<Section>();
        Section sec=new Section();
        String decimalPattern = "([0-9]*)\\.([0-9]*)";
        java.sql.Connection con=getConnection();
        Connection.Response response=Jsoup.connect(web).method(Connection.Method.POST).timeout(10*10000)
                .data("course_number",classes)
                .data("subject","all")
                .data("department",department)
                .data("displat_close","YES")
                .data("course_type","ALL")
                .followRedirects(true).execute();
        Document doc=response.parse();
        Elements detailrows=doc.select("span.course_messages");
        Elements seats=doc.select("span.course_seats");
        Elements terms=doc.select("span.course_term");
        Elements rows=doc.select("tr.list_row");
        int count=0,increase=0;
        for(Element row:rows){
            Elements cells=row.select("td").select("td");
            PreparedStatement posted=con.prepareStatement("INSERT INTO classes(CRN,course,Sec,classtype,Title,Hrs,Days,Times,room ,instructor)" +
                    "VALUES (?,?,?,?,?,?,?,?,?,?)");
            switch(cells.size()){

                case 10:

            Element classUrls=cells.select("a").first();
            String url=classUrls.attr("href");
            String []number=seats.get(increase).text().split(" ");
            sec.seatAvailable=Integer.parseInt(number[6]);
            sec.maxEnrollment=Integer.parseInt(number[2]);
            sec.term=terms.get(increase).text();
            String []fee=detailrows.get(increase).text().split(" ");
            for(int i=0;i<fee.length;i++){
                boolean match = Pattern.matches(decimalPattern, fee[i]);
                if(match==true){
                    sec.perCreditFee=Double.parseDouble(fee[i]);
                }
            }

            sec.courseMessage=detailrows.get(0).text();
            sec.courseUrl=url;
            Document classDetail=Jsoup.connect("https://aps2.missouriwestern.edu/schedule/"+url).get();
            Elements ulists=classDetail.select("ul.details");
            Elements lists=ulists.select("li");
            sec.beginDate=lists.get(5).text();
            sec.endDate=lists.get(6).text();
            for(Element cell:cells){
                count++;}
                sec.department=department;
            sec.crn=cells.get(0).text();
            sec.course=cells.get(1).text();
            sec.section=cells.get(2).text();
            sec.type=cells.get(3).text();
            sec.title=cells.get(4).text();
            int Hrs=Integer.parseInt(cells.get(5).text());
            sec.creditHrs=Hrs;
            sec.days=cells.get(6).text();
            sec.times=cells.get(7).text();
            sec.room=cells.get(8).text();
            sec.instructor=cells.get(9).text();
                    posted.setString(1,sec.crn);
                    posted.setString(2,sec.course);
                    posted.setString(3,sec.section);
                    posted.setString(4,sec.type);
                    posted.setString(5,sec.title);
                    posted.setString(6, String.valueOf(sec.creditHrs));
                    posted.setString(7,sec.days);
                    posted.setString(8,sec.times);
                    posted.setString(9,sec.room);
                    posted.setString(10,sec.instructor);
                    posted.executeUpdate();
            increase++;
            break;
            case 5:
                    sec.days=cells.get(1).text();
                    sec.times=cells.get(2).text();
                    sec.room=cells.get(3).text();
                posted.setString(7,sec.days);
                posted.setString(8,sec.times);
                posted.setString(9,sec.room);
                posted.executeUpdate();
                posted.close();
                sections.add(sec);
                    break;
            default:
                System.out.println("Error");
                System.exit(1);
        }




        for (int i=0;i<sections.size();i++) {
           //System.out.println(sections.get(i));

        }
        }
        System.out.println("All inserts finished");
            con.close();
    }
    private static void showMenu(){
        char ch;
        do{
            System.out.println("\tA.Erase and Build Subjects table");
            System.out.println("\tB.Erase and Build Departments table");
            System.out.println("\tC.Print Subjects table");
            System.out.println("\tD.Print Departments table");
            System.out.println("\tE.Print the report of disciplines by Department");
            System.out.println("\tG.Erase and build sections data (Will be prompted for the department)");
            System.out.println("\tH.Print a simple listing of all sections by department or by discipline");
            System.out.println("\tI.Print faculty and faculty schedules  by department\n");
            System.out.println("\tJ.Print control-break section report for a department");
            System.out.println("\tK.Produce the control-break output");
            System.out.println("\tQ.Quit");

            Scanner input=new Scanner(System.in);
            System.out.println("Type a letter:");
            String s =input.next().toUpperCase().trim();
            ch = (s.length()>0)? s.charAt(0):'x';
            switch (ch){
                case 'A':
                    //connectToDB(eraseSubjectsTable());
                    break;
                case 'B':
                    //connectToDB(eraseDepartmentsTable());
                    break;
                case 'C':
                    printSubjectsTable();
                    break;
                case 'D':
                    printDepartsTable();
                    break;
                case 'E':
                    printByDepartments();
                    break;
                case 'G':
                    eraseSectionData();
                    break;
                case 'H':
                    printSections();
                    break;
                case 'I':
                    printInfoOfFfaculty();
                    break;
                case 'J':
                    printControlBreakSection();
                    break;
                case 'K':
                    produceControlBreakOutput();
                    break;
                case 'Q':
                    System.out.println("Cya!!");
                    break;
            }
        }
        while(ch!='Q');
        }

    private static String eraseSubjectsTable() {
        return " ";

    }
    private static String printByDepartments() {
        return " ";
    }
    private static void printDepartsTable() {
    }
    private static void printSubjectsTable() {
    }
    private static String eraseDepartmentsTable() {
        return "";
    }
    private static void eraseSectionData() {
    }
    private static void printSections() {
    }
    private static void printInfoOfFfaculty() {
    }
    private static void printControlBreakSection() {
    }
    private static void produceControlBreakOutput() {
    }

    }




class Section{
    String department;
    String notes;
    String crn;
    String course;
    String section;
    String courseUrl;
    String type;
    String courseMessage;
    int seatAvailable;
    String beginDate;
    String endDate;
    int maxEnrollment;
    String title;
    int creditHrs;
    String days;
    String times;
    String room;
    String instructor;
    String term;
    double addFee;
    double titleFee;

    double perCreditFee;
    public Section(){
        courseMessage="Nothing";
        addFee=0;
        maxEnrollment=18;
        creditHrs=3;
        instructor="staff";
        seatAvailable=20;
        titleFee=0;
        days="Online";
    }

    @Override
    public String toString() {
        return "Section{" +
                "department='" + department + '\'' +
                ", notes='" + notes + '\'' +
                ", crn='" + crn + '\'' +
                ", course='" + course + '\'' +
                ", section='" + section + '\'' +
                ", courseUrl='" + courseUrl + '\'' +
                ", type='" + type + '\'' +
                ", courseMessage='" + courseMessage + '\'' +
                ", seatAvailable=" + seatAvailable +
                ", beginDate='" + beginDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", maxEnrollment=" + maxEnrollment +
                ", title='" + title + '\'' +
                ", creditHrs=" + creditHrs +
                ", days='" + days + '\'' +
                ", times='" + times + '\'' +
                ", room='" + room + '\'' +
                ", instructor='" + instructor + '\'' +
                ", term='" + term + '\'' +
                ", addFee=" + creditHrs*perCreditFee +
                ", titleFee=" + titleFee +
                ", perCreditFee=" + perCreditFee +
                '}';
    }

}