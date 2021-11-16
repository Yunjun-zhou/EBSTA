package org.wsh.utils;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;


class DatasetUtilsTest {

    @Test
    void test(){
        System.out.println(((char)('a'+1)));
    }

    @Test
    void writeTaskDatasets() {
        try {
            DatasetUtils.writeTaskDatasets();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void writeSkillDatasets() {
        DatasetUtils.writeSkillDatasets();
    }

    @Test
    void writeWorkerDatasets() {
        try {
            DatasetUtils.writeWorkerDatasets();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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