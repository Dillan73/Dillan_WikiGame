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
    String endLink = "https://en.wikipedia.org/wiki/United_Nations_Office_on_Drugs_and_Crime";    // ending link, where the program is trying to get to
    int maxDepth = 2; //the max depth its allowed to go to

    long time = System.currentTimeMillis();
    long currTime = 0;
    long timeTaken = 0;

    public static void main(String[] args) {
        WikiGame w = new WikiGame();
    }

    public WikiGame() {
        if(startLink.equals(endLink)){
            path.add(endLink);
            output(true);
        }

        toCheck.add(new Link(startLink, 0));
        origins.put(startLink, "THE_START_LINK");

        boolean success = findLink();
        output(success);
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

        HtmlParser parser = new HtmlParser(currLink);
        String[] innerLinks = parser.findInnerLinks();

        timeForParsing+=parser.timeForParsing;
        timeForOpening+=parser.timeForOpening;

        timeTaken = System.currentTimeMillis() - currTime;

        System.out.println("findInners took: " + timeTaken);

        if(innerLinks == null){
            return findLink();
        }
        int added = 0;
        for(String innerLink : innerLinks){
            if(innerLink.equals(endLink)){
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

    private void output(boolean found) {
        timeTaken = System.currentTimeMillis() - time;
        if(found){
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

        System.out.println("in total, find links ran: " + timesRan + " times");
    }

}
