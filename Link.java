public class Link implements Comparable<Link>{
    public String url;
    public int depth;

    public Link(String url, int depth) {
        this.depth = depth;
        this.url = url;
    }

    public int compareTo(Link other){
        return Integer.compare(this.depth, other.depth);
    }

    public void printInfo(){
        System.out.println("url is " + url + " at a depth of " + depth);
    }
}
