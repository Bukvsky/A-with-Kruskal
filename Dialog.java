import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class Dialog {
    private static Font defaultFont = new Font("Arial", Font.BOLD, 14);
    private static Font hoverFont = new Font("Arial", Font.BOLD, 18);
    private static Cell[][] cells;


    public static void createInputWindow() {
        JFrame frame = new JFrame("Maze Size Input");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 250);
        frame.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        frame.add(mainPanel, BorderLayout.CENTER);

        mainPanel.add(Box.createVerticalStrut(50)); // Obniżenie tekstu i pola

        JPanel inputPanel = new JPanel(new FlowLayout());
        JLabel label = new JLabel("Size (only square size):");
        label.setForeground(new Color(255, 236, 234));
        JTextField textSize = new JTextField(10);
        textSize.setBackground(new Color(113, 98, 122));
        textSize.setForeground(new Color(255, 236, 234));

        inputPanel.add(label);
        inputPanel.add(textSize);
        inputPanel.setBackground(new Color(46, 41, 58));
        mainPanel.add(inputPanel);
        mainPanel.setBackground(new Color(46, 41, 58));

        // Panel na przyciski (obok siebie)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(46, 41, 58));

        JButton generateButton = new JButton("Generate Maze");
        JButton randomizeButton = new JButton("Randomize Maze Size");

        stylizujPrzycisk(generateButton);
        stylizujPrzycisk(randomizeButton);

        // Ustawienie stałego rozmiaru przycisków (żeby się nie powiększały)
        generateButton.setPreferredSize(new Dimension(240, 40));
        randomizeButton.setPreferredSize(new Dimension(240, 40));

        hoverEfect(generateButton);
        hoverEfect(randomizeButton);

        buttonPanel.add(generateButton);
        buttonPanel.add(randomizeButton);
        mainPanel.add(buttonPanel);

        generateButton.addActionListener(e -> {
            try {
                int size = Integer.parseInt(textSize.getText());
                if (size > 0) {
                    frame.dispose();
                    launchAstar(size);
                } else {
                    JOptionPane.showMessageDialog(frame, "Enter a positive number!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        randomizeButton.addActionListener(e -> {
            int randomSize = new Random().nextInt(40) + 10;
            textSize.setText(String.valueOf(randomSize));
            frame.dispose();
            launchAstar(randomSize);
        });

        frame.setVisible(true);
    }

    private static void stylizujPrzycisk(JButton button) {
        button.setBorderPainted(false);
        button.setBackground(new Color(61, 53, 75));
        button.setForeground(new Color(255, 236, 234));

        button.setFont(defaultFont);
        button.setFocusPainted(false);
    }

    private static void hoverEfect(JButton button) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setFont(hoverFont);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setFont(defaultFont);
            }
        });
    }
    private static void launchAstar(int size){
        Grid grid = new Grid(size);
        grid.startMaze(size);
        cells=grid.getCells();
        Astar astar = new Astar(cells,grid.getStart(),grid.getEnd());
        astar.findPath();

    }
}


