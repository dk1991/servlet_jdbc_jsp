package conn;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionUtils {

    public static Connection getConnection() throws SQLException {
        return MySQLConnUtils.getMySQLConnection();
    }

    public static void closeQuietly(Connection conn) {
        try {
            conn.close();
        } catch (Exception e) {
        }
    }

    public static void rollbackQuietly(Connection conn) {
        try {
            /*
            оператор языка SQL, который применяется для того, чтобы: отменить все изменения,
            внесённые начиная с момента начала транзакции или с какой-то точки сохранения (SAVEPOINT);
            очистить все точки сохранения данной транзакции;
            завершить транзакцию; освободить все блокировки данной транзакции
             */
            conn.rollback();
        } catch (Exception e) {
        }
    }
}
