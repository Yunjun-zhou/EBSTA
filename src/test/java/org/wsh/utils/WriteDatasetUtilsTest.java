package org.wsh.utils;

import org.junit.jupiter.api.Test;

import java.io.IOException;

public class WriteDatasetUtilsTest {
    @Test
    void writeDatasets() {
        try {
            DatasetUtils.writeSkillDatasets();
            DatasetUtils.writeTaskDatasets();
            DatasetUtils.writeWorkerDatasets();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//    @Test
//    void writeSkillDatasets() {
//        DatasetUtils.writeSkillDatasets();
//    }

//    @Test
//    void writeTaskDatasets() {
//        try {
//            DatasetUtils.writeTaskDatasets();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

}
