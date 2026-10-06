package com.example.search.config;
import org.apache.hc.core5.http.HttpHost;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.transport.rest_client.RestClientTransport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.opensearch.client.RestClient;
import org.opensearch.client.transport.OpenSearchTransport;
import org.opensearch.client.json.jackson.JacksonJsonpMapper;

@Configuration
public class OpenSearchConfig {
    @Bean
    public OpenSearchClient openSearchClient(){
        RestClient restClient=RestClient.builder(new HttpHost( "http","localhost",9200)).build();
        OpenSearchTransport transport = new RestClientTransport(restClient,new JacksonJsonpMapper());
        return new OpenSearchClient(transport);
    }
}
