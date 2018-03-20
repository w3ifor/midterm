import java.sql.*;
import java.util.ArrayList;

public class Sqlite {
    static Connection conn;
    ArrayList<String> data1 = new ArrayList<>();
    ArrayList<String> data2 = new ArrayList<>();
    ArrayList<String> data3 = new ArrayList<>();
    ArrayList<String> data4 = new ArrayList<>();
    ArrayList<String> data5 = new ArrayList<>();
    ArrayList<String> data6 = new ArrayList<>();
    ArrayList<String> data7 = new ArrayList<>();
    ArrayList<String> data8 = new ArrayList<>();
    ArrayList<String> data9 = new ArrayList<>();
    ArrayList<String> data10 = new ArrayList<>();
    ArrayList<String> data11= new ArrayList<>();
    ArrayList<String> data12 =new ArrayList<>();
    ArrayList<String> data13= new ArrayList<>();
    ArrayList<String> data14= new ArrayList<>();
    ArrayList<String> data15 = new ArrayList<>();
    ArrayList<String> data16 = new ArrayList<>();
    ArrayList<String> data17 = new ArrayList<>();
    ArrayList<String> data18 = new ArrayList<>();
    ArrayList<String> data19 = new ArrayList<>();
    ArrayList<String> data20 = new ArrayList<>();
    ArrayList<String> data21= new ArrayList<>();
    ArrayList<String> data22 = new ArrayList<>();
    ArrayList<String> data23 = new ArrayList<>();
    ArrayList<String> data24 = new ArrayList<>();
    ArrayList<String> data25 = new ArrayList<>();
    ArrayList<String> data26 = new ArrayList<>();

