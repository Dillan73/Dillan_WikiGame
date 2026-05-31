//this is the custom class i made to add to the treeset
//  I wanted to sort by depth, but also need the actual url, so i made this class so id be able to store both data values in the same instance
public class Link implements Comparable<Link>{
    public String url;
    public int depth;

    public Link(String url, int depth) { //how i make a link
        this.depth = depth;
        this.url = url;
    }

    public int compareTo(Link other){ //for treeset comparision
        if(this.depth != other.depth){
            return Integer.compare(this.depth, other.depth);
        }
        if(!this.url.equals(other.url)){
            return 1;
        }
        return 0; //this makes it so if the depth and url are the same, the links are equivalent. I did this so the treeset will filter it out as a duplicate
    }

    public String toString(){
        return "url is " + url + " at a depth of " + depth; //this I did to make it easier to print while debugging
    }
}
