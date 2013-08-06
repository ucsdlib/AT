/*
 * Created by JFormDesigner on Fri Mar 30 14:32:16 EDT 2007
 */

package org.archiviststoolkit.editor;

import java.awt.*;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.archiviststoolkit.swing.ATBasicComponentFactory;
import org.archiviststoolkit.model.RepositoryStatistics;
import org.archiviststoolkit.mydomain.DomainEditorFields;
import org.archiviststoolkit.structure.ATFieldInfo;

/**
 * @author Lee Mandell
 */
public class RepositoryStatisticsFields extends DomainEditorFields {
    public RepositoryStatisticsFields() {
        initComponents();
    }

    public Component getInitialFocusComponent() {
        return yearOfReport;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        label51 = new JLabel();
        yearOfReport = ATBasicComponentFactory.createIntegerField(detailsModel,RepositoryStatistics.PROPERTYNAME_YEAR_OF_REPORT);
        separator1 = new JSeparator();
        panel4 = new JPanel();
        label21 = new JLabel();
        rights = ATBasicComponentFactory.createCheckBox(detailsModel, RepositoryStatistics.PROPERTYNAME_DIGITIZATION, RepositoryStatistics.class);
        rights4 = ATBasicComponentFactory.createCheckBox(detailsModel, RepositoryStatistics.PROPERTYNAME_PHOTOGRAPHIC_REPRODUCTION, RepositoryStatistics.class);
        rights2 = ATBasicComponentFactory.createCheckBox(detailsModel, RepositoryStatistics.PROPERTYNAME_EXHIBIT_LOANS, RepositoryStatistics.class);
        rights5 = ATBasicComponentFactory.createCheckBox(detailsModel, RepositoryStatistics.PROPERTYNAME_RETAIL_GIFT_SALES, RepositoryStatistics.class);
        rights3 = ATBasicComponentFactory.createCheckBox(detailsModel, RepositoryStatistics.PROPERTYNAME_FOOD_BEVERAGE, RepositoryStatistics.class);
        separator2 = new JSeparator();
        panel5 = new JPanel();
        label22 = new JLabel();
        label42 = new JLabel();
        extentNumber = ATBasicComponentFactory.createDoubleField(detailsModel,RepositoryStatistics.PROPERTYNAME_PROFESSIONAL_FTE);
        label43 = new JLabel();
        extentNumber2 = ATBasicComponentFactory.createDoubleField(detailsModel,RepositoryStatistics.PROPERTYNAME_NON_PROFESSIONAL_FTE);
        label44 = new JLabel();
        extentNumber3 = ATBasicComponentFactory.createDoubleField(detailsModel,RepositoryStatistics.PROPERTYNAME_STUDENT_FTE);
        label45 = new JLabel();
        extentNumber4 = ATBasicComponentFactory.createDoubleField(detailsModel,RepositoryStatistics.PROPERTYNAME_VOLUNTEER_FTE);
        separator3 = new JSeparator();
        panel6 = new JPanel();
        label23 = new JLabel();
        label46 = new JLabel();
        extentNumber5 = ATBasicComponentFactory.createDoubleField(detailsModel,RepositoryStatistics.PROPERTYNAME_FUNCT_DIST_ADMIN);
        label47 = new JLabel();
        extentNumber6 = ATBasicComponentFactory.createDoubleField(detailsModel,RepositoryStatistics.PROPERTYNAME_FUNCT_DIST_PROCESSING);
        label48 = new JLabel();
        extentNumber7 = ATBasicComponentFactory.createDoubleField(detailsModel,RepositoryStatistics.PROPERTYNAME_FUNCT_DIST_PRESERVATION);
        label49 = new JLabel();
        extentNumber8 = ATBasicComponentFactory.createDoubleField(detailsModel,RepositoryStatistics.PROPERTYNAME_FUNCT_DIST_REFERENCE);
        separator4 = new JSeparator();
        panel7 = new JPanel();
        label24 = new JLabel();
        rights6 = ATBasicComponentFactory.createCheckBox(detailsModel, RepositoryStatistics.PROPERTYNAME_COLL_FOCI_HISTORICAL, RepositoryStatistics.class);
        rights7 = ATBasicComponentFactory.createCheckBox(detailsModel, RepositoryStatistics.PROPERTYNAME_COLL_FOCI_INSTITUTIONAL, RepositoryStatistics.class);
        rights8 = ATBasicComponentFactory.createCheckBox(detailsModel, RepositoryStatistics.PROPERTYNAME_COLL_FOCI_MANUSCRIPT, RepositoryStatistics.class);
        rights9 = ATBasicComponentFactory.createCheckBox(detailsModel, RepositoryStatistics.PROPERTYNAME_COLL_FOCI_PERSONAL_PAPERS, RepositoryStatistics.class);
        rights10 = ATBasicComponentFactory.createCheckBox(detailsModel, RepositoryStatistics.PROPERTYNAME_COLL_FOCI_OTHER, RepositoryStatistics.class);
        separator5 = new JSeparator();
        label50 = new JLabel();
        scrollPane42 = new JScrollPane();
        title = ATBasicComponentFactory.createTextArea(detailsModel.getModel(RepositoryStatistics.PROPERTYNAME_MAJOR_SUBJECT_AREAS));
        panel8 = new JPanel();
        label40 = new JLabel();
        extentNumber9 = ATBasicComponentFactory.createDoubleField(detailsModel,RepositoryStatistics.PROPERTYNAME_PERCENTAGE_OFF_SITE);
        separator6 = new JSeparator();
        panel10 = new JPanel();
        label26 = new JLabel();
        label56 = new JLabel();
        extentNumber14 = ATBasicComponentFactory.createIntegerField(detailsModel,RepositoryStatistics.PROPERTYNAME_NET_USABLE_AREA);
        label57 = new JLabel();
        extentNumber15 = ATBasicComponentFactory.createIntegerField(detailsModel,RepositoryStatistics.PROPERTYNAME_ADMINISTRATION_OFFICES);
        label60 = new JLabel();
        extentNumber18 = ATBasicComponentFactory.createIntegerField(detailsModel,RepositoryStatistics.PROPERTYNAME_CLASSROOMS);
        label58 = new JLabel();
        extentNumber16 = ATBasicComponentFactory.createIntegerField(detailsModel,RepositoryStatistics.PROPERTYNAME_COLLECTIONS_STORAGE);
        label59 = new JLabel();
        extentNumber17 = ATBasicComponentFactory.createIntegerField(detailsModel,RepositoryStatistics.PROPERTYNAME_READING_ROOM);
        CellConstraints cc = new CellConstraints();

        //======== this ========
        setBorder(Borders.DLU4_BORDER);
        setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        setBackground(new Color(200, 205, 232));
        setLayout(new FormLayout(
            new ColumnSpec[] {
                FormFactory.DEFAULT_COLSPEC,
                FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                new ColumnSpec("max(default;400px):grow")
            },
            new RowSpec[] {
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.LINE_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.LINE_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.LINE_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.LINE_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.LINE_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.LINE_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.LINE_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.LINE_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.LINE_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.LINE_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.LINE_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.LINE_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.LINE_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.LINE_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC
            }));

        //---- label51 ----
        label51.setText("Year of report");
        label51.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label51, RepositoryStatistics.class, RepositoryStatistics.PROPERTYNAME_YEAR_OF_REPORT);
        add(label51, cc.xy(1, 1));

