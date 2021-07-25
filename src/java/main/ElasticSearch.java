package main;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.ejb.Init;
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
import org.zkoss.zul.Textbox;

public class ElasticSearch extends SelectorComposer<Component> {

    @Wire
    private Label label, tableName;

    @Wire
    private Div divSelect, divWhere, divOrderBy, divLimit;

    @Wire
    private Combobox cbbTable;

    private List<Table> myModel = new ArrayList();

    private ListModel mySubModel;

    @Init
    public void init(@BindingParam("arg1") String arg1) {
        System.out.println(arg1);
        label.setValue("A1");
    }

    @Listen("onClick = #btnSelect")
    public void addSelect() {
        divSelect.setClass("divColumn");
        divSelect.appendChild(genDivSelect());
    }

    public Div genDivSelect() {
        Div div = new Div();
        div.setStyle("display: flex");
        Textbox tb1 = new Textbox();
        tb1.setClass("tbCondition");
        tb1.setValue("textbox1");
        Label lb1 = new Label();
        lb1.setValue("column1");
        Button btn = new Button();
        btn.setLabel("-");
        btn.setClass("btnMinus");
        div.appendChild((Component) tb1);
        div.appendChild((Component) lb1);
        div.appendChild((Component) btn);
        return div;
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
