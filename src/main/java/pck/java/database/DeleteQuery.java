package pck.java.database;

import java.util.ArrayList;

public class DeleteQuery extends BaseQuery {
    private String table = "";
    private ArrayList<String> conditions = new ArrayList<>();

    public DeleteQuery() {
    }

    public void clearCondition() {
        conditions.clear();
    }

    public void clearTable() {
        table = "";
    }

    public void clear() {
        table = "";
        conditions.clear();
    }

    public DeleteQuery deleteFrom(String table) {
        this.table = table;
        return this;
    }

    public DeleteQuery where(String condition) {
        conditions.add(condition);
        return this;
    }

    public DeleteQuery where(String key, String value) {
        StringBuilder condition = new StringBuilder();
        condition.append(key)
                .append("=")
                .append(value);
        conditions.add(String.valueOf(condition));
        return this;
    }

    public String getQuery() {
        StringBuilder query = new StringBuilder("delete from ");
        query.append(table);
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
