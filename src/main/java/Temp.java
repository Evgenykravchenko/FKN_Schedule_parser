import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;



public class Temp {

    public static void main(String[] args) throws IOException {
        //given:
        Connection connection = Jsoup.connect("http://fkn.univer.omsk.su/academics/Schedule/schedule2_2.htm");
        //when:
        final Document document = connection.get();


    }
}