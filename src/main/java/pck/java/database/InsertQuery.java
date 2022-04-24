package pck.java.database;

import java.util.ArrayList;

public class InsertQuery extends BaseQuery {
    private String table;
    private ArrayList<String> columns = new ArrayList<>();
    private ArrayList<String> values = new ArrayList<>();

    public InsertQuery() {
    }

    public void clearTable() {
        table = "";
    }

    public void clearColumns() {
        columns.clear();
    }

    public void clearValues() {
        values.clear();
    }

    public void clear() {
        table = "";
        columns.clear();
        values.clear();
    }

    public InsertQuery insertInto(String table) {
        this.table = table;
        return this;
    }

    public InsertQuery columns(String col) {
        columns.add(col);
        return this;
    }

    public InsertQuery columns(ArrayList<String> cols) {
        columns.addAll(cols);
        return this;
    }

    public InsertQuery values(String val) {
        values.add(val);
        return this;
    }

    public InsertQuery values(ArrayList<String> val) {
        StringBuilder buff = new StringBuilder();
        appendList(buff, val, "", ",");
        values.add(String.valueOf(buff));
        return this;
    }

    public String getQuery() {
        StringBuilder query = new StringBuilder("insert into ");
        query.append(table)
                .append(" (");
        appendList(query, columns, "", ",");
        query.append(") values (");
        appendList(query, values, "", "),(");
        query.append(")");
        return query.toString();
    }

    @Override
    public String toString() {
        return this.getQuery();
    }
}
