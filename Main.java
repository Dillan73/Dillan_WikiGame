import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

//todo: add comments

public class Main {
    private JMenuBar mb;
    private JMenu file, edit, help;
    private JMenuItem cut, copy, paste, selectAll;
    private int WIDTH=800;
    private int HEIGHT=700;

    public static void main(String[] args) {
        String startLink = "https://en.wikipedia.org/wiki/family";  // beginning link, where the program will start
        String endLink = "https://en.wikipedia.org/wiki/National_park";    // ending link, where the program is trying to get to
        int maxDepth = 10; //the max depth its allowed to go to
        int k = 12;

        WikiGame wikiGame = new WikiGame(startLink, endLink, maxDepth, k);
    }

    private void prepareGUI() {
        JFrame mainFrame = new JFrame("The URL-link-getter!");
        mainFrame.setSize(WIDTH, HEIGHT);
        mainFrame.setLayout(new GridLayout(1,1));
        //make the main frame with a border layout

        CardLayout cardLayout = new CardLayout();
        JPanel cardPanel = new JPanel(cardLayout);

        cardLayout.first(cardPanel);
        mainFrame.add(cardPanel, BorderLayout.CENTER);

        JPanel starterPanel = new JPanel();
        starterPanel.setLayout(new GridLayout(1,1));
        cardPanel.add(starterPanel, "Starting UI");

        JButton startButton = new JButton("This is the Wiki Game! Find different paths between your start and end links!");
        starterPanel.add(startButton);
        //TODO: make startButton work

        JPanel mainPanel = new JPanel();
        starterPanel.setLayout(new GridLayout(3,1));
        cardPanel.add(mainPanel, "main UI");

        JPanel inputPanel = new JPanel();
        starterPanel.setLayout(new GridLayout(2,1));
        mainPanel.add(inputPanel);

        JPanel buttonPanel = new JPanel();
        starterPanel.setLayout(new GridLayout(1,3));
        mainPanel.add(inputPanel);

        JPanel outputPanel = new JPanel();
        starterPanel.setLayout(new GridLayout(1,1));
        mainPanel.add(inputPanel);

        JTextArea startLinkTA = new JTextArea("Replace this with your starting link!");
        JScrollPane startLinkSP = new JScrollPane(startLinkTA);
        startLinkTA.setLineWrap(true);
        inputPanel.add(startLinkSP);

        JTextArea endLinkTA = new JTextArea("Replace this with your ending link!");
        JScrollPane endLinkSP = new JScrollPane(endLinkTA);
        startLinkTA.setLineWrap(true);
        inputPanel.add(endLinkSP);

        //todo: hook up input TAs with the rest of code

        //todo: make button panel and hook buttons up with rest of code stuff

        //todo: make output panel and hook up output TA with rest of code

//        submit = new JButton(" \n \n Press to see your links! \n \n ");
//        submit.setActionCommand("Submit");
//        submit.addActionListener(e -> cardLayoutMain.next(cardMain));
//        submit.addActionListener(new ButtonClickListener());
//        //submit.setBorder(BorderFactory.createEmptyBorder(3,3,3,3));
//        inputButtons.add(submit, BorderLayout.SOUTH);
//        // adds the submit button to the input section
//
//        JButton clear = new JButton("Clear input fields");
//        clear.setActionCommand("Clear");
//        clear.addActionListener(new ButtonClickListener());
//        inputButtons.add(clear);
        // adds the clear button to the input section with an action listener

        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
        mainFrame.setVisible(true);
    }

    //Has the submit button call submitUI
    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            //System.out.println("Making it to BCL");

//            if (command.equals("Submit")) {
//                boolean success = sumbitUI();
//                if(!success){
//                    cardLayoutMain.next(cardMain);
//                    //making a failed run stay on the input page
//                }
//            }
//            if (command.equals("Reset")) {
//                System.out.println("Reset Works");
//                outputText.setText("");
//            }
//            if (command.equals("Clear")) {
//                System.out.println("Making it to BCL If");
//                linkText.setText("");
//                termText.setText("");
//            }
        }
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