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
public class ElementOrderBy {

    private ElementColumn ec;
    private String type;

    public ElementOrderBy() {
    }

    public ElementOrderBy(ElementColumn ec, String type) {
        this.ec = ec;
        this.type = type;
    }

    public ElementColumn getEc() {
        return ec;
    }

    public void setEc(ElementColumn ec) {
        this.ec = ec;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ElementOrderBy{" + "ec=" + ec + ", type=" + type + '}';
    }
}
