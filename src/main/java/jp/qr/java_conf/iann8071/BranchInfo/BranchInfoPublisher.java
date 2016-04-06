package jp.qr.java_conf.iann8071.BranchInfo;

import hudson.FilePath;
import hudson.Launcher;
import hudson.model.*;
import hudson.model.listeners.RunListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;
import jenkins.model.lazy.AbstractLazyLoadRunMap;
import jenkins.tasks.SimpleBuildStep;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by iann8071 on 2016/04/05.
 */
public class BranchInfoPublisher extends Recorder {

    PrintStream logger;

    private String branchName;

    @DataBoundConstructor
    public BranchInfoPublisher() {

    }

    public String getBranchName() {
        return branchName;
    }

    @DataBoundSetter
    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    @Override
    public boolean perform(AbstractBuild build, Launcher launcher, BuildListener listener) {
        logger = listener.getLogger();
        Job job = build.getParent();
        String branchName = (String) (build.getBuildVariables().get(this.branchName.replace("$","").replace("{","").replace("}","")));
        setSameBranchAsPreviousBuild(build, branchName);
        build.addAction(new BranchInfoAction(new BranchInfo((branchName))));
        printAllBuilds(build);
        return true;
    }

    private void setSameBranchAsPreviousBuild(AbstractBuild owner, String branchName) {
        ArrayList<AbstractBuild> builds = new ArrayList<AbstractBuild>();
        for(AbstractBuild build = owner; true; build = build.getPreviousBuild()) {
            BranchInfoAction resultAction = build.getAction(BranchInfoAction.class);
            if(resultAction != null) {
                BranchInfo result = resultAction.getResult();
                if(result != null && result.getBranchName() != null && result.getBranchName().equals(branchName)) {
                    logger.println("same branch is " + result.getBranchName());
                    logger.println("build is " + build.getNumber());
                } else {
                    logger.println("different branch is " + result.getBranchName());
                    logger.println("build is " + build.getNumber());
                    builds.add(build);
                }
            }

            if(build.getPreviousBuild() == null)
                break;
        }
        setPreviousBuilds(builds);
    }

    private void printAllBuilds(AbstractBuild owner) {
        for(AbstractBuild build = owner; build.getPreviousBuild() != null; build = build.getPreviousBuild()) {
            BranchInfoAction resultAction = build.getAction(BranchInfoAction.class);
            if(resultAction != null) {
                BranchInfo result = resultAction.getResult();
                if(result != null && result.getBranchName() != null) {
                    logger.println(result.getBranchName());
                }
            }
        }
    }

    private void setPreviousBuilds(ArrayList<AbstractBuild> builds) {
        for(int i = 0; i < builds.size(); i++) {
            AbstractBuild build = builds.get(i);
            build.getRunMixIn().dropLinks();
        }
    }

    private void setPreviousBuild(AbstractBuild build, AbstractBuild previousBuild) {
        try {
            Field previousBuildField = build.getClass().getSuperclass().getSuperclass().getDeclaredField("buildNavigator");
            previousBuildField.setAccessible(true);
            logger.println("previous Build number is " + previousBuildField.get(build).getClass());
            previousBuildField.set(build, previousBuild);
            logger.println("previous Build number is " + previousBuildField.get(build));
            logger.println("previous Build number is " + build.getPreviousBuild());

        } catch (IllegalAccessException e) {
            logger.println(e.getStackTrace().toString());
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @Override
    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }

    public BuildStepDescriptor<Publisher> getDescriptor() {
        return new DescriptorImpl();
    }
}
