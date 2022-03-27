package app.database;

import java.util.ArrayList;

public class UpdateQuery extends BaseQuery {
    private String table = "";
    private ArrayList<String> values = new ArrayList<>();
    private ArrayList<String> conditions = new ArrayList<>();

    public UpdateQuery() {
    }

    public void clearTable() {
        table = "";
    }

    public void clearConditions() {
        conditions.clear();
    }

    public void clearValues() {
        values.clear();
    }

    public void clear() {
        table = "";
        conditions.clear();
        values.clear();
    }

    public UpdateQuery update(String table) {
        this.table = table;
        return this;
    }

    public UpdateQuery where(String condition) {
        conditions.add(condition);
        return this;
    }

    public UpdateQuery where(String key, String value) {
        StringBuilder condition = new StringBuilder();
        condition.append(key)
                .append("=")
                .append(value);
        conditions.add(String.valueOf(condition));
        return this;
    }

    public UpdateQuery set(String key, String value) {
        StringBuilder newValue = new StringBuilder();
        newValue.append(key)
                .append("=")
                .append(value);
        values.add(String.valueOf(newValue));
        return this;
    }

    public UpdateQuery set(String newValue) {
        values.add(newValue);
        return this;
    }

    public String getQuery() {
        StringBuilder query = new StringBuilder("update ");
        query.append(table);
        if (values.size() > 0) {
            query.append(" set ");
            appendList(query, values, "", ", ");
        }
        if (conditions.size() > 0) {
            query.append(" where ");
            appendList(query, conditions, "", " and ");
        }
        return String.valueOf(query);
    }

    @Override
    public String toString() {
        return this.getQuery();
    }
}
