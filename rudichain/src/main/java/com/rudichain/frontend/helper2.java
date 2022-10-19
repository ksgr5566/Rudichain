package com.rudichain.frontend;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.Desktop;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;

import com.rudichain.backend.block;
import com.rudichain.constants;

public class helper2 extends JFrame implements constants{
    
        helper2(int i){

            block k = HomePage.chain.chain.get(i);
            JPanel p2=new JPanel();
            p2.setLayout(new GridLayout(8, 2, 30, 30));
            p2.setBackground(Color.BLACK);

            TitledBorder Title1 = null;
            if(i==0){
                Title1 = new TitledBorder("Genesis Block");
            }else{
                Title1 = new TitledBorder("Block "+ i);
            }

            Title1.setTitleFont(new Font("Consolas",Font.BOLD,25));
            Title1.setTitleColor(new Color(197,175,107));
            p2.setBorder(Title1);
            JLabel h11=new JLabel("Hash : ");
            JLabel h21=new JLabel("TimeStamp : ");
            JLabel h31=new JLabel("Data : ");
            JLabel h41=new JLabel("nonce : ");
            JLabel h51=new JLabel("Difficulty : ");
            JLabel minR = new JLabel("Miner: ");

            JLabel h011 = null; JLabel h61 = null; JLabel h71 = null;
            JLabel h91 = null; JLabel h101 = null; JLabel minVal = null;

            if(i==0){
                h011 = new JLabel(GENISIS_BLOCK.lastHash);
                h61 = new JLabel(GENISIS_BLOCK.hash);
                h71 = new JLabel(Long.toString(GENISIS_BLOCK.timestamp));
                h91 = new JLabel(Long.toString(GENISIS_BLOCK.nonce));
                h101 = new JLabel(Integer.toString(GENISIS_BLOCK.difficulty));
                minVal = new JLabel("mined by the system");
            }else{
                h011 = new JLabel(k.lastHash);
                h61=new JLabel(k.hash);
                h71 = new JLabel(Long.toString(k.timestamp));
                h91 = new JLabel(Long.toString(k.nonce));
                h101 = new JLabel(Integer.toString(k.difficulty));
               
                ArrayList<String> h = new ArrayList<>();
                k.data.forEach(v->{
                    if(v.input == null){
                        Set keys = v.OutputMap.keySet();
                        Iterator itertr = keys.iterator();
                        String minerPubKey =itertr.next().toString();
                        h.add(minerPubKey);
                    }
                });
                minVal = new JLabel(h.get(0));
            }

            JButton h81=new JButton("Show Transactions");
            h81.setFocusable(false);
            h81.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    new chainPageHelper(k.data);
                }
            });

            h11.setForeground(new Color(0x00FF00));
            h21.setForeground(new Color(0x00FF00));
            h31.setForeground(new Color(0x00FF00));
            h41.setForeground(new Color(0x00FF00));
            h51.setForeground(new Color(0x00FF00));
            minR.setForeground(new Color(0x00FF00));
            h61.setForeground(Color.white);
            h71.setForeground(Color.white);
            minVal.setForeground(Color.white);
            h81.setForeground(Color.black);
            h91.setForeground(Color.white);
            h101.setForeground(Color.white);
            h11.setFont(new Font("Consolas",Font.BOLD,17));
            h21.setFont(new Font("Consolas",Font.BOLD,17));
            h31.setFont(new Font("Consolas",Font.BOLD,17));
            h41.setFont(new Font("Consolas",Font.BOLD,17));
            h51.setFont(new Font("Consolas",Font.BOLD,17));
            h61.setFont(new Font("Consolas",Font.BOLD,17));
            h71.setFont(new Font("Consolas",Font.BOLD,17));
            h81.setFont(new Font("Consolas",Font.BOLD,17));
            h91.setFont(new Font("Consolas",Font.BOLD,17));
            h101.setFont(new Font("Consolas",Font.BOLD,17));
            minR.setFont(new Font("Consolas",Font.BOLD,17));
            minVal.setFont(new Font("Consolas",Font.BOLD,17));
            p2.add(h11);
            p2.add(h61);
            p2.add(h21);
            p2.add(h71);
            p2.add(h41);
            p2.add(h91);
            p2.add(h51);
            p2.add(h101);
            p2.add(minR);
            p2.add(minVal);
            p2.add(h31);
            p2.add(h81);

            ImageIcon logo = null;
            if(System.getProperty("os.name").toLowerCase().indexOf("mac")>=0){
                logo = new ImageIcon("src/main/java/com/rudichain/frontend/images/logo.PNG");
            }else{
                logo = new ImageIcon("src\\main\\java\\com\\rudichain\\frontend\\images\\logo.PNG");
            }
            this.setIconImage(logo.getImage());

            this.setTitle("Rudichain: Block "+ i);

            this.add(p2);
            this.setMinimumSize(new Dimension(1000,750));
            this.setVisible(true);
            this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    
        }
    
}