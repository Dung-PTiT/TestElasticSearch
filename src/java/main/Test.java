/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.util.ArrayList;
import java.util.List;
import model.ElementColumn;
import model.ElementOrderBy;
import model.ElementWhere;
import model.SelectQuery;

/**
 *
 * @author Dell
 */
public class Test {

    public static void main(String[] args) {
        SelectQuery sq = new SelectQuery();
        sq.setDbName("tool");
        sq.setTblName("crbt");

        List<ElementColumn> ecs = new ArrayList<>();
        ecs.add(new ElementColumn("id"));
        ecs.add(new ElementColumn("name"));
        ecs.add(new ElementColumn("code"));
        sq.setEcs(ecs);

        List<ElementWhere> ews = new ArrayList<>();
        ews.add(new ElementWhere(new ElementColumn("id"), "=", "3"));
        ews.add(new ElementWhere(new ElementColumn("name"), "LIKE", "abc"));
        sq.setEws(ews);

        List<ElementOrderBy> eobs = new ArrayList<>();
        eobs.add(new ElementOrderBy(new ElementColumn("id"), "ASC"));
        eobs.add(new ElementOrderBy(new ElementColumn("name"), "DESC"));
        sq.setEobs(eobs);

        System.out.println(sq.toString());
    }
}
