package org.wsh.utils;

import cn.hutool.core.util.RandomUtil;
import org.wsh.datastructure.Location;
import org.wsh.datastructure.Skill;
import org.wsh.datastructure.Task;
import org.wsh.datastructure.Worker;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class DatasetUtils {
    static final String fileDir = System.getProperty("user.dir") + "/src/main/resources/";
    static final String taskFile = "task.txt";
    static final String workerFile = "worker.txt";
    static final String skillFile = "skill.txt";

    // 数量
    static final int k_task = 1000;
    static final int k_work = 5000;
    static final int k_skill = 10;

    static final Double lng_max = 0.5;
    static final Double lng_min = 0.0;
    static final Double lat_max = 0.5;
    static final Double lat_min = 0.0;
    static final Double price_max = 20d;
    static final Double price_min = 5d;
    static final int range_max = 400;
    static final int range_min = 300;
    static final int range_fixed = 300;
    static final int skill_min = 1;
    static final int skill_max = 5;

    public static void writeTaskDatasets() throws IOException {
        Map<Integer, Skill> skillsMap = skillMap();
        String fileName = fileDir + taskFile;
        try {
            //打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            FileWriter writer = new FileWriter(fileName, true);
            for (int i = 0; i < k_task; i++) {
                double lng = RandomUtil.randomDouble(lng_min, lng_max);
                double lat = RandomUtil.randomDouble(lat_min, lat_max);
                int range_extra = RandomUtil.randomInt(range_min, range_max);
                int count = RandomUtil.randomInt(skill_min, skill_max);
                List<Integer> skill = new ArrayList<>(RandomUtil.randomEleList(skillsMap.values().stream().map(Skill::getId).collect(Collectors.toList()), count));
                writer.write(i + "," + lng + "," + lat + "," + range_fixed + "," + range_extra + "," + range_extra + "," + skill.stream().map(String::valueOf).collect(Collectors.joining("-")) + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeSkillDatasets() {
        String fileName = fileDir + skillFile;
        try {
            //打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            FileWriter writer = new FileWriter(fileName, true);
            for (int i = 0; i < k_skill; i++) {
                writer.write(i + "," + ((char) ('a' + i)) + "," + RandomUtil.randomDouble(price_min, price_max) + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeWorkerDatasets() throws IOException {
        String fileName = fileDir + workerFile;
        Map<Integer, Skill> skillsMap = skillMap();
        try {
            //打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            FileWriter writer = new FileWriter(fileName, true);
            for (int i = 0; i < k_work; i++) {
                double lng = RandomUtil.randomDouble(lng_min, lng_max);
                double lat = RandomUtil.randomDouble(lat_min, lat_max);
                int count = RandomUtil.randomInt(skill_min, skill_max);
                List<Integer> skill = new ArrayList<>(RandomUtil.randomEleList(skillsMap.values().stream().map(Skill::getId).collect(Collectors.toList()), count));
                writer.write(i + "," + lng + "," + lat + "," + skill.stream().map(String::valueOf).collect(Collectors.joining("-")) + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从文件读取task数据
     *
     * @return
     */
    public static Map<Integer, Task> taskMap() throws IOException {
        Map<Integer, Task> taskHashMap = new HashMap<>();
        String filePath = fileDir + taskFile;
        BufferedReader in = null;
        Map<Integer, Skill> skillsMap = skillMap();
        try {
            in = new BufferedReader(new FileReader(filePath));
            String str;
            String[] strings;
            while ((str = in.readLine()) != null) {
                strings = str.split(",");
                int id = Integer.parseInt(strings[0]);
                List<Skill> skills = Arrays.stream(strings[6].split("-")).map(t -> skillsMap.get(Integer.parseInt(t))).collect(Collectors.toList());
                taskHashMap.put(id,
                        new Task(id,new Location(Double.parseDouble(strings[1]),Double.parseDouble(strings[2])),
                                Double.parseDouble(strings[3]),Double.parseDouble(strings[4]), skills ));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            in.close();
        }
        return taskHashMap;
    }

    /**
     * 从文件读取worker数据
     *
     * @return
     */
    public static Map<Integer, Worker> workerMap() throws IOException {
        Map<Integer, Worker> workerMap = new HashMap<>();
        String filePath = fileDir + workerFile;

        Map<Integer, Skill> skillsMap = skillMap();
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(filePath));
            String str;
            String[] strings;
            while ((str = in.readLine()) != null) {
                strings = str.split(",");
                int id = Integer.parseInt(strings[0]);
                List<Skill> skills = Arrays.stream(strings[3].split("-")).map(t -> skillsMap.get(Integer.parseInt(t))).collect(Collectors.toList());
                workerMap.put(id,
                        new Worker(id, new Location(Double.parseDouble(strings[1]), Double.parseDouble(strings[2])), skills));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            in.close();
        }
        return workerMap;
    }

    /**
     * 从文件读取skill数据
     */
    public static Map<Integer, Skill> skillMap() throws IOException {
        Map<Integer, Skill> skillMap = new HashMap<>();
        String filePath = fileDir + skillFile;
        BufferedReader in = null;

        try {
            in = new BufferedReader(new FileReader(filePath));
            String str;
            String[] strings;
            while ((str = in.readLine()) != null) {
                strings = str.split(",");
                int id = Integer.parseInt(strings[0]);
                skillMap.put(id, new Skill(id, strings[1], Double.parseDouble(strings[2])));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            in.close();
        }
        return skillMap;
    }
}
