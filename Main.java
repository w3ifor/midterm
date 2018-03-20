import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);
        String websiteDnD = "https://aps2.missouriwestern.edu/schedule/Default.asp?tck=201910";//disciplines and depts
        ArrayList<String> disciplinesAbbrev = new ArrayList<>();
        ArrayList<String> disciplinesFull = new ArrayList<>();
        ArrayList<String> deptsAbbrev = new ArrayList<>();
        ArrayList<String> deptsFull = new ArrayList<>();
        Sqlite db = new Sqlite();
        db.makeConnection("classes.db");
        try {
            Document docDisc = Jsoup.connect(websiteDnD).get();
            if (db.makeConnection("classes.db")) {//if connected add data to database.
                System.out.println("Database opened!");
                addToDatabase(deptsAbbrev, deptsFull, "departments", db);
                System.out.println("Data inserted into departments table");
            } else {
                System.out.println("Database failed to open!");
            }
            char letter = 'n';
            while (letter != 'Q') {//while letter doesnt equal Q it continues the loop
                printMenu();
                String command = scan.next();
                letter = executeCommand(command, db, docDisc, disciplinesAbbrev, disciplinesFull, deptsAbbrev, deptsFull);
            }


        } catch (IOException e) {
            ///e.printStackTrace();
            System.err.println("Error: " + e.getMessage());
        }

    }

    public static void printMenu() {
        System.out.println(" A= Erase and Build Subjects table\n B=Erase and Build Department Table\n C=prints subject table\n D= prints department table\n" +
                " E=Prints Report of disciplines by department\n G=Erase and build sections data\n H=prints a listing of sections\n I(i)= Prints faculty and their schedules by department,\n" +
                " J= Print control-break section report for a department\n K= Produces control-break\n Q=quit program");
        System.out.println("Enter a command: ");
    }

    public static char executeCommand(String command, Sqlite db, Document docDisc, ArrayList<String> disciplinesAbbrev, ArrayList<String> disciplinesFull, ArrayList<String> deptsAbbrev, ArrayList<String> deptsFull) {
        String cmd = "";
        char returned = 'j';
        cmd = command.toUpperCase();
        if (cmd.equals("A")) {
            db.deleteTable("subjects");
            db.createTable("subjects", "abbreviation", "fullName");
            Elements discOptions = docDisc.select("#subject option");//finds the label with id=subject and pulls all option tags from it
            System.out.println("-------");
            for (Element option : discOptions) {
                addToArraylist(disciplinesAbbrev, disciplinesFull, option.val(), option.text());

            }
            disciplinesAbbrev.remove(0);//removes the first entry which is a title of sorts.
            disciplinesFull.remove(0);//see above comment
            if (db.makeConnection("classes.db")) {//if connected add data to database.
                System.out.println("Database opened!");
                addToDatabase(disciplinesAbbrev, disciplinesFull, "subjects", db);
                System.out.println("Data inserted into subjects table");
            } else {
                System.out.println("Database failed to open!");
            }
            returned = 'A';
        } else if (cmd.equals("Q")) {
            System.out.println("Closing.");
            System.exit(10);
            returned = 'Q';
        } else if (cmd.equals("B")) {
            db.deleteTable("departments");
            db.createTable("departments", "abbreviation", "fullName");
            Elements deptsOptions = docDisc.select("#department option");//finds the label with id=department and pulls option tags
            System.out.println("----");
            for (Element option : deptsOptions) {
                addToArraylist(deptsAbbrev, deptsFull, option.val(), option.text());
            }
            deptsAbbrev.remove(0);//removes the title
            deptsFull.remove(0);//removes the title
            if (db.makeConnection("classes.db")) {//if connected add data to database.
                addToDatabase(deptsAbbrev, deptsFull, "departments", db);
                System.out.println("Data inserted into departments table");
            } else {
                System.out.println("Database failed to open!");
            }
            returned = 'B';
        } else if (cmd.equals("C")) {//prints the table subjects
            db.printTable("subjects", "abbreviation", "fullName");
            returned = 'C';
        } else if (cmd.equals("D")) {//prints the table departments
            db.printTable("departments", "abbreviation", "fullName");
            returned = 'D';
        } else if(cmd.equals("E")){
            returned='E';

        }else if (cmd.equals("G")) {//prints a report of discplines(subjects) by department
            db.deleteTable("sections");
            db.createTable("sections", "department", "CRN", "course", "Title"
                    , "courseURL", "Sec", "ClassType", "Hrs", "Days", "Times", "Room", "Instructor", "maxEntrollment", "seatAvailable", "note",
                    "courseMessage", "beginDate", "endDate", "titleFee", "feePerCredit", "term");
            for(int x=0;x<deptsAbbrev.size();x++){
                try {
                    getData("https://aps2.missouriwestern.edu/schedule/Default.asp?tck=201910",deptsAbbrev.get(x),"");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            returned = 'G';
        }else if(cmd.equals("H")){
            db.printTable("sections", "department", "CRN", "course", "Title"
                    , "courseURL", "Sec", "ClassType", "Hrs", "Days", "Times", "Room", "Instructor", "maxEntrollment", "seatAvailable", "note",
                    "courseMessage", "beginDate", "endDate", "titleFee", "feePerCredit", "term");
            returned='H';
        }else if(cmd.equals("I")){
            db.printTable("sections","Instructor","Days","Times");
            returned='I';
        }else if(cmd.equals("J")){
            Scanner kbd = new Scanner(System.in);
            db.deleteTable("sections");
            db.createTable("sections", "department", "CRN", "course", "Title"
                    , "courseURL", "Sec", "ClassType", "Hrs", "Days", "Times", "Room", "Instructor", "maxEntrollment", "seatAvailable", "note",
                    "courseMessage", "beginDate", "endDate", "titleFee", "feePerCredit", "term");
            for(int x=0;x<deptsAbbrev.size();x++){
                try {
                    getData("https://aps2.missouriwestern.edu/schedule/Default.asp?tck=201910", deptsAbbrev.get(x), "");
                    pause(kbd);
                } catch (Exception e) {
                    e.printStackTrace();
            }
        }
        }else if(cmd.equals("K")){
            Scanner kbd = new Scanner(System.in);
            for(int x=0;x<deptsAbbrev.size();x++){
                try {
                    db.printTable("sections", "department", "CRN", "course", "Title"
                            , "courseURL", "Sec", "ClassType", "Hrs", "Days", "Times", "Room", "Instructor", "maxEntrollment", "seatAvailable", "note",
                            "courseMessage", "beginDate", "endDate", "titleFee", "feePerCredit", "term");
                    System.out.println("Finish "+deptsAbbrev.get(x)+" department.");
                    pause(kbd);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            returned ='K';
        }
        return returned;
    }

    private static void pause(Scanner kbd) {
        System.out.println();
        System.out.print("Next department.");
        kbd.nextLine();
        System.out.println();
    }

    public static void addToDatabase(ArrayList<String> abbrev, ArrayList<String> fullName, String tableName, Sqlite db) {//adds data to opened database
        for (int i = 0; i < abbrev.size(); i++) {
            db.insert(tableName, abbrev.get(i), fullName.get(i));
        }
    }

    public static void addToArraylist(ArrayList<String> addVal, ArrayList<String> addFull, String val, String full) {//adds scraped data into arraylists
        addVal.add(val);
        addFull.add(full);
    }

    public static java.sql.Connection getConnection() throws Exception {
        try {
            String dbFile = "jdbc:sqlite:classes.db";
            java.sql.Connection conn = DriverManager.getConnection(dbFile);
            return conn;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public static void getData(String web, String department, String classes) throws Exception {
        String temp = department;
        ArrayList<Section> sections = new ArrayList<Section>();
        Section sec = new Section();
        String decimalPattern = "([0-9]*)\\.([0-9]*)";
        java.sql.Connection con = getConnection();
        Connection.Response response = Jsoup.connect(web).method(Connection.Method.POST).timeout(10 * 100000)
                .data("course_number", classes)
                .data("subject", "ALL")
                .data("department", department)
                .data("displat_close", "YES")
                .data("course_type", "ALL")
                .followRedirects(true).execute();
        Document doc = response.parse();
        Elements detailrows = doc.select("span.course_messages");
        Elements seats = doc.select("span.course_seats");
        Elements terms = doc.select("span.course_term");
        Elements rows = doc.select("tr.list_row");
        int increase = 0;
        for (Element row : rows) {
            sec = new Section();
            Elements cells = row.select("td").select("td");
            PreparedStatement posted = con.prepareStatement("INSERT INTO sections(department,CRN,course,title,courseurl,Sec,classtype,Hrs,Days,Times,room ,instructor,maxEntrollment,seatAvailable,note,coursemessage,beginDate,endDate,titlefee,feepercredit,term)" +
                    "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            switch (cells.size()) {

                case 10:

                    Element classUrls = cells.select("a").first();
                    String url = classUrls.attr("href");
                    String[] number = seats.get(increase).text().split(" ");
                    sec.seatAvailable = Integer.parseInt(number[6]);
                    sec.maxEnrollment = Integer.parseInt(number[2]);
                    sec.term = terms.get(increase).text();
                    String[] fee = detailrows.get(increase).text().split(" ");
                    for (int i = 0; i < fee.length; i++) {
                        boolean match = Pattern.matches(decimalPattern, fee[i]);
                        if (match == true) {
                            sec.perCreditFee = Double.parseDouble(fee[i]);

                        }
                    }

                    sec.courseMessage = detailrows.get(0).text();
                    sec.courseUrl = url;
                    Document classDetail = Jsoup.connect("https://aps2.missouriwestern.edu/schedule/" + url).get();
                    Elements ulists = classDetail.select("ul.details");
                    Elements lists = ulists.select("li");
                    sec.beginDate = lists.get(5).text().split(" ")[2];
                    sec.endDate = lists.get(6).text().split(" ")[2];
                    sec.department = department;
                    sec.crn = cells.get(0).text();
                    sec.course = cells.get(1).text();
                    sec.section = cells.get(2).text();
                    sec.type = cells.get(3).text();
                    sec.title = cells.get(4).text();
                    int Hrs = Integer.parseInt(cells.get(5).text());
                    sec.creditHrs = Hrs;
                    sec.days = cells.get(6).text();
                    sec.times = cells.get(7).text();
                    sec.room = cells.get(8).text();
                    sec.instructor = cells.get(9).text();
                    posted.setString(1, sec.department);
                    posted.setString(14, String.valueOf(sec.seatAvailable));
                    posted.setString(13, String.valueOf(sec.maxEnrollment));
                    posted.setString(21, sec.term);
                    posted.setString(19, String.valueOf(sec.perCreditFee));
                    posted.setString(20, String.valueOf(sec.perCreditFee * sec.creditHrs));
                    posted.setString(16, sec.courseMessage);
                    posted.setString(15, sec.courseMessage);
                    posted.setString(5, sec.courseUrl);
                    posted.setString(17, sec.beginDate);
                    posted.setString(18, sec.endDate);
                    posted.setString(2, sec.crn);
                    posted.setString(3, sec.course);
                    posted.setString(6, sec.section);
                    posted.setString(7, sec.type);
                    posted.setString(4, sec.title);
                    posted.setString(8, String.valueOf(sec.creditHrs));
                    posted.setString(9, sec.days);
                    posted.setString(10, sec.times);
                    posted.setString(11, sec.room);
                    posted.setString(12, sec.instructor);
                    posted.executeUpdate();
                    sections.add(sec);
                    increase++;
                    break;
                case 5:
                    sec.days = cells.get(1).text();
                    sec.times = cells.get(2).text();
                    sec.room = cells.get(3).text();
                    posted.setString(9, sec.days);
                    posted.setString(10, sec.times);
                    posted.setString(11, sec.room);
                    posted.executeUpdate();
                    posted.close();
                    sections.add(sec);
                    break;
                default:
                    System.out.println("Error");
                    System.exit(1);
            }
        }

        con.close();
    }
}

class Section {
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

    public Section() {
        courseMessage = "Nothing";
        addFee = 0;
        maxEnrollment = 18;
        creditHrs = 3;
        instructor = "staff";
        seatAvailable = 20;
        titleFee = 0;
        days = "Online";
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
                ", addFee=" + creditHrs * perCreditFee +
                ", titleFee=" + titleFee +
                ", perCreditFee=" + perCreditFee +
                '}';
    }

}