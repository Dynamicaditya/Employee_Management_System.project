package model;

// Salary.java
// Represents one row of the "salary" table.
public class Salary {

    private int salaryId;
    private int employeeId;
    private String employeeName; // convenience field for display (from JOIN)
    private double basicSalary;
    private double allowance;
    private double deduction;
    private double netSalary;
    private String paymentDate;

    public Salary() {
    }

    // Constructor for adding a NEW salary record (before netSalary is calculated)
    public Salary(int employeeId, double basicSalary, double allowance, double deduction, String paymentDate) {
        this.employeeId = employeeId;
        this.basicSalary = basicSalary;
        this.allowance = allowance;
        this.deduction = deduction;
        this.paymentDate = paymentDate;
        this.netSalary = calculateNetSalary(); // calculated automatically in Java
    }

    // Constructor for READING a salary record from the database
    public Salary(int salaryId, int employeeId, String employeeName, double basicSalary,
                  double allowance, double deduction, double netSalary, String paymentDate) {
        this.salaryId = salaryId;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.basicSalary = basicSalary;
        this.allowance = allowance;
        this.deduction = deduction;
        this.netSalary = netSalary;
        this.paymentDate = paymentDate;
    }

    // Business logic: Net Salary = Basic Salary + Allowance - Deduction
    // Kept simple and inside Core Java as required.
    public double calculateNetSalary() {
        return basicSalary + allowance - deduction;
    }

    public int getSalaryId() { return salaryId; }
    public void setSalaryId(int salaryId) { this.salaryId = salaryId; }

    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }

    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }

    public double getBasicSalary() { return basicSalary; }
    public void setBasicSalary(double basicSalary) { this.basicSalary = basicSalary; }

    public double getAllowance() { return allowance; }
    public void setAllowance(double allowance) { this.allowance = allowance; }

    public double getDeduction() { return deduction; }
    public void setDeduction(double deduction) { this.deduction = deduction; }

    public double getNetSalary() { return netSalary; }
    public void setNetSalary(double netSalary) { this.netSalary = netSalary; }

    public String getPaymentDate() { return paymentDate; }
    public void setPaymentDate(String paymentDate) { this.paymentDate = paymentDate; }
}
