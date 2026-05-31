//TODO fix that parenthesis issue?
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

public class WikiGame {
//    long timeForOpening = 0;
//    long timeForParsing = 0;
//    int timesRan = 0;

    Hashtable<String, String> origins = new Hashtable<>(); //where i store links' origins (to prevent dupes and remake the path)

    ArrayList<String> path = new ArrayList<>(); // this is the path from start to end
    ArrayDeque<Link> toCheck = new ArrayDeque<>(); //this is the treeset of links to check in order

    //TODO: connect to input
    String startLink;  // beginning link, where the program will start
    String endLink;    // ending link, where the program is trying to get to
    int maxDepth; //the max depth its allowed to go to
    int k;

//    long time = System.currentTimeMillis();
//    long currTime = 0;
//    long timeTaken = 0;

    public WikiGame(String start, String end, int maxDepth, int k) {
        this.startLink = start;
        this.endLink = end;
        this.maxDepth = maxDepth;
        this.k = k;

        pathFinder();
    }

    public void pathFinder(){
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
//        timesRan++;

        if(toCheck.isEmpty()){ //nothing to check
            System.out.println("toCheck became empty");
            return false;
        }

        //takes the first link from the treeset and its data
        Link currLinkVariable = toCheck.pollFirst();
        String currLink = currLinkVariable.url;
        int depth = currLinkVariable.depth;

        if(depth == maxDepth){ //all links in toCheck have been checked to see if its endLink, and none are below maxDepth --> anything else that gets to endLink would go past maxDepth
            System.out.println("hit maxDepth.");
            return false;
        }

        System.out.println("running with " + currLink);

        //grabs all links from this link and adds it to toCheck
//        currTime = System.currentTimeMillis();

        HtmlParser parser = new HtmlParser(currLink);
        HashSet<String> innerLinks = parser.findInnerLinks();

//        timeForParsing+=parser.timeForParsing;
//        timeForOpening+=parser.timeForOpening;
//
//        timeTaken = System.currentTimeMillis() - currTime;
//
//        System.out.println("findInners took: " + timeTaken);

        if(innerLinks == null){
            return findLink();
        }

        double limiter = ((double) k)/innerLinks.size();
        //System.out.println(limiter);

        int added = 0;
        for(String innerLink : innerLinks){
            innerLink = "https://en.wikipedia.org" + innerLink;
            if(innerLink.equals(endLink)){
                path.add(innerLink); //adds the endLink
                fillPath(currLink); //uses origin to recursively fill the rest of the path
                return true;
            }

            if(origins.containsKey(innerLink)){ //if ive already seen this link, so i continue
                continue;
            }

            //if this link is new and not the end link, adds it to the origins and toCheck
            if(Math.random() > limiter){
                continue;
            }
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
//        timeTaken = System.currentTimeMillis() - time;
        if(found){
            System.out.println("found it********************************************************************");
            System.out.println(path);
            //TODO:connect to output
        } else {
            System.out.println("did not find it********************************************************************");
            //TODO:connect to output
        }

        System.out.println("toCheck ended with: " + toCheck.size());
//        System.out.println("time to open connections was: " + timeForOpening);
//        System.out.println("time for parsing was: " + timeForParsing);
//
//        System.out.println("in total, find links ran: " + timesRan + " times");
    }

}
