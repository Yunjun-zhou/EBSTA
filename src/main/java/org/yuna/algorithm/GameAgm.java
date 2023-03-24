package org.wsh.algorithm;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.RandomUtil;
import org.apache.commons.lang3.SerializationUtils;
import org.wsh.datastructure.MatchingPair;
import org.wsh.datastructure.Skill;
import org.wsh.datastructure.Task;
import org.wsh.datastructure.Worker;
import org.wsh.utils.DistanceUtils;
import org.wsh.utils.ExtraCostUtils;
import org.wsh.utils.PlatformRevenueUtils;

import java.util.*;
import java.util.stream.Collectors;

public class GameAgm {

    public List<MatchingPair> calculate(List<Task> tasks, List<Worker> workers) {
        /** 获取当前系统时间*/
        long startTime =  System.currentTimeMillis();
        List<Task> tasks_p = new ArrayList<>();
        List<Worker> workers_p = new ArrayList<>();
        tasks.forEach(task -> tasks_p.add(SerializationUtils.clone(task)));      //克隆出T'
        workers.forEach(worker -> workers_p.add(SerializationUtils.clone(worker)));     //克隆出W'
        List<MatchingPair> matchingPairs = randomA(tasks_p, workers_p);      //先用随机算法算出匹配对

//        System.out.println("----第一步----");
//        System.out.println(matchingPairs.size());
//        System.out.println(PlatformRevenueUtils.getVwsPriceSum(matchingPairs));
//        System.out.println("-------------");

        List<MatchingPair> matchingPairs_p = new ArrayList<>();    //初始化M'
        int epoll = 0;
        do {
            matchingPairs_p.clear();
            matchingPairs.forEach(m -> matchingPairs_p.add(SerializationUtils.clone(m)));    //M'记录上一轮的匹配对
            workers.forEach(worker -> {
                List<Task> tasks_star = tasks.stream().filter(t -> worker.getSList().stream().anyMatch(s -> t.getSList().contains(s)) && DistanceUtils.getDistance(t.getL(), worker.getL()) < t.getR()+t.getB()).collect(Collectors.toList());   //得到包含w技能的任务集合
                Task task_star = getPriceIncrease(tasks_star, worker, matchingPairs);
//                if (task_star == null) {
//                    System.out.println("null");
//                }
                Task task_star_p = SerializationUtils.clone(task_star);      //将t*的值记录到t*'
                if (task_star != null) {   //如果能找到t*
                    if (!matchingPairs.stream().map(MatchingPair::getTask).collect(Collectors.toList()).contains(task_star)) {   //如果t*没有匹配
                        List<Worker> vws = new ArrayList<>();     //初始化一个有效工人集合
                        extracted(workers_p, matchingPairs, worker, task_star_p, vws);
                    } else {                   //如果t*匹配了
                        MatchingPair matchingPair = matchingPairs.stream().filter(m -> m.getTask().getId() == task_star.getId()).findFirst().get();   //如果t*已经有匹配
                        workers_p.addAll(matchingPair.getWorkers());
                        matchingPairs.remove(matchingPair);
                        List<Worker> vws = new ArrayList<>();
                        extracted(workers_p, matchingPairs, worker, task_star_p, vws);
                    }
                }
            });
//            System.out.println(epoll++);
//            System.out.println(matchingPairs.size());
//            System.out.println("总利润"+PlatformRevenueUtils.getVwsPriceSum(matchingPairs));
//            System.out.println(PlatformRevenueUtils.getVwsPriceSum(matchingPairs) - PlatformRevenueUtils.getVwsPriceSum(matchingPairs_p));
        } while (Math.abs(PlatformRevenueUtils.getVwsPriceSum(matchingPairs) - PlatformRevenueUtils.getVwsPriceSum(matchingPairs_p)) > 0.1);

        /** 获取当前的系统时间，与初始时间相减就是程序运行的毫秒数，除以1000就是秒数*/
        long endTime =  System.currentTimeMillis();
        long usedTime = (endTime-startTime);
        System.out.println("算法运行时间："+usedTime);
        return matchingPairs;
    }

