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
        ArrayList<String> innerLinks = findInnerLinks(startLink);
        for(String innerLink : innerLinks){
            if(findLink(innerLink, endLink, depth-1)){
                path.add(startLink);
                return true;
            }
        }

        //false if no possibilities work
        return false;
    }

    ArrayList<String> findInnerLinks(String link){
        ArrayList<String> innerLinks = new ArrayList<>();

        return innerLinks;
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
