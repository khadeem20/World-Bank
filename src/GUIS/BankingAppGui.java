package GUIS;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import db_objs.User;

public class BankingAppGui extends BaseFrame implements ActionListener{
    private JTextField currentBalanceField;
    public JTextField getCurrentBalanceField(){
        return currentBalanceField;
    }
   
    public BankingAppGui(User user){
        super("Banking App", user);
    }

    @Override
    protected void addGuiComponents() {
        //create Hello User Label 
        String welcomeText= (String) "<html><body style='text-align:center'><b>Hello " + user.getUsername() + 
                            "</b><br>What would you like to do today?</hmtl>" ;

        JLabel welcomeLabel= new JLabel(welcomeText);
        welcomeLabel.setBounds(0,20,getWidth()-10,40);
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Dialog", Font.PLAIN,16 ));

        //create balance label
        JLabel balanceLabel = new JLabel("Current Balance ");   
        
        balanceLabel.setBounds(0,80,super.getWidth() -10 ,30);
        balanceLabel.setFont(new Font("Dialog", Font.BOLD, 22));
        balanceLabel.setHorizontalAlignment(SwingConstants.CENTER);

        
        //create current balance field
        currentBalanceField= new JTextField("$"+ user.getCurrentBalance());
        currentBalanceField.setBounds(15,120,super.getWidth() - 50, 40);
        currentBalanceField.setHorizontalAlignment(SwingConstants.RIGHT);
        currentBalanceField.setFont(new Font("Dialog",Font.BOLD,28));
        currentBalanceField.setEditable(false);



         //create deposit button
        JButton despositButton = new JButton("Deposit");
        despositButton.setBounds(15,180, getWidth() - 50 , 50);
        despositButton.setHorizontalAlignment(SwingConstants.CENTER);
        despositButton.setFont(new Font("Dialog", Font.BOLD, 22));
        despositButton.addActionListener(this);


          //create withdraw button
        JButton withdrawButton = new JButton("Withdraw");
        withdrawButton.setBounds(15,250, getWidth() - 50 , 50);
        withdrawButton.setHorizontalAlignment(SwingConstants.CENTER);
        withdrawButton.setFont(new Font("Dialog", Font.BOLD, 22));
        withdrawButton.addActionListener(this);


        //create past transaction button
        JButton pastTransButton = new JButton("Past Transaction");
        pastTransButton.setBounds(15,320, getWidth() - 50 , 50);
        pastTransButton.setHorizontalAlignment(SwingConstants.CENTER);
        pastTransButton.setFont(new Font("Dialog", Font.BOLD, 22));
        pastTransButton.addActionListener(this);


         //create tranfer button
        JButton transferButton = new JButton("Transfer");
        transferButton.setBounds(15,390, getWidth() - 50 , 50);
        transferButton.setHorizontalAlignment(SwingConstants.CENTER);
        transferButton.setFont(new Font("Dialog", Font.BOLD, 22));
        transferButton.addActionListener(this);

          //create logout button
        JButton logoutButton = new JButton("Logout");
        logoutButton.setBounds(15,500, getWidth() - 50 , 50);
        logoutButton.setHorizontalAlignment(SwingConstants.CENTER);
        logoutButton.setFont(new Font("Dialog", Font.BOLD, 22));
        logoutButton.addActionListener(this);

        //add components
        add(welcomeLabel);
        add(balanceLabel);
        add(currentBalanceField);
        add(despositButton);
        add(withdrawButton);
        add(pastTransButton);
        add(transferButton);
        add(logoutButton);
    }

    @Override 
    public void actionPerformed(ActionEvent e) {
        String buttonPressed = e.getActionCommand();

        // user pressed logout
        if(buttonPressed.equalsIgnoreCase("Logout")){
            // return user to the login gui
            new LoginGui().setVisible(true);

            // dispose of this gui
            this.dispose();

            // don't bother running the rest of the code
            return;
        }

        // other functions
        BankingAppDialog bankingAppDialog = new BankingAppDialog(this, user);

        // set the title of the dialog header to the action
        bankingAppDialog.setTitle(buttonPressed);

        // if the button pressed is deposit, withdraw, or transfer
        if(buttonPressed.equalsIgnoreCase("Deposit") || buttonPressed.equalsIgnoreCase("Withdraw")
                    || buttonPressed.equalsIgnoreCase("Transfer")){
            // add in the current balance and amount gui components to the dialog
            bankingAppDialog.addCurrentBalanceAndAmount();

            // add action button
            bankingAppDialog.addActionButton(buttonPressed);

            // for the transfer action it will require more components
            if(buttonPressed.equalsIgnoreCase("Transfer")){
                bankingAppDialog.addUserField();
            }

        }else if(buttonPressed.equalsIgnoreCase("Past Transaction")){
            bankingAppDialog.addPastTransactionComponents();
        }

        // make the app dialog visible
        bankingAppDialog.setVisible(true);
    }


}
