public class Link {
    public String url;
    public int depth;

    public int compareTo(Link other){
        return Integer.compare(this.depth, other.depth);
    }
}
