import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;


public class midterm {
    public static void main(String args[]) throws IOException {
        String website="https://aps2.missouriwestern.edu/schedule/default.asp?tck=201910";
        getData(website,"csc");
    }
    public static void getData(String web,String classes) throws IOException {
        ArrayList<Section> sections = new ArrayList<Section>();
        String decimalPattern = "([0-9]*)\\.([0-9]*)";
        Connection.Response response=Jsoup.connect(web).method(Connection.Method.POST).timeout(10*1000)
                .data("course_number",classes).followRedirects(true).execute();
        Document doc=response.parse();
        Elements detailrows=doc.select("span.course_messages");
        Elements seats=doc.select("span.course_seats");
        Elements terms=doc.select("span.course_term");
        Elements rows=doc.select("tr.list_row");
        int count=0,increase=0;
        for(Element row:rows){
            Section sec=new Section();

            Elements cells=row.select("td").select("td");
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

            sections.add(sec);
            increase++;
            }

        for (int i=0;i<sections.size();i++) {
           System.out.println(sections.get(i));

        }

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