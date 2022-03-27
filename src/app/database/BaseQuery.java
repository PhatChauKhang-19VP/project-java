package app.database;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

public abstract class BaseQuery {
    protected void appendList(StringBuilder sql, List<?> list, String init, String sep) {
        // if we insert the first item
        // we do not insert the separator.
        boolean first = true;
        for (Object s : list) {
            if (first) {
                sql.append(init);
            } else {
                sql.append(sep);
            }
            sql.append(s);
            first = false;
        }
    }
}
