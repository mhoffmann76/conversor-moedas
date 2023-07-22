import javax.swing.*;

public class CurrencyConverterApp {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                CurrencyConverterFrame frame = new CurrencyConverterFrame();
                frame.setVisible(true);
            }
        });
    }
}
