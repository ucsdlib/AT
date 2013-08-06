package org.archiviststoolkit.maintenance.upgrades;

import org.netbeans.spi.wizard.DeferredWizardResult;
import org.archiviststoolkit.hibernate.SessionFactory;
import org.archiviststoolkit.model.Constants;
import org.archiviststoolkit.util.DatabaseConnectionUtils;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

public class UpgradeTo_1_0_36 extends Upgrade {

    protected boolean doUpgrade(Connection conn, int initialStep, int numberOfSteps, DeferredWizardResult.ResultProgressHandle progress) {

      return true;
            // everything is ok
 	}

	protected int getNumberOfSteps() {
        return 0;
    }

    protected boolean upgradeNeeded(Connection conn) throws SQLException {
        return Constants.compareVersions("1.0.36", DatabaseConnectionUtils.getDatabaseVersionInfo(conn)) == Constants.VERSION_GREATER;
    }

    protected boolean runDetermineSequenceNumbers() {
        return true;
    }

	protected boolean runLoadLookupLists() {
		return true;
	}
}
