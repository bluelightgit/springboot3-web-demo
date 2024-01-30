package com.mySpring.demo.Configuration;


import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;

@Configuration
public class ElasticSearchConfig extends ElasticsearchConfiguration {
    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                .connectedTo("localhost:9200")
                .usingSsl("02D3523FE27FBB1F37E42B43D93A9D8A16A3B7701820B60A7DEF1C52EACD98AB") //add the generated sha-256 fingerprint $openssl x509 -in /usr/share/elasticsearch/config/certs/http_ca.crt -sha256 -fingerprint | grep SHA256 | sed 's/://g'
                .withBasicAuth("elastic", "d=ABgcKOKDxmuSWf4_T_") //add your username and password
                .build();
    }
}
