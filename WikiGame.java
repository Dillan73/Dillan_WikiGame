//TODO fix that parenthesis issue?
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

/*
https://www.codecademy.com/resources/docs/java/comparable

links that i used in testing and kept here to easily copy
    //https://en.wikipedia.org/wiki/Joe_Biden
    //https://en.wikipedia.org/wiki/Donald_Trump
    //https://en.wikipedia.org/wiki/Category:American_sex_offenders
    //https://en.wikipedia.org/wiki/Views_of_Kanye_West
    //https://en.wikipedia.org/wiki/Wyoming_Democratic_Party
    //https://en.wikipedia.org/wiki/The_College_Dropout

    //https://www.thewikigame.com/play/String_theory
 */


public class WikiGame {

    long timeForOpening = 0;
    long timeForParsing = 0;
    int timesRan = 0;

    Hashtable<String, String> origins = new Hashtable<>(); //where i store links' origins (to prevent dupes and remake the path)

    ArrayList<String> path = new ArrayList<>(); // this is the path from start to end
    ArrayDeque<Link> toCheck = new ArrayDeque<>(); //this is the treeset of links to check in order
    boolean finished = false; //this tells me im done (idt i need this but i made it)

    //TODO: connect to input
    String startLink = "https://en.wikipedia.org/wiki/League_of_Nations";  // beginning link, where the program will start
    String endLink = "https://en.wikipedia.org/wiki/Documentary_film";    // ending link, where the program is trying to get to
    int maxDepth = 2; //the max depth its allowed to go to

    long time = System.currentTimeMillis();
    long currTime = 0;
    long timeTaken = 0;

    public static void main(String[] args) {
        WikiGame w = new WikiGame();
    }


    public WikiGame() {
        toCheck.add(new Link(startLink, 0));
        if(startLink.equals(endLink)){
            path.add(endLink);
            //TODO: make this be done + connect to output
        }
        origins.put(startLink, "THE_START_LINK");

        boolean success = findLink();


        timeTaken = System.currentTimeMillis() - time;
        if(success){
            System.out.println("found it********************************************************************");
            System.out.println(path);
            System.out.println("time taken: " + timeTaken);
            //TODO:connect to output
        } else {
            System.out.println("did not find it********************************************************************");
            System.out.println("time taken: " + timeTaken);
            //TODO:connect to output
        }

        System.out.println("toCheck ended with: " + toCheck.size());
        System.out.println("time to open connections was: " + timeForOpening);
        System.out.println("time for parsing was: " + timeForParsing);
        System.out.println("other time was: " + (timeTaken-timeForOpening-timeForParsing));

        System.out.println("in total, find links ran: " + timesRan + " times");

    }

    // recursion method
    public boolean findLink() {
        timesRan++;
        if(toCheck.isEmpty()){ //nothing to check
            return false;
        }

        //takes the first link from the treeset and its data
        Link currLinkVariable = toCheck.pollFirst();
        String currLink = currLinkVariable.url;
        int depth = currLinkVariable.depth;

        if(depth == maxDepth){ //all links in toCheck have been checked to see if its endLink, and none are below maxDepth --> anything else that gets to endLink would go past maxDepth
            System.out.println("hit maxDepth with: " + toCheck.size() + " items in toCheck");
            return false;
        }

        System.out.println("running with " + currLink);

        //grabs all links from this link and adds it to toCheck
        currTime = System.currentTimeMillis();
        String[] innerLinks = findInnerLinks(currLink);

        timeTaken = System.currentTimeMillis() - currTime;

        System.out.println("findInners took: " + timeTaken);
        currTime = System.currentTimeMillis();

        if(innerLinks == null){
            return findLink();
        }
        int added = 0;
        for(String innerLink : innerLinks){
            if(innerLink.equals(endLink)){
                timeTaken = System.currentTimeMillis() - time;
                System.out.println("*****found it at time = " + timeTaken);

                path.add(innerLink); //adds the endLink
                fillPath(currLink); //uses origin to recursively fill the path
                return true;
            }
            if(origins.containsKey(innerLink)){ //if ive already seen this link
                continue;
            }

            //if this link is new and not the end link, adds it to the origins and toCheck
            origins.put(innerLink, currLink);
            toCheck.add(new Link(innerLink, depth+1));
            added++;
        }
        System.out.println("Made it to the end and added " + added + " links");

        timeTaken = System.currentTimeMillis() - currTime;
        System.out.println("rest took: " + timeTaken);
        return findLink();
    }

    public void fillPath(String link){
        if(link.equals("THE_START_LINK")){
            return;
        }
        path.addFirst(link);
        link = origins.get(link);
        fillPath(link);
    }

    String[] findInnerLinks(String link){
        ArrayList<String> listOfLinks= new ArrayList<>();

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
                addLinks(hrefParts, listOfLinks);
                addLinks(srcParts, listOfLinks);
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


        String[] allLinks = new String[listOfLinks.size()];
        for(int i = 0; i < listOfLinks.size(); i++){
            allLinks[i] = "https://en.wikipedia.org" + listOfLinks.get(i);
        }
        System.out.println("total number of links was: " + allLinks.length);
        return allLinks;
        //converting the links to an array to return
    }

    //Helper readUrls function that takes in a line and adds any links if the link has href to the arraylist
    void addLinks(String[] parts, ArrayList<String> links){
        for(int i = parts.length-1; i > 0; i--){
            String after = parts[i].substring(1);
            if(!after.startsWith("/wiki/")){ //not a wiki link or a category/file/etc link that i can ignore
                continue;
            }
            //cuts extra part after each link
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
            if(link.contains(":")){
                continue;
            }
            //adds the link to the arraylist
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
