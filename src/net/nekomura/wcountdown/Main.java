package net.nekomura.wcountdown;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimerTask;

public class Main {
    static int mouseAtX;
    static int mouseAtY;
    public static void  main(String[] args) throws ParseException, IOException {
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension sc = kit.getScreenSize();
        JFrame mainFrame = new JFrame();
        mainFrame.setSize(400,268);
        mainFrame.setType(Window.Type.UTILITY);
        mainFrame.setTitle("倒數計時器");
        mainFrame.setIconImage(null);
        mainFrame.setLocation(39*sc.width/45-290,sc.height/9);
        mainFrame.setUndecorated(true);
        mainFrame.setAlwaysOnTop(true);
        mainFrame.setBackground(new Color(0,0,0,0));

        mainFrame.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e)
            {
                mouseAtX = e.getPoint().x;
                mouseAtY= e.getPoint().y;
            }
        });

        mainFrame.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                mainFrame.setLocation((e.getXOnScreen()-mouseAtX),(e.getYOnScreen()-mouseAtY));
            }
        });

        ConfigUtils.get();

        JLabel pretitle = new JLabel("距離");
        pretitle.setBounds(2, 5, 400, 14);
        pretitle.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 14));
        pretitle.setHorizontalAlignment(JLabel.LEFT);

        JLabel posttitle = new JLabel("還有");
        posttitle.setBounds(-2, 60, 400, 14);
        posttitle.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 14));
        posttitle.setHorizontalAlignment(JLabel.RIGHT);

        JLabel title = new JLabel(ConfigUtils.timeName);
        title.setBounds(0, 0, 400, 78);
        title.setFont(new Font("Microsoft JhengHei", Font.BOLD, 32));
        title.setForeground(Color.RED);
        title.setHorizontalAlignment(JLabel.CENTER);

        JLabel bgLabel = new JLabel(new ImageIcon(ImageBase64.getFromBASE64(ResImages.bgBASE64)));
        bgLabel.setBounds(0, 0, 400, 268);

        JLabel toolbarLabel = new JLabel(new ImageIcon(ImageBase64.getFromBASE64(ResImages.toolbarBASE64)));
        toolbarLabel.setBounds(222, 239, 179, 29);

        JButton exitButton = new JButton(new ImageIcon(ImageBase64.getFromBASE64(ResImages.quitBtnBASE64)));
        exitButton.setContentAreaFilled(false);
        exitButton.setBorderPainted(false);
        exitButton.setBounds(320, 240, 74, 27);
        exitButton.addActionListener(e -> System.exit(0));

        JButton settingButton = new JButton(new ImageIcon(ImageBase64.getFromBASE64(ResImages.settingBtnBASE64)));
        settingButton.setContentAreaFilled(false);
        settingButton.setBorderPainted(false);
        settingButton.setBounds(245, 240, 68, 27);
        settingButton.addActionListener(e -> {
            try {
                ConfigUtils.set();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        long ctime = System.currentTimeMillis();

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
        java.util.Date date = df.parse(ConfigUtils.targetTime);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        long ttime = cal.getTimeInMillis();

        long lefttime = ttime - ctime;

        long leftdays = lefttime/(1000*60*60*24);
        long lefthours = lefttime%(1000*60*60*24)/(1000*60*60);
        long leftmins = lefttime%(1000*60*60*24)%(1000*60*60)/(1000*60);
        long leftsecs = lefttime%(1000*60*60*24)%(1000*60*60)%(1000*60)/1000;

        JLabel leftTimeLabel = new JLabel("<html><body><p style=\"text-align:center; width:300px\">" + leftdays + ":" + TimeUtils.keepTwoDigit(lefthours) + ":" + TimeUtils.keepTwoDigit(leftmins) + ":" + TimeUtils.keepTwoDigit(leftsecs) + "</p></body></html>");
        leftTimeLabel.setBounds(0, 80, 400, 188);
        leftTimeLabel.setHorizontalAlignment(JLabel.CENTER);
        leftTimeLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 58));

        JPanel p = new JPanel();
        p.setLayout(null);
        p.setOpaque(false);
        p.add(posttitle);
        p.add(pretitle);
        p.add(title);
        p.add(settingButton);
        p.add(exitButton);
        p.add(toolbarLabel);
        p.add(leftTimeLabel);
        p.add(bgLabel);
        mainFrame.getContentPane().add(p);
        mainFrame.setVisible(true);

        java.util.Timer timer = new java.util.Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                try {
                    long ctime = System.currentTimeMillis();

                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
                    java.util.Date date = df.parse(ConfigUtils.targetTime);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    long ttime = cal.getTimeInMillis();

                    long lefttime = ttime - ctime;

                    long leftdays = lefttime/(1000*60*60*24);
                    long lefthours = lefttime%(1000*60*60*24)/(1000*60*60);
                    long leftmins = lefttime%(1000*60*60*24)%(1000*60*60)/(1000*60);
                    long leftsecs = lefttime%(1000*60*60*24)%(1000*60*60)%(1000*60)/1000;

                    if((leftdays <= 0 & lefthours <= 0 & leftmins <= 0 & leftsecs <= 0) && !(leftdays == 0 && lefthours == 0 && leftmins == 0 && leftsecs == 0)) { //避免0:00:00:00還沒出現就直接跳"時間到了"
                        leftTimeLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 36));
                        leftTimeLabel.setText("The Time Has Come!");
                        leftTimeLabel.setForeground(Color.RED);
                    }else {
                        leftTimeLabel.setText("<html><body><p style=\"text-align:center; width:300px\">" + leftdays + ":" + TimeUtils.keepTwoDigit(lefthours) + ":" + TimeUtils.keepTwoDigit(leftmins) + ":" + TimeUtils.keepTwoDigit(leftsecs) + "</p></body></html>");
                        leftTimeLabel.setBounds(0, 80, 400, 188);
                        leftTimeLabel.setHorizontalAlignment(JLabel.CENTER);
                        leftTimeLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 58));
                        leftTimeLabel.setForeground(new Color(51,51,51));
                    }

                    title.setText(ConfigUtils.timeName);

                }catch(Throwable ignored) {

                }

            }
        }, 0L, 100L);
    }
}
