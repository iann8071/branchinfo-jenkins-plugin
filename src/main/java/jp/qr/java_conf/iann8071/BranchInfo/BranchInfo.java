package jp.qr.java_conf.iann8071.BranchInfo;

import hudson.model.ModelObject;
import org.kohsuke.stapler.export.Exported;
import org.kohsuke.stapler.export.ExportedBean;

import java.io.Serializable;

/**
 * Created by iann8071 on 2016/04/06.
 */
public class BranchInfo {
    private String branchName;

    public BranchInfo(String branchName) {
        this.branchName = branchName;
    }

    public String getBranchName(){
        return branchName;
    }

    public String getDisplayName() {
        return "test";
    }

    public String toString(){
        return branchName;
    }
}
