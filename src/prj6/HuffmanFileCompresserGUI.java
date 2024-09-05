package prj6;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class HuffmanFileCompresserGUI extends JFrame {
    private JTextArea inputFileArea;
    private JTextArea compressedFileArea;
    private JTextArea decompressedFileArea;

    public HuffmanFileCompresserGUI() {
        setTitle("Huffman File Compressor");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        inputFileArea = new JTextArea(5, 30);
        compressedFileArea = new JTextArea(5, 30);
        decompressedFileArea = new JTextArea(5, 30);

        JScrollPane inputScrollPane = new JScrollPane(inputFileArea);
        JScrollPane compressedScrollPane = new JScrollPane(compressedFileArea);
        JScrollPane decompressedScrollPane = new JScrollPane(decompressedFileArea);

        JButton browseInputButton = new JButton("Browse");
        JButton browseCompressedButton = new JButton("Browse");
        JButton browseDecompressedButton = new JButton("Browse");

        JButton compressButton = new JButton("Compress");
        JButton decompressButton = new JButton("Decompress");

        browseInputButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    inputFileArea.setText(fileChooser.getSelectedFile().getAbsolutePath());
                }
            }
        });

        browseCompressedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showSaveDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    compressedFileArea.setText(fileChooser.getSelectedFile().getAbsolutePath());
                }
            }
        });

        browseDecompressedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showSaveDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    decompressedFileArea.setText(fileChooser.getSelectedFile().getAbsolutePath());
                }
            }
        });

        compressButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputFileName = inputFileArea.getText();
                String compressedFileName = compressedFileArea.getText();

                try {
                    HuffmanFileCompresser.compressFile(inputFileName, compressedFileName);
                    JOptionPane.showMessageDialog(null, "Compression complete.");
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error compressing file.");
                }
            }
        });

        decompressButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String compressedFileName = compressedFileArea.getText();
                String decompressedFileName = decompressedFileArea.getText();

                try {
                    HuffmanFileCompresser.decompressFile(compressedFileName, decompressedFileName);
                    JOptionPane.showMessageDialog(null, "Decompression complete.");
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error decompressing file.");
                }
            }
        });

        JPanel panel = new JPanel(new GridLayout(4, 3, 10, 10));
        panel.add(new JLabel("Input File:"));
        panel.add(inputScrollPane);
        panel.add(browseInputButton);
        panel.add(new JLabel("Compressed File:"));
        panel.add(compressedScrollPane);
        panel.add(browseCompressedButton);
        panel.add(new JLabel("Decompressed File:"));
        panel.add(decompressedScrollPane);
        panel.add(browseDecompressedButton);
        panel.add(compressButton);
        panel.add(decompressButton);

        getContentPane().add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new HuffmanFileCompresserGUI().setVisible(true);
            }
        });
    }
}