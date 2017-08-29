package com.cnblogs.m24.learnzookeeper.curatorapi;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.CuratorTempFramework;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class CuratorListenerTest2 {
    public static void main(String args[]) {
        CuratorFramework client = getClient();
        String path = "/test";

        try {
            CuratorListener listener = new CuratorListener() {
                public void eventReceived(CuratorFramework client, CuratorEvent event) throws Exception {
                    System.out.println("监听事件触发，event内容为：" + event);
                }
            };
            client.getCuratorListenable().addListener(listener);
            // 异步获取节点数据
            client.getData().inBackground().forPath(path);
            // 变更节点内容
            client.setData().forPath(path,"123".getBytes());

            Thread.sleep(Integer.MAX_VALUE);
        } catch (Exception e) {
            e.printStackTrace();
            client.close();
        } finally {
            client.close();
        }
    }

    private static CuratorFramework getClient() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString("localhost:2181")
                .retryPolicy(retryPolicy)
                .sessionTimeoutMs(6000)
                .connectionTimeoutMs(3000)
                //.namespace("demo")
                .build();
        client.start();
        return client;
    }
}
