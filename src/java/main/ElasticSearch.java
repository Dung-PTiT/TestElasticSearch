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
import org.zkoss.zk.ui.event.*;
import org.zkoss.util.media.*;

public class ElasticSearch extends SelectorComposer<Component> {

    private final String SELECT = "select";
    private final String WHERE = "where";
    private final String ORDERBY = "orderby";
    private final String LIMIT = "limit";
    private final String query = "SELECT * FROM abc WHERE id = 1 ORDER BY id ASC LIMIT 10";

    @Wire
    private Label label, tableName, queryLabel;

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

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        genTable();
        queryLabel.setValue(query);
    }

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
        int tmp = lsIdItemWhere.get(0);
        lbWhere.removeChild((Listitem) lbWhere.query("#lsItemWhere_" + id));
        lsIdItemWhere.remove(Integer.valueOf(id));

        if (tmp == id) {
            Listitem item = (Listitem) lbWhere.query("#lsItemWhere_" + lsIdItemWhere.get(0));
            item.removeChild((Listcell) lbWhere.query("#listcell0_" + lsIdItemWhere.get(0)));
        }
    }

    @NotifyChange
    @Command("*")
    public void checkItemSelect(int id) {
        Combobox cbbFunc = (Combobox) lbSelect.query("#cbbFunc_" + id);
        String func = cbbFunc.getValue();
        Combobox cbbCol = (Combobox) lbSelect.query("#cbbSelectCol_" + SELECT + "_" + id);
        String col = cbbCol.getValue();
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
            listCell0.setId("listcell0_" + tmp1);
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
        listCell1.setStyle("border: none");
        Combobox cbbCol = genCbbColumn(new Combobox());
        cbbCol.setId("cbbWhereCol_" + WHERE + "_" + tmp1);
        cbbCol.setTooltiptext("cbbWhereCol_" + WHERE + "_" + tmp1);
        listCell1.appendChild((Component) cbbCol);

        Listcell listCell2 = new Listcell();
        listCell2.setStyle("border: none");
        Combobox cbbCondition = genCbbCondition(new Combobox());
        cbbCondition.setId("cbbWhereCon_" + tmp1);
        cbbCondition.setTooltiptext("cbbWhereCon_" + tmp1);
        listCell2.appendChild((Component) cbbCondition);

        Listcell listCell3 = new Listcell();
        listCell3.setId("lc3Value_" + tmp1);
        listCell3.setStyle("border: none;");
        listCell3.setVisible(false);
        Textbox tbValue = new Textbox("--- Value ---");
        tbValue.setId("tbValue_" + tmp1);
        tbValue.setTooltiptext("tbValue_" + tmp1);
        listCell3.appendChild((Component) tbValue);

        Listcell listCell4 = new Listcell();
        listCell4.setId("lc4Value_" + tmp1);
        listCell4.setStyle("border: none;");
        listCell4.setVisible(false);
        Textbox tbStart = new Textbox("--- Start ---");
        tbStart.setId("tbStart_" + tmp1);
        tbStart.setTooltiptext("tbStart_" + tmp1);
        listCell4.appendChild((Component) tbStart);
        Textbox tbEnd = new Textbox("--- End ---");
        tbEnd.setId("tbEnd_" + tmp1);
        tbEnd.setTooltiptext("tbEnd_" + tmp1);
        listCell4.appendChild((Component) tbEnd);

        Listcell listCell5 = new Listcell();
        listCell5.setId("lc5Value_" + tmp1);
        listCell5.setStyle("border: none;");
        listCell5.setVisible(false);
        Textbox tbValueList = new Textbox("--- Value List ---");
        tbValueList.setId("tbValueList_" + tmp1);
        tbValueList.setTooltiptext("tbValueList_" + tmp1);
        listCell5.appendChild((Component) tbValueList);

        Listcell listCell6 = new Listcell();
        listCell6.setStyle("border: none; text-align: start");
        Button button = genBtnMinus(new Button());
        button.setId("btnDelWhere_" + tmp1);
        button.setTooltiptext("btnDelWhere_" + tmp1);
        listCell6.appendChild((Component) button);

        listitem.appendChild(listCell1);
        listitem.appendChild(listCell2);
        listitem.appendChild(listCell3);
        listitem.appendChild(listCell4);
        listitem.appendChild(listCell5);
        listitem.appendChild(listCell6);
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
                String condition = currentCbb.getValue();
                if (!condition.equals("--- Condition ---")) {
                    if (condition.equals("=") || condition.equals(">") || condition.equals("<")
                            || condition.equals(">=") || condition.equals("<=") || condition.equals("LIKE")
                            || condition.equals("NOT LIKE")) {
                        lbWhere.query("#lc3Value_" + tmp1).setVisible(true);

                        lbWhere.query("#lc4Value_" + tmp1).setVisible(false);
                        Textbox tbStart = (Textbox) lbWhere.query("#tbStart_" + tmp1);
                        tbStart.setValue("--- Start ---");
                        Textbox tbEnd = (Textbox) lbWhere.query("#tbEnd_" + tmp1);
                        tbEnd.setValue("--- End ---");

                        lbWhere.query("#lc5Value_" + tmp1).setVisible(false);
                        Textbox tbValueList = (Textbox) lbWhere.query("#tbValueList_" + tmp1);
                        tbValueList.setValue("--- Value List ---");
                    } else if (condition.equals("IS NULL") || condition.equals("IS NOT NULL")) {
                        lbWhere.query("#lc3Value_" + tmp1).setVisible(false);
                        Textbox tbValue = (Textbox) lbWhere.query("#tbStart_" + tmp1);
                        tbValue.setValue("--- Value ---");

                        lbWhere.query("#lc4Value_" + tmp1).setVisible(false);
                        Textbox tbStart = (Textbox) lbWhere.query("#tbStart_" + tmp1);
                        tbStart.setValue("--- Start ---");
                        Textbox tbEnd = (Textbox) lbWhere.query("#tbEnd_" + tmp1);
                        tbEnd.setValue("--- End ---");

                        lbWhere.query("#lc5Value_" + tmp1).setVisible(false);
                        Textbox tbValueList = (Textbox) lbWhere.query("#tbValueList_" + tmp1);
                        tbValueList.setValue("--- Value List ---");
                    } else if (condition.equals("IS BETWEEN") || condition.equals("IS NOT BETWEEN")) {
                        lbWhere.query("#lc3Value_" + tmp1).setVisible(false);
                        Textbox tbValue = (Textbox) lbWhere.query("#tbStart_" + tmp1);
                        tbValue.setValue("--- Value ---");

                        lbWhere.query("#lc4Value_" + tmp1).setVisible(true);

                        lbWhere.query("#lc5Value_" + tmp1).setVisible(false);
                        Textbox tbValueList = (Textbox) lbWhere.query("#tbValueList_" + tmp1);
                        tbValueList.setValue("--- Value List ---");
                    } else if (condition.equals("IS IN LIST") || condition.equals("IS NOT IN LIST")) {
                        lbWhere.query("#lc3Value_" + tmp1).setVisible(false);
                        Textbox tbValue = (Textbox) lbWhere.query("#tbStart_" + tmp1);
                        tbValue.setValue("--- Value ---");

                        lbWhere.query("#lc4Value_" + tmp1).setVisible(false);
                        Textbox tbStart = (Textbox) lbWhere.query("#tbStart_" + tmp1);
                        tbStart.setValue("--- Start ---");
                        Textbox tbEnd = (Textbox) lbWhere.query("#tbEnd_" + tmp1);
                        tbEnd.setValue("--- End ---");

                        lbWhere.query("#lc5Value_" + tmp1).setVisible(true);
                    }
                }
            }
        });
    }

    @Listen("onClick = #btnExe")
    public void execute() {
//        Messagebox.show("Total select: " + countItemWhere + " | " + lsIdItemWhere.toString(),
//                "Information", Messagebox.OK, Messagebox.INFORMATION);
        List<ElementWhere> ews = new ArrayList<>();
        if (!lsIdItemWhere.isEmpty()) {
            for (int i = 0; i < lsIdItemWhere.size(); i++) {
                int index = lsIdItemWhere.get(i);
                ElementWhere ew = new ElementWhere();
                if (i > 0) {
                    Combobox cbbConj = (Combobox) lbWhere.query("#cbbConj_" + index);
                    if (cbbConj.getValue().equals("--- Conjunction ---")) {
                        Messagebox.show("Choose conjunction | Line: " + (i + 1), "Error", Messagebox.OK, Messagebox.ERROR);
                        return;
                    } else {
                        ew.setConjunction(cbbConj.getValue());
                    }
                } else {
                    ew.setConjunction("none");
                }
                Combobox cbbCol = (Combobox) lbWhere.query("#cbbWhereCol_" + WHERE + "_" + index);
                Combobox cbbCon = (Combobox) lbWhere.query("#cbbWhereCon_" + index);
                Textbox tbValue = (Textbox) lbWhere.query("#tbValue_" + index);
                Textbox tbStart = (Textbox) lbWhere.query("#tbStart_" + index);
                Textbox tbEnd = (Textbox) lbWhere.query("#tbEnd_" + index);
                Textbox tbValueList = (Textbox) lbWhere.query("#tbValueList_" + index);

                if (cbbCol.getValue().equals("--- Column ---")) {
                    Messagebox.show("Choose column | Line: " + (i + 1), "Error", Messagebox.OK, Messagebox.ERROR);
                    return;
                } else if (cbbCon.getValue().equals("--- Condition ---")) {
                    Messagebox.show("Choose condition | Line: " + (i + 1), "Error", Messagebox.OK, Messagebox.ERROR);
                    return;
                }

                ew.setColumn(cbbCol.getValue());
                ew.setCondition(cbbCon.getValue());

                if (tbValue.getValue().isEmpty() || tbValue.getValue().equals("--- Value ---")) {
                    tbValue.setValue("none");
                } else {
                    ew.setValue(tbValue.getValue().trim());
                }

                if (tbStart.getValue().isEmpty() || tbStart.getValue().equals("--- Start ---")) {
                    tbStart.setValue("none");
                } else {
                    ew.setValueStart(tbStart.getValue().trim());
                }

                if (tbEnd.getValue().isEmpty() || tbEnd.getValue().equals("--- End ---")) {
                    tbEnd.setValue("none");
                } else {
                    ew.setValueEnd(tbEnd.getValue().trim());
                }

                if (tbValueList.getValue().isEmpty() || tbValueList.getValue().equals("--- Value List ---")) {
                    tbValueList.setValue("none");
                } else {
                    ew.setValueList(tbValueList.getValue().trim());
                }
                ews.add(ew);
            }
        }

        String where = buildWhere(ews);
        Messagebox.show("Resutl: " + where, "Information", Messagebox.OK, Messagebox.INFORMATION);
    }

    private String buildWhere(List<ElementWhere> ews) {
        String sql = "";
        for (int i = 0; i < ews.size(); i++) {
            ElementWhere ew = ews.get(i);
            if (!ew.getConjunction().equals("none")) {
                sql += " " + ew.getConjunction();
            }
            sql += " " + ew.getColumn();
            if (ew.getCondition().equals("=") || ew.getCondition().equals(">") || ew.getCondition().equals("<")
                    || ew.getCondition().equals(">=") || ew.getCondition().equals("<=")) {
                if (!ew.getValue().equals("none")) {
                    sql += " " + ew.getCondition() + " " + ew.getValue();
                }
            } else if (ew.getCondition().equals("IS NULL") || ew.getCondition().equals("IS NOT NULL")) {
                sql += " " + ew.getCondition();
            } else if (ew.getCondition().equals("LIKE") || ew.getCondition().equals("NOT LIKE")) {
                if (!ew.getValue().equals("none")) {
                    sql += " " + ew.getCondition() + " '%" + ew.getValue() + "%'";
                }
            } else if (ew.getCondition().equals("IS BETWEEN") || ew.getCondition().equals("IS NOT BETWEEN")) {
                if (!ew.getValueStart().equals("none") && !ew.getValueEnd().equals("none")) {
                    sql += " " + ew.getCondition().replace("IS ", "") + " " + ew.getValueStart() + " AND " + ew.getValueEnd();
                }
            } else if (ew.getCondition().equals("IS IN LIST") || ew.getCondition().equals("IS NOT IN LIST")) {
                if (!ew.getValueList().equals("none")) {
                    String initStr = ew.getValueList();
                    String strArr[] = initStr.split(",");
                    StringBuilder strValue = new StringBuilder();
                    String delimiter = "";
                    for (String s : strArr) {
                        strValue.append(delimiter).append("'").append(s).append("'");
                        delimiter = ",";
                    }
                    sql += " " + ((ew.getCondition().equals("IS IN LIST")) ? "IN" : "NOT IN") + " (" + strValue + ")";
                }
            }
        }
        return sql;
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
        tables.add("category qqqqqqqqqqqqqqqqqqqqqqqqqqq");
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
                item.setTooltiptext(bean.getName());
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

    public void onUpload$btn(UploadEvent e)// throws InterruptedException
    {
        if (e.getMedias() != null) {
            StringBuilder sb = new StringBuilder("You uploaded: \n");

            for (Media m : e.getMedias()) {
                sb.append(m.getName());
                sb.append(" (");
                sb.append(m.getContentType());
                sb.append(")\n");
                String filename = m.getName();
                //nos fijamos las extenciones
                if (filename.indexOf(".txt") == -1 || filename.indexOf(".xls") == -1 || filename.indexOf(".csv") == -1) {
                    Messagebox.show("Mal");
                }
            }

            Messagebox.show(sb.toString());
        } else {
            Messagebox.show("You uploaded no files!");
        }
    }
}

