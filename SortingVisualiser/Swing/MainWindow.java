package SortingVisualiser.Swing;

import javax.swing.*;
import java.awt.*;


public class MainWindow extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final int WIDTH = 1000;
    private static final int HEIGHT = WIDTH * 9 / 16 + 46;

    private JButton quickButton, bubbleButton, mergeButton, heapButton;
    private JPanel currentSortPanel; // Panel to hold the current sorting visualization

    public MainWindow() {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Center buttons horizontally
        buttonPanel.setBackground(Color.BLACK);

        quickButton = new JButton("Quick Sort");
        quickButton.setFocusable(false);
        quickButton.addActionListener(e -> showSortingPanel(new QuickSort()));
        buttonPanel.add(quickButton);

        bubbleButton = new JButton("Bubble Sort");
        bubbleButton.setFocusable(false);
        bubbleButton.addActionListener(e -> showSortingPanel(new BubbleSort()));
        buttonPanel.add(bubbleButton);

        mergeButton = new JButton("Merge Sort");
        mergeButton.setFocusable(false);
        mergeButton.addActionListener(e -> showSortingPanel(new MergeSort()));
        buttonPanel.add(mergeButton);

        heapButton = new JButton("Heap Sort");
        heapButton.setFocusable(false);
        heapButton.addActionListener(e -> showSortingPanel(new HeapSort()));
        buttonPanel.add(heapButton);

        add(buttonPanel, BorderLayout.NORTH);
    }

    private void showSortingPanel(JPanel sortingPanel) {
        // Remove the current sorting visualization panel, if any
        if (currentSortPanel != null) {
            remove(currentSortPanel);
        }

        // Add the new sorting visualization panel
        currentSortPanel = sortingPanel;
        add(currentSortPanel);

        // Revalidate and repaint to reflect the changes
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Main Frame");
            frame.setResizable(false);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new MainWindow());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
