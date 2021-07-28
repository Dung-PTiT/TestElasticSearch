package main;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import model.Table;
import org.zkoss.bind.annotation.BindingParam;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.ListModels;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;

public class ElasticSearch extends SelectorComposer<Component> {

    @Wire
    private Label label, tableName;

    @Wire
    private Div divSelect, divWhere, divOrderBy, divLimit;

    @Wire
    private Combobox cbbTable;

    @Wire
    private Listbox lbSelect;

    private List<Table> myModel = new ArrayList();

    private ListModel mySubModel;
    
    @Listen("onClick = #btnSelect")
    public void addSelect() {
        lbSelect.setStyle("border: none");
        Listcell listCell1 = new Listcell();
        Listcell listCell2 = new Listcell();
        Listcell listCell3 = new Listcell();
        listCell1.setStyle("border: none");
        listCell2.setStyle("border: none");
        listCell3.setStyle("border: none; text-align: start");
        listCell1.appendChild((Component) genCbbFunc(new Combobox()));
        listCell2.appendChild((Component) genCbbColumn(new Combobox()));
        listCell3.appendChild((Component) genBtnMinus(new Button()));
        Listitem listitem = new Listitem();
        listitem.appendChild(listCell1);
        listitem.appendChild(listCell2);
        listitem.appendChild(listCell3);
        lbSelect.appendChild(listitem);
        System.out.println("----------Size: " + lbSelect.getItems().size());
    }

    public Combobox genCbbFunc(Combobox cbbFunc) {
        cbbFunc.setStyle("width: 100px");
        cbbFunc.appendItem("MAX");
        cbbFunc.appendItem("MIN");
        cbbFunc.appendItem("COUNT");
        cbbFunc.appendItem("AVERAGE");
        return cbbFunc;
    }

    public Combobox genCbbColumn(Combobox cbbColumn) {
        cbbColumn.setStyle("width: 100px");
        cbbColumn.appendItem("id");
        cbbColumn.appendItem("name");
        cbbColumn.appendItem("title");
        cbbColumn.appendItem("description");
        return cbbColumn;
    }

    public Button genBtnMinus(Button btn) {
        btn.setLabel("-");
        btn.setStyle("cursor: pointer");
        return btn;
    }

    @Listen("onClick = #btnWhere")
    public void addWhere() {
        divWhere.setClass("divColumn");
        divWhere.appendChild(genDivWhere());
    }

    public Div genDivWhere() {
        Div div = new Div();
        Label lb1 = new Label();
        lb1.setValue("column1");
        div.appendChild((Component) lb1);
        return div;
    }

    @Listen("onClick = #btnOrderBy")
    public void addOrderBy() {
        divOrderBy.setClass("divColumn");
        divOrderBy.appendChild(genDivOrderBy());
    }

    public Div genDivOrderBy() {
        Div div = new Div();
        Label lb1 = new Label();
        lb1.setValue("column1");
        div.appendChild((Component) lb1);
        return div;
    }

    @Listen("onClick = #btnLimit")
    public void addLimit() {
        divLimit.setClass("divColumn");
        divLimit.appendChild(genDivOrderBy());
    }

    public Div genLimit() {
        Div div = new Div();
        Label lb1 = new Label();
        lb1.setValue("column1");
        div.appendChild((Component) lb1);
        return div;
    }

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        //call super (required, or the page will not finish composition)
        super.doAfterCompose(comp);
        //generate some data
//        List<String> tables = getTables();
        List<String> tables = new ArrayList<>();
        tables.add("category");
        tables.add("message");
        tables.add("post");
        tables.add("tag");
        tables.add("users");

        for (int i = 0; i < tables.size(); i++) {
            myModel.add(new Table(String.valueOf(i), tables.get(i)));
        }

        //Comparator have many uses. Here, the comparator will return 0 is the item match the input string and should be included in the result)		
        Comparator myComparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                // the value of the combobox input
                String input = (String) o1;
                // the bean from the model
                Table bean = (Table) o2;
                return bean.getName().contains(input) ? 0 : 1;
            }
        };

        //Generate a ListModel from the flat data, then aF subModel from the listmodel
        //Comparator is used when filtering to identify which bean should be included in the result
        //This subModel will return the top 15 results from when filtering. Anything further will be ignored. You can implement your own submodel to have control over this process.
        mySubModel = ListModels.toListSubModel(new ListModelList(myModel), myComparator, 10);
        cbbTable.setModel(mySubModel);

        //Renderer will be called when creating the comboitem (each line in combobox) from the bean. You can implement your own renderer to control what is displayed
        ComboitemRenderer<Table> myRenderer = new ComboitemRenderer<Table>() {

            @Override
            public void render(Comboitem item, Table bean, int index) throws Exception {
                //set the text of the row
                item.setLabel(bean.getName());
                //set the value of the row (available when receiving events from Combobox)
                item.setValue(bean);
            }
        };
        cbbTable.setItemRenderer(myRenderer);
    }

    //listen on onSelect event from the component ID (#) myCb. See SelectorComposer documentation for more info on the selectors
    @Listen("onSelect=#cbbTable")
    public void handleCbChange(SelectEvent<Comboitem, Table> event) {
        //get the selected object, and get the bean from the object value. Do whatever your need with the bean from there :)
        tableName.setValue(((Table) event.getSelectedItems().iterator().next().getValue()).getName());
    }

}
