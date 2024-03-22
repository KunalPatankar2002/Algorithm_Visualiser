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

public class HeapSort extends JPanel {
    private static final long serialVersionUID = 1L;
    private final int width = 1000, height = width * 9 / 16;
    private final int size = 200;
    private final float barWidth = (float) width / size;
    private float[] barHeight = new float[size];
    private SwingWorker<Void, Void> shuffler, sorter;
    private int swap1, swap2;

    public HeapSort() {
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
        g2d.setColor(Color.MAGENTA);
        Rectangle2D.Float bar;
        for (int i = 0; i < size; i++) {
            bar = new Rectangle2D.Float(i * barWidth, height - barHeight[i], barWidth, barHeight[i]);
            g2d.fill(bar);
        }
        g2d.setColor(Color.WHITE);
        bar = new Rectangle2D.Float(swap1 * barWidth, height - barHeight[swap1], barWidth,
                barHeight[swap1]);
        g2d.fill(bar);
        g2d.setColor(Color.GRAY);
        bar = new Rectangle2D.Float(swap2 * barWidth, height - barHeight[swap2], barWidth,
                barHeight[swap2]);
        g2d.fill(bar);
    }

    private void max_heapify(float[] barHeight, int size, int i) throws InterruptedException {

        int l = 2 * i + 1;
        int r = 2 * i + 2;
        
        int largest = i;
    
        if (l < size && barHeight[l] > barHeight[i]) {
            largest = l;
        }
    
        if (r < size && barHeight[r] > barHeight[largest]) {
            largest = r;
        }
    
        if (largest != i) {
            // swap elements
            float temp = barHeight[i];
            barHeight[i] = barHeight[largest];
            barHeight[largest] = temp;
            swap1 = i;
            swap2 = largest;
            Thread.sleep(1);
            repaint();
            max_heapify(barHeight, size, largest);
        }
    }
    
    private void build_max_heap(float[] barHeight) throws InterruptedException {
        int size = barHeight.length;
    
        for (int i = size / 2 - 1; i >= 0; i--) {
            max_heapify(barHeight, size, i);
        }
    }
    
    private void heap_sort(float[] barHeight) throws InterruptedException {
        build_max_heap(barHeight);
        
        System.out.println();
        for (int i = barHeight.length - 1; i >= 1; i--) {
            // swap elements
            float temp = barHeight[i];
            barHeight[i] = barHeight[0];
            barHeight[0] = temp;
            swap1 = i;
            swap2 = 0;
            Thread.sleep(1);
            repaint();
    
            max_heapify(barHeight, i, 0);
        }
    }
    

    private void initSorter() {
        sorter = new SwingWorker<>() {
            @Override
            public Void doInBackground() throws InterruptedException {
                // Sorting using merge sort
                heap_sort(barHeight);
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
            frame.setContentPane(new HeapSort());
            frame.validate();
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
