package com.timebridge;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class JournalPanel extends JPanel {
    private DefaultListModel<JournalEntry> listModel;

    public JournalPanel() {
        setLayout(new BorderLayout());
        listModel = new DefaultListModel<>();
        JList<JournalEntry> journalList = new JList<>(listModel);
        journalList.setCellRenderer(new JournalEntryRenderer());
        journalList.setVisibleRowCount(10);
        journalList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        journalList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JournalEntry selectedEntry = journalList.getSelectedValue();
                    if (selectedEntry != null) {
                        showFullEntryDialog(selectedEntry);
                    }
                }
            }
        });

        JPanel buttonsPanel = new JPanel();
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");

        editButton.addActionListener(e -> editSelectedEntry(journalList));
        deleteButton.addActionListener(e -> deleteSelectedEntry(journalList));

        buttonsPanel.add(editButton);
        buttonsPanel.add(deleteButton);

        add(new JScrollPane(journalList), BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);
    }

    public void addJournalEntry(String date, String entry) {
        JournalEntry journalEntry = new JournalEntry(date, entry);
        listModel.addElement(journalEntry);
    }

    private void editSelectedEntry(JList<JournalEntry> journalList) {
        int selectedIndex = journalList.getSelectedIndex();
        if (selectedIndex != -1) {
            JournalEntry selectedEntry = listModel.getElementAt(selectedIndex);
            JTextArea textArea = new JTextArea(selectedEntry.getEntry());
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(300, 150));

            int result = JOptionPane.showConfirmDialog(this, scrollPane, "Edit journal entry:", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String newEntry = textArea.getText();
                if (newEntry != null && !newEntry.trim().isEmpty()) {
                    selectedEntry.setEntry(newEntry);
                    listModel.setElementAt(selectedEntry, selectedIndex);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an entry to edit.");
        }
    }

    private void deleteSelectedEntry(JList<JournalEntry> journalList) {
        int selectedIndex = journalList.getSelectedIndex();
        if (selectedIndex != -1) {
            listModel.removeElementAt(selectedIndex);
        } else {
            JOptionPane.showMessageDialog(this, "Please select an entry to delete.");
        }
    }

    private void showFullEntryDialog(JournalEntry entry) {
        JTextArea textArea = new JTextArea(entry.getEntry());
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(300, 150));

        JDialog dialog = new JDialog((Frame) null, entry.getDate(), true);
        dialog.setLayout(new BorderLayout());
        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private static class JournalEntry {
        private String date;
        private String entry;

        public JournalEntry(String date, String entry) {
            this.date = date;
            this.entry = entry;
        }

        public String getDate() {
            return date;
        }

        public String getEntry() {
            return entry;
        }

        public void setEntry(String entry) {
            this.entry = entry;
        }

        @Override
        public String toString() {
            return date + ": " + (entry.length() > 20 ? entry.substring(0, 20) + "..." : entry);
        }
    }

    private static class JournalEntryRenderer extends JLabel implements ListCellRenderer<JournalEntry> {
        @Override
        public Component getListCellRendererComponent(JList<? extends JournalEntry> list, JournalEntry value, int index, boolean isSelected, boolean cellHasFocus) {
            setText(value.toString());
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            setOpaque(true);
            return this;
        }
    }
}
