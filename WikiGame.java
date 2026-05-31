//TODO fix that parenthesis issue?
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

    Hashtable<String, Integer> checked = new Hashtable<>(); //where i store urls ive checked
    ArrayList<String> path = new ArrayList<>(); // this is the path from start to end
    TreeSet<Link> toCheck = new TreeSet<>(); //this is the treeset of links to check in order
    boolean finished = false; //this tells me im done (idt i need this but i made it)

    //TODO: connect to input
    String startLink = "https://en.wikipedia.org/wiki/Donald_Trump";  // beginning link, where the program will start
    String endLink = "https://en.wikipedia.org/wiki/The_College_Dropout";    // ending link, where the program is trying to get to
    int maxDepth = 3; //the max depth its allowed to go to

    long time = System.currentTimeMillis();
    long timeTaken = 0;

    public static void main(String[] args) {
        WikiGame w = new WikiGame();
    }


    public WikiGame() {

        toCheck.add(new Link(startLink, 0));
        findLink();
        while(!path.contains(startLink)){
            path.add(endLink); //does this here bc endLink will change
            fillPath();
           //i found that going to lower depths takes a pretty short time, so this progressively adds one more link to the path
        }

        timeTaken = System.currentTimeMillis() - time;
        if(!path.isEmpty()){
            System.out.println("found it********************************************************************");
            System.out.println(path);
            System.out.println("time taken: " + timeTaken);
            //TODO:connect to output
        } else {
            System.out.println("did not find it********************************************************************");
            System.out.println("time taken: " + timeTaken);
            //TODO:connect to output
        }

    }

    // recursion method
    public boolean findLink() {
        if(toCheck.isEmpty()){ //if theres nothing to check, we failed
            return false;
        }

        //takes the first link from the treeset and its data
        Link currLinkVariable = toCheck.pollFirst();
        String currLink = currLinkVariable.url;
        int depth = currLinkVariable.depth;

        if (checked.containsKey(currLink) && checked.get(currLink) <= depth){ //this checks to make sure i haven't done this process on this link.
            return findLink(); //this now recurses without this link in toCheck
        }

        checked.put(currLink, depth);
        //System.out.println("Added " + currLink + " at a depth of " + depth + " to the hashSet");

        //didnt get there in the maxDepth allowed
        if(depth > maxDepth){
            System.out.println("thinks its at max depth");
            return false;
        }

        //found it
        if(currLink.equals(endLink)){
            timeTaken = System.currentTimeMillis() - time;
            System.out.println("thinks it found it at time = " + timeTaken);
            path.addFirst(endLink);
            return true;
        }

        //no need to add links off of this
        if (depth == maxDepth){
            return findLink();
        }

//        System.out.println("depth is: " + depth + ", link is: " + currLink);

        //grabs all links from this link and adds it to toCheck
        String[] innerLinks = findInnerLinks(currLink);
        if(innerLinks == null){
            return findLink();
        }
        int added = 0;
        for(String innerLink : innerLinks){
            if(innerLink.equals(endLink)){ //sees if this innerLink is the desired link
                timeTaken = System.currentTimeMillis() - time;
                System.out.println("thinks it found it at time = " + timeTaken);
                path.addFirst(currLink);
                return true;
            }
            toCheck.add(new Link(innerLink, depth+1));
            added++;
        }
//        System.out.println("Made it to the end and added " + added + " links");
        return findLink();
    }

    public void fillPath(){
        //clears the treeset and hashtable
        toCheck.clear();
        checked.clear();

        //runs findLink with the link found to be one before
        endLink = path.get(0);
        toCheck.add(new Link(startLink, 0));
        findLink();
    }

    String[] findInnerLinks(String link){
        ArrayList<String> listOfLinks= new ArrayList<>();

        //adds all links to the listOfLinks by searing for "href=" and "src="
        try{
            URL url = new URL(link);
            URLConnection urlc = url.openConnection();
            urlc.setRequestProperty("User-Agent", "Mozilla 5.0 (Windows; U; " + "Windows NT 5.1; en-US; rv:1.8.0.11) ");

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(urlc.getInputStream())
            );
            String line;
            while ( (line = reader.readLine()) != null ) {
                String[] hrefParts = line.split("href=");
                String[] srcParts = line.split("src=");
                addLinks(hrefParts, listOfLinks);
                addLinks(srcParts, listOfLinks);
            }

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
        for(int i = parts.length-1; i >0; i--){
            String after = parts[i].substring(1);
            if(!after.startsWith("/wiki/") || after.contains(":")){ //not a wiki link or a category/file/etc link that i can ignore
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
