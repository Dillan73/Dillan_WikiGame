public class Link implements Comparable<Link>{
    public String url;
    public int depth;

    public Link(String url, int depth) {
        this.depth = depth;
        this.url = url;
    }

    public int compareTo(Link other){
        if(this.depth != other.depth){
            return Integer.compare(this.depth, other.depth);
        }
        if(!this.url.equals(other.url)){
            return 1;
        }
        return 0;
    }

    public String toString(){
        return "url is " + url + " at a depth of " + depth;
    }
}
