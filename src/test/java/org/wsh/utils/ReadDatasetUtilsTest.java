package org.wsh.utils;

import org.junit.jupiter.api.Test;

import java.io.IOException;

public class ReadDatasetUtilsTest {

    @Test
    void taskMap() {
        try {
            DatasetUtils.taskMap();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void workerMap() {
        try {
            DatasetUtils.workerMap();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void skillMap() {
        try {
            DatasetUtils.skillMap();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
