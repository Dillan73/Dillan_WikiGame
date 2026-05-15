import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class WikiGame {

    private int maxDepth;
    private ArrayList<String> path = new ArrayList<>();

    public static void main(String[] args) {
        WikiGame w = new WikiGame();
    }

    public WikiGame() {

        String startLink = "";  // beginning link, where the program will start
        String endLink = "";    // ending link, where the program is trying to get to
        maxDepth = 1;           // start this at 1 or 2, and if you get it going fast, increase

        for(maxDepth = 0; maxDepth < 2; maxDepth++){
            if(findLink(startLink, endLink, maxDepth)){
                path.add(startLink);
                break;
            }
            //done cuz js moving to next iteration otherwise
        }
        if(!path.isEmpty()){
            System.out.println("found it********************************************************************");
            System.out.println(path);
            //do wtvr w this path that we need to do
        } else {
            System.out.println("did not found it********************************************************************");
        }

    }

    // recursion method
    public boolean findLink(String startLink, String endLink, int depth) {

        System.out.println("depth is: " + depth + ", link is: https://en.wikipedia.org" + startLink);

        //didnt get there in curr value of maxDepth
        if (depth < 0){
            return false;
        }

        //found it
        if(startLink.equals(endLink)){
            path.add(startLink);
            return true;
        }

        //check all links
        String[] innerLinks = findInnerLinks(startLink);
        for(String innerLink : innerLinks){
            if(findLink(innerLink, endLink, depth-1)){
                path.add(startLink);
                return true;
            }
        }

        //false if no possibilities work
        return false;
    }

    String[] findInnerLinks(String link){
        String[] innerLinks = readUrl(link);


        return innerLinks;
    }

    //Turns a into the links by calling href and src
    String[] readUrl(String link){
        ArrayList<String> listOfLinks= new ArrayList<>();
        try{
            URL url = new URL(link);
            URLConnection urlc = url.openConnection();
            urlc.setRequestProperty("User-Agent", "Mozilla 5.0 (Windows; U; " + "Windows NT 5.1; en-US; rv:1.8.0.11) ");

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(urlc.getInputStream())
            );
            String line;
            while ( (line = reader.readLine()) != null ) {
                href(line, listOfLinks);
                src(line, listOfLinks);
            }
            //adding all links with href and or src and or multiple to links arraylist
        }catch(Exception e){
            //System.out.println("getLinks");
            return null;
        }
        String[] allLinks = new String[listOfLinks.size()];
        for(int i = 0; i < listOfLinks.size(); i++){
            allLinks[i] = listOfLinks.get(i);
        }
        return allLinks;
        //converting the links to an array to return
    }

    //Helper readUrls function that takes in a line and adds any links if the link has href to the arraylist
    void href(String line, ArrayList<String> links){
        //adding all the links with href in a line to the arraylist
        String[] parts = line.split("href=");
        for(int i = parts.length-1; i >0; i--){
            String after = parts[i].substring(1);
            int quoteIndex = after.indexOf("\"");
            int apostropheIndex = after.indexOf("\'");
            if(quoteIndex < 0){
                quoteIndex = after.length();
            }
            if(apostropheIndex < 0){
                apostropheIndex = after.length();
            }
            int index = Math.min(quoteIndex, apostropheIndex);
            String link = after.substring(0,index);
            links.add(link);
        }
    }
    //Helper readUrls function that takes in a line and adds any links if the link has src to the arraylist
    void src(String line, ArrayList<String> links){
        //adding all the links with href in a line to the arraylist
        String[] parts = line.split("src=");
        for(int i = parts.length-1; i >0; i--){
            String after = parts[i].substring(1);
            int quoteIndex = after.indexOf("\"");
            int apostropheIndex = after.indexOf("\'");
            if(quoteIndex < 0){
                quoteIndex = after.length();
            }
            if(apostropheIndex < 0){
                apostropheIndex = after.length();
            }
            int index = Math.min(quoteIndex, apostropheIndex);
            String link = after.substring(0,index);
            links.add(link);
        }
        //adding all the links with src in a line to the arraylist
    }

    /*
        recursive function:
            base case(s):
                if depth < 0: return false
                if start=end:
                    return true
            iterative step:
                run findInnerLinks() to get all links
                for each link:
                    if(run findLink(*that link*, endLink, depth-1)){
                        add this link to the arraylist
                        return true
                    if false:
                        return false

        findInnerLinks():
            use old project's code to get an arraylist of links from original url

        project setup:
            for(int i = 0; i < limit (found from speed); i++){
                if(findLink(startLink, endLink, i)){
                    reverse the order of the array and "return" it
                        return as in give it back to user and break
                }
            }
            sout a failure msg like ("your links r too detached for me to help")
    */


}
