package com.rudichain.frontend;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import com.rudichain.cryptography.ECDSA;

public class LoginFrame extends JFrame implements ActionListener{

    JButton button1, button2;
    JTextField pub, priv;
    JLabel warn;

    public LoginFrame(){

      JLabel display = new JLabel("Login/Signup", JLabel.CENTER);
      display.setFont(new Font("Consolas",Font.BOLD,25));
      display.setForeground(Color.white);
      display.setHorizontalAlignment(JLabel.CENTER);
    
      JLabel label1 = new JLabel(" Public Key: ");
      label1.setVerticalAlignment(JLabel.TOP);
      label1.setForeground(new Color(0x00FF00));
        
      pub = new JTextField();
      pub.setDocument(new JTextFieldLimit(66));
      pub.setPreferredSize(new Dimension(500,30));
       
		  pub.setFont(new Font("Consolas",Font.BOLD,15));
		  pub.setForeground(Color.white);
		  pub.setBackground(Color.black);
		  pub.setCaretColor(Color.white);

      JLabel label2 = new JLabel("Private Key:");
      label2.setForeground(new Color(0x00FF00));
        
      priv = new JTextField();
      priv.setDocument(new JTextFieldLimit(45));
      priv.setPreferredSize(new Dimension(500,30));

		  priv.setFont(new Font("Consolas",Font.BOLD,15));
		  priv.setForeground(Color.white);
		  priv.setBackground(Color.black);
		  priv.setCaretColor(Color.white);

      JPanel disPanel = new JPanel();
      disPanel.setBackground(new Color(0x123456));
      disPanel.setBounds(0,100,586,80);
      disPanel.add(display);

      JPanel jpanel = new JPanel();
      jpanel.setBackground(Color.black);
      jpanel.setBounds(0,200,586,80);
      jpanel.add(label1);
      jpanel.add(pub);
      jpanel.add(label2);
      jpanel.add(priv);

      warn = new JLabel("**Invalid Details-> Please try again or generate new keys!!**");
      warn.setForeground(Color.red);
      warn.setBounds(0,180,586,20);
      warn.setVisible(false);

      button1 = new JButton("Login");
      button1.setBounds(250,300,75,30);
      button1.setFocusable(false);
      button1.addActionListener(this);

      button2 = new JButton("Generate new Keys");
      button2.setBounds(216, 340, 146, 30);
      button2.setFocusable(false);
      button2.addActionListener(this);
       
      this.setTitle("Rudichain");
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      this.setLayout(null);
      this.setResizable(false);
      this.setSize(600,600);
      ImageIcon logo = new ImageIcon("src\\main\\java\\com\\rudichain\\frontend\\images\\logo.PNG");
      if(System.getProperty("os.name").toLowerCase().indexOf("mac")>=0){
        logo = new ImageIcon("src/main/java/com/rudichain/frontend/images/logo.PNG");
      }
      this.setIconImage(logo.getImage());
      this.getContentPane().setBackground(new Color(0x123456));
      this.setLocationRelativeTo(null);

      this.add(disPanel);
      this.add(jpanel);
      this.add(button1);
      this.add(button2);
      this.add(warn);
      this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource()==button1){
            String key1 = pub.getText();
            String key2 = priv.getText();

            if(key1.length()==0 || key2.length()==0){
                warn.setVisible(true);
                return;
            }

            if(!ECDSA.keyCheck(key1, key2)){
                warn.setVisible(true);
                pub.setText("");
                priv.setText("");
            }else{
                this.dispose();
                new HomePage(key1,key2);
            }  
        }
        if(e.getSource()==button2){
            this.dispose();
            new HomePage(null,null);
        }	
	}
    
}

class JTextFieldLimit extends PlainDocument {
    private int limit;
    JTextFieldLimit(int limit) {
      super();
      this.limit = limit;
    }
  
    JTextFieldLimit(int limit, boolean upper) {
      super();
      this.limit = limit;
    }
  
    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
      if (str == null)
        return;
  
      if ((getLength() + str.length()) <= limit) {
        super.insertString(offset, str, attr);
      }
    }
  }