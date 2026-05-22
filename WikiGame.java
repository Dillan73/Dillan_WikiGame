//TODO fix that parenthesis issue
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;
import java.util.TreeSet;
/*
https://www.codecademy.com/resources/docs/java/comparable

 */


//https://en.wikipedia.org/wiki/Donald_Trump%5E^https://en.wikipedia.org/wiki/Joe_Biden
public class WikiGame {

    Hashtable<String, Integer> checked = new Hashtable<>();
    ArrayList<String> path = new ArrayList<>();
    TreeSet<Link> linksToCheck = new TreeSet<>();


    public static void main(String[] args) {
        WikiGame w = new WikiGame();
    }

    //https://en.wikipedia.org/wiki/Joe_Biden
    //https://en.wikipedia.org/wiki/Donald_Trump
    //https://en.wikipedia.org/wiki/Category:American_sex_offenders
    //https://en.wikipedia.org/wiki/Views_of_Kanye_West
    public WikiGame() {

        String startLink = "https://en.wikipedia.org/wiki/Donald_Trump";  // beginning link, where the program will start
        String endLink = "https://en.wikipedia.org/wiki/Views_of_Kanye_West";    // ending link, where the program is trying to get to
        int maxRecursiveDepth = 2;           // start this at 1 or 2, and if you get it going fast, increase

        long time = System.currentTimeMillis();
        for(int maxDepth = 0; maxDepth <= maxRecursiveDepth; maxDepth++){
            if(findLink(startLink, endLink, maxDepth)){
                //path.add(startLink);
                break;
            }
            //done cuz js moving to next iteration otherwise
        }
        time = System.currentTimeMillis() - time;
        if(!path.isEmpty()){
            System.out.println("found it********************************************************************");
            System.out.println(path);
            System.out.println("time taken: " + time);
            //do wtvr w this path that we need to do
        } else {
            System.out.println("did not find it********************************************************************");
            System.out.println("time taken: " + time);
        }

    }

    // recursion method
    public boolean findLink(String startLink, String endLink, int depth) {

        //found it
        if(startLink.equals(endLink)){
            path.addFirst(endLink);
            return true;
        }

        //didnt get there in curr value of maxDepth
        if (depth == 0){
            return false;
        }

        //System.out.println("depth is: " + depth + ", link is: " + startLink);

        //check all links
        String[] innerLinks = findInnerLinks(startLink);
        if(innerLinks == null){
            return false;
        }
        for(String innerLink : innerLinks){
            if(findLink(innerLink, endLink, depth-1)){
                path.addFirst(startLink);
                return true;
            }

        }

        //false if no possibilities work
        return false;
    }

    String[] findInnerLinks(String link){
        ArrayList<String> listOfLinks= new ArrayList<>();
        try{
            URL url = new URL(link);
            URLConnection urlc = url.openConnection();
            urlc.setRequestProperty("User-Agent", "Mozilla 5.0 (Windows; U; " + "Windows NT 5.1; en-US; rv:1.8.0.11) ");

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(urlc.getInputStream())
            );
            String line;
            while ( (line = reader.readLine()) != null ) { //
                String[] hrefParts = line.split("href=");
                String[] srcParts = line.split("src=");
                addLinks(hrefParts, listOfLinks);
                addLinks(srcParts, listOfLinks);
            }
//
            //adding all links with href and or src and or multiple to links arraylist
        }catch(Exception e){
            //System.out.println("getLinks");
            return null;
        }
        String[] allLinks = new String[listOfLinks.size()];
        for(int i = 0; i < listOfLinks.size(); i++){
            allLinks[i] = "https://en.wikipedia.org" + listOfLinks.get(i);
        }
        return allLinks;
        //converting the links to an array to return
    }

    //Helper readUrls function that takes in a line and adds any links if the link has href to the arraylist
    void addLinks(String[] parts, ArrayList<String> links){
        //adding all the links with href in a line to the arraylist
        for(int i = parts.length-1; i >0; i--){
            String after = parts[i].substring(1);
            if(!after.startsWith("/wiki/")){
                continue;
            }
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
