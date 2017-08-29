package com.cnblogs.m24.learnzookeeper.clientapi;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

public class TestSession implements Watcher {
    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String args[]) throws IOException {
        Long startTime = new Date().getTime();
        ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181", 5000, new TestSession());
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("创建连接花费时间：" + (new Date().getTime() - startTime) + "ms");
        System.out.println("连接状态：" + zooKeeper.getState());
    }

    public void process(WatchedEvent event) {
        System.out.println("Receive watcher event:" + event);
        if (Event.KeeperState.SyncConnected == event.getState()) {
            countDownLatch.countDown();
        }
    }
}
