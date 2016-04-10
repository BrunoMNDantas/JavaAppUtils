package apputils.repository.repository.jdbc;


import apputils.repository.repository.IRepository;
import apputils.repository.utils.RepositoryException;

import java.sql.*;
import java.util.Collection;

public abstract class JDBCRepository<T,K> implements IRepository<T,K> {

    @FunctionalInterface
    public interface JDBCFunction<T,R>{
        R apply(T t) throws SQLException, RepositoryException;
    }

    @FunctionalInterface
    public interface JDBCBiFunction<T,U,R>{
        R apply(T t, U u) throws SQLException, RepositoryException;
    }

    @FunctionalInterface
    public interface JDBCConsumer<T>{
        void accept(T t) throws SQLException, RepositoryException;
    }



    protected final String jdbcDriver;
    protected final String dbURL;
    protected final String user;
    protected final String password;
    protected final String getQuery;
    protected final String getAllQuery;
    protected final String insertQuery;
    protected final String deleteQuery;
    protected final String updateQuery;


    public JDBCRepository(String jdbcDriver, String dbURL, String user, String password,
                          String getQuery, String getAllQuery, String insertQuery,
                          String deleteQuery, String updateQuery){
        this.jdbcDriver = jdbcDriver;
        this.dbURL = dbURL;
        this.user = user;
        this.password = password;
        this.getQuery = getQuery;
        this.getAllQuery = getAllQuery;
        this.insertQuery = insertQuery;
        this.deleteQuery = deleteQuery;
        this.updateQuery = updateQuery;
    }


    @Override
    public T get(K k) throws RepositoryException {
        try{
            return executeStatement((conn)-> conn.prepareStatement(getQuery),
                                    (stmt)->prepareGetStatement(stmt, k),
                                    (conn, stmt)->get(conn, stmt, k));
        } catch(SQLException e) {
            throw new RepositoryException("Error getting element with key: " + k, e);
        }
    }

    @Override
    public Collection<T> getAll() throws RepositoryException {
        try{
            return executeStatement((conn) -> conn.prepareStatement(getAllQuery),
                                    (stmt) -> prepareGetAllStatement(stmt),
                                    (conn, stmt) -> getAll(conn, stmt));
        } catch(SQLException e) {
            throw new RepositoryException("Error getting all elements", e);
        }
    }

    @Override
    public boolean insert(T t) throws RepositoryException {
        try{
            return executeStatement((conn)->conn.prepareStatement(insertQuery),
                                    (stmt)->prepareInsertStatement(stmt, t),
                                    (conn, stmt)->insert(conn, stmt, t));
        } catch(SQLException e) {
            throw new RepositoryException("Error inserting element", e);
        }
    }

    @Override
    public boolean delete(T t) throws RepositoryException {
        try{
            return executeStatement((conn)->conn.prepareStatement(deleteQuery),
                                    (stmt)->prepareDeleteStatement(stmt, t),
                                    (conn, stmt)->delete(conn, stmt, t));
        } catch(SQLException e) {
            throw new RepositoryException("Error deleting element", e);
        }
    }

    @Override
    public boolean update(T t) throws RepositoryException {
        try{
            return executeStatement((conn)->conn.prepareStatement(updateQuery),
                                    (stmt)->prepareUpdateStatement(stmt, t),
                                    (conn, stmt)->update(conn, stmt, t));
        } catch(SQLException e) {
            throw new RepositoryException("Error updating element", e);
        }
    }



    protected <R> R execute(JDBCFunction<Connection, R> executor) throws SQLException, RepositoryException {
        Connection conn = null;

        try {
            Class.forName(jdbcDriver);

            conn = DriverManager.getConnection(dbURL, user, password);

            return executor.apply(conn);
        } catch(ClassNotFoundException e) {
            throw new RepositoryException("JDBC Driver [" + jdbcDriver + "] not found!", e);
        } finally {
            closeConnection(conn);
        }
    }

    protected void closeConnection(Connection conn) throws SQLException {
        if(conn != null && !conn.isClosed())
            conn.close();
    }

    protected <S extends Statement, R> R executeStatement(
                                            Connection conn,
                                            JDBCFunction<Connection, S> statementSupplier,
                                            JDBCConsumer<S> statementPreparer,
                                            JDBCBiFunction<Connection, S, R> statementExecutor)
                                                    throws SQLException, RepositoryException {
        S stmt = null;
        try{
            stmt = statementSupplier.apply(conn);
            statementPreparer.accept(stmt);
            return statementExecutor.apply(conn, stmt);
        } finally {
            closeStatement(stmt);
        }
    }

    protected <S extends Statement, R> R executeStatement(
                                            JDBCFunction<Connection, S> statementSupplier,
                                            JDBCConsumer<S> statementPreparer,
                                            JDBCBiFunction<Connection, S, R> statementExecutor)
                                                    throws SQLException, RepositoryException {
        return execute((conn) -> executeStatement(conn, statementSupplier, statementPreparer, statementExecutor));
    }

    protected void closeStatement(Statement stmt) throws SQLException {
        if(stmt != null && !stmt.isClosed())
            stmt.close();
    }


    protected void closeResultSet(ResultSet rs) throws SQLException {
        if(rs != null && !rs.isClosed())
            rs.close();
    }

    protected  <R> R consumeResultSet(PreparedStatement stmt, JDBCFunction<ResultSet, R> converter) throws SQLException, RepositoryException {
        ResultSet rs = null;
        try{
            rs = stmt.executeQuery();
            return converter.apply(rs);
        }finally {
            closeResultSet(rs);
        }
    }


    protected abstract void prepareGetStatement(PreparedStatement stmt, K key) throws SQLException, RepositoryException;
    protected abstract void prepareGetAllStatement(PreparedStatement stmt) throws SQLException, RepositoryException;
    protected abstract void prepareInsertStatement(PreparedStatement stmt, T elem) throws SQLException, RepositoryException;
    protected abstract void prepareDeleteStatement(PreparedStatement stmt, T elem) throws SQLException, RepositoryException;
    protected abstract void prepareUpdateStatement(PreparedStatement stmt, T elem) throws SQLException, RepositoryException;

    protected abstract T get(Connection conn, PreparedStatement stmt, K key) throws SQLException, RepositoryException;
    protected abstract Collection<T> getAll(Connection conn, PreparedStatement stmt) throws SQLException, RepositoryException;
    protected abstract boolean insert(Connection conn, PreparedStatement stmt, T elem) throws SQLException, RepositoryException;
    protected abstract boolean delete(Connection conn, PreparedStatement stmt, T elem) throws SQLException, RepositoryException;
    protected abstract boolean update(Connection conn, PreparedStatement stmt, T elem) throws SQLException, RepositoryException;

}