        //---- yearOfReport ----
        yearOfReport.setColumns(4);
        add(yearOfReport, cc.xywh(3, 1, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

        //---- separator1 ----
        separator1.setForeground(new Color(147, 131, 86));
        add(separator1, cc.xywh(1, 3, 3, 1));

        //======== panel4 ========
        {
            panel4.setOpaque(false);
            panel4.setLayout(new FormLayout(
                new ColumnSpec[] {
                    FormFactory.DEFAULT_COLSPEC,
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    FormFactory.DEFAULT_COLSPEC,
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    FormFactory.DEFAULT_COLSPEC,
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                },
                new RowSpec[] {
                    FormFactory.DEFAULT_ROWSPEC,
                    FormFactory.LINE_GAP_ROWSPEC,
                    FormFactory.DEFAULT_ROWSPEC,
                    FormFactory.LINE_GAP_ROWSPEC,
                    FormFactory.DEFAULT_ROWSPEC
                }));

            //---- label21 ----
            label21.setText("Services provided (check all that apply):");
            panel4.add(label21, cc.xywh(1, 1, 7, 1));

            //---- rights ----
            rights.setText("Digitization / scanning");
            rights.setOpaque(false);
            rights.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            rights.setText(ATFieldInfo.getLabel(RepositoryStatistics.class, RepositoryStatistics.PROPERTYNAME_DIGITIZATION));
            panel4.add(rights, cc.xy(3, 3));

            //---- rights4 ----
            rights4.setText("Photographic reproduction");
            rights4.setOpaque(false);
            rights4.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            rights4.setText(ATFieldInfo.getLabel(RepositoryStatistics.class, RepositoryStatistics.PROPERTYNAME_PHOTOGRAPHIC_REPRODUCTION));
            panel4.add(rights4, cc.xy(5, 3));

            //---- rights2 ----
            rights2.setText("Exhibit loans");
            rights2.setOpaque(false);
            rights2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            rights2.setText(ATFieldInfo.getLabel(RepositoryStatistics.class, RepositoryStatistics.PROPERTYNAME_EXHIBIT_LOANS));
            panel4.add(rights2, cc.xy(7, 3));

            //---- rights5 ----
            rights5.setText("Retail / gift sales");
            rights5.setOpaque(false);
            rights5.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            rights5.setText(ATFieldInfo.getLabel(RepositoryStatistics.class, RepositoryStatistics.PROPERTYNAME_RETAIL_GIFT_SALES));
            panel4.add(rights5, cc.xy(3, 5));

            //---- rights3 ----
            rights3.setText("Food and beverage");
            rights3.setOpaque(false);
            rights3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            rights3.setText(ATFieldInfo.getLabel(RepositoryStatistics.class, RepositoryStatistics.PROPERTYNAME_FOOD_BEVERAGE));
            panel4.add(rights3, cc.xy(5, 5));
        }
        add(panel4, cc.xywh(1, 5, 3, 1));

        //---- separator2 ----
        separator2.setForeground(new Color(147, 131, 86));
        add(separator2, cc.xywh(1, 7, 3, 1));

        //======== panel5 ========
        {
            panel5.setOpaque(false);
            panel5.setLayout(new FormLayout(
                new ColumnSpec[] {
                    FormFactory.DEFAULT_COLSPEC,
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    FormFactory.DEFAULT_COLSPEC,
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    FormFactory.DEFAULT_COLSPEC,
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    FormFactory.DEFAULT_COLSPEC,
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                },
                new RowSpec[] {
                    FormFactory.DEFAULT_ROWSPEC,
                    FormFactory.LINE_GAP_ROWSPEC,
                    FormFactory.DEFAULT_ROWSPEC,
                    FormFactory.LINE_GAP_ROWSPEC,
                    FormFactory.DEFAULT_ROWSPEC
                }));

            //---- label22 ----
            label22.setText("Staff size (complete all that apply):");
            panel5.add(label22, cc.xywh(1, 1, 9, 1));

            //---- label42 ----
            label42.setText("Professional FTE");
            label42.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            ATFieldInfo.assignLabelInfo(label42, RepositoryStatistics.class, RepositoryStatistics.PROPERTYNAME_PROFESSIONAL_FTE);
            panel5.add(label42, cc.xy(3, 3));

            //---- extentNumber ----
            extentNumber.setColumns(5);
            panel5.add(extentNumber, cc.xy(5, 3));

            //---- label43 ----
            label43.setText("Non-professional FTE");
            label43.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            ATFieldInfo.assignLabelInfo(label43, RepositoryStatistics.class, RepositoryStatistics.PROPERTYNAME_NON_PROFESSIONAL_FTE);
            panel5.add(label43, cc.xy(7, 3));

            //---- extentNumber2 ----
            extentNumber2.setColumns(5);
            panel5.add(extentNumber2, cc.xywh(9, 3, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

            //---- label44 ----
            label44.setText("Student FTE");
            label44.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            ATFieldInfo.assignLabelInfo(label44, RepositoryStatistics.class, RepositoryStatistics.PROPERTYNAME_STUDENT_FTE);
            panel5.add(label44, cc.xy(3, 5));

            //---- extentNumber3 ----
            extentNumber3.setColumns(5);
            panel5.add(extentNumber3, cc.xy(5, 5));

            //---- label45 ----
            label45.setText("Volunteer FTE");
            label45.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            ATFieldInfo.assignLabelInfo(label45, RepositoryStatistics.class, RepositoryStatistics.PROPERTYNAME_VOLUNTEER_FTE);
            panel5.add(label45, cc.xy(7, 5));

            //---- extentNumber4 ----
            extentNumber4.setColumns(5);
            panel5.add(extentNumber4, cc.xywh(9, 5, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
        }
        add(panel5, cc.xywh(1, 9, 3, 1));

        //---- separator3 ----
        separator3.setForeground(new Color(147, 131, 86));
        add(separator3, cc.xywh(1, 11, 3, 1));

        //======== panel6 ========
        {
            panel6.setOpaque(false);
            panel6.setLayout(new FormLayout(
                new ColumnSpec[] {
                    FormFactory.DEFAULT_COLSPEC,
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    FormFactory.DEFAULT_COLSPEC,
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    FormFactory.DEFAULT_COLSPEC,
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    FormFactory.DEFAULT_COLSPEC,
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                },
                new RowSpec[] {
                    FormFactory.DEFAULT_ROWSPEC,
                    FormFactory.LINE_GAP_ROWSPEC,
                    FormFactory.DEFAULT_ROWSPEC,
                    FormFactory.LINE_GAP_ROWSPEC,
                    FormFactory.DEFAULT_ROWSPEC
                }));

            //---- label23 ----
            label23.setText("FTE Functional Distribution (complete all that apply):");
            panel6.add(label23, cc.xywh(1, 1, 9, 1));

            //---- label46 ----
            label46.setText("Administration");
            label46.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            ATFieldInfo.assignLabelInfo(label46, RepositoryStatistics.class, RepositoryStatistics.PROPERTYNAME_FUNCT_DIST_ADMIN);
            panel6.add(label46, cc.xy(3, 3));

            //---- extentNumber5 ----
            extentNumber5.setColumns(5);
            panel6.add(extentNumber5, cc.xy(5, 3));

            //---- label47 ----
            label47.setText("Processing");
            label47.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            ATFieldInfo.assignLabelInfo(label47, RepositoryStatistics.class, RepositoryStatistics.PROPERTYNAME_FUNCT_DIST_PROCESSING);
            panel6.add(label47, cc.xy(7, 3));

            //---- extentNumber6 ----
            extentNumber6.setColumns(5);
            panel6.add(extentNumber6, cc.xywh(9, 3, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

            //---- label48 ----
            label48.setText("Preservation");
            label48.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            ATFieldInfo.assignLabelInfo(label48, RepositoryStatistics.class, RepositoryStatistics.PROPERTYNAME_FUNCT_DIST_PRESERVATION);
            panel6.add(label48, cc.xy(3, 5));

            //---- extentNumber7 ----
            extentNumber7.setColumns(5);
            panel6.add(extentNumber7, cc.xy(5, 5));

            //---- label49 ----
            label49.setText("Reference");
            label49.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            ATFieldInfo.assignLabelInfo(label49, RepositoryStatistics.class, RepositoryStatistics.PROPERTYNAME_FUNCT_DIST_REFERENCE);
            panel6.add(label49, cc.xy(7, 5));

            //---- extentNumber8 ----
            extentNumber8.setColumns(5);
            panel6.add(extentNumber8, cc.xywh(9, 5, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
        }
        add(panel6, cc.xywh(1, 13, 3, 1));

        //---- separator4 ----
        separator4.setForeground(new Color(147, 131, 86));
        add(separator4, cc.xywh(1, 15, 3, 1));

        //======== panel7 ========
        {
            panel7.setOpaque(false);
            panel7.setLayout(new FormLayout(
                new ColumnSpec[] {
                    FormFactory.DEFAULT_COLSPEC,
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    FormFactory.DEFAULT_COLSPEC,
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    FormFactory.DEFAULT_COLSPEC,
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                },
                new RowSpec[] {
                    FormFactory.DEFAULT_ROWSPEC,
                    FormFactory.LINE_GAP_ROWSPEC,
                    FormFactory.DEFAULT_ROWSPEC,
                    FormFactory.LINE_GAP_ROWSPEC,
                    FormFactory.DEFAULT_ROWSPEC
                }));

            //---- label24 ----
            label24.setText("Collection Foci (check all that apply):");
            panel7.add(label24, cc.xywh(1, 1, 7, 1));

            //---- rights6 ----
            rights6.setText("Historical Records");
            rights6.setOpaque(false);
            rights6.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            rights6.setText(ATFieldInfo.getLabel(RepositoryStatistics.class, RepositoryStatistics.PROPERTYNAME_COLL_FOCI_HISTORICAL));
            panel7.add(rights6, cc.xy(3, 3));

            //---- rights7 ----
            rights7.setText("Instutional Records");
            rights7.setOpaque(false);
            rights7.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            rights7.setText(ATFieldInfo.getLabel(RepositoryStatistics.class, RepositoryStatistics.PROPERTYNAME_COLL_FOCI_INSTITUTIONAL));
            panel7.add(rights7, cc.xy(5, 3));

            //---- rights8 ----
            rights8.setText("Manuscript Collections");
            rights8.setOpaque(false);
            rights8.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            rights8.setText(ATFieldInfo.getLabel(RepositoryStatistics.class, RepositoryStatistics.PROPERTYNAME_COLL_FOCI_MANUSCRIPT));
            panel7.add(rights8, cc.xy(7, 3));

            //---- rights9 ----
            rights9.setText("Personal Papers");
            rights9.setOpaque(false);
            rights9.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            rights9.setText(ATFieldInfo.getLabel(RepositoryStatistics.class, RepositoryStatistics.PROPERTYNAME_COLL_FOCI_PERSONAL_PAPERS));
            panel7.add(rights9, cc.xy(3, 5));

            //---- rights10 ----
            rights10.setText("Other");
            rights10.setOpaque(false);
            rights10.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            rights10.setText(ATFieldInfo.getLabel(RepositoryStatistics.class, RepositoryStatistics.PROPERTYNAME_COLL_FOCI_OTHER));
            panel7.add(rights10, cc.xy(5, 5));
        }
        add(panel7, cc.xywh(1, 17, 3, 1));

        //---- separator5 ----
        separator5.setForeground(new Color(147, 131, 86));
        add(separator5, cc.xywh(1, 19, 3, 1));

        //---- label50 ----
        label50.setText("Major Subject Areas");
        label50.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        ATFieldInfo.assignLabelInfo(label50, RepositoryStatistics.class, RepositoryStatistics.PROPERTYNAME_MAJOR_SUBJECT_AREAS);
        add(label50, cc.xywh(1, 21, 3, 1));

        //======== scrollPane42 ========
        {
            scrollPane42.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            scrollPane42.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            scrollPane42.setPreferredSize(new Dimension(200, 68));

            //---- title ----
            title.setRows(4);
            title.setLineWrap(true);
            title.setWrapStyleWord(true);
            title.setPreferredSize(new Dimension(200, 64));
            title.setMinimumSize(new Dimension(200, 16));
            scrollPane42.setViewportView(title);
        }
        add(scrollPane42, cc.xywh(1, 23, 3, 1));

        //======== panel8 ========
        {
            panel8.setOpaque(false);
            panel8.setLayout(new FormLayout(
                new ColumnSpec[] {
                    FormFactory.DEFAULT_COLSPEC,
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    FormFactory.DEFAULT_COLSPEC
                },
                RowSpec.decodeSpecs("default")));

            //---- label40 ----
            label40.setText("Percentage of collection stored off-site");
            label40.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            ATFieldInfo.assignLabelInfo(label40, RepositoryStatistics.class, RepositoryStatistics.PROPERTYNAME_PERCENTAGE_OFF_SITE);
            panel8.add(label40, cc.xy(1, 1));

            //---- extentNumber9 ----
            extentNumber9.setColumns(5);
            panel8.add(extentNumber9, cc.xy(3, 1));
        }
        add(panel8, cc.xywh(1, 25, 3, 1));

        //---- separator6 ----
        separator6.setForeground(new Color(147, 131, 86));
        add(separator6, cc.xywh(1, 27, 3, 1));

        //======== panel10 ========
        {
            panel10.setOpaque(false);
            panel10.setLayout(new FormLayout(
                new ColumnSpec[] {
                    FormFactory.DEFAULT_COLSPEC,
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    FormFactory.DEFAULT_COLSPEC,
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    FormFactory.DEFAULT_COLSPEC,
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    FormFactory.DEFAULT_COLSPEC,
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    FormFactory.DEFAULT_COLSPEC,
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    FormFactory.DEFAULT_COLSPEC,
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    FormFactory.DEFAULT_COLSPEC
                },
                new RowSpec[] {
                    FormFactory.DEFAULT_ROWSPEC,
                    FormFactory.LINE_GAP_ROWSPEC,
                    FormFactory.DEFAULT_ROWSPEC,
                    FormFactory.LINE_GAP_ROWSPEC,
                    FormFactory.DEFAULT_ROWSPEC
                }));

            //---- label26 ----
            label26.setText("Repository's Physical Characteristics (complete all that apply):");
            panel10.add(label26, cc.xywh(1, 1, 9, 1));

            //---- label56 ----
            label56.setText("Net usable area");
            label56.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            ATFieldInfo.assignLabelInfo(label56, RepositoryStatistics.class, RepositoryStatistics.PROPERTYNAME_NET_USABLE_AREA);
            panel10.add(label56, cc.xy(3, 3));

            //---- extentNumber14 ----
            extentNumber14.setColumns(5);
            panel10.add(extentNumber14, cc.xy(5, 3));

            //---- label57 ----
            label57.setText("Administrative offices");
            label57.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            ATFieldInfo.assignLabelInfo(label57, RepositoryStatistics.class, RepositoryStatistics.PROPERTYNAME_ADMINISTRATION_OFFICES);
            panel10.add(label57, cc.xy(7, 3));

            //---- extentNumber15 ----
            extentNumber15.setColumns(5);
            panel10.add(extentNumber15, cc.xywh(9, 3, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

            //---- label60 ----
            label60.setText("Classroom(s)");
            label60.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            ATFieldInfo.assignLabelInfo(label60, RepositoryStatistics.class, RepositoryStatistics.PROPERTYNAME_CLASSROOMS);
            panel10.add(label60, cc.xy(11, 3));

            //---- extentNumber18 ----
            extentNumber18.setColumns(5);
            panel10.add(extentNumber18, cc.xywh(13, 3, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

            //---- label58 ----
            label58.setText("Collections storage");
            label58.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            ATFieldInfo.assignLabelInfo(label58, RepositoryStatistics.class, RepositoryStatistics.PROPERTYNAME_COLLECTIONS_STORAGE);
            panel10.add(label58, cc.xy(3, 5));

            //---- extentNumber16 ----
            extentNumber16.setColumns(5);
            panel10.add(extentNumber16, cc.xy(5, 5));

            //---- label59 ----
            label59.setText("Reading room");
            label59.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            ATFieldInfo.assignLabelInfo(label59, RepositoryStatistics.class, RepositoryStatistics.PROPERTYNAME_READING_ROOM);
            panel10.add(label59, cc.xy(7, 5));

            //---- extentNumber17 ----
            extentNumber17.setColumns(5);
            panel10.add(extentNumber17, cc.xywh(9, 5, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
        }
        add(panel10, cc.xywh(1, 29, 3, 1));
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JLabel label51;
    public JFormattedTextField yearOfReport;
    private JSeparator separator1;
    private JPanel panel4;
    private JLabel label21;
    public JCheckBox rights;
    public JCheckBox rights4;
    public JCheckBox rights2;
    public JCheckBox rights5;
    public JCheckBox rights3;
    private JSeparator separator2;
    private JPanel panel5;
    private JLabel label22;
    private JLabel label42;
    public JFormattedTextField extentNumber;
    private JLabel label43;
    public JFormattedTextField extentNumber2;
    private JLabel label44;
    public JFormattedTextField extentNumber3;
    private JLabel label45;
    public JFormattedTextField extentNumber4;
    private JSeparator separator3;
    private JPanel panel6;
    private JLabel label23;
    private JLabel label46;
    public JFormattedTextField extentNumber5;
    private JLabel label47;
    public JFormattedTextField extentNumber6;
    private JLabel label48;
    public JFormattedTextField extentNumber7;
    private JLabel label49;
    public JFormattedTextField extentNumber8;
    private JSeparator separator4;
    private JPanel panel7;
    private JLabel label24;
    public JCheckBox rights6;
    public JCheckBox rights7;
    public JCheckBox rights8;
    public JCheckBox rights9;
    public JCheckBox rights10;
    private JSeparator separator5;
    private JLabel label50;
    private JScrollPane scrollPane42;
    public JTextArea title;
    private JPanel panel8;
    private JLabel label40;
    public JFormattedTextField extentNumber9;
    private JSeparator separator6;
    private JPanel panel10;
    private JLabel label26;
    private JLabel label56;
    public JFormattedTextField extentNumber14;
    private JLabel label57;
    public JFormattedTextField extentNumber15;
    private JLabel label60;
    public JFormattedTextField extentNumber18;
    private JLabel label58;
    public JFormattedTextField extentNumber16;
    private JLabel label59;
    public JFormattedTextField extentNumber17;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
