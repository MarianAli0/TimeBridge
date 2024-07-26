package com.timebridge;

import javax.swing.*;
import com.toedter.calendar.JCalendar;
import java.awt.*;
import java.util.Date;


/**
 * Panel for displaying a calendar and adding journal entries for specific dates.
 */
public class CalendarPanel extends JPanel {
    private JCalendar calendar;
    private JButton addJournalButton;
    private JournalPanel journalPanel;

    public CalendarPanel(JournalPanel journalPanel) {
        this.journalPanel = journalPanel;
        setLayout(new BorderLayout());

        calendar = new JCalendar();
        calendar.getDayChooser().addPropertyChangeListener("day", evt -> {
            Date selectedDate = calendar.getDate();
            showAddJournalButton(selectedDate);
        });

        add(calendar, BorderLayout.CENTER);
        addJournalButton = new JButton("+");
        addJournalButton.addActionListener(e -> showJournalDialog(calendar.getDate()));
        add(addJournalButton, BorderLayout.SOUTH);
        addJournalButton.setVisible(false);
    }

    
    /**
     * Shows the "Add Journal" button when a date is selected.
     */
    private void showAddJournalButton(Date selectedDate) {
        addJournalButton.setVisible(true);
    }

    /**
     * Displays a dialog for entering a journal entry for the selected date.
     */
    private void showJournalDialog(Date date) {
        String dateString = date.toString();
        JTextArea textArea = new JTextArea(5, 20);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(300, 150));

        int result = JOptionPane.showConfirmDialog(this, scrollPane, "Enter journal entry for " + dateString + ":", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String journalEntry = textArea.getText();
            if (journalEntry != null && !journalEntry.trim().isEmpty()) {
                journalPanel.addJournalEntry(dateString, journalEntry);
                addJournalButton.setVisible(false);
            }
        }
    }
}
