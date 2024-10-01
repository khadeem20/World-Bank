package GUIS;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Desktop.Action;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import db_objs.Connector;

public class RegisterGui extends BaseFrame{

    public RegisterGui(){
        super("App Registration Page");

    }
    
    
    @Override
    protected void addGuiComponents() {
        //create Registration Page Label 
        JLabel pageLabel = new JLabel("Registration");   
        
        pageLabel.setBounds(0,20,super.getWidth(),40);
        pageLabel.setFont(new Font("Dialog", Font.BOLD, 32));
        pageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // create username label and text field  
        JLabel username_Label = new JLabel("Username:");
        username_Label.setBounds(20,120,getWidth() - 30, 24 );
        username_Label.setFont(new Font("Dialog", Font.PLAIN, 20));
       // username_Label.setHorizontalAlignment(SwingConstants.CENTER);

        JTextField username_Field = new JTextField();
        username_Field.setBounds(20,160, getWidth() - 50, 40);
        username_Field.setFont(new Font("Dialog", Font.PLAIN, 28));
        //username_Field.setHorizontalAlignment(SwingConstants.CENTER);


        // create password label and text field
        JLabel password_Label = new JLabel("Password:");
        password_Label.setBounds(20,220,getWidth() - 30, 24 );
        password_Label.setFont(new Font("Dialog", Font.PLAIN, 20));
        //password_Label.setHorizontalAlignment(SwingConstants.CENTER);

        JPasswordField password_Field = new JPasswordField();
        password_Field.setBounds(20,260, getWidth() - 50, 40);
        password_Field.setFont(new Font("Dialog",Font.PLAIN, 28));
        //password_Field.setHorizontalAlignment(SwingConstants.CENTER);

        // Re-enter password label and field
        JLabel passwordCheck_Label = new JLabel("Re-enter Password:");
        passwordCheck_Label.setBounds(20,320,getWidth() - 30, 24 );
        passwordCheck_Label.setFont(new Font("Dialog", Font.PLAIN, 20));
        //passwordCheck_Label.setHorizontalAlignment(SwingConstants.CENTER);


        JPasswordField passwordCheck = new JPasswordField();
        passwordCheck.setBounds(20,360, getWidth() - 50, 40);
        passwordCheck.setFont(new Font("Dialog",Font.PLAIN, 28));
        //passwordCheck.setHorizontalAlignment(SwingConstants.CENTER);



        //create register button
        JButton registerButton = new JButton("Register");
        registerButton.setBounds(20,460, getWidth() - 50 , 40);
        registerButton.setFont(new Font("Dialog",Font.BOLD, 20));
        //registerButton.setHorizontalAlignment(SwingConstants.CENTER);
        registerButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
            //get info
            String username= username_Field.getText();
            String password= String.valueOf(password_Field.getPassword());
            String repassword= String.valueOf(passwordCheck.getPassword());
            
                //validate the user input
                if(validateUserInput(username,password,repassword)){
                    //try to register user
                    if(Connector.register(username, password)){

                        //dispose of the register frame 
                        RegisterGui.this.dispose();

                        //launch the login frane
                        LoginGui loginGui= new LoginGui();

                        loginGui.setVisible(true);
                        JOptionPane.showMessageDialog(RegisterGui.this,"User Registered Successfully"); 
                    }else{
                        JOptionPane.showMessageDialog(RegisterGui.this, "User already exits");
                    }
                    

                }else{
                    JOptionPane.showMessageDialog(RegisterGui.this,"Error: Username must be at least 6 characters\n" + 
                    "and/or Passwords must match");
                }
            }
        });

        //register here text field
        JLabel accountJLabel = new JLabel("<html><a href=\"#\"> Have an account? Sign-in Here <a/> </hmtl>");
        accountJLabel.setBounds(0,510, getWidth() - 10, 30);
        accountJLabel.setFont(new Font("Dialog", Font.PLAIN,20));
        accountJLabel.setHorizontalAlignment(SwingConstants.CENTER);
        accountJLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){

                //switch to login gui page
                RegisterGui.this.dispose();;
                new LoginGui().setVisible(true);

            }
        });
       
        //Add Components
        add(pageLabel);
        add(username_Label);
        add(username_Field);
        add(password_Label);
        add(password_Field);
        add(passwordCheck_Label);
        add(passwordCheck);
        add(registerButton);
        add(accountJLabel);
        
    }

    private boolean validateUserInput(String username, String password, String repassword){
        //all field must have a value
        if (username.length() == 0 || password.length() == 0 || repassword.length() == 0) return false;

        //username has to be atleast 6 characters long
        if(username.length() < 6) return false;

        //password and repassword must be the same
        if(!password.equals(repassword)) return false;

        //passes validation
        return true;
    }
    

    
    
}
