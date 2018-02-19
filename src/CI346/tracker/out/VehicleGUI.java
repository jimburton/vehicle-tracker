package CI346.tracker.out;

import CI346.tracker.Vehicle;
import CI346.tracker.VehicleFactory;
import CI346.tracker.VehicleTracker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class VehicleGUI extends JFrame {
    private static final int DOT_SIZE = 5;
    private VehicleTracker tracker;

    public VehicleGUI(VehicleTracker tracker) {
        this.tracker = tracker;
        setTitle("Vehicle Tracker");
        setSize(VehicleFactory.MAX_X, VehicleFactory.MAX_Y);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        PaintPanel panel = new PaintPanel();
        panel.setPreferredSize(new Dimension(VehicleFactory.MAX_X,VehicleFactory.MAX_Y));
        getContentPane().add(panel, BorderLayout.CENTER);
        pack();
        setVisible(true);
    }


    class PaintPanel extends JPanel {
        Timer timer;

        public PaintPanel() {
            super();
            setDoubleBuffered(true);
            this.setBackground( Color.LIGHT_GRAY );
            refreshScreen();
        }

        public void refreshScreen() {
            timer = new Timer(0, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    repaint();
                }
            });
            timer.setRepeats(true);
            // Aprox. 60 FPS
            timer.setDelay(17);
            timer.start();
        }

        public void paint( Graphics g ) {
            super.paint( g ); // clears drawing area
            g.drawRect (10, 10, 200, 200);
            Map<String, Vehicle> locations = tracker.getLocations();
            String targetID = tracker.getTargetID();
            Vehicle p;
            for(String id: locations.keySet()) {
                p = tracker.getLocation(id);
                if(p != null) {
                    if(id.equals(targetID)) {
                        g.setColor(Color.RED);
                    }
                    g.fillOval(p.x, p.y, DOT_SIZE, DOT_SIZE);
                    g.drawString(id.substring(id.length() - 1), p.x+DOT_SIZE, p.y);
                    if(id.equals(targetID)) {
                        g.setColor(Color.BLACK);
                    }
                }
            }
        }
    }
}
