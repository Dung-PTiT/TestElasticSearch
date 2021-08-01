/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.util.ArrayList;
import java.util.List;
import model.ElementColumn;

/**
 *
 * @author Dell
 */
public class Test {

    public static void main(String[] args) {
//        SelectQuery sq = new SelectQuery();
//        sq.setDbName("tool");
//        sq.setTblName("crbt");
//
//        List<ElementColumn> ecs = new ArrayList<>();
//        ecs.add(new ElementColumn("id"));
//        ecs.add(new ElementColumn("name"));
//        ecs.add(new ElementColumn("code"));
//        sq.setEcs(ecs);
//
//        List<ElementWhere> ews = new ArrayList<>();
//        ews.add(new ElementWhere(new ElementColumn("id"), "=", "3"));
//        ews.add(new ElementWhere(new ElementColumn("name"), "LIKE", "abc"));
//        sq.setEws(ews);
//
//        List<ElementOrderBy> eobs = new ArrayList<>();
//        eobs.add(new ElementOrderBy(new ElementColumn("id"), "ASC"));
//        eobs.add(new ElementOrderBy(new ElementColumn("name"), "DESC"));
//        sq.setEobs(eobs);
//
//        System.out.println(sq.toString());
//        
//        List<Integer> lIntegers = new ArrayList<Integer>();
//        lIntegers.add(1);
//        lIntegers.add(2);
//        lIntegers.add(300);
//        lIntegers.remove(Integer.valueOf(300));
//        System.out.println("TestClass.main()"+lIntegers);

        String s = "1,2,3,4";
        String s1[] = s.split(",");
        for (String s2 : s1) {
            System.out.println(s2);
        }
        StringBuilder str = new StringBuilder();
        String delimiter = "";
        for (String s2 : s1) {
            str.append(delimiter).append("'" + s2 + "'");
            delimiter = ",";
        }
        System.out.println(str);
    }
}
