
package algo_proj;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

class Point4 {
    int x, y;

    Point4(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

public class BruteForceConvexHull extends JFrame {
    private static final long serialVersionUID = 1L;
    private final List<Point4> points;
    private final List<Point4> convexHull;
    private boolean convexHullComputed;

    public BruteForceConvexHull() {
        points = new ArrayList<>();
        convexHull = new ArrayList<>();
        convexHullComputed = false;

        setTitle("Convex Hull Visualization");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawGraph(g);
                drawPoints(g);
                drawConvexHull(g);
            }
        };

        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                points.add(new Point4(evt.getX() - 400, 300 - evt.getY()));
                convexHullComputed = false;
                repaint();
            }
        });

        JButton computeHullButton = new JButton("Compute Convex Hull");
        computeHullButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!convexHullComputed) {
                    computeAndDisplayConvexHull();
                    convexHullComputed = true;
                }
            }
        });

        add(computeHullButton, "South");
        add(panel);
    }

    private void drawGraph(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;


        // Draw grid lines
        g2d.setColor(new Color(200, 200, 200, 200));
        for (int i = 0; i <= 800; i += 20) {
            g2d.drawLine(i, 0, i, 600);
        }
        for (int i = 0; i <= 600; i += 20) {
            g2d.drawLine(0, i, 800, i);
        }
        g2d.setColor(Color.BLACK);

        // Draw x-axis
        g2d.drawLine(0, 300, 800, 300);
        for (int i = 0; i <= 800; i += 20) {
            g2d.drawLine(i, 295, i, 305);
            if (i % 40 == 0) {
                g2d.drawString(Integer.toString((i - 400) / 20), i - 5, 320);
            }
        }

        // Draw y-axis
        g2d.drawLine(400, 0, 400, 600);
        for (int i = 0; i <= 600; i += 20) {
            g2d.drawLine(395, i, 405, i);
            if (i % 40 == 0) {
                g2d.drawString(Integer.toString((300 - i) / 20), 380, i + 5);
            }
        }


    }

    private void drawPoints(Graphics g) {
        for (Point4 point : points) {
            g.setColor(Color.BLACK);
            g.fillOval(point.x + 400 - 3, 300 - point.y - 3, 6, 6);
        }
    }

    private void drawConvexHull(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        for (Point4 point : points) {
            if (convexHull.contains(point)) {
                g2d.setColor(Color.RED);
            } else {
                g2d.setColor(Color.BLACK);
            }
            g2d.fillOval(point.x + 400 - 3, 300 - point.y - 3, 6, 6);
        }
    }

    private boolean allOnOneSide(Point4 p1, Point4 p2, List<Point4> points) {
        int n = points.size();
        boolean oneSide = true;
        int side = 0;

        for (int i = 0; i < n && oneSide; i++) {
            if (points.get(i) != p1 && points.get(i) != p2) {
                int crossProduct = (p2.y - p1.y) * (points.get(i).x - p1.x) - (p2.x - p1.x) * (points.get(i).y - p1.y);
                if (crossProduct != 0) {
                    if (side == 0) {
                        side = crossProduct;
                    } else if (side * crossProduct < 0) {
                        oneSide = false;
                    }
                }
            }
        }

        return oneSide;
    }

    private List<Point4> computeHull(List<Point4> points) {
        List<Point4> convexHull = new ArrayList<>();
        int n = points.size();

        if (n < 3)
            return points;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    Point4 p1 = points.get(i);
                    Point4 p2 = points.get(j);
                    if (allOnOneSide(p1, p2, points)) {
                        if (!convexHull.contains(p1)) {
                            convexHull.add(p1);
                        }
                        if (!convexHull.contains(p2)) {
                            convexHull.add(p2);
                        }
                    }
                }
            }
        }

        return convexHull;
    }

    private void computeAndDisplayConvexHull() {
        convexHull.clear();
        if (points.size() >= 3) {
            List<Point4> hull = computeHull(points);
            convexHull.addAll(hull);
            repaint();

            // Output the convex hull
            System.out.println("Convex Hull Points:");
            for (Point4 point : hull) {
                System.out.println("(" + point.x + ", " + point.y + ")");
            }
        } else {
            System.out.println("At least three points are required to compute the convex hull.");
        }
    }
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            BruteForceConvexHull frame = new BruteForceConvexHull();
            frame.setVisible(true);
        });
    }
}