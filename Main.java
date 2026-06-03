import javax.swing.*;
import javax.swing.border.Border;
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
        Main runner = new Main();
        runner.prepareGUI();
    }

    JTextArea startLinkTA;
    JTextArea endLinkTA;
    JTextArea depthTA;
    JTextArea kTA;
    JTextArea outputTA;
    JTextArea processTA;

    public void prepareGUI() {
        JFrame mainFrame = new JFrame("The URL-link-getter!");
        mainFrame.setSize(WIDTH, HEIGHT);
        mainFrame.setLayout(new BorderLayout());
        //make the main frame with a border layout

        CardLayout cardLayout = new CardLayout();
        JPanel cardPanel = new JPanel(cardLayout);
        mainFrame.add(cardPanel, BorderLayout.CENTER);

        //the intro/start panel
        JPanel starterPanel = new JPanel();
        starterPanel.setLayout(new BorderLayout());
        cardPanel.add(starterPanel, "Starting UI");

        //the label and button to give info and go to the actual game
        JLabel startLabel = new JLabel("<html>  \n  This is the Wiki Game! Find paths between different wikipedia links. Customize the links looked at per page and the max depth based on whether you care most about finding a path quickly or finding the quickest path. You may run multiple searches at once! \n <html>", JLabel.CENTER);
        starterPanel.add(startLabel, BorderLayout.NORTH);

        JButton startButton = new JButton("Click to Start");
        starterPanel.add(startButton);
        startButton.setActionCommand("Start");
        startButton.addActionListener(e -> cardLayout.next(cardPanel));
        startButton.addActionListener(new ButtonClickListener());
        //TODO: make startButton work

        //the main card of the cardLayout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(2,1));
        cardPanel.add(mainPanel, "main UI");

        //a panel to input links, maxDepth, and k
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        mainPanel.add(inputPanel);

        //a panel for outputs
        JPanel outputPanel = new JPanel();
        outputPanel.setLayout(new GridLayout(2,1));
        mainPanel.add(outputPanel);

        //a panel to customize maxDepth and k
        JPanel customTAsPanel = new JPanel();
        customTAsPanel.setLayout(new GridLayout(4, 1));
        inputPanel.add(customTAsPanel);

        //scroll panes for inputting the links
        startLinkTA = new JTextArea("Replace this with your starting link.");
        JScrollPane startLinkSP = new JScrollPane(startLinkTA);
        startLinkTA.setLineWrap(true);
        customTAsPanel.add(startLinkSP);

        endLinkTA = new JTextArea("Replace this with your ending link.");
        JScrollPane endLinkSP = new JScrollPane(endLinkTA);
        endLinkTA.setLineWrap(true);
        customTAsPanel.add(endLinkSP);

        //scroll panes for max depth and k
        depthTA = new JTextArea("Replace this with the max depth a path can go through links. Leave blank to not have a depth limitation.");
        JScrollPane depthSP = new JScrollPane(depthTA);
        depthTA.setLineWrap(true);
        customTAsPanel.add(depthSP);

        kTA = new JTextArea("Replace this with how many other links a Wiki page should branch out to on average. Leave blank to branch out as much as possible from each page.");
        JScrollPane kSP = new JScrollPane(kTA);
        kTA.setLineWrap(true);
        customTAsPanel.add(kSP);

        //the text area that wikiGame outputs to
        processTA = new JTextArea("This will display the process being used");
        JScrollPane processSP = new JScrollPane(processTA);
        processTA.setLineWrap(true);
        outputPanel.add(processSP);

        outputTA = new JTextArea("This is where paths will be shown. will appear");
        JScrollPane outputSP = new JScrollPane(outputTA);
        outputTA.setLineWrap(true);
        outputPanel.add(outputSP);

        //button to run the program
        JButton submit = new JButton(" \n \n \n Run \n \n \n ");
        submit.setActionCommand("Submit");
        submit.addActionListener(e -> processTA.setText("Searching..."));
        submit.addActionListener(e -> new Thread(() -> {
            runWikiGame();
        }).start());
        submit.addActionListener(new ButtonClickListener());
        inputPanel.add(submit, BorderLayout.EAST);


        mainFrame.setVisible(true);
    }

    private void runWikiGame() {
        //grabs the inputs from the text areas
        String startLink = startLinkTA.getText();
        String endLink = endLinkTA.getText();
        int maxDepth;
        int k;

        String maxDepthString = depthTA.getText();
        String kString = kTA.getText();

        //converts k and maxDepth to rly large if left blank (no limits)
        if(maxDepthString.equals("")){
            maxDepth = 1000000;
        }else{
            maxDepth = Integer.parseInt(maxDepthString);
        }

        if(kString.equals("")){
            k = 1000000;
        }else{
            k = Integer.parseInt(kString);
        }

        //Creates a WikiGame that will run with the given inputs
        WikiGame wikiGame = new WikiGame(startLink, endLink, maxDepth, k, outputTA, processTA);
    }

    //Has the submit button call submitUI
    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //String command = e.getActionCommand();
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

    //https://en.wikipedia.org/wiki/National_park

    //https://www.wikipedia.org/wiki/String_theory
 */