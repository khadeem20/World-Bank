package db_objs;

import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
//import db_objs.User;
import java.util.ArrayList;
import db_objs.Encryptor;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;

// this call will intereact with out MYSQL database
public class Connector {
    //database configuration
    private static final String DB_URL= "jdbc:mysql://127.0.0.1:3306/worldbank";
    private static final String DB_USERNAME= "root";
    private static final String DB_PASSWORD ="Theretard101";
    //private static SecretKey secretKey = null;

    //if valid return an object wiht the user's info
    public static User validateLogin(String username, String password){
        try{
            //establish a conn wiht the database using the config
            Connection connection = DriverManager.getConnection(DB_URL,DB_USERNAME,DB_PASSWORD);

            //create sql query
            PreparedStatement statement = connection.prepareStatement("Select * FROM users WHERE username =? ");
            //replace the ?
            statement.setString(1, username);
    
            //execute query and store into a result set
            ResultSet resultSet = statement.executeQuery();

            //next()return s true or  false

            if(resultSet.next()){
                //get the stored password
                String storedPassword = resultSet.getString("password");

                if (Encryptor.validatePassword(password, storedPassword)) {
                    //get id
                    int userID= resultSet.getInt("cid");
                    //get current balance
                    BigDecimal currentBalance = resultSet.getBigDecimal("current_balance");

                    //returnn user object
                    return new User(userID,username, password, currentBalance);
                }
            }



        }catch(SQLException e){
            e.printStackTrace();

        }catch(Exception e){
            e.printStackTrace();
        }
        //not a valid user
        return null;
    }

    //add a new user to the database
    // returns a boolean
    public static boolean register(String username, String paSsword){

        
        try{
            //encryption logic
            String hashed_password=  Encryptor.encrypt(paSsword);


            //check username has already been taken
            if(!checkUser(username)){
                Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
                
                PreparedStatement preparedStatement = connection.prepareStatement(
                    " INSERT INTO users(username,password) VALUES(?,?)");
                
                preparedStatement.setString(1,username);
                preparedStatement.setString(2, hashed_password);   
                preparedStatement.executeUpdate();

                return true;
                /*preparedStatement = connection.prepareStatement(
                    "SELECT * FROM user WHERE username = ? AND WHERE password =?"
                );

                ResultSet resultSet = preparedStatement.executeQuery();

                //check if anything was returned
                if(resultSet.next()){
                    return true;
                }
                    */
            }

        }catch(SQLException e){
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }

        return false;

    }

    //check if the username already exists in the db

    private static boolean checkUser(String username){
        try{
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            
            PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM users WHERE username= ?"
            );

            preparedStatement.setString(1,username);
            ResultSet resultSet = preparedStatement.executeQuery();

            //checks if the statement returned a value
            if (resultSet.next()){
                return true;
            }


        }catch(SQLException e){
            e.printStackTrace();
        }

        return false;
    }

    // true - update to db was a success
    // false - update to the db was a fail
    public static boolean addTransactionToDatabase(Transaction transaction){
        try{
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            PreparedStatement insertTransaction = connection.prepareStatement(
                "INSERT transactions(user_id, type, tamount, tdate) " +
                        "VALUES(?, ?, ?, NOW())"
            );
            // NOW() will put in the current date

            insertTransaction.setInt(1, transaction.getUserId());
            insertTransaction.setString(2, transaction.getTransactionType());
            insertTransaction.setBigDecimal(3, transaction.getTransactionAmount());

            // update database
            insertTransaction.executeUpdate();

            return true;

        }catch(SQLException e){
            e.printStackTrace();
        }

        return false;
    }

    // true - update balance successful
    // false - update balance fail
    public static boolean updateCurrentBalance(User user){
        try{
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            PreparedStatement updateBalance = connection.prepareStatement(
                    "UPDATE users SET current_balance = ? WHERE cid = ?"
            );

            updateBalance.setBigDecimal(1, user.getCurrentBalance());
            updateBalance.setInt(2, user.getCid());

            updateBalance.executeUpdate();
            return true;

        }catch(SQLException e){
            e.printStackTrace();
        }

        return false;
    }

    // true - transfer was a success
    // false - transfer was a fail
    public static boolean transfer(User user, String transferredUsername, float transferAmount){
        try{
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            PreparedStatement queryUser = connection.prepareStatement(
                    "SELECT * FROM users WHERE username = ?"
            );

            queryUser.setString(1, transferredUsername);
            ResultSet resultSet = queryUser.executeQuery();

            while (resultSet.next()) {
                // perfrom transfer
                User transferredUser = new User(
                        resultSet.getInt("cid"),
                        transferredUsername,
                        resultSet.getString("password"),
                        resultSet.getBigDecimal("current_balance")
                );

                // create transaction
                Transaction transferTransaction = new Transaction(
                        user.getCid(),
                        "Transfer",
                        new BigDecimal(-transferAmount),
                        null
                );

                // this transaction will belong to the transferred user
                Transaction receivedTransaction = new Transaction(
                        transferredUser.getCid(),
                        "Transfer",
                        new BigDecimal(transferAmount),
                        null
                );

                // update transfer user
                transferredUser.setCurrentBalance(transferredUser.getCurrentBalance().add(BigDecimal.valueOf(transferAmount)));
                updateCurrentBalance(transferredUser);

                // update user current balance
                user.setCurrentBalance(user.getCurrentBalance().subtract(BigDecimal.valueOf(transferAmount)));
                updateCurrentBalance(user);

                // add these transactions to the database
                addTransactionToDatabase(transferTransaction);
                addTransactionToDatabase(receivedTransaction);

                return true;

            }
        }catch(SQLException e){
            e.printStackTrace();
        }

        return false;
    }

    // get all transactions (used for past transaction)
    public static ArrayList<Transaction> getPastTransaction(User user){
        ArrayList<Transaction> pastTransactions = new ArrayList<>();
        try{
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            PreparedStatement selectAllTransaction = connection.prepareStatement(
                "SELECT * FROM transactions WHERE user_id = ?"
            );
            selectAllTransaction.setInt(1, user.getCid());

            ResultSet resultSet = selectAllTransaction.executeQuery();

            // iterate throught the results (if any)
            while(resultSet.next()){
                // create transaction obj
                Transaction transaction = new Transaction(
                        user.getCid(),
                        resultSet.getString("type"),
                        resultSet.getBigDecimal("tamount"),
                        resultSet.getDate("tdate")
                );

                // store into array list
                pastTransactions.add(transaction);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }

        return pastTransactions;
    }



}
