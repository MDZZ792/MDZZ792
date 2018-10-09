package com.test.Etcd;

import com.test.Etcd.Util.EtcdClient;
import com.test.Etcd.Util.EtcdClientException;

import java.io.IOException;
import java.net.URI;
import java.util.UUID;

public class EtcdTest {

    private static String prefix = "/test001-"+ UUID.randomUUID().toString().replaceAll("[-]","");
    private static EtcdClient client = new EtcdClient(URI.create("http://127.0.0.1:2379/"));


    public static void main(String[] args) {
        try {
            client.set(prefix+"/testmesage","hello");

//            client.deleteDir("/unittest-75cb5d49-63f4-48b4-943f-c3ff56fb0511",false);
//            client.delete("/unittest-75cb5d49-63f4-48b4-943f-c3ff56fb0511/message");
//            client.set("/test/testkey","1");
            System.out.println(client.get(prefix+"/testmesage"));
            client.close();
        } catch (EtcdClientException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
