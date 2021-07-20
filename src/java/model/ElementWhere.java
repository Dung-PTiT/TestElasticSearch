/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Dell
 */
public class ElementWhere {

    private ElementColumn ec;
    private String condition;
    private String value;

    public ElementWhere() {
    }

    public ElementWhere(ElementColumn ec, String condition, String value) {
        this.ec = ec;
        this.condition = condition;
        this.value = value;
    }

    public ElementColumn getEc() {
        return ec;
    }

    public void setEc(ElementColumn ec) {
        this.ec = ec;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "ElementWhere{" + "ec=" + ec + ", condition=" + condition + ", value=" + value + '}';
    }
}
