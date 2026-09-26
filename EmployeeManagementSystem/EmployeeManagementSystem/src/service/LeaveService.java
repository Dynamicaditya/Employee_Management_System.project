package service;

import dao.LeaveDAO;
import model.Leave;

import java.util.List;

// LeaveService.java
// Business rules for leave requests sit here, above the DAO.
public class LeaveService {

    private final LeaveDAO leaveDAO = new LeaveDAO();

    public String applyLeave(Leave leave) {
        if (leave.getFromDate() == null || leave.getToDate() == null
                || leave.getFromDate().isEmpty() || leave.getToDate().isEmpty()) {
            return "From date and To date are required.";
        }
        if (leave.getFromDate().compareTo(leave.getToDate()) > 0) {
            return "From date cannot be after To date.";
        }
        if (leave.getReason() == null || leave.getReason().trim().isEmpty()) {
            return "Please provide a reason for leave.";
        }
        leave.setStatus("Pending"); // every new leave request starts as Pending
        boolean success = leaveDAO.applyLeave(leave);
        return success ? null : "Could not submit leave request.";
    }

    public List<Leave> getAllLeaves() {
        return leaveDAO.getAllLeaves();
    }

    public boolean approveLeave(int leaveId) {
        return leaveDAO.updateLeaveStatus(leaveId, "Approved");
    }

    public boolean rejectLeave(int leaveId) {
        return leaveDAO.updateLeaveStatus(leaveId, "Rejected");
    }
}
