/*
 * Created on Jun 24, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package dmtools.ui;

import java.awt.Insets;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;

/**
 * @author Dominic
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class MessageBox extends JFrame {
    private JLabel lblMessage = new JLabel();
    private JButton btnOk = new JButton();;
    private String strMsg = new String();
    
    /** Creates new form MessageBox */
    public MessageBox(String title,String msg) {
        super(title);
        strMsg = msg;
        
        createLayout();
        lblMessage.setText(msg);
    }

    private void createLayout() {
        setLayout(new GridBagLayout());
        GridBagConstraints gridBagConstraints1;
        
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                exitForm(evt);
            }
        });
        
        lblMessage.setText("Message");
        lblMessage.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 0;
        gridBagConstraints1.gridwidth = 3;
        gridBagConstraints1.gridheight = 2;
        add(lblMessage, gridBagConstraints1);
        
        btnOk.setLabel("OK");
        btnOk.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                btnOkKeyPressed(evt);
            }
        });
        
        btnOk.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                btnOkMouseClicked(evt);
            }
        });
        
        gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 3;
        gridBagConstraints1.gridwidth = 3;
        add(btnOk, gridBagConstraints1);
        
        pack();
    }

    private void btnOkKeyPressed(KeyEvent evt) {
        if(evt.getKeyCode() == evt.VK_ENTER)
        {
            hide();
            dispose();
        }        
    }

    private void btnOkMouseClicked(MouseEvent evt) {
        hide();
        dispose();
    }

    /** Exit the Application */
    private void exitForm(WindowEvent evt) {
        hide();
        dispose();
    }
    
    public Insets getInsets() {
        return new Insets(20,0,25,0);
    }
    
    public void setMessage(String msg) {
        strMsg = msg;
    }        
}
