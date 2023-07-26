package ca.jrvs.apps.jdbc;

import java.sql.*;

public class JDBCExecutor {

    public static void main(String... args){
        DatabaseConnectionManager dcm = new DatabaseConnectionManager("localhost",
                "hplussport", "postgres", "password");
        try{
            Connection connection = dcm.getConnection();

//            CustomerDAO customerDAO = new CustomerDAO(connection);
//            Customer customer = new Customer();
//            customer.setFirstName("John");
//            customer.setLastName("Adams");
//            customer.setEmail("jadams@wh.gov");
//            customer.setPhone("(555) 555-9845");
//            customer.setAddress("1234 Main St");
//            customer.setCity("Arlington");
//            customer.setState("VA");
//            customer.setZipCode("01234");
//
//            Customer dbCustomer = customerDAO.create(customer);
//            System.out.println(dbCustomer);
//            dbCustomer = customerDAO.findById(dbCustomer.getId());
//            System.out.println(dbCustomer);
//            dbCustomer.setEmail("john.adams@wh.gov");
//            dbCustomer = customerDAO.update(dbCustomer);
//            System.out.println(dbCustomer);
//            customerDAO.delete(dbCustomer.getId());

            OrderDAO orderDAO = new OrderDAO(connection);
            Order order = new Order();
            order.setCreationDate(Timestamp.valueOf("2023-07-26 00:00:00"));
            order.setTotalDue(123.45);
            order.setStatus("cancelled");
            order.setCustomerId(10000);
            order.setSalespersonId(108);

            Order dbOrder = orderDAO.create(order);
            System.out.println(dbOrder);
            dbOrder = orderDAO.findById(dbOrder.getId());
            System.out.println(dbOrder);
            dbOrder.setStatus("past due");
            dbOrder = orderDAO.update(dbOrder);
            System.out.println(dbOrder);
            orderDAO.delete(dbOrder.getId());

        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
