package com.rudichain.frontend;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.awt.FlowLayout;
import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;

import com.rudichain.cryptography.ECDSA;
import com.rudichain.wallet.Transaction;

public class chainPageHelper extends JFrame{

    public chainPageHelper(ArrayList<Transaction> k){

        if(k==null){
            JOptionPane.showMessageDialog(new JFrame(),"No Transactions in GENESIS","Error Box",JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        JPanel main1 = new JPanel();
        main1.setLayout(new BoxLayout(main1, BoxLayout.Y_AXIS));
        main1.setBackground(Color.black);

        k.forEach(c->{
            JPanel box = new JPanel();
            box.setBackground(Color.black);
            box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));

            JPanel first = new JPanel();
            first.setLayout(new FlowLayout(FlowLayout.LEADING));
            first.setBackground(Color.black);

            JLabel txt1 = new JLabel("Transaction Id:  ");
            txt1.setForeground(new Color(0x00FF00));
        
            JLabel res1 = new JLabel(c.id);
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

            JLabel txt2_6 = null; JLabel txt4 = null; JLabel txt6 = null;
            if(c.input == null){
                txt2_6 = new JLabel("reward transaction");
                txt4 = new JLabel("reward transaction");
                txt6 = new JLabel("reward transaction");
            }else{
                txt2_6 = new JLabel(Long.toString(c.input.timestamp));
                txt4 = new JLabel(ECDSA.compressPubKey(c.input.address));
                txt6 = new JLabel(Double.toString(c.input.amount));
            }
       
            txt2_6.setForeground(Color.white);
           
            twoPointFive.add(txt2_3);
            twoPointFive.add(txt2_6);

            JPanel third = new JPanel();
            third.setLayout(new FlowLayout(FlowLayout.LEADING));
            third.setBackground(Color.black);

            JLabel txt3 = new JLabel("Input--> Sender: ");
            txt3.setForeground(new Color(0x00FF00));
            
            txt4.setForeground(Color.white);
            
            JLabel txt5 = new JLabel("Balance of Sender at Input-->");
            txt5.setForeground(new Color(0x00FF00));
            
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

            c.OutputMap.forEach((a,b)->{

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

            box.setBorder(BorderFactory.createLineBorder(new Color(0x123456),10));

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
        this.setTitle("Rudichain-Transactions");
        this.setIconImage(logo.getImage());
        this.setLayout(new BorderLayout());
        this.setMinimumSize(new Dimension(1000,750));
        this.getContentPane().add(scrollPane);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    }
    
}