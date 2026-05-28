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
    TreeSet<Link> toCheck = new TreeSet<>();
    boolean stopTS = false;

    String startLink = "https://en.wikipedia.org/wiki/Donald_Trump";  // beginning link, where the program will start
    String endLink = "https://en.wikipedia.org/wiki/My_Beautiful_Dark_Twisted_Fantasy";    // ending link, where the program is trying to get to
    int maxDepth = 3;

    public static void main(String[] args) {
        WikiGame w = new WikiGame();
    }

    //https://en.wikipedia.org/wiki/Joe_Biden
    //https://en.wikipedia.org/wiki/Donald_Trump
    //https://en.wikipedia.org/wiki/Category:American_sex_offenders
    //https://en.wikipedia.org/wiki/Views_of_Kanye_West
    public WikiGame() {



        long time = System.currentTimeMillis();
//        for(int depth = 0; depth <= maxDepth; depth++){
//            if(findLink(startLink, endLink, depth)){
//                //path.add(startLink);
//                break;
//            }
//            //done cuz js moving to next iteration otherwise
//        }
        toCheck.add(new Link(startLink, 0));
        while(!toCheck.isEmpty() && !stopTS){
            findLink(toCheck, endLink);
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
    public boolean findLink(TreeSet<Link> toCheck, String endLink) {
        Link currLinkVariable = toCheck.pollFirst();
        String currLink = currLinkVariable.url;
        int depth = currLinkVariable.depth;

        if (!checked.containsKey(currLink) || checked.get(currLink) <= depth){
            return findLink(toCheck, endLink);
        }
        checked.put(currLink, depth);

        //didnt get there
        if(depth > maxDepth){
            return false;
        }

        //found it
        if(currLink.equals(endLink)){
            path.addFirst(endLink);
            return true;
        }

        //no need to add links off of this
        if (depth == maxDepth){
            return findLink(toCheck, endLink);
        }

        System.out.println("depth is: " + depth + ", link is: " + startLink);

        //check all links
        String[] innerLinks = findInnerLinks(currLink);
        if(innerLinks == null){
            return findLink(toCheck, endLink);
        }
        for(String innerLink : innerLinks){
            toCheck.add(new Link(innerLink, depth+1));

        }

        //false if no possibilities work
        return findLink(toCheck, endLink);
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
