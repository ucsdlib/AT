/*
 * Created by JFormDesigner on Wed Mar 18 14:18:10 EDT 2009
 */

package org.archiviststoolkit.plugin.demo1;

import java.awt.*;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import com.jgoodies.binding.PresentationModel;
import org.archiviststoolkit.swing.ATBasicComponentFactory;
import org.archiviststoolkit.model.ArchDescription;

/**
 * @author Nathan Stevens
 */
public class MyEditorPanel1 extends JPanel {
    private PresentationModel detailsModel;

    public MyEditorPanel1(PresentationModel detailsModel) {
        this.detailsModel = detailsModel;
        initComponents();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        button1 = new JButton();
        label1 = new JLabel();
        panel1 = new JPanel();
        label2 = new JLabel();
        textField1 = ATBasicComponentFactory.createTextField(detailsModel.getModel(ArchDescription.PROPERTYNAME_DATE_EXPRESSION),false);
        CellConstraints cc = new CellConstraints();

        //======== this ========
        setLayout(new BorderLayout());

        //---- button1 ----
        button1.setText("Should See Button");
        add(button1, BorderLayout.NORTH);

        //---- label1 ----
        label1.setText("Embedded Editor Panel");
        label1.setFont(new Font("Lucida Grande", Font.BOLD, 24));
        label1.setHorizontalAlignment(SwingConstants.CENTER);
        add(label1, BorderLayout.CENTER);

        //======== panel1 ========
        {
            panel1.setLayout(new FormLayout(
                new ColumnSpec[] {
                    FormFactory.DEFAULT_COLSPEC,
                    FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                    new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                },
                RowSpec.decodeSpecs("default")));

            //---- label2 ----
            label2.setText("Bound Label");
            panel1.add(label2, cc.xy(1, 1));
            panel1.add(textField1, cc.xy(3, 1));
        }
        add(panel1, BorderLayout.SOUTH);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JButton button1;
    private JLabel label1;
    private JPanel panel1;
    private JLabel label2;
    private JTextField textField1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
