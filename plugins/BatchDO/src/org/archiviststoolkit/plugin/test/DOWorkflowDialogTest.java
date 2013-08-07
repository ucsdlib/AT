package org.archiviststoolkit.plugin.test;

import org.archiviststoolkit.plugin.DOWorkflowDialog;
import org.archiviststoolkit.plugin.utils.ImportExportUtils;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Created by IntelliJ IDEA.
 * User: nathan
 * Date: 4/17/12
 * Time: 10:16 AM
 * To change this template use File | Settings | File Templates.
 */
public class DOWorkflowDialogTest {
    private DOWorkflowDialog doWorkflowDialog = null;

    @Test
    public void testGetSafeTitle() throws Exception {
        String shortTitle = "Judson\nCentennial (1890-1990)-Dance<tag> Theater-A Symposium";
        String expectedShortTitle = "Judson Centennial (1890-1990)-Dance Theater-A Symposium";
        assertEquals(expectedShortTitle, ImportExportUtils.getSafeTitle(shortTitle));

        String longTitle = "Judson Centennial (1890-1990)-Dance Theater-A Symposium on Its Place in America Dance 5/11/1990; Lucinda Childs and David Gordon, April 19-21";
        String expectedLongTitle = "Judson Centennial (1890-1990)-Dance Theater-A Symposium on It...ds and David Gordon, April 19-21";
        assertEquals(expectedLongTitle, ImportExportUtils.getSafeTitle(longTitle));
    }
}
