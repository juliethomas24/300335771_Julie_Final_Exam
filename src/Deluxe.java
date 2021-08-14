public class Deluxe extends Savings implements Compund_Interest{


    public Deluxe(String custNo, String custName, String Deposit, String noOfYears, String savings) {
        super(custNo, custName, Deposit, noOfYears, savings);

    }

    public double generateTable() {
        double rate=0.15;
        double n =12.0;
        double years = Double.parseDouble(getNoOfYears());
        double p = Double.parseDouble(getDeposit());
        double amount = p * Math.pow(1 + (rate / n), n * years);

        double cinterest = amount - p;

        return cinterest;

    }

    @Override
    public void displayTable() {
        System.out.println(" Table updated");

    }
}
