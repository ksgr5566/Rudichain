package com.rudichain.frontend;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
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

import com.rudichain.backend.blockchain;
import com.rudichain.network.pubsub;
import com.rudichain.wallet.*;
import com.rudichain.cryptography.ECDSA;

public class HomePage extends JFrame{

    static blockchain chain;
    static Wallet wallet;
    static TransactionPool pool;
    static pubsub client;
    static TransactionMiner miner;
    public static boolean synced = false;
    public static boolean synced2 = false;
    
    HomePage(String publicKey, String privateKey){

        handleProcess(publicKey, privateKey);

        JButton viewChain = new JButton("Rudichain");
        viewChain.setFocusable(false);
        viewChain.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if(!BlockChainPage.singleFrame) new BlockChainPage();
            }
        });

        JButton viewPool = new JButton("Transaction Pool");
        viewPool.setFocusable(false);
        viewPool.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if(!mine.oneFrame) new mine();
            }
        });

        JButton history = new JButton("My Transactions");
        history.setFocusable(false);
        history.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if(!TransactionsFrame.onlyFrame) new TransactionsFrame();
            }
        });

        JPanel navBar = new JPanel();
        navBar.setBackground(new Color(0x123456));
        navBar.setAlignmentY(30);
        navBar.setLayout(new FlowLayout());
        navBar.add(viewChain);
        navBar.add(viewPool);
        navBar.add(history);

        JPanel down = new JPanel();
        down.setLayout(new BorderLayout());
        down.setBackground(new Color(0x123456));
        down.setPreferredSize(new Dimension(this.getWidth(),70));

        JPanel downHelp = new JPanel();
        downHelp.setLayout(new FlowLayout(FlowLayout.CENTER));
        downHelp.setBackground(new Color(0x123456));

        JLabel info = new JLabel("Contribute to our Source Code here: ");
        info.setFont(new Font("Consolas",Font.BOLD,15));
        info.setForeground(Color.white);
        info.setBackground(new Color(0x123456));

        Icon icon = null;
        if(System.getProperty("os.name").toLowerCase().indexOf("mac")>=0){
            icon = new ImageIcon("src/main/java/com/rudichain/frontend/images/image2.PNG");
        }else{
            icon = new ImageIcon("src\\main\\java\\com\\rudichain\\frontend\\images\\image2.PNG");
        }
        JButton git = new JButton(icon);
        git.setBackground(Color.black);
        git.setFocusable(false);
        git.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                try {
                    Desktop.getDesktop().browse(new URL("https://github.com/ksgr5566/Rudichain/").toURI());
                } catch (Exception a){
                    JOptionPane.showMessageDialog(new JFrame(),"Unable to open link through a browser\n " + 
                                                               "Please visit: https://github.com/ksgr5566/Rudichain/", "Error Box",JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        downHelp.add(info);
        downHelp.add(git);
        down.add(downHelp, BorderLayout.CENTER);
        
        JPanel grandParent = new JPanel();
        grandParent.setBackground(Color.black);
        grandParent.setLayout(new BorderLayout());
    
        JPanel parent = new JPanel();
        parent.setBackground(Color.black);
        parent.setLayout(new BorderLayout());

        BufferedImage image = null;
        try {                
            if(System.getProperty("os.name").toLowerCase().indexOf("mac")>=0){
                image = ImageIO.read(new File("src/main/java/com/rudichain/frontend/images/image1.PNG"));
            }else{
                image = ImageIO.read(new File("src\\main\\java\\com\\rudichain\\frontend\\images\\image1.PNG"));
            }
         } catch (IOException ex) {
            System.err.println("Cannot find logo2.png");
         }
        ImageIcon imageIcon = new ImageIcon(image);
        JLabel picLabel = new JLabel(imageIcon);

        picLabel.setHorizontalAlignment(JLabel.CENTER);

        parent.add(picLabel, BorderLayout.NORTH);

        JPanel subParent = new JPanel();
        subParent.setLayout(new BorderLayout());
        subParent.setPreferredSize(new Dimension(this.getWidth(),150));

        JPanel fillGap1 = new JPanel();
        fillGap1.setLayout(new BorderLayout());
        fillGap1.setPreferredSize(new Dimension(this.getWidth(),75));
        fillGap1.setBackground(Color.black);

        JPanel child1 = new JPanel();
        child1.setLayout(new FlowLayout());
        child1.setBackground(Color.black);

        JPanel child2 = new JPanel();
        child2.setLayout(new FlowLayout());
        child2.setBackground(Color.black);

        JPanel child3 = new JPanel();
        child3.setLayout(new FlowLayout());

        JPanel child4 = new JPanel();
        child4.setLayout(new FlowLayout());

        JLabel label1 = new JLabel("Public Key: ");
        label1.setForeground(new Color(0x00FF00));
        JTextField txt1 = new JTextField();
        txt1.setText(wallet.publicKey);
        txt1.setForeground(Color.white);
	    txt1.setBackground(Color.black);
        txt1.setFont(new Font("Consolas",Font.BOLD,15));
        txt1.setPreferredSize(new Dimension(550,30));
        txt1.setVisible(true);
        txt1.setEditable(false);

        JButton clp1 = new JButton("...");
        clp1.setFocusable(false);
        clp1.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                String myString = wallet.publicKey;
                StringSelection stringSelection = new StringSelection(myString);
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(stringSelection, null);
            }
        });
        
        child1.add(label1);
        child1.add(txt1);
        child1.add(clp1);

        subParent.add(child1, BorderLayout.NORTH);

        JLabel label2 = new JLabel("Private Key:");
        label2.setForeground(new Color(0x00FF00));
        JTextField txt2 = new JTextField();
        txt2.setFont(new Font("Consolas",Font.BOLD,15));
        txt2.setForeground(Color.white);
		txt2.setBackground(Color.black);
        txt2.setText("********************************************");
        txt2.setPreferredSize(new Dimension(400,30));
        txt2.setEditable(false);

        JButton chldbutton = new JButton("show");
        chldbutton.setFocusable(false);
        chldbutton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if(txt2.getText().equals(wallet.keys.privateKey())){  
                    txt2.setText("********************************************");
                    chldbutton.setText("show");
                }else{
                    txt2.setText(wallet.keys.privateKey());
                    chldbutton.setText("hide");
                } 
            }
        });

        JButton clp2 = new JButton("...");
        clp2.setFocusable(false);
        clp2.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                String myString = wallet.keys.privateKey();
                StringSelection stringSelection = new StringSelection(myString);
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(stringSelection, null);
            }
        });

        child2.add(label2);
        child2.add(txt2);
        child2.add(chldbutton);
        child2.add(clp2);

        subParent.add(child2, BorderLayout.CENTER);

        JLabel label3 = new JLabel(String.valueOf(wallet.balance));
        label3.setForeground(Color.white);
		label3.setBackground(Color.black);
        
        String refreshUTF = "\u27F3";
        JButton refresh1 = new JButton(refreshUTF);
        refresh1.setFocusable(false);
        refresh1.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                label3.setText(Double.toString(Wallet.calcBalance(chain, wallet.publicKey)));
            }
        });

        JLabel balance01 = new JLabel("Balance: ");
        balance01.setForeground(new Color(0x00FF00));
        
        child2.add(balance01);
        child2.add(label3);
        child2.add(refresh1);

        parent.add(subParent, BorderLayout.CENTER);
        parent.add(fillGap1, BorderLayout.SOUTH);

        JPanel broParent1 = new JPanel();
        broParent1.setLayout(new FlowLayout());
        broParent1.setBackground(Color.black);

        JPanel broChild1 = new JPanel();
        broChild1.setLayout(new FlowLayout());
        broChild1.setBackground(Color.black);

        JPanel broChild2 = new JPanel();
        broChild2.setLayout(new FlowLayout());
        broChild2.setBackground(Color.black);

        JLabel label4 = new JLabel("Recipient Public Key: ");
        label4.setForeground(new Color(0x00FF00));
        JTextField txt3 = new JTextField();
        txt3.setFont(new Font("Consolas",Font.BOLD,15));
		txt3.setForeground(Color.white);
		txt3.setBackground(Color.black);
		txt3.setCaretColor(Color.white);
        txt3.setDocument(new JTextFieldLimit(66));
        txt3.setPreferredSize(new Dimension(500,30));

        broChild1.add(label4);
        broChild1.add(txt3);

        JLabel label5 = new JLabel("Enter Amount: ");
        label5.setForeground(new Color(0x00FF00));
        JTextField txt4 = new JTextField();
        txt4.setFont(new Font("Consolas",Font.BOLD,15));
		txt4.setForeground(Color.white);
		txt4.setBackground(Color.black);
		txt4.setCaretColor(Color.white);
        txt4.setPreferredSize(new Dimension(100,30));
        JButton send = new JButton("send");
        send.setFocusable(false);
        send.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                String address = txt3.getText();
                String amount = txt4.getText();
                double d = 0;
                
                try{
                    d = Double.parseDouble(amount);
                    if(d>wallet.calcBalance(chain, wallet.publicKey) || d<=0){
                        throw new Exception();
                    }
                }catch(Exception a){
                    JOptionPane.showMessageDialog(new JFrame(),"Enter Valid amount!!!","Error Box",JOptionPane.ERROR_MESSAGE);
                    txt4.setText("");
                    return;
                }
                if(txt3.getText().length()==0){
                    JOptionPane.showMessageDialog(new JFrame(),"Try Entering a recipient address!","Error Box",JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int result = JOptionPane.showConfirmDialog(new JFrame(),"Please cross check the recipient public key again.\n" +
                                                                        "This is an irreversible transaction!\n" +
                                                                        "Do you want to Proceed?",
                                                                        "Confirmation Box", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                if(result == JOptionPane.YES_OPTION){
                    Transaction transaction = pool.exists(wallet.publicKey);
                    try{
                        if(transaction!=null){
                            transaction.update(wallet,address,d);
                        }else{
                            transaction = wallet.createTransaction(address, d, chain);
                        }
                    }catch(InvalidTransaction a1){
                        JOptionPane.showMessageDialog(new JFrame(),"Invalid Transaction!! Please try again!","Error Box",JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    pool.setTransaction(transaction);
                    client.broadcastTransactions(transaction);
                    //System.out.println(new Gson().toJson(transaction));
                    txt3.setText("");
                    txt4.setText("");
                }else{
                    return;
                }
            }
        });

        broChild2.add(label5);
        broChild2.add(txt4);
        broChild2.add(send);

        broParent1.add(broChild2, BorderLayout.NORTH);
     
        JLabel label6 = new JLabel("Transaction Panel");
        label6.setHorizontalAlignment(JLabel.CENTER);
        label6.setFont(new Font("Consolas",Font.BOLD,25));
        label6.setForeground(new Color(197,175,107));

        fillGap1.add(label6, BorderLayout.CENTER);
        fillGap1.add(broChild1, BorderLayout.SOUTH);

        grandParent.add(parent, BorderLayout.NORTH);
        grandParent.add(broParent1, BorderLayout.CENTER);
        grandParent.add(down, BorderLayout.SOUTH);

        JScrollPane scrollPane = new JScrollPane(grandParent);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        this.setTitle("Rudichain-Home");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        
        this.setSize(1000,1000);

        ImageIcon logo = null;
        if(System.getProperty("os.name").toLowerCase().indexOf("mac")>=0){
            logo = new ImageIcon("src/main/java/com/rudichain/frontend/images/logo.PNG");
        }else{
            logo = new ImageIcon("src\\main\\java\\com\\rudichain\\frontend\\images\\logo.PNG");
        }
        this.setIconImage(logo.getImage());
        this.getContentPane().setBackground(Color.black);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        this.setMinimumSize(new Dimension(1000,750));
        
        this.add(navBar, BorderLayout.NORTH);
        //this.add(grandParent, BorderLayout.CENTER);
        this.getContentPane().add(scrollPane);
        this.setVisible(true);

    }

    private static void handleProcess(String publicKey, String privateKey){
        if(publicKey==null & privateKey==null){
            wallet = new Wallet();
        }else{
            ECDSA keypair = new ECDSA(publicKey, privateKey);
            wallet = new Wallet(keypair);
        }
        chain = new blockchain();
        pool = new TransactionPool();
        client = new pubsub(chain, pool, wallet);
        miner = new TransactionMiner(chain, client, pool, wallet);
        try{
            if(!synced2){
                client.introduceSelf2();
                TimeUnit.SECONDS.sleep(2);
                synced2 = true;
            }
            if(!synced){
                client.introduceSelf();
                TimeUnit.SECONDS.sleep(2);
                synced = true;
            }
        }catch(InterruptedException e){
            System.err.println("Homepage Interrupted Exception!!!");
        }
        if(publicKey != null){
            wallet.setBalance(Wallet.calcBalance(chain, publicKey));
        }
    }

}