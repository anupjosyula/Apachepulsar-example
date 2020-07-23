package com.example.pulsar_admin;

import org.apache.pulsar.client.admin.PulsarAdmin;
import org.apache.pulsar.client.admin.PulsarAdminException;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.url.PulsarURLStreamHandlerFactory;
import org.apache.pulsar.client.api.url.URL;
import org.apache.pulsar.common.policies.data.ClusterData;
import org.apache.pulsar.common.policies.data.Policies;
import org.apache.pulsar.common.policies.data.TenantInfo;
import org.apache.pulsar.shade.com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.pulsar.shade.com.fasterxml.jackson.databind.ObjectMapper;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


public class createTenantsNamespacesTopics {



    public static void main(String[] args) throws PulsarClientException, PulsarAdminException, JsonProcessingException {
        //TO create a cluster


        ObjectMapper mapper = new ObjectMapper();



        PulsarAdmin admin = PulsarAdmin.builder()
                .serviceHttpUrl("http://localhost:8080")
                .build();

        // To create a new cluster
        /*admin.clusters().createCluster("cl1", clusterData);

        System.out.println("New Cluster created");
        System.out.println(" ");
        System.out.println("Details of new CLuster created are"); */

        // to get the list of individiual cluster
        //System.out.println(admin.clusters().getCluster("cl1"));

        // to get the list of all clusters

        //admin.clusters().createCluster("cluster1",new ClusterData("http://localhost:8080","","pulsar://localhost:6650",""));

        // creating tenant wolterkluwers and assinging roles and clusters to it.
        TenantInfo tenantInfo = new TenantInfo();
        Set<String> s = new HashSet<>();
        s.add("admin");
        s.add("role1");
        tenantInfo.setAdminRoles(s);
        Set<String> s1 = new HashSet<>();
        s1.add("standalone");
        s1.add("cluster1");
        tenantInfo.setAllowedClusters(s1);
        System.out.println("roles added are " + s);
        System.out.println("clusters added are" + s1);
        //admin.tenants().createTenant("tenant1", tenantInfo);

        // creating 2 namespaces java and javaGeeks
        admin.namespaces().createNamespace("tenant1/namespace1");
        admin.namespaces().createNamespace("tenant1/namespace2");

        //Creating partitioned and non partitioned topics
        admin.topics().createNonPartitionedTopic("tenant1/namespace1/topic1");
        admin.topics().createNonPartitionedTopic("tenant1/namespace1/topic2");
        admin.topics().createNonPartitionedTopic("tenant1/namespace2/topic3");

        // define number of partitions for the topic
        admin.topics().createPartitionedTopic("tenant1/namespace2/topic4",4);

        // displaying the values set
        System.out.println("List of all clusters is " + admin.clusters().getClusters());
        System.out.println("List of all tenants is " + admin.tenants().getTenants());
        System.out.println("List of all namespaces in tenant1 " + admin.namespaces().getNamespaces("tenant1"));
        System.out.println("List of all topics in namespace1 " + admin.topics().getList("tenant1/namespace1"));
        System.out.println("List of all topics in namespace2 " + admin.topics().getList("tenant1/namespace2"));
        System.out.println("permissions of topic1 are " + admin.topics().getPermissions("persistent://tenant1/namespace1/topic1"));

        // to update the cluster
       // admin.clusters().updateCluster("cl1", clusterData);

        // to delete a cluster
       // admin.clusters().deleteCluster("cl1");

        // to get the list of active brokers in a cluster
       /* System.out.println("List of all brokers in cluster is " + admin.brokers().getActiveBrokers("My-cluster-1"));

        // to get the lsit of all namespaces which are owned and served by a given broker
       System.out.println("List of all brokers in cluster is " + mapper.writeValueAsString(admin.brokers().getOwnedNamespaces("standalone","localhost:8080")));*/



    }
}

