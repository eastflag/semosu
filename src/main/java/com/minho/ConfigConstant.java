package com.minho;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by eastflag on 2016-09-30.
 */
@Component
public class ConfigConstant {
    @Value("${upload.root.folder}")
    public String uploadRootFolder;

    @Value("${upload.question.folder}")
    public String uploadQuestionFolder;

    @Value("${backend.host}")
    public String backendHost;

    @Value("${front.host}")
    public String frontHost;
}
