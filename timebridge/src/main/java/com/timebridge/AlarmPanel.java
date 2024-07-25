package com.timebridge;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel for managing alarms, including setting new alarms,
 * and enabling/disabling/deleting existing alarms.
 */

public class AlarmPanel extends JPanel {
    private List<Alarm> alarms;
    private DefaultListModel<Alarm> listModel;

    public AlarmPanel() {
        setLayout(new BorderLayout());
        alarms = new ArrayList<>();
        listModel = new DefaultListModel<>();

        JButton setAlarmButton = new JButton("Set Alarm");
        setAlarmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAlarmDialog();
            }
        });

        add(setAlarmButton, BorderLayout.NORTH);

        JList<Alarm> alarmList = new JList<>(listModel);
        alarmList.setCellRenderer(new AlarmCellRenderer());
        alarmList.setVisibleRowCount(10);
        add(new JScrollPane(alarmList), BorderLayout.CENTER);
    }

    private void showAlarmDialog() {
        JPanel panel = new JPanel(new GridLayout(4, 2));
        SpinnerModel hourModel = new SpinnerNumberModel(0, 0, 23, 1);
        JSpinner hourSpinner = new JSpinner(hourModel);
        SpinnerModel minuteModel = new SpinnerNumberModel(0, 0, 59, 1);
        JSpinner minuteSpinner = new JSpinner(minuteModel);
        
        JCheckBox[] dayCheckboxes = new JCheckBox[7];
        String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        for (int i = 0; i < days.length; i++) {
            dayCheckboxes[i] = new JCheckBox(days[i]);
            panel.add(dayCheckboxes[i]);
        }
        
        panel.add(new JLabel("Hour:"));
        panel.add(hourSpinner);
        panel.add(new JLabel("Minute:"));
        panel.add(minuteSpinner);

        int option = JOptionPane.showConfirmDialog(null, panel, "Set Alarm", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            int hour = (int) hourSpinner.getValue();
            int minute = (int) minuteSpinner.getValue();
            StringBuilder daysStringBuilder = new StringBuilder();
            for (JCheckBox dayCheckbox : dayCheckboxes) {
                if (dayCheckbox.isSelected()) {
                    daysStringBuilder.append(dayCheckbox.getText()).append(" ");
                }
            }
            String daysString = daysStringBuilder.toString().trim();
            if (!daysString.isEmpty()) {
                String time = String.format("%02d:%02d", hour, minute);
                Alarm alarm = new Alarm(time, daysString, true);
                alarms.add(alarm);
                listModel.addElement(alarm);
            } else {
                JOptionPane.showMessageDialog(null, "Please select at least one day.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static class Alarm {
        private String time;
        private String days;
        private boolean enabled;

        public Alarm(String time, String days, boolean enabled) {
            this.time = time;
            this.days = days;
            this.enabled = enabled;
        }

        public String getTime() {
            return time;
        }

        public String getDays() {
            return days;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        @Override
        public String toString() {
            return time + " (" + days + ")";
        }
    }

    private class AlarmCellRenderer extends JPanel implements ListCellRenderer<Alarm> {
        private JLabel timeLabel;
        private JLabel daysLabel;
        private JToggleButton toggleButton;
        private JButton deleteButton;

        public AlarmCellRenderer() {
            setLayout(new BorderLayout());
            timeLabel = new JLabel();
            daysLabel = new JLabel();
            toggleButton = new JToggleButton("On/Off");
            toggleButton.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    JToggleButton toggle = (JToggleButton) e.getSource();
                    Alarm alarm = (Alarm) toggle.getClientProperty("alarm");
                    if (toggle.isSelected()) {
                        toggle.setText("On");
                        alarm.setEnabled(true);
                    } else {
                        toggle.setText("Off");
                        alarm.setEnabled(false);
                    }
                }
            });

            deleteButton = new JButton("Delete");
            deleteButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int index = listModel.indexOf((Alarm) deleteButton.getClientProperty("alarm"));
                    if (index >= 0) {
                        listModel.remove(index);
                    }
                }
            });

            JPanel textPanel = new JPanel(new GridLayout(2, 1));
            textPanel.add(timeLabel);
            textPanel.add(daysLabel);

            JPanel buttonPanel = new JPanel(new BorderLayout());
            buttonPanel.add(toggleButton, BorderLayout.NORTH);
            buttonPanel.add(deleteButton, BorderLayout.SOUTH);

            add(textPanel, BorderLayout.CENTER);
            add(buttonPanel, BorderLayout.EAST);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Alarm> list, Alarm value, int index, boolean isSelected, boolean cellHasFocus) {
            timeLabel.setText(value.getTime());
            daysLabel.setText(value.getDays());
            toggleButton.setSelected(value.isEnabled());
            toggleButton.putClientProperty("alarm", value);
            deleteButton.putClientProperty("alarm", value);
            return this;
        }
    }
}
