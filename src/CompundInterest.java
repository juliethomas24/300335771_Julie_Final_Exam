import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
/*
 * Created by JFormDesigner on Fri Aug 13 19:09:04 EDT 2021
 */



/**
 * @author unknown
 *
 * github link:
 */
public class CompundInterest extends JFrame {

    // Instantiating and object named con
    public static ArrayList<Savings> list1 = new ArrayList<Savings>();



    Connections1 con = new Connections1();

    //Declaring a Connection object. Import class in connection and add exception in connect (mouse over)
    Connection conObj = con.connect();

    //Now database is connected

    //6. Creating a main method.
    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        //To show the form we need to instantiate a category
        //Add exception in Database (mouse over)
        CompundInterest form1 = new CompundInterest();

        //Calling the method that updates table
        form1.updateTable();

        //Make the form visible and run to see if it works
        form1.setVisible(true);
        form1.setDefaultCloseOperation(EXIT_ON_CLOSE);




    }

    public void updateTable() throws SQLException {
        //Defining a String variable for a query and an object. Add exception (mouse over)
        String query1 = "Select * from savings";
        PreparedStatement query = conObj.prepareStatement(query1);

        //Getting the result fo the data
        ResultSet rs = query.executeQuery();

        //Set a table (df) with the name of the table (table1) in the form
        DefaultTableModel df = (DefaultTableModel) table1.getModel();

        DefaultTableModel df2 = (DefaultTableModel) table2.getModel();

        //Traverse the table, and we will need a bi dimensional array for that
        //First the row count to go to the end
        rs.last();
        int z = rs.getRow();

        //put it back to its original place
        rs.beforeFirst();

        //Creating the array called array
        String [][] array = new String[0][];

        //See if there is still one row or not. Rows > 0
        if(z > 0) {
            //The number of rows existing and 5 columns fixed (as we set it in the table from mySQL)
            array = new String[z][5];
        }

        //Traverse putting the data from the database to the array
        int j = 0;
        //Creating a two-dimensional array inputting the data from each row
        // We are using getString cuz it is a String array as we defined before, if it was a double should be getDouble
        while (rs.next()) {
            array[j][0] = rs.getString("custno");
            array[j][1] = rs.getString("custname");
            array[j][2] = rs.getString("cdep");
            array[j][3] = rs.getString("nyears");
            array[j][4] = rs.getString("savtype");
            ++j;

        }

        //Creating column headers for our table
        String Columns[] = {"Number", "Name", "Deposit", "Years", "Type of Saving"};

        //Creating the array called array2
        String [][] array2 = new String[0][];

        //See if there is still one row or not. Rows > 0
        if(z > 0) {
            //The number of rows existing and 5 columns fixed (as we set it in the table from mySQL)
            array2 = new String[z][4];
        }

        //Traverse putting the data from the database to the array
        int k = 0;
        //Creating a two-dimensional array inputting the data from each row

        while (rs.next()) {
            array[k][0] = rs.getString("Years");
            array[k][1] = rs.getString("Starting");
            array[k][2] = rs.getString("Interest");
            array[k][3] = rs.getString("Ending Value");
            ++k;

        }


        String Columns2[] = {"Years", "Starting", "Interest", "Ending Value"};

        //Creating the table
        DefaultTableModel model = new DefaultTableModel(array, Columns);
        table1.setModel(model);

        DefaultTableModel model2 = new DefaultTableModel(array, Columns2);
        table2.setModel(model2);


    }


    public CompundInterest() throws SQLException, ClassNotFoundException {
        initComponents();
    }

    private void btnAddActionPerformed(ActionEvent e) throws SQLException {
        String custno = txtno.getText();
        String custname = txtName.getText();
        String dep = txtDeposit.getText();
        String years =txtYears.getText();
        String savtype = cboSaving.getSelectedItem().toString();

        if(savtype.equals("Savings-Deluxe")){

            Deluxe del = new Deluxe(custno, custname, dep, years, savtype);
             double CI = del.generateTable();


        }
        else if(savtype.equals("Savings-Regular")){

            Regular reg = new Regular(custno, custname, dep, years, savtype);
            double CI = reg.generateTable();

        }


        list1.add(new Savings(custno, custname, dep, years, savtype));
        //WriteFile();


        String[][] array = new String[list1.size()][5];


        for(int i =0; i<list1.size(); ++i){
            array[i][0] = list1.get(i).getCustNo();
            array[i][1] = list1.get(i).getCustName();
            array[i][2] = list1.get(i).getDeposit();
            array[i][3] = list1.get(i).getNoOfYears();
            array[i][4] = list1.get(i).getSavings();

        }


        String Columns[] = {"Number", "Name", "Deposit", "Years", "Type of Saving"};

        DefaultTableModel model = new DefaultTableModel(array, Columns);

        table1.setModel(model);

        //Creating a query. The first one is verifying the user is not inputting a catcode already present
        String query1 = "Select * from savings where custno =?";
        //will need to add exception here
        PreparedStatement query = conObj.prepareStatement(query1);

        query.setString(1, custno);

        ResultSet rs = query.executeQuery();

        if(rs.isBeforeFirst()) {
            JOptionPane.showMessageDialog(null, "This record exists already");

            //WE NEED TO SET THE BOXES TO EMPTY SPACE HERE

            return;
        }

        //Data is validated as new. We create insert query
        String query2 = "Insert into savings values (?,?,?,?,?)";
        query = conObj.prepareStatement(query2);
        
        double cdep = Double.parseDouble(dep);
        int nyears = Integer.parseInt(years);

        //Filling parameters
        query.setString(1, custno);
        query.setString(2, custname);
        query.setString(3, dep);
        query.setInt(4, nyears);
        query.setString(5, savtype);





        //Updating query
        query.executeUpdate();
        JOptionPane.showMessageDialog(null, "Record added");
        updateTable();


    }

    private void btnEditActionPerformed(ActionEvent e)throws SQLException {
        //Need to get old value from the table
        DefaultTableModel df = (DefaultTableModel) table1.getModel();


        int index = table1.getSelectedRow();

        String custno = txtno.getText();
        String custname = txtName.getText();
        double dep = Double.parseDouble(txtDeposit.getText());
        int years = Integer.parseInt(txtYears.getText());
        String savtype = cboSaving.getSelectedItem().toString();
        // Define the old value and store there the data we want to edit
        
        String oldvalue = df.getValueAt(index, 0).toString();

        //Now preparing a query copied from Add Btn with some adjustments
        String query = "Update category set custno=?,custname=?,dep=?,years=?,savtype =? where custno=?";
        PreparedStatement query2 = conObj.prepareStatement(query);

        query2.setString(1, custno);
        query2.setString(2, custname);
        query2.setString(3, oldvalue);

        query2.executeUpdate();

        updateTable();  
        
        
        
        
        
             




    }

    private void btnDeleteActionPerformed(ActionEvent e) throws SQLException{

        String custno = txtno.getText();
        String custname = txtName.getText();
        double dep = Double.parseDouble(txtDeposit.getText());
        int years = Integer.parseInt(txtYears.getText());
        String savtype = cboSaving.getSelectedItem().toString();

        String query = "Delete from category where custno =?";

        PreparedStatement query2 = conObj.prepareStatement(query);

        query2.setString(1, custno);


        query2.executeUpdate();

        updateTable();


        
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - unknown
        lblNo = new JLabel();
        txtno = new JTextField();
        lblName = new JLabel();
        txtName = new JTextField();
        lblInitialDeposit = new JLabel();
        txtDeposit = new JTextField();
        lblnoYears = new JLabel();
        txtYears = new JTextField();
        lbltypeSaving = new JLabel();
        cboSaving = new JComboBox<>();
        panel1 = new JPanel();
        scrollPane1 = new JScrollPane();
        table1 = new JTable();
        panel2 = new JPanel();
        scrollPane2 = new JScrollPane();
        table2 = new JTable();
        btnAdd = new JButton();
        btnEdit = new JButton();
        btnDelete = new JButton();

        //======== this ========
        var contentPane = getContentPane();
        contentPane.setLayout(new MigLayout(
            "hidemode 3",
            // columns
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]",
            // rows
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]"));

        //---- lblNo ----
        lblNo.setText("Enter the Customer Number");
        contentPane.add(lblNo, "cell 3 0 2 1");
        contentPane.add(txtno, "cell 13 0");

        //---- lblName ----
        lblName.setText("Enter the Customer Name");
        contentPane.add(lblName, "cell 3 1");
        contentPane.add(txtName, "cell 13 1");

        //---- lblInitialDeposit ----
        lblInitialDeposit.setText("Enter the Initial Deposit");
        contentPane.add(lblInitialDeposit, "cell 3 2");
        contentPane.add(txtDeposit, "cell 13 2");

        //---- lblnoYears ----
        lblnoYears.setText("Enter the number of years");
        contentPane.add(lblnoYears, "cell 3 3");
        contentPane.add(txtYears, "cell 13 3");

        //---- lbltypeSaving ----
        lbltypeSaving.setText("Choose the type of savings");
        contentPane.add(lbltypeSaving, "cell 3 4");

        //---- cboSaving ----
        cboSaving.setModel(new DefaultComboBoxModel<>(new String[] {
            "Savings-Deluxe",
            "Savings-Regular"
        }));
        contentPane.add(cboSaving, "cell 13 4");

        //======== panel1 ========
        {
            panel1.setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.TitledBorder(new javax.
            swing.border.EmptyBorder(0,0,0,0), "JF\u006frmD\u0065sig\u006eer \u0045val\u0075ati\u006fn",javax.swing.border
            .TitledBorder.CENTER,javax.swing.border.TitledBorder.BOTTOM,new java.awt.Font("Dia\u006cog"
            ,java.awt.Font.BOLD,12),java.awt.Color.red),panel1. getBorder
            ()));panel1. addPropertyChangeListener(new java.beans.PropertyChangeListener(){@Override public void propertyChange(java
            .beans.PropertyChangeEvent e){if("\u0062ord\u0065r".equals(e.getPropertyName()))throw new RuntimeException
            ();}});
            panel1.setLayout(new MigLayout(
                "hidemode 3",
                // columns
                "[fill]" +
                "[fill]" +
                "[fill]",
                // rows
                "[]" +
                "[]" +
                "[]" +
                "[]"));

            //======== scrollPane1 ========
            {

                //---- table1 ----
                table1.setSurrendersFocusOnKeystroke(true);
                scrollPane1.setViewportView(table1);
            }
            panel1.add(scrollPane1, "cell 5 5");
        }
        contentPane.add(panel1, "cell 1 6 5 2");

        //======== panel2 ========
        {
            panel2.setLayout(new MigLayout(
                "hidemode 3",
                // columns
                "[fill]" +
                "[fill]" +
                "[fill]",
                // rows
                "[]" +
                "[]" +
                "[]"));

            //======== scrollPane2 ========
            {
                scrollPane2.setViewportView(table2);
            }
            panel2.add(scrollPane2, "cell 1 1");
        }
        contentPane.add(panel2, "cell 7 7 7 1");

        //---- btnAdd ----
        btnAdd.setText("Add");
        btnAdd.addActionListener(e -> {
            try {
                btnAddActionPerformed(e);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        contentPane.add(btnAdd, "cell 3 13 2 1");

        //---- btnEdit ----
        btnEdit.setText("Edit");
        btnEdit.addActionListener(e -> {
            try {
                btnEditActionPerformed(e);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        contentPane.add(btnEdit, "cell 5 13");

        //---- btnDelete ----
        btnDelete.setText("Delete");
        btnDelete.addActionListener(e -> {
            try {
                btnDeleteActionPerformed(e);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        contentPane.add(btnDelete, "cell 7 13 4 1");
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - unknown
    private JLabel lblNo;
    private JTextField txtno;
    private JLabel lblName;
    private JTextField txtName;
    private JLabel lblInitialDeposit;
    private JTextField txtDeposit;
    private JLabel lblnoYears;
    private JTextField txtYears;
    private JLabel lbltypeSaving;
    private JComboBox<String> cboSaving;
    private JPanel panel1;
    private JScrollPane scrollPane1;
    private JTable table1;
    private JPanel panel2;
    private JScrollPane scrollPane2;
    private JTable table2;
    private JButton btnAdd;
    private JButton btnEdit;
    private JButton btnDelete;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
