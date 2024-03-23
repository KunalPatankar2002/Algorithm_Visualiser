package SortingVisualiser.Swing;

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

public class MergeSort extends JPanel {
    private static final long serialVersionUID = 1L;
    private final int width = 1000, height = width * 9 / 16;
    private final int size = 200;
    private final float barWidth = (float) width / size;
    private float[] barHeight = new float[size];
    private SwingWorker<Void, Void> shuffler, sorter;
    private int current_index;

    public MergeSort() {
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
        g2d.setColor(Color.BLUE);
        Rectangle2D.Float bar;
        for (int i = 0; i < size; i++) {
            bar = new Rectangle2D.Float(i * barWidth, height - barHeight[i], barWidth, barHeight[i]);
            g2d.fill(bar);
        }
        g2d.setColor(Color.WHITE);
        bar = new Rectangle2D.Float(current_index * barWidth, height - barHeight[current_index], barWidth,
                barHeight[current_index]);
        g2d.fill(bar);
    }

    private void merge(int left, int mid, int right) throws InterruptedException {
        int leftSize = mid - left + 1;
        int rightSize = right - mid;

        // Create temporary arrays
        float[] leftArray = new float[leftSize];
        float[] rightArray = new float[rightSize];

        // Copy data to temporary arrays
        for (int i = 0; i < leftSize; ++i)
            leftArray[i] = barHeight[left + i];
        for (int j = 0; j < rightSize; ++j)
            rightArray[j] = barHeight[mid + 1 + j];

        // Merge the temporary arrays back into barHeight
        int i = 0, j = 0, k = left;
        while (i < leftSize && j < rightSize) {
            if (leftArray[i] <= rightArray[j])
                barHeight[k++] = leftArray[i++];
            else
                barHeight[k++] = rightArray[j++];
            current_index = k;
            Thread.sleep(1);
            repaint();
        }

        // Copy remaining elements of leftArray
        while (i < leftSize) {
            barHeight[k++] = leftArray[i++];
            current_index = k;
            Thread.sleep(1);
            repaint();
        }

        // Copy remaining elements of rightArray
        while (j < rightSize) {
            barHeight[k++] = rightArray[j++];
            current_index = k;
            Thread.sleep(1);
            repaint();
        }

    }

    private void mergeSort(int left, int right) throws InterruptedException {
        if (left < right) {
            // Find the middle point
            int mid = (left + right) / 2;

            // Sort first and second halves
            mergeSort(left, mid);
            mergeSort(mid + 1, right);

            // Merge the sorted halves
            merge(left, mid, right);
        }
    }

    private void initSorter() {
        sorter = new SwingWorker<>() {
            @Override
            public Void doInBackground() throws InterruptedException {
                // Sorting using merge sort
                mergeSort(0, barHeight.length - 1);
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
            JFrame frame = new JFrame("Merge Sort Visualiser");
            frame.setResizable(false);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setContentPane(new MergeSort());
            frame.validate();
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
