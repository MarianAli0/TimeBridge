package com.timebridge;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AlarmPanel extends JPanel {
    private List<Alarm> alarms;
    private DefaultListModel<Alarm> alarmListModel;
    private JList<Alarm> alarmList;
    private Timer timer;
    private JLabel clockLabel;

    public AlarmPanel() {
        setLayout(new BorderLayout());

        // Initialize alarms list and list model
        alarms = new ArrayList<>();
        alarmListModel = new DefaultListModel<>();
        alarmList = new JList<>(alarmListModel);
        alarmList.setCellRenderer(new AlarmCellRenderer());

        // Initialize and configure clock label
        clockLabel = new JLabel();
        clockLabel.setFont(new Font("Arial", Font.BOLD, 48));
        clockLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(clockLabel, BorderLayout.NORTH);

        // Create and start the timer to update the clock label every second
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateClock();
                checkAlarms();
            }
        });
        timer.start();

        // Add alarm list to the center
        add(new JScrollPane(alarmList), BorderLayout.CENTER);

        // Add set alarm button at the bottom
        JButton setAlarmButton = new JButton("Set Alarm");
        setAlarmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showSetAlarmDialog();
            }
        });
        add(setAlarmButton, BorderLayout.SOUTH);
    }

    private void updateClock() {
        Calendar calendar = Calendar.getInstance();
        String time = String.format("%02d:%02d:%02d", 
            calendar.get(Calendar.HOUR_OF_DAY), 
            calendar.get(Calendar.MINUTE), 
            calendar.get(Calendar.SECOND));
        clockLabel.setText(time);
    }

    private void checkAlarms() {
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);
        int currentDay = calendar.get(Calendar.DAY_OF_WEEK) - 1; // Calendar.DAY_OF_WEEK starts from 1 (Sunday)

        for (Alarm alarm : alarms) {
            if (alarm.enabled && alarm.hours == currentHour && alarm.minutes == currentMinute && alarm.daysOfWeek[currentDay]) {
                triggerAlarm(alarm);
            }
        }
    }

    private void triggerAlarm(Alarm alarm) {
        // Display alarm details
        JOptionPane.showMessageDialog(this, "Alarm: " + alarm.name, "Alarm Triggered", JOptionPane.INFORMATION_MESSAGE);
        
        // Handle alarm sound, vibration, and snooze
        if (alarm.sound) {
            // Play alarm sound (sound handling code would be here)
        }
        if (alarm.vibration) {
            // Trigger vibration (vibration handling code would be here)
        }
        if (alarm.snooze) {
            // Snooze functionality (snooze handling code would be here)
        }
    }

    private void showSetAlarmDialog() {
        JComboBox<String> hoursBox = new JComboBox<>();
        for (int i = 0; i < 24; i++) {
            hoursBox.addItem(String.format("%02d", i));
        }
        JComboBox<String> minutesBox = new JComboBox<>();
        for (int i = 0; i < 60; i++) {
            minutesBox.addItem(String.format("%02d", i));
        }

        JCheckBox[] dayCheckBoxes = new JCheckBox[7];
        String[] days = {"S", "M", "T", "W", "T", "F", "S"};
        for (int i = 0; i < 7; i++) {
            dayCheckBoxes[i] = new JCheckBox(days[i]);
        }

        JTextField alarmNameField = new JTextField(10);
        JCheckBox alarmSoundCheckBox = new JCheckBox("Alarm sound");
        JCheckBox vibrationCheckBox = new JCheckBox("Vibration");
        JCheckBox snoozeCheckBox = new JCheckBox("Snooze");

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2));
        panel.add(new JLabel("Hours:"));
        panel.add(hoursBox);
        panel.add(new JLabel("Minutes:"));
        panel.add(minutesBox);
        panel.add(new JLabel("Days:"));
        JPanel daysPanel = new JPanel();
        for (JCheckBox dayCheckBox : dayCheckBoxes) {
            daysPanel.add(dayCheckBox);
        }
        panel.add(daysPanel);
        panel.add(new JLabel("Alarm name:"));
        panel.add(alarmNameField);
        panel.add(alarmSoundCheckBox);
        panel.add(vibrationCheckBox);
        panel.add(snoozeCheckBox);

        int result = JOptionPane.showConfirmDialog(null, panel, "Set Alarm", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int hours = Integer.parseInt((String) hoursBox.getSelectedItem());
                int minutes = Integer.parseInt((String) minutesBox.getSelectedItem());
                boolean[] daysOfWeek = new boolean[7];
                for (int i = 0; i < 7; i++) {
                    daysOfWeek[i] = dayCheckBoxes[i].isSelected();
                }
                String alarmName = alarmNameField.getText();
                boolean alarmSound = alarmSoundCheckBox.isSelected();
                boolean vibration = vibrationCheckBox.isSelected();
                boolean snooze = snoozeCheckBox.isSelected();

                Alarm alarm = new Alarm(hours, minutes, daysOfWeek, alarmName, alarmSound, vibration, snooze);
                alarms.add(alarm);
                alarmListModel.addElement(alarm);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid time format", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class Alarm {
        private int hours;
        private int minutes;
        private boolean[] daysOfWeek;
        private String name;
        private boolean sound;
        private boolean vibration;
        private boolean snooze;
        private boolean enabled;

        public Alarm(int hours, int minutes, boolean[] daysOfWeek, String name, boolean sound, boolean vibration, boolean snooze) {
            this.hours = hours;
            this.minutes = minutes;
            this.daysOfWeek = daysOfWeek;
            this.name = name;
            this.sound = sound;
            this.vibration = vibration;
            this.snooze = snooze;
            this.enabled = true;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("%02d:%02d ", hours, minutes));
            String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
            for (int i = 0; i < 7; i++) {
                if (daysOfWeek[i]) {
                    sb.append(days[i]).append(" ");
                }
            }
            sb.append(name).append(" ");
            sb.append(enabled ? "Enabled" : "Disabled");
            return sb.toString();
        }
    }

    private class AlarmCellRenderer extends JCheckBox implements ListCellRenderer<Alarm> {
        @Override
        public Component getListCellRendererComponent(JList<? extends Alarm> list, Alarm value, int index, boolean isSelected, boolean cellHasFocus) {
            setText(value.toString());
            setSelected(value.enabled);
            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    value.setEnabled(isSelected());
                }
            });
            return this;
        }
    }
}
