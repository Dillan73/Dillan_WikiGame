//I wanted to sort by depth, but also need the actual url, so i made this class to be able to store both data values in the same instance (and with it, the deque)
public class Link implements Comparable<Link>{
    public String url;
    public int depth;

    public Link(String url, int depth) { //how i make a link
        this.depth = depth;
        this.url = url;
    }

    public int compareTo(Link other){ //old way to help with sorting when i used a treeset (now its just FIFO)
        if(this.depth != other.depth){
            return Integer.compare(this.depth, other.depth);
        }
        return this.url.compareTo(other.url); //returns smth thats only 0 if the urls r the same

    }

    public String toString(){ //this I did to make it easier to print while debugging
        return "url is " + url + " at a depth of " + depth;
    }
}
