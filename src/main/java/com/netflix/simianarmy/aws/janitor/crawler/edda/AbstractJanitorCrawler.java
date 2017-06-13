package com.netflix.simianarmy.aws.janitor.crawler.edda;

import com.netflix.simianarmy.Resource;
import com.netflix.simianarmy.janitor.JanitorCrawler;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public abstract class AbstractJanitorCrawler implements JanitorCrawler {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractJanitorCrawler.class);

    protected void updateResources(List<Resource> resources, Map<String, List<String>> elBtoASGMap) {
        for(Resource resource : resources) {
            List<String> asgList = elBtoASGMap.get(resource.getId());
            if (asgList != null && asgList.size() > 0) {
                resource.setAdditionalField("referencedASGCount", "" + asgList.size());
                String asgStr = StringUtils.join(asgList,",");
                resource.setDescription(resource.getDescription() + ", ASGS=" + asgStr);
                LOGGER.debug(String.format("Resource ELB %s is referenced by ASGs %s", resource.getId(), asgStr));
            } else {
                resource.setAdditionalField("referencedASGCount", "0");
                resource.setDescription(resource.getDescription() + ", ASGS=none");
                LOGGER.debug(String.format("No ASGs found for ELB %s", resource.getId()));
            }
        }
    }
}
