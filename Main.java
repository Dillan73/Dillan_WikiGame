public class Main {

    public static void main(String[] args) {
        String startLink = "https://en.wikipedia.org/wiki/League_of_Nations";  // beginning link, where the program will start
        String endLink = "https://en.wikipedia.org/wiki/Joe_Biden";    // ending link, where the program is trying to get to
        int maxDepth = 10; //the max depth its allowed to go to
        int k = 10;

        WikiGame wikiGame = new WikiGame(startLink, endLink, maxDepth, k);
    }
}

/*

 //https://en.wikipedia.org/wiki/Joe_Biden
    //https://en.wikipedia.org/wiki/Donald_Trump
    //https://en.wikipedia.org/wiki/Category:American_sex_offenders
    //https://en.wikipedia.org/wiki/Views_of_Kanye_West
    //https://en.wikipedia.org/wiki/Wyoming_Democratic_Party
    //https://en.wikipedia.org/wiki/The_College_Dropout

    //https://www.thewikigame.com/play/String_theory
 */