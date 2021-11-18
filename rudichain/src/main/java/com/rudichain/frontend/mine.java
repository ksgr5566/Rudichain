package com.rudichain.frontend;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import com.rudichain.cryptography.ECDSA;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class mine extends JFrame{

    public static boolean oneFrame = false;

    public mine(){

        int n = HomePage.pool.transactionMap.size();
        oneFrame = true;
        //....................Top Panel......................................................//
        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));

        JPanel topHelper1 = new JPanel();
        topHelper1.setBackground(new Color(0x123456));
        topHelper1.setLayout(new FlowLayout(FlowLayout.CENTER));

        JPanel topHelper2 = new JPanel();
        topHelper2.setBackground(new Color(0x123456));
        topHelper2.setLayout(new FlowLayout(FlowLayout.CENTER));

        JLabel txt = new JLabel("Would you like to start mining the existing pool?");
        txt.setForeground(Color.white);
        txt.setFont(new Font("Consolas",Font.BOLD,20));

        topHelper1.add(txt);

        JButton mineButton = new JButton("Let's Mine!!!");
        mineButton.setBackground(Color.black);
        mineButton.setForeground(Color.white);
        mineButton.setFont(new Font("Consolas",Font.BOLD,17));
        mineButton.setFocusable(false);
        mineButton.setPreferredSize(new Dimension(250, 40));
        mineButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                HomePage.miner.mineTransactions();
                JOptionPane.showMessageDialog(new JFrame(), "Please wait while Mining is in Progess....", "Be Patient!",JOptionPane.INFORMATION_MESSAGE);
                oneFrame = false;
                dispose();
            }
        });

        JButton refresh = new JButton("\u27F3");
        refresh.setFocusable(false);
        refresh.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                oneFrame = false;
                dispose();
                new mine();
            }
        });

        topHelper2.add(mineButton);
        topHelper2.add(refresh);

        top.add(topHelper1);
        top.add(topHelper2);
        //..........................................................................................//


        JPanel main1 = new JPanel();
        main1.setLayout(new BoxLayout(main1, BoxLayout.Y_AXIS));
        main1.setBackground(Color.black);

        if(n==0){
            JLabel sample = new JLabel("No transactions in the Pool now. Try refreshing! or Wait and Try later!!");
            sample.setForeground(Color.red);
            main1.add(sample);
        }

        HomePage.pool.transactionMap.forEach((k,v)->{

            JPanel box = new JPanel();
            box.setBackground(Color.black);
            box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));

            JPanel first = new JPanel();
            first.setLayout(new FlowLayout(FlowLayout.LEADING));
            first.setBackground(Color.black);

            JLabel txt1 = new JLabel("Transaction Id:  ");
            txt1.setForeground(new Color(0x00FF00));
        
            JLabel res1 = new JLabel(k);
            res1.setForeground(Color.white);
           
            first.add(txt1);
            first.add(res1);

            JPanel second = new JPanel();
            second.setLayout(new FlowLayout(FlowLayout.LEADING));
            second.setBackground(Color.black);

            JLabel txt2 = new JLabel("---Transaction--- ");
            txt2.setForeground(Color.red);

            second.add(txt2);

            JPanel twoPointFive = new JPanel();
            twoPointFive.setLayout(new FlowLayout(FlowLayout.LEADING));
            twoPointFive.setBackground(Color.black);

            JLabel txt2_3 = new JLabel("TimeStamp---->   ");
            txt2_3.setForeground(new Color(0x00FF00));
            
            JLabel txt2_6 = new JLabel(Long.toString(v.input.timestamp));
            txt2_6.setForeground(Color.white);
           
            twoPointFive.add(txt2_3);
            twoPointFive.add(txt2_6);

            JPanel third = new JPanel();
            third.setLayout(new FlowLayout(FlowLayout.LEADING));
            third.setBackground(Color.black);

            JLabel txt3 = new JLabel("Input--> Sender: ");
            txt3.setForeground(new Color(0x00FF00));
            
            JLabel txt4 = new JLabel(ECDSA.compressPubKey(v.input.address));
            txt4.setForeground(Color.white);
            
            JLabel txt5 = new JLabel("Balance of Sender at Input-->");
            txt5.setForeground(new Color(0x00FF00));
            
            JLabel txt6 = new JLabel(Double.toString(v.input.amount));
            txt6.setForeground(Color.white);

            third.add(txt3);
            third.add(txt4);
            third.add(txt5);
            third.add(txt6);

            JPanel fourth = new JPanel();
            fourth.setLayout(new FlowLayout(FlowLayout.LEADING));
            fourth.setBackground(Color.black);

            JLabel txt7 = new JLabel("Output:  ");
            txt7.setForeground(new Color(0x00FF00));

            fourth.add(txt7);

            box.add(first);
            box.add(second);
            box.add(twoPointFive);
            box.add(third);
            box.add(fourth);

            v.OutputMap.forEach((a,b)->{
                JPanel loopanel = new JPanel();
                loopanel.setBackground(Color.black);

                JLabel looptxt1 = new JLabel("Public Key: ");
                looptxt1.setForeground(new Color(0x00FF00));
                
                JLabel looptxt2 = new JLabel(a);
                looptxt2.setForeground(Color.white);
               
                JLabel looptxt3 = new JLabel("Amount-->");
                looptxt3.setForeground(new Color(0x00FF00));

                JLabel looptxt4 = new JLabel(Double.toString(b));
                looptxt4.setForeground(Color.white);

                loopanel.add(looptxt1);
                loopanel.add(looptxt2);
                loopanel.add(looptxt3);
                loopanel.add(looptxt4);

                box.add(loopanel);

            });

            box.setBorder(BorderFactory.createLineBorder(new Color(197,175,107),5));

            main1.add(Box.createVerticalStrut(10));  
            main1.add(box);

        });

        JScrollPane scrollPane = new JScrollPane(main1);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        ImageIcon logo = null;
        if(System.getProperty("os.name").toLowerCase().indexOf("mac")>=0){
            logo = new ImageIcon("src/main/java/com/rudichain/frontend/images/logo.PNG");
        }else{
            logo = new ImageIcon("src\\main\\java\\com\\rudichain\\frontend\\images\\logo.PNG");
        }
        this.setIconImage(logo.getImage());
        this.setLayout(new BorderLayout());
        this.setMinimumSize(new Dimension(1000,750));
        this.add(top, BorderLayout.NORTH);
        this.getContentPane().add(scrollPane);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                oneFrame = false;
                dispose();
            }
        });   
    }
    
}