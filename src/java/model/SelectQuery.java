package model;

import java.util.Arrays;
import java.util.List;

public class SelectQuery {

    private String tblName;
    private String dbName;
    private List<ElementColumn> ecs;
    private List<ElementWhere> ews;
    private List<ElementOrderBy> eobs;

    public SelectQuery() {
    }

    public SelectQuery(List<ElementColumn> ecs, String tblName, String dbName, List<ElementWhere> ews, List<ElementOrderBy> eobs) {
        this.ecs = ecs;
        this.tblName = tblName;
        this.dbName = dbName;
        this.ews = ews;
        this.eobs = eobs;
    }

    public List<ElementColumn> getEcs() {
        return ecs;
    }

    public void setEcs(List<ElementColumn> ecs) {
        this.ecs = ecs;
    }

    public String getTblName() {
        return tblName;
    }

    public void setTblName(String tblName) {
        this.tblName = tblName;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public List<ElementWhere> getEws() {
        return ews;
    }

    public void setEws(List<ElementWhere> ews) {
        this.ews = ews;
    }

    public List<ElementOrderBy> getEobs() {
        return eobs;
    }

    public void setEobs(List<ElementOrderBy> eobs) {
        this.eobs = eobs;
    }

    @Override
    public String toString() {
        return "SELECT "
                + genElementColum(dbName, tblName, ecs)
                + " FROM " + dbName + "." + tblName
                + " WHERE "
                + genElementWhere(dbName, tblName, ews)
                + " ORDER BY "
                + genElementOrderBy(dbName, tblName, eobs);
    }

    public String genElementColum(String dbName, String tblName, List<ElementColumn> ecs) {
        String prefix = dbName + "." + tblName + ".";
        StringBuilder str = new StringBuilder();
        String delimiter = "";
        if (!ecs.isEmpty()) {
            for (ElementColumn ec : ecs) {
                str.append(delimiter).append(prefix + ec.getName());
                delimiter = ", ";
            }
        }
        return str.toString();
    }

    public String genElementWhere(String dbName, String tblName, List<ElementWhere> ews) {
        String prefix = dbName + "." + tblName + ".";
        StringBuilder str = new StringBuilder();
        String delimiter = "";
        if (!ecs.isEmpty()) {
            for (ElementWhere ew : ews) {
                str.append(delimiter).append(prefix + ew.getEc().getName() + " " + ew.getCondition() + " " + ew.getValue());
                delimiter = " AND ";
            }
        }
        return str.toString();
    }

    public String genElementOrderBy(String dbName, String tblName, List<ElementOrderBy> eobs) {
        String prefix = dbName + "." + tblName + ".";
        StringBuilder str = new StringBuilder();
        String delimiter = "";
        if (!ecs.isEmpty()) {
            for (ElementOrderBy eob : eobs) {
                str.append(delimiter).append(prefix + eob.getEc().getName() + " " + eob.getType());
                delimiter = ", ";
            }
        }
        return str.toString();
    }
}
