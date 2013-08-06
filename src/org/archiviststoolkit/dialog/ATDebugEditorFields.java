/*
 * Created by JFormDesigner on Fri Feb 20 12:03:46 EST 2009
 */

package org.archiviststoolkit.dialog;

import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import org.archiviststoolkit.mydomain.DomainEditorFields;
import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.model.ArchDescription;
import org.archiviststoolkit.model.AccessionsResourcesCommon;
import org.archiviststoolkit.model.ResourcesCommon;
import org.archiviststoolkit.swing.ATBasicComponentFactory;
import org.archiviststoolkit.swing.InfiniteProgressPanel;

import java.awt.*;

/**
 * @author Nathan Stevens
 */
public class ATDebugEditorFields extends DomainEditorFields {
    public ATDebugEditorFields() {
        super();
        initComponents();
    }

    public Component getInitialFocusComponent() {
        return null;
    }

    // Method to set the model;
    public void setModel(DomainObject domainObject, InfiniteProgressPanel progressPanel) {
        super.setModel(domainObject, null);

        if(domainObject instanceof ResourcesCommon) {
            ResourcesCommon rc = (ResourcesCommon)domainObject;
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        label1 = new JLabel();
        textField1 = ATBasicComponentFactory.createISODateField(detailsModel.getModel(ArchDescription.PROPERTYNAME_ISODATE_BEGIN));
        textField3 = ATBasicComponentFactory.createISODateField(detailsModel.getModel(ArchDescription.PROPERTYNAME_ISODATE_END));
        label2 = new JLabel();
        textField2 = ATBasicComponentFactory.createISODateField(detailsModel.getModel(AccessionsResourcesCommon.PROPERTYNAME_ISOBULK_DATE_BEGIN));
        textField4 = ATBasicComponentFactory.createISODateField(detailsModel.getModel(AccessionsResourcesCommon.PROPERTYNAME_ISOBULK_DATE_END));
        CellConstraints cc = new CellConstraints();

        //======== this ========
        setLayout(new FormLayout(
            new ColumnSpec[] {
                new ColumnSpec(Sizes.dluX(79)),
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

        //---- label1 ----
        label1.setText("ISO Date Begin/End");
        add(label1, cc.xy(1, 1));

        //---- textField1 ----
        textField1.setColumns(10);
        add(textField1, cc.xy(3, 1));

        //---- textField3 ----
        textField3.setColumns(10);
        add(textField3, cc.xy(5, 1));

        //---- label2 ----
        label2.setText("ISO Bluk Date Begin/End");
        add(label2, cc.xy(1, 5));

        //---- textField2 ----
        textField2.setColumns(10);
        add(textField2, cc.xy(3, 5));

        //---- textField4 ----
        textField4.setColumns(10);
        add(textField4, cc.xy(5, 5));
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JLabel label1;
    private JTextField textField1;
    private JTextField textField3;
    private JLabel label2;
    private JTextField textField2;
    private JTextField textField4;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
