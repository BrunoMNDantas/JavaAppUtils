package apputils.repository.repository.jdbc;

public abstract class PostgreRepository<T,K,F> extends JDBCRepository<T,K,F> {

    public static final String JDBC_DRIVER = "org.postgresql.Driver";
    public static final String DB_URL = "jdbc:postgresql://127.0.0.1:5432/tempdb";
    public static final String USER = "postgres";
    public static final String PASSWORD = "postgres";


    public PostgreRepository(String jdbcDriver, String dbURL,
                             String user, String password,
                             String getQuery, String getAllQuery,
                             String insertQuery, String deleteQuery, String updateQuery) {
        super(jdbcDriver, dbURL, user, password, getQuery, getAllQuery, insertQuery, deleteQuery, updateQuery);
    }

    public PostgreRepository(String user, String password,
                             String getQuery, String getAllQuery,
                             String insertQuery, String deleteQuery, String updateQuery) {
        super(JDBC_DRIVER, DB_URL, user, password, getQuery, getAllQuery, insertQuery, deleteQuery, updateQuery);
    }

    public PostgreRepository(String getQuery, String getAllQuery,
                             String insertQuery, String deleteQuery, String updateQuery) {
        super(JDBC_DRIVER, DB_URL, USER, PASSWORD, getQuery, getAllQuery, insertQuery, deleteQuery, updateQuery);
    }

}
