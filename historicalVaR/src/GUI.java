import javax.swing.*;
import java.awt.*;



public class GUI {

    BrowserListener browserListener;
    VaRComputingListener varComputingListener;

    public GUI() {

        super();

        JFrame frame = new JFrame();
        frame.setSize(600,450);
        frame.setLocationRelativeTo(null);

        JLabel labelCSV        = new JLabel("Select a csv file:");
        JLabel labelColumn     = new JLabel("Select the column name of the prices in the csv file:");
        JLabel labelConfidence = new JLabel("Enter the confidence level :");
        JLabel labelInvestment = new JLabel("Enter the desired investment:");
        JLabel labelConfUnit   = new JLabel("%");
        JLabel labelInvestUnit = new JLabel("$");

        JButton browser = new JButton("Select CSV File");
        browserListener = new BrowserListener();
        browser.addActionListener(browserListener);
        JButton button = new JButton("Calculate VaR");
        varComputingListener = new VaRComputingListener(browserListener);
        button.addActionListener(varComputingListener);

        browserListener.getColumnName().setBackground(Color.WHITE);
        browserListener.getCsvName().setBackground(Color.WHITE);

        GridBagConstraints c = new GridBagConstraints();

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 50, 30));
        panel.setLayout(new GridBagLayout());

        c.gridy = 0;
        c.gridwidth = 5;
        panel.add(labelCSV, c);
        c.insets = new Insets(10,0,20,0);
        c.gridwidth = 1;
        c.gridy = 1;
        c.ipady = 15;
        c.ipadx = 250;
        panel.add(browserListener.getCsvName(), c);
        c.insets = new Insets(10,20,20,0);
        c.ipady = 0;
        c.ipadx = 0;
        panel.add(browser, c);

        c.insets = new Insets(0,0,10,0);
        c.gridy = 2;
        c.ipady = 0;
        c.ipadx = 0;
        c.gridwidth = 5;
        panel.add(labelColumn, c);
        c.insets = new Insets(0,0,20,0);
        c.gridy = 3;
        c.ipady = 10;
        c.ipadx = 100;
        panel.add(browserListener.getColumnName(), c);
        c.insets = new Insets(0,0,10,0);
        c.gridy = 4;
        c.ipady = 0;
        c.ipadx = 0;
        panel.add(labelConfidence, c);
        c.insets = new Insets(0,165,30,0);
        c.gridy = 5;
        c.ipady = 15;
        c.ipadx = 50;
        c.gridwidth = 1;
        panel.add(varComputingListener.getConfidenceLevel(), c);
        c.insets = new Insets(0,-110,30,0);
        c.ipady = 15;
        c.ipadx = 50;
        panel.add(labelConfUnit, c);

        c.insets = new Insets(-10,0,10,0);
        c.gridwidth = 5;
        c.gridy = 6;
        c.ipady = 0;
        c.ipadx = 0;
        panel.add(labelInvestment, c);
        c.insets = new Insets(0,160,10,0);
        c.gridy = 7;
        c.ipady = 15;
        c.ipadx = 80;
        c.gridwidth = 1;
        panel.add(varComputingListener.getInvestment(), c);
        c.insets = new Insets(0,-85,15,0);
        c.ipady = 15;
        c.ipadx = 50;
        panel.add(labelInvestUnit, c);

        c.gridx = 0;
        c.gridy = 8;
        c.ipady = 10;
        c.ipadx = 30;
        c.insets = new Insets(10,0,0,0);
        c.gridwidth = 5;
        panel.add(button, c);

        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Historical VaR Calculator");
        frame.setVisible(true);
    }


    public static void main(String[] args) {
        new GUI();
    }



}
