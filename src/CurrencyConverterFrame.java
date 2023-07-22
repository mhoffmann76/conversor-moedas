import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class CurrencyConverterFrame extends JFrame {
    private JTextField amountTextField;
    private JComboBox<String> fromCurrencyComboBox;
    private JComboBox<String> toCurrencyComboBox;
    private JButton convertButton;
    private JButton clearButton;
    private JTable resultTable;
    private DefaultTableModel tableModel;

    private Map<String, Double> exchangeRates;

    public CurrencyConverterFrame() {
        setTitle("Currency Converter");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(400, 300));

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());

        JPanel topPanel = createTopPanel();
        JPanel middlePanel = createMiddlePanel();
        JPanel bottomPanel = createBottomPanel();

        contentPanel.add(topPanel, BorderLayout.NORTH);
        contentPanel.add(middlePanel, BorderLayout.CENTER);
        contentPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(contentPanel, BorderLayout.NORTH);

        initializeExchangeRates();

        pack();
        setLocationRelativeTo(null);
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JLabel amountLabel = new JLabel("Valor:");
        amountTextField = new JTextField(10);
        panel.add(amountLabel);
        panel.add(amountTextField);

        return panel;
    }

    private JPanel createMiddlePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        String[] currencies = {"USD", "EUR", "GBP", "BRL"};

        fromCurrencyComboBox = new JComboBox<>(currencies);
        JLabel toLabel = new JLabel("to");
        toCurrencyComboBox = new JComboBox<>(currencies);
        convertButton = new JButton("Converter");
        clearButton = new JButton("Limpar");

        panel.add(fromCurrencyComboBox);
        panel.add(toLabel);
        panel.add(toCurrencyComboBox);
        panel.add(convertButton);
        panel.add(clearButton);

        convertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                convertCurrency();
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFields();
            }
        });

        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new Object[][]{}, new String[]{"De", "Para", "Resultado"});
        resultTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(resultTable);

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void convertCurrency() {
        String fromCurrency = fromCurrencyComboBox.getSelectedItem().toString();
        String toCurrency = toCurrencyComboBox.getSelectedItem().toString();
        String amountText = amountTextField.getText();

        // Input validation for the amount field
        if (amountText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Favor inserir um valor válido para converção.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Valor inválido. Favor enserir um número válido.", "Entrada inválida", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if exchange rates exist for the selected currencies
        if (!exchangeRates.containsKey(fromCurrency + "_BRL") || !exchangeRates.containsKey(toCurrency + "_BRL")) {
            JOptionPane.showMessageDialog(this, "Exchange rates not available for the selected currencies.", "Exchange Rate Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double fromRate = exchangeRates.get(fromCurrency + "_BRL");
        double toRate = exchangeRates.get(toCurrency + "_BRL");

        double result;
        if (fromCurrency.equals("BRL")) {
            result = amount / toRate;
        } else if (toCurrency.equals("BRL")) {
            result = amount * fromRate;
        } else {
            double brlToFromRate = 1 / fromRate;
            result = (amount * brlToFromRate) * toRate;
        }

        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        String formattedResult = decimalFormat.format(result);

        Object[] rowData = {fromCurrency, toCurrency, formattedResult};
        tableModel.addRow(rowData);
    }


    private void clearFields() {
        amountTextField.setText("");
        fromCurrencyComboBox.setSelectedIndex(0);
        toCurrencyComboBox.setSelectedIndex(0);
        tableModel.setRowCount(0);
    }

    private void initializeExchangeRates() {
        exchangeRates = new HashMap<>();
        exchangeRates.put("USD_BRL", 5.17);
        exchangeRates.put("EUR_BRL", 6.12);
        exchangeRates.put("GBP_BRL", 7.16);
        exchangeRates.put("BRL_USD", 0.19);
        exchangeRates.put("BRL_EUR", 0.16);
        exchangeRates.put("BRL_GBP", 0.14);
        exchangeRates.put("BRL_BRL", 1.0);
    }


}
