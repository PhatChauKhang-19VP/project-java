package pck.java.database;

import java.util.ArrayList;

public class SelectQuery extends BaseQuery {
    private String table = "";
    private ArrayList<String> conditions = new ArrayList<>();
    private ArrayList<String> columns = new ArrayList<>();
    private ArrayList<String> groups = new ArrayList<>();
    private ArrayList<String> orders = new ArrayList<>();

    public SelectQuery() {
    }

    public void clearTable() {
        table = "";
    }

    public void clearColumns() {
        columns.clear();
    }

    public void clearConditions() {
        conditions.clear();
    }

    public void clearOrders() {
        orders.clear();
    }

    public void clearGroups() {
        groups.clear();
    }

    public void clear() {
        table = "";
        columns.clear();
        conditions.clear();
        orders.clear();
        groups.clear();
    }

    public SelectQuery from(String table) {
        this.table = table;
        return this;
    }

    public SelectQuery where(String condition) {
        conditions.add(condition);
        return this;
    }

    public SelectQuery where(String key, String value) {
        StringBuilder condition = new StringBuilder();
        condition.append(key)
                .append("=")
                .append(value);
        conditions.add(String.valueOf(condition));
        return this;
    }

    public SelectQuery select(String col) {
        columns.add(col);
        return this;
    }

    public SelectQuery select(ArrayList<String> cols) {
        columns.addAll(cols);
        return this;
    }

    public SelectQuery orderBy(String order) {
        orders.add(order);
        return this;
    }

    public SelectQuery orderBy(ArrayList<String> orders) {
        this.orders.addAll(orders);
        return this;
    }

    public SelectQuery groupBy(String group) {
        groups.add(group);
        return this;
    }

    public SelectQuery groupBy(ArrayList<String> groups) {
        this.groups.addAll(groups);
        return this;
    }

    public String getQuery() {
        StringBuilder query = new StringBuilder("select ");
        appendList(query, columns, "", ",");
        query.append(" from ").append(table);
        if (conditions.size() > 0) {
            query.append(" where ");
            appendList(query, conditions, "", " and ");
        }
        if (groups.size() > 0) {
            query.append(" group by ");
            appendList(query, groups, "", ",");
        }
        if (orders.size() > 0) {
            query.append(" order by ");
            appendList(query, orders, "", ",");
        }
        return String.valueOf(query);
    }

    @Override
    public String toString() {
        return this.getQuery();
    }
}
