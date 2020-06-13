import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main {
    private static Connection connection;
    private static Document doc;
    private static ArrayList<String> groups;
    private static ArrayList<String> daysOfWeek = new ArrayList<>();
    private static ArrayList<Element> daySchedule = new ArrayList<>();
    private static ArrayList<Integer> rowsEveryDayNumber;

    static {
        daysOfWeek.add("ПН");
        daysOfWeek.add("ВТ");
        daysOfWeek.add("СР");
        daysOfWeek.add("ЧТ");
        daysOfWeek.add("ПТ");
        daysOfWeek.add("СБ");
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Введите курс(1-5): ");
        int courseValue = Integer.parseInt(reader.readLine());

        connection = setConnection("http://fkn.univer.omsk.su/academics/Schedule/schedule" + courseValue + "_2.htm");
        doc = createDocument(connection);
        Parser tableParser = new TableParser(doc);
        List<List<String>> table = tableParser.parseTable();

        for (int i = 2; i < table.get(0).size(); i++) {
            System.out.println(table.get(0).get(i).substring(table.get(0).get(i).length() - 12, table.get(0).get(i).length() - 5));
        }
        System.out.println("Выберите группу из списка: ");
        String group = reader.readLine().toUpperCase();

        for (int i = 0; i < table.size(); i++) {
            System.out.println(table.get(i).get(0)
                    + " "
                    + table.get(i).get(1)
                    + " "
                    + table.get(i).get(getIndexOfColumnWithGroup(table, group)));
        }

        /*System.out.print("Введите день недели: ");
        String weekDay = reader.readLine();*/



        /*for (int i = 0; i < table.size(); i++) {
            if (table.get(i).contains(weekDay.toUpperCase()))
                System.out.println(table.get(i));
        }*/

    }

    public static int getIndexOfColumnWithGroup(List<List<String>> table, String group) {
        for (int i = 0; i < table.get(0).size(); i++) {
            if (table.get(0).get(i).contains(group)) {
                return i;
            }
        }
        return -1;
    }

















    //ОСТАВИТЬ
    public static Connection setConnection(String url) {
        Connection connectionLocale = null;
        try {
            connectionLocale = Jsoup.connect(url);
        } catch (Exception ex) {
            System.out.println("Check you're ethernet connection or url.");
        }
        System.out.println("Connection established.\r\n");
        return connectionLocale;
    }

    //ОСТАВИТЬ
    public static Document createDocument(Connection connection) {
        Document documentLocale = null;
        try {
            documentLocale = connection.get();
        } catch (IOException ex) {
            System.out.println("Can't create document.");
        }
        System.out.println("The document was created.\r\n");
        return documentLocale;
    }

    //ОСТАВИТЬ
    public static ArrayList<String> setGroups(Document document) {
        ArrayList<String> groupsLocale = new ArrayList<>();
        
        Elements table = document.select("td b");
        
        Iterator<Element> group = table.iterator();
        while (group.hasNext()) {
            groupsLocale.add(group.next().text());
        }
        return groupsLocale;
    }

    //ОСТАВИТЬ
    public static void printGroups(ArrayList<String> groups) {
        for (String group: groups) {
            System.out.println(group);
        }
    }

    // TODO: 03.04.2020
    public static void putDayScheduleInList(String dayOfWeek) {
        Elements table = doc.select("tr td");
        Iterator<Element> group = table.iterator();
        int nextDayIndex = 0;

        if (dayOfWeek.equals(daysOfWeek.get(daysOfWeek.size() - 1))) {
            while (group.hasNext()) {
                Element element = group.next();
                if (element.text().equals(dayOfWeek)) {
                    //System.out.println(element);
                    daySchedule.add(element);
                    while (group.hasNext()) {
                        element = group.next();
                        daySchedule.add(element);
                        //System.out.println(element);
                    }
                    break;
                }
            }
        }
        else {
            for (int i = 0; i < daysOfWeek.size(); i++) {
                if (daysOfWeek.get(i).equals(dayOfWeek)) {
                    nextDayIndex = i + 1;
                    while (group.hasNext()) {
                        Element element = group.next();
                        if (element.text().equals(dayOfWeek)) {

                            while (group.hasNext()) {
                                if (element.text().equals(daysOfWeek.get(nextDayIndex))) {
                                    break;
                                } else {
                                    //System.out.println(element);
                                    daySchedule.add(element);
                                    element = group.next();
                                }
                            }
                            break;
                        }
                    }
                    break;
                }
            }
        }
    }

    //ОСТАВИТЬ
    public static ArrayList<Integer> getRowsOnEveryDayList () {
        ArrayList<Integer> rows = new ArrayList<>();
        Elements data = doc.select("td");
        for (String day: daysOfWeek) {
            for (Element row: data) {
                if (row.text().equals(day)) {
                    rows.add(Integer.parseInt(row.attributes().get("rowspan")));
                    break;
                }
            }
        }
        return rows;
    }

    //ОСТАВИТЬ
    public static ArrayList<Element> putCurrentDayScheduleInList(ArrayList<Integer> rowsNumber, int index) {
        ArrayList<Element> daySchedule = new ArrayList<>();
        int sumPreviousIndexes = 1;
        if (index > 0) {
            for (int i = 0; i < index; i++) {
                sumPreviousIndexes += rowsNumber.get(i);
            }
        }
        Elements table = doc.select("tr");
        for (int i = sumPreviousIndexes; i < sumPreviousIndexes + rowsNumber.get(index); i++) {
            daySchedule.add(table.get(i));
        }
        return daySchedule;
    }
}
