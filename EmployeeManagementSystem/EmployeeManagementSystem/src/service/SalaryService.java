package service;

import dao.SalaryDAO;
import model.Salary;

import java.util.List;

// SalaryService.java
// Handles the Net Salary calculation rule and validation before saving.
public class SalaryService {

    private final SalaryDAO salaryDAO = new SalaryDAO();

    public String addSalary(Salary salary) {
        if (salary.getBasicSalary() < 0 || salary.getAllowance() < 0 || salary.getDeduction() < 0) {
            return "Salary values cannot be negative.";
        }

        // Net Salary = Basic Salary + Allowance - Deduction
        // Calculated here in Core Java, exactly as required.
        double netSalary = salary.getBasicSalary() + salary.getAllowance() - salary.getDeduction();
        salary.setNetSalary(netSalary);

        boolean success = salaryDAO.addSalary(salary);
        return success ? null : "Could not save salary record.";
    }

    public List<Salary> getAllSalaries() {
        return salaryDAO.getAllSalaries();
    }

    public List<Salary> getSalariesByEmployee(int employeeId) {
        return salaryDAO.getSalariesByEmployee(employeeId);
    }

    public String updateSalary(Salary salary) {
        double netSalary = salary.getBasicSalary() + salary.getAllowance() - salary.getDeduction();
        salary.setNetSalary(netSalary);
        boolean success = salaryDAO.updateSalary(salary);
        return success ? null : "Could not update salary record.";
    }
}
