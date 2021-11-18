package com.rudichain.frontend;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import com.rudichain.cryptography.ECDSA;

public class TransactionsFrame extends JFrame{

    public static boolean onlyFrame = false;
    
    public TransactionsFrame(){
        onlyFrame = true;

        JPanel top = new JPanel();
        top.setBackground(new Color(0x123456));
        top.setLayout(new FlowLayout(FlowLayout.CENTER));

        JButton refresh = new JButton("\u27F3");
        refresh.setFocusable(false);
        refresh.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                onlyFrame = false;
                dispose();
                new TransactionsFrame();
            }
        });

        top.add(refresh);

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        contentPane.setBackground(new Color(0x123456));

        HomePage.chain.chain.forEach(BLOCK->{

            if(BLOCK.data != null){
                BLOCK.data.forEach(TRANSACTIONS->{
                    if(TRANSACTIONS.input!=null && ECDSA.compressPubKey(TRANSACTIONS.input.address).equals(HomePage.wallet.publicKey)){
    
                        JPanel parent = new JPanel();
                        parent.setLayout(new BoxLayout(parent, BoxLayout.Y_AXIS));
                        parent.setBorder(BorderFactory.createLineBorder(Color.red));
                        parent.add(Box.createRigidArea(new Dimension(10, 10)));
                        parent.setBackground(new Color(0x123456));

                        JPanel pane0 = new JPanel();
                        pane0.setLayout(new FlowLayout(FlowLayout.LEADING));
                        pane0.setBackground(Color.black);

                        JLabel lab1 = new JLabel("TimeStamp:");
                        lab1.setForeground(Color.green);
                        JLabel lab1_1 = new JLabel(Long.toString(TRANSACTIONS.input.timestamp));
                        lab1_1.setForeground(Color.white);

                        pane0.add(lab1);
                        pane0.add(lab1_1);
    
                        JPanel pane1 = new JPanel();
                        pane1.setLayout(new FlowLayout(FlowLayout.LEADING));
                        pane1.setBackground(Color.black);
    
                        JLabel lab0 = new JLabel("Sent---> Before sending you had:");
                        lab0.setForeground(Color.green);
                        
                        JLabel lab2 = new JLabel(Double.toString(TRANSACTIONS.input.amount));
                        lab2.setForeground(Color.blue);
    
                        pane1.add(lab0);
                        //pane1.add(lab1);
                        pane1.add(lab2);
    
                        JPanel pane2 = new JPanel();
                        pane2.setLayout(new BoxLayout(pane2, BoxLayout.Y_AXIS));
                        pane2.setBackground(Color.black);
    
                        JLabel lab01 = new JLabel("Receivers: ");
                        lab01.setForeground(Color.green);
    
                        pane2.add(lab01);
    
                        TRANSACTIONS.OutputMap.forEach((a,b)->{
                            JPanel paneX = new JPanel();
                            paneX.setLayout(new FlowLayout(FlowLayout.CENTER));
                            paneX.setBackground(Color.black);
    
                            JLabel txt1 = null;
                            if(a.equals(HomePage.wallet.publicKey)){
                                txt1 = new JLabel("Back to You --> ");
                            }else{
                                txt1 = new JLabel(a + " :");
                            }
                            txt1.setForeground(new Color(0x00FF00));
    
                            JLabel txt2 = new JLabel(Double.toString(b));
                            txt2.setForeground(Color.white);
    
                            paneX.add(txt1);
                            paneX.add(txt2);
                            
                            pane2.add(paneX);
    
                        });
    
                        parent.add(pane0);
                        parent.add(pane1);
                        parent.add(pane2);
    
                        contentPane.add(parent);
                        
                    }else{
                        TRANSACTIONS.OutputMap.forEach((a,b)->{
                              
                            if(a.equals(HomePage.wallet.publicKey)){
                                JPanel parent = new JPanel();
                                parent.setLayout(new BoxLayout(parent, BoxLayout.Y_AXIS));
                                parent.setBorder(BorderFactory.createLineBorder(Color.blue));
                                parent.add(Box.createRigidArea(new Dimension(10, 10)));
                                parent.setBackground(new Color(0x123456));
    
                                JPanel pane1 = new JPanel();
                                pane1.setLayout(new FlowLayout(FlowLayout.LEADING));
                                pane1.setBackground(Color.black);
                                JPanel pane2 = new JPanel();
                                pane2.setLayout(new FlowLayout(FlowLayout.LEADING));
                                pane2.setBackground(Color.black);
                                JPanel pane0 = new JPanel();
                                pane0.setLayout(new FlowLayout(FlowLayout.LEADING));
                                pane0.setBackground(Color.black);
    
                                JLabel txt1 = new JLabel("You have received: ");
                                txt1.setForeground(Color.red);
                                JLabel txt2 = new JLabel(Double.toString(b));
                                txt2.setForeground(Color.white);
    
                                pane1.add(txt1);
                                pane1.add(txt2);
    
                                if(TRANSACTIONS.input == null){
                                    JLabel txt3 = new JLabel("This is a reward for mining");
                                    txt3.setForeground(new Color(0x00FF00));
                                    pane2.add(txt3);

                                    JLabel lab1 = new JLabel("TimeStamp:");
                                    lab1.setForeground(Color.green);
                                    JLabel lab1_1 = new JLabel(Long.toString(BLOCK.timestamp));
                                    lab1_1.setForeground(Color.white);

                                    pane0.add(lab1);
                                    pane0.add(lab1_1);
                                }else{
                                    
                                    JLabel lab1 = new JLabel("TimeStamp:");
                                    lab1.setForeground(Color.green);
                                    JLabel lab1_1 = new JLabel(Long.toString(TRANSACTIONS.input.timestamp));
                                    lab1_1.setForeground(Color.white);

                                    pane0.add(lab1);
                                    pane0.add(lab1_1);

                                    JLabel txt3 = new JLabel("You got it from: ");
                                    txt3.setForeground(Color.white);
                                    JLabel txt4 = new JLabel(ECDSA.compressPubKey(TRANSACTIONS.input.address));
                                    txt4.setForeground(new Color(0x00FF00));
                                    pane2.add(txt3);
                                    pane2.add(txt4);
                                }
                                
                                parent.add(pane0);
                                parent.add(pane1);
                                parent.add(pane2);
    
                                contentPane.add(parent);
                            }
    
                        });
                    }
                });
            }
        });

        JScrollPane scrollPane = new JScrollPane(contentPane);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        ImageIcon logo = null;
        if(System.getProperty("os.name").toLowerCase().indexOf("mac")>=0){
            logo = new ImageIcon("src/main/java/com/rudichain/frontend/images/logo.PNG");
        }else{
            logo = new ImageIcon("src\\main\\java\\com\\rudichain\\frontend\\images\\logo.PNG");
        }
        this.setIconImage(logo.getImage());
        this.setTitle("Rudichain-My Transactions");
        this.setMinimumSize(new Dimension(1000,750));
        this.setBackground(new Color(0x123456));
        this.setLayout(new BorderLayout());
        this.add(top, BorderLayout.NORTH);
        this.getContentPane().add(scrollPane);
        this.setVisible(true);
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                onlyFrame = false;
                dispose();
            }
        }); 

    }
}