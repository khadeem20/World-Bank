import java.math.BigDecimal;

import javax.swing.*;

import GUIS.BankingAppGui;
import GUIS.LoginGui;
import GUIS.RegisterGui;
import db_objs.User;

public class Driver {
    public static void main(String[] args) throws Exception {
        
        //use invokeLater ot make updates to the GUI more thread-safe
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                //new BankingAppGui(new User(1, "username", "password", new BigDecimal("20.00"))).setVisible(true);
                new LoginGui().setVisible(true);
                //new RegisterGui().setVisible(true);;
            }
        });
    }
}