    public boolean makeConnection(String fileName) {//this connects to the database
        boolean successfulOpen = false;
        String connectString = "jdbc:sqlite:" + fileName;
        try {
            conn = DriverManager.getConnection(connectString);
            if (conn != null) {
                successfulOpen = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return successfulOpen;
    }

    static void close() {//closes database
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void insert(String tableName, String abbrev, String fullName) {//inserts data into table tableName
        String query = "INSERT INTO " + tableName + "(abbreviation,fullName) VALUES(?,?)";
        try {
            PreparedStatement pstmst = conn.prepareStatement(query);
            pstmst.setString(1, abbrev);
            pstmst.setString(2, fullName);
            pstmst.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void deleteTable(String tableName) {//deletes a table in scraping.db with name tableName
        String query = "DROP TABLE " + tableName;
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void createTable(String tableName, String column1Name, String column2Name) {
        String query = "create table if not exists " + tableName + "(" + column1Name + ", " + column2Name + ")";
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    void createTable(String tableName, String column1Name, String column2Name, String column3Name, String column4Name,
                     String column5Name, String column6Name, String column7Name, String column8Name, String column9Name,
                     String column10Name, String column11Name, String column12Name, String column13Name, String column14Name,
                     String column15Name, String column16Name, String column17Name, String column18Name, String column19Name,
                     String column20Name, String column21Name) {
        String query = "create table if not exists " + tableName + "(" + column1Name + ", " + column2Name + ", "+column3Name+", "+column4Name+"" +
                ", "+column5Name+" , " + column6Name + ", " + column7Name + ", " + column8Name + ", " + column9Name + ", " + column10Name + ", " + column11Name +
                ", "+column12Name+" , " + column13Name + ", " + column14Name + ", " + column15Name + ", " + column16Name + ", " + column17Name + ", " + column18Name +
                ", "+column19Name+" , " + column20Name + ", "+column21Name+")";
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    void selectFromTable(String tableName, String column1, String column2) {
        String query = "SELECT " + column1 + ", " + column2 + " FROM " + tableName;
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String insertedDataC1 = rs.getString(column1);
                String insertedDataC2 = rs.getString(column2);
                data1.add(insertedDataC1);
                data2.add(insertedDataC2);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    void selectFromTable(String tableName, String column1, String column2,String column3) {
        String query = "SELECT " + column1 + ", " + column2 + ","+column3+" FROM " + tableName+" ORDER BY "+column1+" ASC";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String insertedDataC1 = rs.getString(column1);
                String insertedDataC2 = rs.getString(column2);
                String insertedDataC3 = rs.getString(column3);
                data24.add(insertedDataC1);
                data25.add(insertedDataC2);
                data26.add(insertedDataC3);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    void selectFromTable(String tableName, String column1, String column2, String column3, String column4, String column5,
                         String column6, String column7, String column8, String column9, String column10, String column11,
                         String column12, String column13, String column14, String column15, String column16, String column17,
                         String column18, String column19, String column20, String column21) {
        String query = "SELECT " + column1 + ", " + column2 + ", " + column3 + ", " + column4 + ", " + column5 + ", " + column6 + "" +
                ", " + column7 + "" +
                ", " + column8 + "" +
                ", " + column9 + ", " + column10 + ", " + column11 + ", " + column12 + ", " + column13 + ", " + column14 + ", " + column15 + "" +
                ", " + column16 + ", " + column17 + ", " + column18 + ", " + column19 + ", " + column20 + ", " + column21 + " FROM " + tableName;
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String insertedDataC1 = rs.getString(column1);
                String insertedDataC2= rs.getString(column2);
                String insertedDataC3 = rs.getString(column3);
                String insertedDataC4= rs.getString(column4);
                String insertedDataC5 = rs.getString(column5);
                String insertedDataC6= rs.getString(column6);
                String insertedDataC7 = rs.getString(column7);
                String insertedDataC8= rs.getString(column8);
                String insertedDataC9 = rs.getString(column9);
                String insertedDataC10= rs.getString(column10);
                String insertedDataC11 = rs.getString(column11);
                String insertedDataC12= rs.getString(column12);
                String insertedDataC13 = rs.getString(column13);
                String insertedDataC14= rs.getString(column14);
                String insertedDataC15 = rs.getString(column15);
                String insertedDataC16= rs.getString(column16);
                String insertedDataC17 = rs.getString(column17);
                String insertedDataC18= rs.getString(column18);
                String insertedDataC19 = rs.getString(column19);
                String insertedDataC20= rs.getString(column20);
                String insertedDataC21= rs.getString(column21);

                data3.add(insertedDataC1);
                data4.add(insertedDataC2);
                data5.add(insertedDataC3);
                data6.add(insertedDataC4);
                data7.add(insertedDataC5);
                data8.add(insertedDataC6);
                data9.add(insertedDataC7);
                data10.add(insertedDataC8);
                data11.add(insertedDataC9);
                data12.add(insertedDataC10);
                data13.add(insertedDataC11);
                data14.add(insertedDataC12);
                data15.add(insertedDataC13);
                data16.add(insertedDataC14);
                data17.add(insertedDataC15);
                data18.add(insertedDataC16);
                data19.add(insertedDataC17);
                data20.add(insertedDataC18);
                data21.add(insertedDataC19);
                data22.add(insertedDataC20);;
                data23.add(insertedDataC21);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void printTable(String tableName, String column1, String column2) {
        selectFromTable(tableName, column1, column2);
        System.out.println("Data for " + tableName);
        System.out.printf("| %-4s ||%-30s |\n", column1, column2);
        for (int i = 0; i < data1.size(); i++) {
            System.out.printf("| %4s || %-30s |\n", data1.get(i), data2.get(i));
        }
    }
    void printTable(String tableName, String column1, String column2,String column3) {
        selectFromTable(tableName, column1, column2,column3);
        System.out.println("Data for " + tableName);
        System.out.printf("| %-30s ||%-20s || %-20s |\n", column1, column2,column3);
        for (int i = 0; i < data24.size(); i++) {
            System.out.printf("| %-30s ||%-20s || %-20s |\n", data24.get(i), data25.get(i),data26.get(i));
        }
    }
    void printTable(String tableName, String column1, String column2, String column3, String column4, String column5,
                    String column6, String column7, String column8, String column9, String column10, String column11,
                    String column12, String column13, String column14, String column15, String column16, String column17,
                    String column18, String column19, String column20, String column21) {
        selectFromTable(tableName, column1, column2, column3, column4, column5, column6, column7, column8, column9, column10, column11,
                column12, column13, column14, column15, column16,
                column17, column18, column19, column20, column21);
        System.out.println("Data for " + tableName);
        System.out.printf("|%4s||%-6s||%-7s||%-25s||%-3s||%-2s||%-2s||%-4s||%-17s||%-17s||%-17s||%-20s||%-3s||%-3s||%-30s||%-30s||%-30s||%-30s|| %-30s |\n",
                column1, column2, column3, column4, column5, column6, column7, column8, column9, column10, column11, column2, column12, column13, column14, column15, column16,
                column17, column18, column19, column20, column21);
        for (int i = 0; i < data3.size(); i++) {
            System.out.printf("|%4s||%-6s||%-7s||%-25s||%-3s||%-2s||%-2s||%-4s||%-17s||%-17s||%-17s||%-20s||%-3s||%-3s||%-30s||%-30s||%-30s||%-30s|| %-30s |\n", data3.get(i), data4.get(i), data5.get(i), data6.get(i), data7.get(i), data8.get(i), data9.get(i), data10.get(i)
                    , data11.get(i), data12.get(i), data13.get(i), data14.get(i)
                    , data15.get(i), data16.get(i), data17.get(i), data18.get(i), data19.get(i), data20.get(i), data21.get(i), data22.get(i), data23.get(i));
        }
    }
}
