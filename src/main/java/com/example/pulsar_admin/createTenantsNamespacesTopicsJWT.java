package com.example.pulsar_admin;

import io.jsonwebtoken.*;
import org.apache.pulsar.client.admin.PulsarAdmin;
import org.apache.pulsar.client.admin.PulsarAdminException;
import org.apache.pulsar.client.api.AuthenticationFactory;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.common.naming.NamespaceName;
import org.apache.pulsar.common.policies.data.AuthAction;
import org.apache.pulsar.common.policies.data.Policies;
import org.apache.pulsar.common.policies.data.SubscriptionAuthMode;
import org.apache.pulsar.common.policies.data.TenantInfo;

import javax.xml.bind.DatatypeConverter;
import java.util.HashSet;
import java.util.Set;

public class createTenantsNamespacesTopicsJWT {

    // secret key generated in Pulsar cli
    private static final String SECRET_KEY = "r51KLy5XQdyUc7zhfOcc2kTxFlKUW1KGv5vprHnMg7Q=";

    public static void main(String[] args) throws Exception {

        // token genereated in GenerateJWTusingSecret_Key class
        String generated_token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyb2xlMiIsImlhdCI6MTU5NTM1NDIyOCwiZXhwIjoxNTk1Mzg1NzY0fQ.sB59zTAhSxve2d54J2XtZMMt1sj7sYZWBqI78H6EP3Y";
        PulsarAdmin admin = PulsarAdmin.builder()
                .authentication(AuthenticationFactory.token(generated_token))
                .serviceHttpUrl("http://localhost:8080")
                .build();

        // adding the authactions roles
        Set<AuthAction> authActions = new HashSet<>();
        authActions.add(AuthAction.produce);
        authActions.add(AuthAction.consume);

        // Adding the TenantInfo details
        TenantInfo tenantInfo = new TenantInfo();
        Set<String> tenantset1 = new HashSet<>();
        tenantset1.add("role2");
        tenantInfo.setAdminRoles(tenantset1);
        Set<String> tenantset2 = new HashSet<>();
        tenantset2.add("standalone");
        tenantInfo.setAllowedClusters(tenantset2);

        // Printing out the added details in TenantInfo
        System.out.println("roles added are " + tenantset1);
        System.out.println("clusters added are" + tenantset2);

        // Creating a new Tenant and passing the TenantInfo
        admin.tenants().createTenant("tn4", tenantInfo);

        // Assigning policies to the namespace
        Policies policies = new Policies();

        Set<AuthAction> authActionSet1 = new HashSet<>();
        authActionSet1.add(AuthAction.produce);
        policies.auth_policies.namespace_auth.put("role2",authActionSet1);

        Set<AuthAction> authActionSet2 = new HashSet<>();
        authActionSet2.add(AuthAction.consume);
        policies.auth_policies.namespace_auth.put("role2",authActionSet2);



       System.out.println("polices" + policies.toString());
        //System.out.println("polices authpolicies namespace:" + p1 );


        // Creating a namespace with the policies mentioned
        admin.namespaces().createNamespace("tn4/ns1", policies);

        // Granting permissions on the namespace
        admin.namespaces().grantPermissionOnNamespace("tn4/ns1", "role2", authActions);
        //Creating Non partioned topics
        admin.topics().createNonPartitionedTopic("tn4/ns1/tp1");
        // Grant permissions to Topic
        admin.topics().grantPermission("tn4/ns1/tp1","role2", authActions);

        // Display the assigned values and roles
        System.out.println("Policies are " + admin.namespaces().getPolicies("tn4/ns1"));
        System.out.println(admin.getClientConfigData().getAuthentication().getAuthData());
        System.out.println("Is client having authentication " + admin.getClientConfigData().getAuthentication().getAuthData().hasDataForHttp());
        System.out.println("permissions of namespace are" + admin.namespaces().getPermissions("tn4/ns1"));
        System.out.println("permissions of topic are" + admin.topics().getPermissions("tn4/ns1/tp1"));
        System.out.println(admin.getClientConfigData().getAuthentication().getAuthData().getHttpAuthType());
        System.out.println(" Http headers are " + admin.getClientConfigData().getAuthentication().getAuthData().getHttpHeaders());
        System.out.println("namespace permission is available "+admin.namespaces().getPermissions("tn4/ns1").containsKey("role2"));
        System.out.println("topic permission is available "+admin.topics().getPermissions("tn4/ns1/tp1").containsKey("role2"));

        // Parsing the JWT token generated
        try {
            Jws<Claims> signedJwt = Jwts.parserBuilder().setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY)).build().parseClaimsJws(generated_token);
            System.out.println("checking if token subject and assigned roles are matching" );
            System.out.println("subject of Jwt token is " + signedJwt.getBody().getSubject());
            System.out.println("Get roles and corrwaponding permissions assigned " +admin.topics().getPermissions("tn4/ns1/tp1"));

        }
        catch(ExpiredJwtException e)
        {
            e.printStackTrace();
            System.out.println("the token is expired");
        }
        catch(MalformedJwtException e)
        {
            e.printStackTrace();
            System.out.println("the token is malformed and not valid");

        }


    }
}
