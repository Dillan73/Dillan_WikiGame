import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;

public class HtmlParser {

    //time keepers i used to help improve efficiency
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

            //opening the connection to get the html
            URL url = new URL(link);
            URLConnection urlc = url.openConnection();
            urlc.setRequestProperty("User-Agent", "Mozilla 5.0 (Windows; U; " + "Windows NT 5.1; en-US; rv:1.8.0.11) ");

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(urlc.getInputStream())
            );

            timeTaken = System.currentTimeMillis() - innerCurrTime;
            //System.out.println("opening the connection took: " + timeTaken);
            timeForOpening += timeTaken;

            innerCurrTime = System.currentTimeMillis();

            String line;

            //parsing the html
            int total = 0;
            while ( (line = reader.readLine()) != null ) {
                if(!line.contains("/wiki/")){ //ignoring lines w/o the chance for a wiki link
                    continue;
                }

                //splitting by href and src to find possible links
                String[] hrefParts = line.split("href=");
                String[] srcParts = line.split("src=");
                addLinks(hrefParts);
                addLinks(srcParts);
                total += hrefParts.length + srcParts.length;
            }

            timeTaken = System.currentTimeMillis() - innerCurrTime;
            timeForParsing += timeTaken;

        }catch(Exception e){
            //System.out.println("getLinks");
            return null;
        }

        return listOfLinks;
    }

    //Helper readUrls function that takes in a line and adds any links if the link has href to the arraylist
    void addLinks(String[] parts){
        for(int i = parts.length-1; i > 0; i--){
            String after = parts[i].substring(1);
            if(!after.startsWith("/wiki/")){ //not a wiki link --> ignore it
                continue;
            }

            //find where the link ends
            int quoteIndex = after.indexOf("\"");
            int apostropheIndex = after.indexOf("'");
            if(quoteIndex < 0){
                quoteIndex = after.length();
            }
            if(apostropheIndex < 0){
                apostropheIndex = after.length();
            }

            //cuts anything after the end of the link
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
