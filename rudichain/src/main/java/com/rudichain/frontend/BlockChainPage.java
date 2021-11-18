package com.rudichain.frontend;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.GridLayout;

import java.awt.Color;
import java.awt.Font;

import javax.swing.ImageIcon;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.rudichain.constants;

public class BlockChainPage extends JFrame implements constants{

    public static boolean singleFrame = false;

    public BlockChainPage(){

        singleFrame = true;

        JPanel topp = new JPanel();
        topp.setBackground(new Color(0x123456));
        topp.setLayout(new FlowLayout(FlowLayout.CENTER));
        
        JLabel txt1 = new JLabel("Find the block ");
        txt1.setFont(new Font("Consolas",Font.BOLD,20));
        txt1.setForeground(Color.white);
        JTextField txt2 = new JTextField();
        txt2.setBackground(Color.black);
        txt2.setForeground(new Color(197,175,107));
        txt2.setCaretColor(Color.white);
        txt2.setFont(new Font("Consolas",Font.BOLD,20));
        txt2.setPreferredSize(new Dimension(60,30));
        JButton but1 = new JButton("\uD83D\uDD0D");
        but1.setFocusable(false);
        but1.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                int z = 0;
                try{
                    z = Integer.valueOf(txt2.getText());
                }catch(Exception aaa){
                    JOptionPane.showMessageDialog(new JFrame(),"Enter a valid Number","Error Box",JOptionPane.ERROR_MESSAGE);
                    txt2.setText("");
                    return;
                }
                try{
                    if(z>HomePage.chain.chain.size()) throw new Exception();
                }catch(Exception bbb){
                    JOptionPane.showMessageDialog(new JFrame(),"Requested Block does not exist!!","Error Box",JOptionPane.ERROR_MESSAGE);
                    txt2.setText("");
                    return;
                }
                new helper2(z);
            }
        });

        JButton refresh = new JButton("\u27F3");
        refresh.setFocusable(false);
        refresh.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                singleFrame = false;
                dispose();
                new BlockChainPage();
            }
        });

        topp.add(txt1);
        topp.add(txt2);
        topp.add(but1);
        topp.add(refresh);

        JPanel Home = new JPanel();
        Home.setBackground(Color.lightGray);
        Home.setLayout(new BorderLayout(0,0));
        Home.setLayout(new GridLayout(3, 1, 5, 5));


        ArrayList<Integer> oo = new ArrayList<>();
        oo.add(1);
        HomePage.chain.chain.forEach(k->{
            JPanel p2=new JPanel();
            p2.setLayout(new GridLayout(8, 2, 30, 30));
            p2.setBackground(Color.BLACK);
            TitledBorder Title1 = null;
            if(oo.size()==1){
                Title1 = new TitledBorder("Genesis Block");
            }else{
                Title1 = new TitledBorder("Block "+ Integer.toString(oo.size()-1));
            }
           
            Title1.setTitleFont(new Font("Consolas",Font.BOLD,25));
            Title1.setTitleColor(new Color(197,175,107));
            p2.setBorder(Title1);
            JLabel h01 = new JLabel("LastHash: ");
            JLabel h11=new JLabel("Hash : ");
            JLabel h21=new JLabel("TimeStamp : ");
            JLabel h31=new JLabel("Data : ");
            JLabel h41=new JLabel("nonce : ");
            JLabel h51=new JLabel("Difficulty : ");
            JLabel minR = new JLabel("Miner: ");

            JLabel h011 = null; JLabel h61 = null; JLabel h71 = null;
            JLabel h91 = null; JLabel h101 = null; JLabel minVal = null;

            if(oo.size()==1){
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
                    if(oo.size()==1 || k.data==null){
                        JOptionPane.showMessageDialog(new JFrame(),"No Transactions in GENESIS","Error Box",JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    new chainPageHelper(k.data);
                }
            });

            h01.setForeground(new Color(0x00FF00));
            h11.setForeground(new Color(0x00FF00));
            h21.setForeground(new Color(0x00FF00));
            h31.setForeground(new Color(0x00FF00));
            h41.setForeground(new Color(0x00FF00));
            h51.setForeground(new Color(0x00FF00));
            minR.setForeground(new Color(0x00FF00));
            h011.setForeground(Color.white);
            h61.setForeground(Color.white);
            h71.setForeground(Color.white);
            minVal.setForeground(Color.white);
            h81.setForeground(Color.black);
            h91.setForeground(Color.white);
            h101.setForeground(Color.white);
            h011.setFont(new Font("Consolas",Font.BOLD,17));
            h01.setFont(new Font("Consolas",Font.BOLD,17));
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
            p2.add(h01);
            p2.add(h011);
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
            
            Home.add(p2);

            oo.add(1);

        });

        Home.setLayout(new GridLayout(0, 1, 10, 10));

        JScrollPane myJScrollPane = new JScrollPane(Home,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        myJScrollPane.getVerticalScrollBar().setUnitIncrement(16);

        ImageIcon logo = null;
        if(System.getProperty("os.name").toLowerCase().indexOf("mac")>=0){
            logo = new ImageIcon("src/main/java/com/rudichain/frontend/images/logo.PNG");
        }else{
            logo = new ImageIcon("src\\main\\java\\com\\rudichain\\frontend\\images\\logo.PNG");
        }
        this.setIconImage(logo.getImage());
        
        this.setMinimumSize(new Dimension(1000,750));
        this.setTitle("Rudichain-Blockchain");
        this.setVisible(true);
        this.setLayout(new BorderLayout());
        this.add(topp,BorderLayout.NORTH);
        this.getContentPane().add(myJScrollPane);

        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                singleFrame = false;
                dispose();
            }
        });  
    }
    
}