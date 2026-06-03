import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

public class WikiGame {
    Hashtable<String, String> origins = new Hashtable<>(); //How I retrace the path and check for dupes

    public ArrayList<String> path = new ArrayList<>(); // where the path is put
    ArrayDeque<Link> toCheck = new ArrayDeque<>(); //the way I store other links to check in the future (sorted by depth)

    //input variables
    String startLink;
    String endLink;
    int maxDepth;
    int k;

    JTextArea outputTA; //how wikiGame outputs
    JTextArea processTA; //how wikiGame shows the process

    long time = System.currentTimeMillis();

    public WikiGame(String start, String end, int maxDepth, int k, JTextArea outputTA, JTextArea processTA) {
        //set the inputs for this instance
        this.startLink = start;
        this.endLink = end;
        this.maxDepth = maxDepth;
        this.k = k;

        this.outputTA = outputTA;
        this.processTA = processTA;

        //find a path
        pathFinder();
    }

    public void pathFinder(){
        //stop if the start and end link are already the same
        if(startLink.equals(endLink)) {
            outputTA.append("I trust you to find a way to get between these links (cuz they're the exact same already...)");
            return;
        }

        //add the start link to the
        toCheck.add(new Link(startLink, 0));
        origins.put(startLink, "THE_START_LINK");

        boolean success = findLink();
        output(success);
    }

    // recursion method
    public boolean findLink() {
        if(toCheck.isEmpty()){ //nothing to check
            outputTA.append("***********did not find it*********** because the list of links to check ran empty (increase branching amount?) \n");
            return false;
        }

        //take the first link from the treeset and its data
        Link currLinkVariable = toCheck.pollFirst();
        String currLink = currLinkVariable.url;
        int depth = currLinkVariable.depth;

        //if this link is at the maxDepth, then all in treeSet are too --> since all have been checked to see if they're endLink, can only find endLink past maxDepth --> failed
        if(depth == maxDepth){
            outputTA.append("***********did not find it*********** because the max depth was reached (increase that or branching amount and run it back?) \n");
            return false;
        }

        processTA.setText("looking at: " + currLink + "\n");
        System.out.println("looking at: " + currLink);

        //grabs all links from this link
        HtmlParser parser = new HtmlParser(currLink);
        HashSet<String> innerLinks = parser.findInnerLinks();

        //if there are no links, recurse with the deque not having this link
        if(innerLinks == null){
            return findLink();
        }

        //processTA.setText("didn't find the end link in " + currLink + "'s " + innerLinks.size() + " inner links. :( \n");

        //the odds each link has to be added
        double limiter = ((double) k)/innerLinks.size();

        int added = 0;
        for(String innerLink : innerLinks){
            innerLink = "https://en.wikipedia.org" + innerLink;
            if(innerLink.equals(endLink)){ //if we've found the end link --> fill the path
                origins.put(innerLink, currLink);
                fillPath(endLink); //uses origin to recursively fill the path
                return true;
            }

            if(origins.containsKey(innerLink)){ //if ive already seen this link, so i continue
                continue;
            }

            //has a chance to skip this link unless it could leave toCheck empty
            if(Math.random() > limiter && !toCheck.isEmpty()){
                continue;
            }

            //add this link to the HashTable and Deque
            origins.put(innerLink, currLink);
            toCheck.add(new Link(innerLink, depth+1));
            added++;
        }
        processTA.setText("Branched out " + added + " ways from " + currLink);

        //recurse
        return findLink();
    }

    public void fillPath(String link){
        if(link.equals("THE_START_LINK")){ //if we finished retracing ---> stop
            return;
        }
        path.add(link); //add this link
        fillPath(origins.get(link)); //recurse with the link one step prior
    }

    private void output(boolean found) {
        //previously used to sout an output, and I just kept it as a way to print the path
        if(found){
            outputTA.append("***********found it*********** in " + (System.currentTimeMillis() - time) + "ms \n");

            //print the path in reverse since it was added backwards
            outputTA.append(path.get(path.size()-1));
            for(int i = path.size()-2; i>=0; i--){
                outputTA.append(" --> " + path.get(i));
            }
            outputTA.append("! \n \n");

            System.out.println(path);
        }
        System.out.println("The process took " + (System.currentTimeMillis() - time) + "ms \n");
    }

}
