package ca.jrvs.apps.jdbc;

import ca.jrvs.apps.jdbc.util.DataTransferObject;
import java.sql.Date;
import java.sql.Timestamp;

public class Order implements DataTransferObject {
    private long id;
    private Timestamp creationDate;
    private double totalDue;
    private String status;
    private long customerId;
    private long salespersonId;

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public double getTotalDue() {
        return totalDue;
    }

    public void setTotalDue(double totalDue) {
        this.totalDue = totalDue;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public long getSalespersonId() {
        return salespersonId;
    }

    public void setSalespersonId(long salespersonId) {
        this.salespersonId = salespersonId;
    }

    @Override
    public String toString() {
        return "OrderDAO{" +
                "id=" + id +
                ", creationDate=" + creationDate +
                ", totalDue=" + totalDue +
                ", status='" + status + '\'' +
                ", customerId=" + customerId +
                ", salespersonId=" + salespersonId +
                '}';
    }
}
