package ca.jrvs.apps.jdbc;


import ca.jrvs.apps.jdbc.util.DataAccessObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class OrderDAO extends DataAccessObject<Order> {
    private static final String INSERT = "INSERT INTO orders (creation_date, total_due, status, customer_id, salesperson_id) VALUES (?, ?, ?, ?, ?)";

    private static final String GET_ONE = "SELECT order_id, creation_date, total_due, status, customer_id, salesperson_id FROM orders WHERE order_id = ?";

    public OrderDAO(Connection connection) {
        super(connection);
    }

    private static final String UPDATE = "UPDATE orders SET creation_date = ?, total_due = ?, status = ?, customer_id = ?, salesperson_id = ? WHERE order_id = ?";

    private static final String DELETE = "DELETE FROM orders WHERE order_id = ?";

    @Override
    public Order findById(long id) {
        Order order = new Order();
        try(PreparedStatement statement = this.connection.prepareStatement(GET_ONE)){
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                order.setId(rs.getLong("order_id"));
                order.setCreationDate(rs.getTimestamp("creation_date"));
                order.setTotalDue(rs.getDouble("total_due"));
                order.setStatus(rs.getString("status"));
                order.setCustomerId(rs.getLong("customer_id"));
                order.setSalespersonId(rs.getLong("salesperson_id"));
            }
        }catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return order;
    }

    @Override
    public List<Order> findAll() {
        return null;
    }

    @Override
    public Order update(Order dto) {
        Order order = null;
        try(PreparedStatement statement = connection.prepareStatement(UPDATE)){
            statement.setTimestamp(1, dto.getCreationDate());
            statement.setDouble(2, dto.getTotalDue());
            statement.setString(3, dto.getStatus());
            statement.setLong(4, dto.getCustomerId());
            statement.setLong(5, dto.getSalespersonId());
            statement.setLong(6, dto.getId());
            statement.execute();
            order = findById(dto.getId());
        }catch(SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return order;
    }

    @Override
    public Order create(Order dto) {
        try(PreparedStatement statement = this.connection.prepareStatement(INSERT)){
            statement.setTimestamp(1, dto.getCreationDate());
            statement.setDouble(2, dto.getTotalDue());
            statement.setString(3, dto.getStatus());
            statement.setLong(4, dto.getCustomerId());
            statement.setLong(5, dto.getSalespersonId());
            statement.execute();
            int id = this.getLastVal(ORDER_SEQUENCE);
            return this.findById(id);
        } catch(SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(long id) {
        try(PreparedStatement statement = connection.prepareStatement(DELETE)){
            statement.setLong(1, id);
            statement.execute();
        }catch(SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
