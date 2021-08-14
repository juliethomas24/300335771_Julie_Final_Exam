public class Savings {
    private String custNo;
    private String custName;
    private String Deposit;
    private String noOfYears;
    private String savings;


    public Savings(String custNo, String custName,String Deposit,String noOfYears,String savings) {
        this.custNo = custNo;
        this.custName = custName;
        this.Deposit = Deposit;
        this.noOfYears = noOfYears;
        this.savings = savings;
    }

    public String getCustNo() {
        return custNo;
    }

    public void setCustNo(String custNo) {
        this.custNo = custNo;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getDeposit() {
        return Deposit;
    }

    public void setDeposit(String deposit) {
        Deposit = deposit;
    }

    public String getNoOfYears() {
        return noOfYears;
    }

    public void setNoOfYears(String noOfYears) {
        this.noOfYears = noOfYears;
    }

    public String getSavings() {
        return savings;
    }

    public void setSavings(String savings) {
        this.savings = savings;
    }
}