class ElementWhere {

    private String conjunction;
    private String column;
    private String condition;
    private String value;
    private String valueStart;
    private String valueEnd;
    private String valueList;

    public ElementWhere() {
    }

    public ElementWhere(String conjunction, String column, String condition, String value, String valueStart, String valueEnd, String valueList) {
        this.conjunction = conjunction;
        this.column = column;
        this.condition = condition;
        this.value = value;
        this.valueStart = valueStart;
        this.valueEnd = valueEnd;
        this.valueList = valueList;
    }

    public String getConjunction() {
        return conjunction;
    }

    public void setConjunction(String conjunction) {
        this.conjunction = conjunction;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
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

    public String getValueStart() {
        return valueStart;
    }

    public void setValueStart(String valueStart) {
        this.valueStart = valueStart;
    }

    public String getValueEnd() {
        return valueEnd;
    }

    public void setValueEnd(String valueEnd) {
        this.valueEnd = valueEnd;
    }

    public String getValueList() {
        return valueList;
    }

    public void setValueList(String valueList) {
        this.valueList = valueList;
    }

    @Override
    public String toString() {
        return "ElementWhere{" + "conjunction=" + conjunction + ", column=" + column + ", condition=" + condition + ", value=" + value + ", valueStart=" + valueStart + ", valueEnd=" + valueEnd + ", valueList=" + valueList + '}';
    }
}
