package com.woowacourse.levellog;

import java.io.File;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class DockerTestContainer {

    @Container
    public static DockerComposeContainer dockerComposeContainer =
            new DockerComposeContainer(new File("src/test/resources/docker-compose.yml"));

    static {
        dockerComposeContainer.start();
    }
}
