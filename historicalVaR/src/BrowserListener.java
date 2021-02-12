import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class BrowserListener implements ActionListener {



    private final JTextField csvName;
    private final JComboBox<String> columnName;


    public JComboBox<String> getColumnName() {
        return columnName;
    }

    public JTextField getCsvName() {
        return csvName;
    }

    public BrowserListener() {
        super();
        csvName = new JTextField();
        csvName.setEditable(false);
        columnName = new JComboBox<>();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        JFileChooser fileChooser = new JFileChooser();
        int response = fileChooser.showOpenDialog(null);

        if (response == JFileChooser.APPROVE_OPTION) {
            String fileName = fileChooser.getSelectedFile().getAbsolutePath();

            if ((5<fileName.length()) && fileName.endsWith(".csv")) {

                try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {

                    String line = br.readLine();
                    if (line != null) {
                        String[] headers = line.split(",");

                        // Pick up only the valid columns as prices (on our example pick all except 'Date')
                        List<String> validColumns = new ArrayList<>();
                        for (int counter = 0; counter < headers.length; counter++) {
                            BufferedReader br2 = new BufferedReader(new FileReader(fileName));
                            br2.readLine(); // skip the header line

                            String line2;
                            while ((line2 = br2.readLine()) != null) {
                                String[] data = line2.split(",", -1);
                                if (!data[counter].isEmpty()) { // In case the column is empty for the current row
                                    try {
                                        Double.parseDouble(data[counter]); // used to check that the data of the column is actually a double, throw exception if not
                                        validColumns.add(headers[counter]);
                                    } catch (NumberFormatException ignored) {}
                                    break;
                                }
                            }
                        }

                        if (validColumns.size()>0) {
                            csvName.setText(fileName);
                            columnName.setModel(new DefaultComboBoxModel<>(validColumns.toArray(new String[0])));
                        }
                        else {
                            //add options to drop columnName down list
                            JOptionPane.showMessageDialog(null, "Not enough data is the file to compute a VaR, please check.", "Invalid input", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "The CSV file is empty, please enter a valid file.", "Invalid input", JOptionPane.ERROR_MESSAGE);
                    }


                } catch (FileNotFoundException e) {
                    JOptionPane.showMessageDialog(null, "The CSV file doesn't exist, please check the path.", "Invalid input", JOptionPane.ERROR_MESSAGE);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Unexpected error reading the file.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            else{
                JOptionPane.showMessageDialog(null, "Please enter a valid CSV file name.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        }


    }
}
