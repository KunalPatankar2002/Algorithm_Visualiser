
package SortingVisualiser;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

public class QuickSort extends JPanel {
    private static final long serialVersionUID = 1L;
    private final int width = 1000, height = width * 9 / 16;
    private final int size = 200;
    private final float barWidth = (float) width / size;
    private float[] barHeight = new float[size];
    private SwingWorker<Void, Void> shuffler, sorter;
    private int current_index, swap1, swap2;

    public QuickSort() {
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(width, height));
        initBarHeight();
        initSorter();
        initShuffler();

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.GREEN);
        Rectangle2D.Float bar;
        for (int i = 0; i < size; i++) {
            bar = new Rectangle2D.Float(i * barWidth, height - barHeight[i], barWidth, barHeight[i]);
            g2d.fill(bar);
        }

        g2d.setColor(Color.WHITE);
        bar = new Rectangle2D.Float(current_index * barWidth, height - barHeight[current_index], barWidth,
                barHeight[current_index]);
        g2d.fill(bar);
        g2d.setColor(Color.GRAY);
        bar = new Rectangle2D.Float(swap1 * barWidth, height - barHeight[swap1], barWidth,
                barHeight[swap1]);
        g2d.fill(bar);
        g2d.setColor(Color.LIGHT_GRAY);
        bar = new Rectangle2D.Float(swap2 * barWidth, height - barHeight[swap2], barWidth,
                barHeight[swap2]);
        g2d.fill(bar);
    }

    private int partition(int low, int high) throws InterruptedException {
        // Choosing the pivot middle element
        float pivot = barHeight[high];
        current_index = high;
        // Index of smaller element and indicates
        // the right position of pivot found so far
        int i = (low - 1);

        for (int j = low; j <= high - 1; j++) {
            // If current element is smaller than the pivot
            if (barHeight[j] < pivot) {
                // Increment index of smaller element
                i++;
                float temp = barHeight[i];
                barHeight[i] = barHeight[j];
                barHeight[j] = temp;
            }
            swap1 = i;
            swap2 = j;
            Thread.sleep(1);
            repaint();
        }
        float temp = barHeight[i + 1];
        barHeight[i + 1] = barHeight[high];
        barHeight[high] = temp;
        swap1 = i+1;
        swap2 = high;
        Thread.sleep(1);
        repaint();
        return (i + 1);
    }

    private void quickSort(int low, int high) throws InterruptedException {
        if (low < high) {
            int partitionIndex = partition(low, high);
            // sort the left part then right
            quickSort(low, partitionIndex - 1);
            quickSort(partitionIndex + 1, high);
        }
    }

    private void initSorter() {
        sorter = new SwingWorker<>() {
            @Override
            public Void doInBackground() throws InterruptedException {
                // Sorting using quick sort
                quickSort(0, size - 1);
                return null;
            }
        };
    }

    private void initShuffler() {
        shuffler = new SwingWorker<>() {
            @Override
            public Void doInBackground() throws InterruptedException {
                // shuffling using Fisher-Yates algorithm
                for (int i = size - 1; i >= 1; i--) {
                    int rand = new Random().nextInt(i + 1);
                    float temp = barHeight[i];
                    barHeight[i] = barHeight[rand];
                    barHeight[rand] = temp;
                    Thread.sleep(1);
                    repaint();
                }
                return null;
            }

            @Override
            public void done() {
                super.done();
                sorter.execute();
            }
        };
        shuffler.execute();
    }

    private void initBarHeight() {
        float interval = (float) height / size;
        for (int i = 0; i < size; i++) {
            barHeight[i] = i * interval;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Quick Sort Visualiser");
            frame.setResizable(false);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setContentPane(new QuickSort());
            frame.validate();
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
