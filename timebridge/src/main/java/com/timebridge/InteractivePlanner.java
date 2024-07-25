package com.timebridge;

import javax.swing.*;

/**
 * Main application class that sets up the JFrame and tabbed pane
 * with Alarm, Calendar, and Journal panels.
 */
public class InteractivePlanner extends JFrame {
    public InteractivePlanner() {
        setTitle("TimeBridge");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTabbedPane tabbedPane = new JTabbedPane();

        // Initialize the three main panels and add them to the tabbed pane
        AlarmPanel alarmPanel = new AlarmPanel();
        JournalPanel journalPanel = new JournalPanel();
        CalendarPanel calendarPanel = new CalendarPanel(journalPanel);

        tabbedPane.addTab("Alarm", alarmPanel);
        tabbedPane.addTab("Calendar", calendarPanel);
        tabbedPane.addTab("Journal", journalPanel);

        add(tabbedPane);
    }

    public static void main(String[] args) {
        // Launch the application
        SwingUtilities.invokeLater(() -> {
            InteractivePlanner planner = new InteractivePlanner();
            planner.setVisible(true);
        });
    }
}
