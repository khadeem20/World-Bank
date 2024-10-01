package GUIS;

import javax.swing.*;
import db_objs.User;

/*Create the template for the other frames to follow
     * Each frame will  be the same size and need to invoke their own addguicomponents()
     * which will be unique in each.
     * ReDo using JPanels instead of frames.
     */
   
public abstract class BaseFrame extends JFrame{

    protected User user;

    public BaseFrame(String title, User user){
        this.user= user;
        
        initalize(title);
    }

    public BaseFrame(String title){
        initalize(title);
    }

    private void initalize(String title){
        // initialize the jframe properiteis and add a title to the frame
        setTitle(title);

        //set size 
        setSize(420, 600);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // set the layout to null to have absolute layout which allows
        // us to manually specify the size and  position of each gui component
        setLayout(null);

        //decide whether gui can be resized
        setResizable(false);
        // launch the gui in the center of the screen
        setLocationRelativeTo(null);

        // call on the method addGuiComponents()
        addGuiComponents();

    }


    protected abstract void addGuiComponents();

}