    private void extracted(List<Worker> workers_p, List<MatchingPair> matchingPairs, Worker worker, Task task_star_p, List<Worker> vws) {
        vws.add(worker);
        MatchingPair matchingPair = new MatchingPair(task_star_p, null);
        if (workers_p.contains(worker)) {//我写的，如果未匹配工人集合中包含了这个工人，就把他移除
            workers_p.remove(worker);
        } else{//我写的
            //matchingPair.getWorkers().stream().filter(worker -> worker.getId() == v).findFirst().get(),
            MatchingPair matchingPair_p = matchingPairs.stream().filter(m -> m.getWorkers().contains(worker)).findFirst().get();//找到工人原本所在的匹配对
            workers_p.addAll(matchingPair_p.getWorkers());//将匹配对中的工人还原到未匹配工人集合
            matchingPairs.remove(matchingPair_p);//将匹配对移除
        }
        List<Skill> workerskillList = worker.getSList().stream().filter(s -> task_star_p.getSList().contains(s)).collect(Collectors.toList());   //得到工人和任务重合的技能集合
        //System.out.println(skillList1);
        task_star_p.getSList().removeAll(worker.getSList().stream().filter(s -> task_star_p.getSList().contains(s)).collect(Collectors.toList()));  //将任务t*'中的工人能完成的技能删除
        task_star_p.setB(task_star_p.getB() - ExtraCostUtils.getExtraCost(task_star_p, worker));        //改变任务t*'的额外预算
        workerskillList.stream().forEach(s -> matchingPair.record(s.getId(), worker.getId()));    //我写的


        while (!task_star_p.getSList().isEmpty()) {   //如果任务的所有技能还未全部被完成
            double Rt = task_star_p.getR() + task_star_p.getB();
            List<Worker> W_star = workers_p.stream().filter(w -> task_star_p.getSList().stream().anyMatch(s -> w.getSList().contains(s)) && DistanceUtils.getDistance(task_star_p.getL(), w.getL()) < Rt).collect(Collectors.toList());   //得到包含t*技能的工人集合
            if (CollectionUtil.isEmpty(W_star)) {   //如果W*为空集
                workers_p.addAll(vws);
                vws = new ArrayList<>();
                break;
            }
            Worker w = W_star.stream().max(Comparator.comparingDouble(cw -> PlatformRevenueUtils.getWorkerMaxRevenue(task_star_p, cw))).get();
            vws.add(w);
            task_star_p.setB(task_star_p.getB() - ExtraCostUtils.getExtraCost(task_star_p, w));
            List<Skill> skillList = w.getSList().stream().filter(s -> task_star_p.getSList().contains(s)).collect(Collectors.toList());   //任务t*'和工人w的交集技能
            skillList.stream().forEach(s -> matchingPair.record(s.getId(), w.getId()));
            task_star_p.getSList().removeAll(skillList);
            workers_p.remove(w);
        }
        if (!vws.isEmpty()) {
            matchingPair.setWorkers(vws);
            matchingPair.setVwsPrice(PlatformRevenueUtils.getPlatformRevenue(matchingPair));
            matchingPairs.add(matchingPair);
            //System.out.println(matchingPair);
        }
    }

    public static Task getPriceIncrease(List<Task> tasks, Worker worker, List<MatchingPair> M) {
//        System.out.println("hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
//        System.out.println(tasks.stream().max(Comparator.comparingDouble(task -> {
//            //MatchingPair m = M.stream().filter(t -> task.getId() == t.getTask().getId()).findFirst().orElse(null);
//            MatchingPair m = M.stream().filter(cm -> cm.getWorkers().stream().map(Worker::getId).collect(Collectors.toList()).contains(worker.getId())).findFirst().orElse(null);
//            return PlatformRevenueUtils.getWorkerMaxRevenue(task, worker) - (m != null ? PlatformRevenueUtils.getWorkerRealRevenue(m, worker) : 0d);
//        })).orElse(null));
        return tasks.stream().max(Comparator.comparingDouble(task -> {
            //MatchingPair m = M.stream().filter(t -> task.getId() == t.getTask().getId()).findFirst().orElse(null);
            MatchingPair m = M.stream().filter(cm -> cm.getWorkers().stream().map(Worker::getId).collect(Collectors.toList()).contains(worker.getId())).findFirst().orElse(null);
//            if (m!=null){
//                System.out.println("新收益"+PlatformRevenueUtils.getWorkerMaxRevenue(task, worker)+"之前收益"+PlatformRevenueUtils.getWorkerRealRevenue(m, worker));
//            } else {
//                System.out.println("新收益"+PlatformRevenueUtils.getWorkerMaxRevenue(task, worker));
//            }
//            System.out.println(task);
//            System.out.println(PlatformRevenueUtils.getWorkerMaxRevenue(task, worker) - (m != null ? PlatformRevenueUtils.getWorkerRealRevenue(m, worker) : 0d));
            return PlatformRevenueUtils.getWorkerMaxRevenue(task, worker) - (m != null ? PlatformRevenueUtils.getWorkerRealRevenue(m, worker) : 0d);
        })).orElse(null);
    }

    private List<MatchingPair> randomA(List<Task> tasks, List<Worker> workers) {
        List<MatchingPair> m = new ArrayList<>();
        tasks.forEach(t -> {
            List<Worker> vws = new ArrayList<>();
            MatchingPair matchingPair = new MatchingPair(t, null);
            for (Skill s : t.getSList()) {
                double Rt = t.getR() + t.getB();
                List<Worker> W_star = workers.stream().filter(w -> w.getSList().contains(s) && DistanceUtils.getDistance(t.getL(), w.getL()) < Rt).collect(Collectors.toList());
                if (CollectionUtil.isEmpty(W_star)) {
                    workers.addAll(vws);
                    vws = new ArrayList<>();
                    break;
                }
                Worker w = W_star.stream().max(Comparator.comparingDouble(cw -> PlatformRevenueUtils.getPlatformRevenue(t, cw, s))).get();
                vws.add(w);
                matchingPair.record(s.getId(), w.getId());
                t.setB(t.getB() - ExtraCostUtils.getExtraCost(t, w));
                workers.remove(w);
            }
            if (!vws.isEmpty()) {
                matchingPair.setWorkers(vws);
                matchingPair.setVwsPrice(PlatformRevenueUtils.getPlatformRevenue(matchingPair));
                //System.out.println(PlatformRevenueUtils.getPlatformRevenue(matchingPair));
                m.add(matchingPair);
            }
        });
        return m;
    }
}
