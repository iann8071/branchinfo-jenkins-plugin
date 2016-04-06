package jp.qr.java_conf.iann8071.BranchInfo;

import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.model.Descriptor;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Publisher;
import org.kohsuke.stapler.StaplerRequest;

/**
 * Created by iann8071 on 2016/04/05.
 */
@Extension
public final class DescriptorImpl extends BuildStepDescriptor<Publisher> {
    public DescriptorImpl() {
        super(BranchInfoPublisher.class);
    }

    @Override
    public boolean isApplicable(Class<? extends AbstractProject> aClass) {
        return true;
    }

    @Override
    public String getDisplayName() {
        return "BranchInfo reports";
    }
}
