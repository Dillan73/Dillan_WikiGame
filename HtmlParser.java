import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;

public class HtmlParser {
    public long timeForOpening = 0;
    public long timeForParsing = 0;

    long timeTaken = 0;

    String link;
    HashSet<String> listOfLinks= new HashSet<>();

    public HtmlParser(String link){
        this.link=link;
    }

    HashSet<String> findInnerLinks(){

        //adds all links to the listOfLinks by searing for "href=" and "src="
        try{
            long innerCurrTime = System.currentTimeMillis();
            URL url = new URL(link);
            URLConnection urlc = url.openConnection();
            urlc.setRequestProperty("User-Agent", "Mozilla 5.0 (Windows; U; " + "Windows NT 5.1; en-US; rv:1.8.0.11) ");

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(urlc.getInputStream())
            );

            timeTaken = System.currentTimeMillis() - innerCurrTime;
            System.out.println("opening the connection took: " + timeTaken);
            timeForOpening += timeTaken;

            innerCurrTime = System.currentTimeMillis();

            String line;

            int total = 0;
            while ( (line = reader.readLine()) != null ) {
                if(!line.contains("/wiki/")){
                    continue;
                }
                String[] hrefParts = line.split("href=");
                String[] srcParts = line.split("src=");
                addLinks(hrefParts);
                addLinks(srcParts);
//                System.out.println(line);
                total += hrefParts.length + srcParts.length;
//                System.out.println(total);
            }
            System.out.println("total # of parts was: " + total);

            timeTaken = System.currentTimeMillis() - innerCurrTime;
            System.out.println("adding links to the ArrayList took: " + timeTaken);
            timeForParsing += timeTaken;

        }catch(Exception e){
            //System.out.println("getLinks");
            return null;
        }

        return listOfLinks;
        //converting the links to an array to return
    }

    //Helper readUrls function that takes in a line and adds any links if the link has href to the arraylist
    void addLinks(String[] parts){
        for(int i = parts.length-1; i > 0; i--){
            String after = parts[i].substring(1);
            if(!after.startsWith("/wiki/")){ //not a wiki link
                continue;
            }
            //cuts extra part after each link
            int quoteIndex = after.indexOf("\"");
            int apostropheIndex = after.indexOf("'");
            if(quoteIndex < 0){
                quoteIndex = after.length();
            }
            if(apostropheIndex < 0){
                apostropheIndex = after.length();
            }
            int index = Math.min(quoteIndex, apostropheIndex);
            String link = after.substring(0,index);
            if(link.contains(":")){ //a file/etc link that i can ignore
                continue;
            }
            //adds the link to the arraylist
            listOfLinks.add(link);
        }
    }
}
