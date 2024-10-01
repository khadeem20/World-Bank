package db_objs;

import java.math.BigDecimal;
import java.math.RoundingMode;

/*
 * User entitty whihc is used to store user information
 */
public class User {
    private final int cid;
    private final String username,password;
    private BigDecimal currentBalance;

    public User(int id, String username, String password, BigDecimal currentBalance){
        cid=id;
        this.username=username;
        this.password= password;
        //assign a usuable default value if current balance is null
        //how this works
        // the ? evals whether the conditon before it is true or false and  it returns the value to the left of the ":" if it is true else the one to the right
        this.currentBalance= (currentBalance == null) ? BigDecimal.ZERO :currentBalance;
    

    }

    
    public int getCid() {
        return cid;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(BigDecimal newBalance){
        // store new value to the 2nd decimal place
        currentBalance = newBalance.setScale(2, RoundingMode.FLOOR);
    }
}
