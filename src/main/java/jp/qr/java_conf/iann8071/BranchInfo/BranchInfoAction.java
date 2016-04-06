package jp.qr.java_conf.iann8071.BranchInfo;

import hudson.model.Action;
import org.kohsuke.stapler.export.Exported;
import org.kohsuke.stapler.export.ExportedBean;

/**
 * Created by iann8071 on 2016/04/05.
 */

public class BranchInfoAction implements Action {

    private BranchInfo result;

    public BranchInfoAction(BranchInfo result) {
        this.result = result;
    }

    public BranchInfo getResult(){
        return result;
    }

    @Override
    public String getIconFileName() {
        return null;
    }

    @Override
    public String getDisplayName() {
        return "test";
    }

    @Override
    public String getUrlName() {
        return null;
    }
}
