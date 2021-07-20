/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;

public class GenerateQuery extends GenericForwardComposer {

    private static final long serialVersionUID = 7306934226849116514L;

    private Tree tree;
    // input box id
    private Textbox cellName;
    // currently selected item
    private Treeitem treeItemSelected;

    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
    }

    // add a child node Click event
    public void onClick$add() {
        String cellNameStr = cellName.getValue();
        treeItemSelected = tree.getSelectedItem();
        addItem(treeItemSelected, cellNameStr);
    }

    private void addItem(Treeitem treeitem, String Str) {
        Treecell treecell = new Treecell(Str);
        Treerow treerow = new Treerow();
        treecell.setParent(treerow); // tree cell inside the only tree row
        if (treeitem.getTreerow() == null) {// a tree item only one GenerateQuery
            // row, no row under item first add a row
            treeitem.appendChild(treerow);
        } else if (treeitem.getTreechildren() == null) {// a tree
            // item can have an optional GenerateQuery
            // children
            Treechildren treechildren = new Treechildren(); // if there is no tree
            // children would add tree
            // children, and treeitem added to the next treechildren
            Treeitem treeitems = new Treeitem();
            addItem(treeitems, Str); // callback, added to the tree row tree item
            treechildren.appendChild(treeitems); // add the tree item to the newly added tree
            // children under
            treeitem.appendChild(treechildren); // add to the tree children tree item
        } else {
            Treeitem treeitems = new Treeitem();
            addItem(treeitems, Str); // callback, added to the tree row tree item
            treeitem.getTreechildren().appendChild(treeitems);// add to the tree item
            // select the item under the tree
            // children under
        }
    }

    /**
     * Click on the Add layer nodes 0
     */
    public void onClick$addRoot() {
        String cellNameStr = cellName.getValue();
        Treeitem treeitem = new Treeitem();
        // Create a cell
        Treecell treecell = new Treecell(cellNameStr);
        Treerow treerow = new Treerow();
        // set the cell to tree row of children, there can not appendChild
        treecell.setParent(treerow);
        // here can not appendChild, because there is only a line item, but you can have a GenerateQuery children
        treerow.setParent(treeitem);
        // add a new item in the tree first child is under the GenerateQuery children
        tree.getFirstChild().appendChild(treeitem);
    }
}
