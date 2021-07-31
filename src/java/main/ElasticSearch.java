package main;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import model.Table;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
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
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

public class ElasticSearch extends SelectorComposer<Component> {

    private final String SELECT = "select";
    private final String WHERE = "where";
    private final String ORDERBY = "orderby";
    private final String LIMIT = "limit";

    @Wire
    private Label label, tableName;

    @Wire
    private Combobox cbbTable;

    @Wire
    private Listbox lbSelect, lbWhere, lbOrderBy, lbLimit, lbHead, lbResult;

    private List<Table> myModel = new ArrayList();

    private ListModel mySubModel;

    private List<String> colsTbl = genCols();

    private List<List<String>> rowsTbl = genRows();

    private Map<String, String> itemSelectMap = new HashMap<>();
    private List<Integer> lsIdItemSelect = new ArrayList<>();
    int countItemSelect = 0;

    private Map<String, String> itemWhereMap = new HashMap<>();
    private List<Integer> lsIdItemWhere = new ArrayList<>();
    int countItemWhere = 0;

    @Listen("onClick = #btnSelect")
    public void addSelect() {
        int tmp1 = countItemSelect;
        lsIdItemSelect.add(tmp1);
        countItemSelect++;
        lbSelect.setStyle("border: none");
        Listcell listCell1 = new Listcell();
        Listcell listCell2 = new Listcell();
        Listcell listCell3 = new Listcell();
        listCell1.setStyle("border: none");
        listCell2.setStyle("border: none");
        listCell3.setStyle("border: none; text-align: start");

        Combobox cbbFunc = genCbbFunc(new Combobox());
        cbbFunc.setId("cbbFunc_" + tmp1);
        cbbFunc.setTooltiptext("cbbFunc_" + tmp1);
        listCell1.appendChild((Component) cbbFunc);

        Combobox cbbCol = genCbbColumn(new Combobox());
        cbbCol.setId("cbbSelectCol_" + SELECT + "_" + tmp1);
        cbbCol.setTooltiptext("cbbSelectCol_" + SELECT + "_" + tmp1);
        listCell2.appendChild((Component) cbbCol);

        Button button = genBtnMinus(new Button());
        button.setId("btnDelSelect_" + tmp1);
        button.setTooltiptext("btnDelSelect_" + tmp1);
        listCell3.appendChild((Component) button);

        Listitem listitem = new Listitem();
        listitem.setId("lsItemSelect_" + tmp1);
        listitem.setTooltiptext("lsItemSelect_" + tmp1);
        listitem.appendChild(listCell1);
        listitem.appendChild(listCell2);
        listitem.appendChild(listCell3);
        lbSelect.appendChild(listitem);

        button.addEventListener(Events.ON_CLICK, new EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                delItemSelect(tmp1);
            }
        });

        cbbFunc.addEventListener(Events.ON_SELECT, new EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                checkItemSelect(tmp1);
            }
        });

        cbbCol.addEventListener(Events.ON_SELECT, new EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                checkItemSelect(tmp1);
            }
        });
    }

    @NotifyChange
    @Command("*")
    public void delItemSelect(int id) {
        lbSelect.removeChild((Listitem) lbSelect.query("#lsItemSelect_" + id));
        lsIdItemSelect.remove(Integer.valueOf(id));
    }

    @NotifyChange
    @Command("*")
    public void delItemWhere(int id) {
        lbWhere.removeChild((Listitem) lbWhere.query("#lsItemWhere_" + id));
        lsIdItemWhere.remove(Integer.valueOf(id));
    }

    @NotifyChange
    @Command("*")
    public void checkItemSelect(int id) {
        Combobox cbbFunc = (Combobox) lbSelect.query("#cbbFunc_" + id);
        String func = cbbFunc.getValue();
        Combobox cbbCol = (Combobox) lbSelect.query("#cbbSelectCol_" + SELECT + "_" + id);
        String col = cbbCol.getValue();
//        if (itemSelectMap.containsKey(func) && !func.equals("--- No Func ---")) {
//            String value = itemSelectMap.get(func);
//            if (value.equals(col) && !col.equals("--- Column ---")) {
//                Messagebox.show("Func and col existed",
//                        "Error", Messagebox.OK, Messagebox.ERROR);
//                return;
//            }
//        } else {
//            itemSelectMap.put(func, col);
//        }
//        Messagebox.show("Size:" + itemSelectMap.size(),
//                "Information", Messagebox.OK, Messagebox.INFORMATION);

        Messagebox.show("Func: " + func + " | Col: " + col,
                "Information", Messagebox.OK, Messagebox.INFORMATION);
    }

    @Listen("onClick = #btnWhere")
    public void addWhere() {
        int tmp1 = countItemWhere;
        lsIdItemWhere.add(tmp1);
        countItemWhere++;
        lbWhere.setStyle("border: none");

        Listitem listitem = new Listitem();
        listitem.setId("lsItemWhere_" + tmp1);
        listitem.setTooltiptext("lsItemWhere_" + tmp1);

        if (lsIdItemWhere.size() > 1) {
            Listcell listCell0 = new Listcell();
            listCell0.setStyle("border: none");
            Combobox cbbConj = new Combobox();
            cbbConj.setId("cbbConj_" + tmp1);
            cbbConj.setTooltiptext("cbbConj_" + tmp1);
            cbbConj.appendItem("--- Conjunction ---");
            cbbConj.appendItem("AND");
            cbbConj.appendItem("OR");
            cbbConj.setSelectedIndex(0);
            listCell0.appendChild((Combobox) cbbConj);
            listitem.appendChild(listCell0);
        }

        Listcell listCell1 = new Listcell();
        Listcell listCell2 = new Listcell();
        Listcell listCell3 = new Listcell();
        listCell1.setStyle("border: none");
        listCell2.setStyle("border: none");
        listCell3.setStyle("border: none; text-align: start");

        Combobox cbbCol = genCbbColumn(new Combobox());
        cbbCol.setId("cbbWhereCol_" + WHERE + "_" + tmp1);
        cbbCol.setTooltiptext("cbbWhereCol_" + WHERE + "_" + tmp1);
        listCell1.appendChild((Component) cbbCol);

        Combobox cbbCondition = genCbbCondition(new Combobox());
        cbbCondition.setId("cbbWhereCon_" + tmp1);
        cbbCondition.setTooltiptext("cbbWhereCon_" + tmp1);
        listCell2.appendChild((Component) cbbCondition);

        Button button = genBtnMinus(new Button());
        button.setId("btnDelWhere_" + tmp1);
        button.setTooltiptext("btnDelWhere_" + tmp1);
        listCell3.appendChild((Component) button);

        listitem.appendChild(listCell1);
        listitem.appendChild(listCell2);
        listitem.appendChild(listCell3);
        lbWhere.appendChild(listitem);

        button.addEventListener(Events.ON_CLICK, new EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                delItemWhere(tmp1);
            }
        });

        cbbCondition.addEventListener(Events.ON_CHANGE, new EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                Combobox currentCbb = (Combobox) lbWhere.query("#cbbWhereCon_" + tmp1);
//       Messagebox.show("CBB: " + currentCbb.getValue(),
//                "Information", Messagebox.OK, Messagebox.INFORMATION);
                String condition = currentCbb.getValue();
                if (!condition.equals("--- Condition ---")) {
                    if (condition.equals("=") || condition.equals(">") || condition.equals("<")
                            || condition.equals(">=") || condition.equals("<=") || condition.equals("LIKE")
                            || condition.equals("NOT LIKE")) {
//                        Textbox tbValue = new Textbox();
//                        tbValue.setId("tblVal_" + tmp1);
//                        tbValue.setTooltiptext("tblVal_" + tmp1);
//                        listCell2.appendChild(tbValue);
//                        listitem.appendChild(listCell2);
//                        lbWhere.appendChild(listitem);
                    } else if (condition.equals("IS NULL") || condition.equals("IS NOT NULL")) {
                        Messagebox.show("Type 2",
                                "Information", Messagebox.OK, Messagebox.INFORMATION);
                    } else if (condition.equals("IS BETWEEN") || condition.equals("IS NOT BETWEEN")) {
                        Messagebox.show("Type 3",
                                "Information", Messagebox.OK, Messagebox.INFORMATION);
                    } else if (condition.equals("IS IN LIST") || condition.equals("IS NOT IN LIST")) {
                        Messagebox.show("Type 4",
                                "Information", Messagebox.OK, Messagebox.INFORMATION);
                    }
                }
            }
        });
    }

    @Listen("onClick = #btnExe")
    public void execute() {
        Messagebox.show("Total select: " + countItemWhere + " | " + lsIdItemWhere.toString(),
                "Information", Messagebox.OK, Messagebox.INFORMATION);
    }

    public Combobox genCbbFunc(Combobox cbbFunc) {
        cbbFunc.setStyle("width: 100px");
        cbbFunc.appendItem("--- No Func ---");
        cbbFunc.appendItem("MAX");
        cbbFunc.appendItem("MIN");
        cbbFunc.appendItem("COUNT");
        cbbFunc.appendItem("AVERAGE");
        cbbFunc.setSelectedIndex(0);
        return cbbFunc;
    }

    public Combobox genCbbColumn(Combobox cbbColumn) {
        cbbColumn.setStyle("width: 100px");
        cbbColumn.appendItem("--- Column ---");
        cbbColumn.appendItem("*");
        cbbColumn.appendItem("id");
        cbbColumn.appendItem("name");
        cbbColumn.appendItem("title");
        cbbColumn.appendItem("description");
        cbbColumn.setSelectedIndex(0);
        return cbbColumn;
    }

    public Combobox genCbbCondition(Combobox cbbCondition) {
        cbbCondition.setStyle("width: 100px");
        cbbCondition.appendItem("--- Condition ---");
        cbbCondition.appendItem("=");
        cbbCondition.appendItem(">");
        cbbCondition.appendItem("<");
        cbbCondition.appendItem(">=");
        cbbCondition.appendItem("<=");
        cbbCondition.appendItem("LIKE");
        cbbCondition.appendItem("NOT LIKE");
        cbbCondition.appendItem("IS NULL");
        cbbCondition.appendItem("IS NOT NULL");
        cbbCondition.appendItem("IS BETWEEN");
        cbbCondition.appendItem("IS NOT BETWEEN");
        cbbCondition.appendItem("IS IN LIST");
        cbbCondition.appendItem("IS NOT IN LIST");
        cbbCondition.setSelectedIndex(0);
        return cbbCondition;
    }

    public Button genBtnMinus(Button btn) {
        btn.setLabel("-");
        btn.setStyle("cursor: pointer");
        return btn;
    }

    @Listen("onClick = #btnOrderBy")
    public void addOrderBy() {
        lbOrderBy.setClass("divColumn");
        lbOrderBy.appendChild(genDivOrderBy());
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
        lbLimit.setClass("divColumn");
        lbLimit.appendChild(genDivOrderBy());
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
        super.doAfterCompose(comp);
        genTable();
    }

    //listen on onSelect event from the component ID (#) myCb. See SelectorComposer documentation for more info on the selectors
    @Listen("onChange=#cbbTable")
    public void handleCbChange() {
        tableName.setValue((cbbTable.getText().isEmpty()) ? "<Table>" : cbbTable.getText());
        reset();
        rowsTbl = genRows();
        colsTbl = genCols();
        genTable();
    }

    public void genTable() {
        List<String> tables = new ArrayList<>();
        tables.add("category");
        tables.add("message");
        tables.add("post");
        tables.add("tag");
        tables.add("users");

        for (int i = 0; i < tables.size(); i++) {
            myModel.add(new Table(String.valueOf(i), tables.get(i)));
        }

        Comparator myComparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                String input = (String) o1;
                Table bean = (Table) o2;
                return bean.getName().contains(input) ? 0 : 1;
            }
        };
        mySubModel = ListModels.toListSubModel(new ListModelList(myModel), myComparator, 10);
        cbbTable.setModel(mySubModel);

        ComboitemRenderer<Table> myRenderer = new ComboitemRenderer<Table>() {

            @Override
            public void render(Comboitem item, Table bean, int index) throws Exception {
                item.setLabel(bean.getName());
                item.setValue(bean);
            }
        };
        cbbTable.setItemRenderer(myRenderer);

        Listitem lisitem1 = new Listitem();
        for (int i = 0; i < colsTbl.size(); i++) {
            Listcell listcell = new Listcell();
            listcell.setLabel(colsTbl.get(i));
            lisitem1.appendChild(listcell);
        }
        lbHead.appendChild(lisitem1);

        for (int i = 0; i < rowsTbl.size(); i++) {
            List<String> tmp = rowsTbl.get(i);
            Listitem listitem = new Listitem();
            for (int j = 0; j < tmp.size(); j++) {
                Listcell listcell = new Listcell();
                listcell.setLabel(tmp.get(j));
                listitem.appendChild(listcell);
            }
            lbResult.appendChild(listitem);
        }
    }

    public static List<List<String>> genRows() {
        List<List<String>> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            List<String> strList = new ArrayList<>();
            for (int j = 0; j < 8; j++) {
                Random generator = new Random();
                int value = generator.nextInt(4) + 1;
                strList.add(String.valueOf(value));
            }
            list.add(strList);
        }
        return list;
    }

    public static List<String> genCols() {
        List<String> strList = new ArrayList<>();
        for (int j = 0; j < 8; j++) {
            Random generator = new Random();
            int value = generator.nextInt(4) + 1;
            strList.add("Col " + String.valueOf(value));
        }
        return strList;
    }

    public void reset() {
        countItemSelect = 0;
        countItemWhere = 0;
        lsIdItemSelect.clear();
        lbSelect.getItems().clear();
        lbWhere.getItems().clear();
        lbHead.getItems().clear();
        lbResult.getItems().clear();
        myModel.clear();
        cbbTable.getItems().clear();
    }
}
