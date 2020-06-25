package net.nekomura;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ConfigUtils {
    public static String targetTime;
    public static String timeName;

    public static void get() throws IOException {
        File config = new File("config\\config.conf");
        if(!config.exists() || Files.notExists(Paths.get("config"))) {
            set();
        }
        String content = FileUtils.readFileToString(config, "utf-8");
        JSONObject configJSON = new JSONObject(content);
        targetTime = configJSON.getString("ttime");
        timeName = configJSON.getString("tname");
    }

    public static void set() throws IOException {
        JLabel targetTimeLabel = new JLabel("設定時間(年-月-日-時-分):", SwingConstants.RIGHT);
        JLabel timeNameLabel = new JLabel("日期名稱:", SwingConstants.RIGHT);

        JTextField targetTimeField = new JTextField(targetTime, 20);
        JTextField timeNameField = new JTextField(timeName, 20);

        Container cp = new Container();
        cp.setLayout(new GridBagLayout());
        cp.setBackground(UIManager.getColor("control"));
        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.insets = new Insets(2, 2, 2, 2);
        c.anchor = GridBagConstraints.EAST;

        cp.add(targetTimeLabel, c);
        cp.add(timeNameLabel, c);

        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.CENTER;

        cp.add(targetTimeField, c);
        c.gridx = 1;
        c.gridy = GridBagConstraints.RELATIVE;
        cp.add(timeNameField, c);
        c.weightx = 0.0;
        c.fill = GridBagConstraints.NONE;

        int result = JOptionPane.showOptionDialog(null, cp,"設定",JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION, null, null, null);

        if (result == JOptionPane.OK_OPTION) {
            if(targetTimeField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "時間不得為空。", "錯誤", JOptionPane.WARNING_MESSAGE);
                set();
                return;
            }
            if(!TimeUtils.isTimeFormat(targetTimeField.getText())) {
                JOptionPane.showMessageDialog(null, "輸入之時間不符合格式(yyyy-MM-dd-HH-mm)", "錯誤", JOptionPane.WARNING_MESSAGE);
                set();
                return;
            }
        }else if (result == JOptionPane.CLOSED_OPTION && !new File("config\\config.conf").exists()) {
            int closecmd = JOptionPane.showOptionDialog(null, "請問是否要關閉程式?","提示", JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_CANCEL_OPTION, null, null, null);
            if(closecmd == JOptionPane.OK_OPTION) {
                System.exit(0);
            }
        }

        targetTime = targetTimeField.getText();
        timeName = timeNameField.getText();

        write();
    }

    public static void write() throws IOException {
        /*建立config資料夾*/
        File folder = new File("config");
        folder.mkdir();

        /*建立config文件*/
        File configFile = new File("config\\config.conf");
        configFile.createNewFile();
        OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream("config\\config.conf"),"UTF-8");

        JSONObject configJSON = new JSONObject();
        configJSON.put("ttime", targetTime);
        configJSON.put("tname", timeName);

        out.write(configJSON.toString());
        out.close();
    }
}