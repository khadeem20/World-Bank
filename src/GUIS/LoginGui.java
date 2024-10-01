package GUIS;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

import db_objs.Connector;
import db_objs.User;


public class LoginGui extends BaseFrame {

    public LoginGui(){
        super("World Banking App");
    }

    @Override
    protected void addGuiComponents() {
        //create Banking App Label 
        JLabel appLabel = new JLabel("Banking Application");   
        
        appLabel.setBounds(0,20,super.getWidth(),40);
        appLabel.setFont(new Font("Dialog", Font.BOLD, 32));
        appLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // create username label and text field  
        JLabel username_Label = new JLabel("Username");
        username_Label.setBounds(20,120,getWidth() - 30, 24 );
        username_Label.setFont(new Font("Dialog", Font.PLAIN, 20));
        username_Label.setHorizontalAlignment(SwingConstants.CENTER);

        JTextField username_Field = new JTextField();
        username_Field.setBounds(20,160, getWidth() - 50, 40);
        username_Field.setFont(new Font("Dialog", Font.PLAIN, 28));
        username_Field.setHorizontalAlignment(SwingConstants.CENTER);


        // create password label and text field
        JLabel password_Label = new JLabel("Password");
        password_Label.setBounds(20,280,getWidth() - 30, 24 );
        password_Label.setFont(new Font("Dialog", Font.PLAIN, 20));
        password_Label.setHorizontalAlignment(SwingConstants.CENTER);

        JPasswordField password_Field = new JPasswordField();
        password_Field.setBounds(20,320, getWidth() - 50, 40);
        password_Field.setFont(new Font("Dialog",Font.PLAIN, 28));
        password_Field.setHorizontalAlignment(SwingConstants.CENTER);


        //create login button
        JButton login_button = new JButton("Login");
        login_button.setBounds(20,460, getWidth() - 50 , 40);
        login_button.setHorizontalAlignment(SwingConstants.CENTER);
        login_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                //get info
                String username= username_Field.getText();
                String password= String.valueOf(password_Field.getPassword());

                //validate login
                User user = Connector.validateLogin(username, password);

                //if user is null that means invalid other the acc is valid
                if(user != null){

                    // if details are valid 
                    // toss this frame
                    LoginGui.this.dispose();
                    //launch the bank app frame
                    BankingAppGui bankingAppGui= new BankingAppGui(user);
                    bankingAppGui.setVisible(true);

                    //show success dialog
                    JOptionPane.showMessageDialog(bankingAppGui, "Login Successful");
                }else{
                    //invalid login
                    JOptionPane.showMessageDialog(LoginGui.this, "Login Failed");
                }
            }
            
        });

        //register here text field
        JLabel registerLabel = new JLabel("<html><a href=\"#\"> Don't have an account? Register Here <a/> </hmtl>");
        registerLabel.setBounds(0,510, getWidth() - 10, 30);
        registerLabel.setFont(new Font("Dialog", Font.PLAIN,20));
        registerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        registerLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                //swith the the register gui
                LoginGui.this.dispose();
                new RegisterGui().setVisible(true);

            }
        });






        //Add Components
        add(appLabel);
        add(username_Label);
        add(username_Field);
        add(password_Label);
        add(password_Field);
        add(login_button);
        add(registerLabel);

        
    }
    
}
