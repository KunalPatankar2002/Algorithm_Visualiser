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

public class BubbleSort extends JPanel {
    private static final long serialVersionUID = 1L;
    private final int width = 1000, height = width * 9 / 16;
    private final int size = 200;
    private final float barWidth = (float) width / size;
    private float[] barHeight = new float[size];
    private SwingWorker<Void, Void> shuffler, sorter;
    private int swap1, swap2;

    public BubbleSort() {
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
        g2d.setColor(Color.ORANGE);
        Rectangle2D.Float bar;
        for (int i = 0; i < size; i++) {
            bar = new Rectangle2D.Float(i * barWidth, height - barHeight[i], barWidth, barHeight[i]);
            g2d.fill(bar);
        }
        g2d.setColor(Color.PINK);
        bar = new Rectangle2D.Float(swap1 * barWidth, height - barHeight[swap1], barWidth,
                barHeight[swap1]);
        g2d.fill(bar);
        g2d.setColor(Color.YELLOW);
        bar = new Rectangle2D.Float(swap2 * barWidth, height - barHeight[swap2], barWidth,
                barHeight[swap2]);
        g2d.fill(bar);
    }

    private void initSorter() {
        sorter = new SwingWorker<>() {
            @Override
            public Void doInBackground() throws InterruptedException {
                // Sorting using bubble sort
                float temp = 0;
                for (int i = 0; i < size; i++) {
                    for (int j = 1; j < (size - i); j++) {
                        if (barHeight[j - 1] > barHeight[j]) {
                            // swap elements
                            temp = barHeight[j - 1];
                            barHeight[j - 1] = barHeight[j];
                            barHeight[j] = temp;
                        }
                        swap1 = j-1;
                        swap2 = j;
                        Thread.sleep(10);
                        repaint();
                    }
                }
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
                    Thread.sleep(10);
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
            JFrame frame = new JFrame("Bubble Sort Visualiser");
            frame.setResizable(false);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setContentPane(new BubbleSort());
            frame.validate();
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
