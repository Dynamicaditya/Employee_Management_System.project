package model;

// Leave.java
// Represents one row of the "leaves" table.
// Note: class is named "Leave" (not "LeaveRequest") to match the assignment,
// but LeaveRequest would also be a valid/clearer name in a real project.
public class Leave {

    private int leaveId;
    private int employeeId;
    private String employeeName; // convenience field, filled by JOIN queries for display
    private String leaveType;    // Casual Leave / Sick Leave / Earned Leave
    private String fromDate;
    private String toDate;
    private String reason;
    private String status;       // Pending / Approved / Rejected

    public Leave() {
    }

    public Leave(int employeeId, String leaveType, String fromDate, String toDate, String reason, String status) {
        this.employeeId = employeeId;
        this.leaveType = leaveType;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.reason = reason;
        this.status = status;
    }

    public Leave(int leaveId, int employeeId, String employeeName, String leaveType,
                 String fromDate, String toDate, String reason, String status) {
        this.leaveId = leaveId;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.leaveType = leaveType;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.reason = reason;
        this.status = status;
    }

    public int getLeaveId() { return leaveId; }
    public void setLeaveId(int leaveId) { this.leaveId = leaveId; }

    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }

    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }

    public String getLeaveType() { return leaveType; }
    public void setLeaveType(String leaveType) { this.leaveType = leaveType; }

    public String getFromDate() { return fromDate; }
    public void setFromDate(String fromDate) { this.fromDate = fromDate; }

    public String getToDate() { return toDate; }
    public void setToDate(String toDate) { this.toDate = toDate; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
