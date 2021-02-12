import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VaRComputingListener implements ActionListener {

    JTextField confidenceLevel;
    JTextField investment;
    BrowserListener browserListener;

    public VaRComputingListener(BrowserListener browserListener) {
        super();
        confidenceLevel = new JTextField();
        investment      = new JTextField();
        this.browserListener = browserListener;
    }

    public JTextField getConfidenceLevel() {
        return confidenceLevel;
    }

    public JTextField getInvestment() {
        return investment;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Double confidenceLevel = null;
        Double investment = null;

        try {
            confidenceLevel = Double.parseDouble(this.confidenceLevel.getText());
            investment = Double.parseDouble(this.investment.getText());
        } catch (NumberFormatException ignored) {}

        VaR var = new VaR(confidenceLevel, investment);

        List<Double> prices;
        try {
            prices = pickUpData(var);
            var.calculateHistoricalVaR(prices);
        } catch (IllegalArgumentException ignored) {}

        if (var.getValue().getErrorMessage() != null){
            JOptionPane.showMessageDialog(null, new JLabel(var.getValue().getErrorMessage(), JLabel.CENTER), "Invalid input", JOptionPane.ERROR_MESSAGE);
        }
        else{
            String htmlText =
                    "<html><body><div style='text-align:center; margin-bottom:5px;'>The historical VaR is:</div>" +
                            "<div style='font-size:20px;border:2px solid green;'>"+ (-var.getValue().getValue())+" $</div>" +
                            "</body></html>";
            JOptionPane.showMessageDialog(null, new JLabel(htmlText, JLabel.CENTER), "Historical Value at Risk", JOptionPane.PLAIN_MESSAGE);
        }
    }


    private List<Double> pickUpData(VaR var) throws IllegalArgumentException {

        List<Double> prices = new ArrayList<>();
        int columnIndex = -1;

        String columnName = String.valueOf(browserListener.getColumnName().getSelectedItem());
        String csvFileName = browserListener.getCsvName().getText();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFileName))) {
            String line = br.readLine(); // skip the first line (headers)
            if (line != null){
                String[] headers = line.split(",");
                for (int counter=0; counter<headers.length; counter++){
                    if (headers[counter].equals(columnName)){
                        columnIndex = counter;
                    }
                }
            }

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",", -1);
                if (!data[columnIndex].isEmpty()) // in case this is an empty column for this row
                    prices.add(Double.parseDouble(data[columnIndex]));
            }
        } catch(FileNotFoundException e) {
            var.getValue().setErrorMessage("The CSV file doesn't exist, please check the path.");
            throw new IllegalArgumentException();
        } catch (IOException e) {
            var.getValue().setErrorMessage("Unexpected error reading the file.");
            throw new IllegalArgumentException();
        }

        return prices;
    }

}
