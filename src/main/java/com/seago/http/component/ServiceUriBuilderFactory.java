package com.seago.http.component;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;

import java.net.URI;
import java.util.Map;

/**
 * 解析微服务名称，获取对应的ip地址和端口号
 * */
public class ServiceUriBuilderFactory implements UriBuilderFactory {

    private String name;

    private BeanFactory beanFactory;

    public ServiceUriBuilderFactory(String name, BeanFactory beanFactory){
        this.name = name;
        this.beanFactory = beanFactory;
    }

    @Override
    public UriBuilder uriString(String uriTemplate) {
        return null;
    }

    @Override
    public UriBuilder builder() {
        return null;
    }

    @Override
    public URI expand(String uriTemplate, Map<String, ?> uriVariables) {
        LoadBalancerClient loadBalancerClient = beanFactory.getBean(LoadBalancerClient.class);
        ServiceInstance instance = loadBalancerClient.choose(name, null);
        String reconstructedUrl = loadBalancerClient.reconstructURI(instance, URI.create(uriTemplate)).toString();
        return URI.create(reconstructedUrl);
    }

    @Override
    public URI expand(String uriTemplate, Object... uriVariables) {
        return null;
    }

}
