package org.archiviststoolkit.util;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: Nathan Stevens
 * Date: Jul 23, 2008
 * Time: 1:26:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class UpgradeUtils {
  /**
   *
   * @param stmt The SQL statement used to access the database
   * @param tableName The name of the table
   * @return The table id for that given table
   * @throws java.sql.SQLException
   */
  public static int getTableId(Statement stmt, String tableName) throws SQLException {
    int tableId = -1;

    String sqlString = "SELECT DatabaseTables.tableId FROM DatabaseTables " +
            "WHERE DatabaseTables.tableName = '" + tableName + "'";

    ResultSet rs = stmt.executeQuery(sqlString);

    while (rs.next()) {
      tableId = rs.getInt(1);
    }

    return tableId;
  }

  /**
   *
   * @param stmt The SQL statement used to access the database
   * @param tableId The table id
   * @param fieldName The field name
   * @return the field id
   * @throws java.sql.SQLException
   */
  public static int getFieldId(Statement stmt, int tableId, String fieldName) throws SQLException {
    int fieldId = -1;

    String sqlString = "SELECT DatabaseFields.fieldId FROM DatabaseFields " +
            "WHERE DatabaseFields.tableId = '" + tableId + "' " +
            "AND DatabaseFields.fieldName = '" + fieldName + "'";

    ResultSet rs = stmt.executeQuery(sqlString);

    while (rs.next()) {
      fieldId = rs.getInt(1);
    }

    return fieldId;
  }

  /**
   *
   * @param stmt The SQL statement used to access the database
   * @param tableName The name of the table
   * @param fieldName The field name
   * @return field id
   * @throws java.sql.SQLException
   */
  public static int getFieldId(Statement stmt, String tableName, String fieldName) throws SQLException {
    int tableId = getTableId(stmt, tableName);
    return getFieldId(stmt, tableId, fieldName);
  }
}
