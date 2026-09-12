package com.langchain.common.Client.ZKWatcher;

import com.langchain.common.Client.cache.ServiceCache;
import lombok.AllArgsConstructor;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;

@AllArgsConstructor
public class WatchZK {

    //zk客户端
    private CuratorFramework client;
    //服务缓存
    private ServiceCache cache;

    //监听当前节点和子节点的更新，创建，删除
    public void watchToUpdate(String path){
        CuratorCache curatorCache = CuratorCache.build(client,"/");

        curatorCache.listenable().addListener(new CuratorCacheListener() {
            @Override
            public void event(Type type, ChildData childData, ChildData childData1) {
                switch (type.name()){
                    case "NODE_CREATED":
                        String[] pathList = parsePath(childData1);
                        if (pathList.length <= 2){
                            break;
                        }else {
                            cache.addServiceToCache(pathList[1],pathList[2]);
                        }
                        break;
                    case "NODE_CHANGE":
                        if (childData.getData() != null){
                            System.out.println("修改前的数据"+new String(childData.getData()));
                        }else {
                            System.out.println("节点第一次赋值");
                        }
                        String[] pathList1 = parsePath(childData);
                        String[] pathList2 = parsePath(childData1);
                        cache.replaceServiceAddress(pathList1[1],pathList1[2],pathList2[2]);
                        System.out.println("修改后的数据"+new String(childData1.getData()));
                        break;
                    case "NODE_DELETED":
                        String[] pathList_d = parsePath(childData);
                        if (pathList_d.length <= 2) break;
                        else {
                            cache.delete(pathList_d[1],pathList_d[2]);
                        }
                        break;
                    default:
                        break;
                }
            }
        });
        curatorCache.start();
    }

    private String[] parsePath(ChildData childData){
        String path = new String(childData.getData());
        return path.split("/");
    }

}
