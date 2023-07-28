package com.seago.http.support;

import com.seago.http.component.AuthorizationQueryFilter;
import com.seago.http.component.ServiceUriBuilderFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.core.publisher.Mono;

import static com.seago.http.constant.HeaderConstant.AUTHORIZATION_HEADER;
import static com.seago.http.constant.HeaderConstant.BEARER_PREFIX;

public class WebClientFactoryBean implements FactoryBean<Object>, BeanFactoryAware {

	private Class<?> type;

	private String name;

	private String url;

	private Class<?> fallbackFactory = void.class;

	private int readTimeoutMillis;

	private int connectTimeoutMillis;

	private BeanFactory beanFactory;

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	@Override
	public Object getObject() {
		WebClient.Builder builder = beanFactory.getBean(WebClient.Builder.class);
		builder.uriBuilderFactory(new ServiceUriBuilderFactory(this.name,beanFactory));
		WebClient webClient = builder.baseUrl(this.name)
				.filter(ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
					// 获取token
					String token = AuthorizationQueryFilter.getToken();
					// 获取请求头中的Authorization字段
					HttpHeaders httpHeaders = HttpHeaders.writableHttpHeaders(clientRequest.headers());
					httpHeaders.add(AUTHORIZATION_HEADER,BEARER_PREFIX+token);
					return Mono.just(clientRequest);
				})).build();
		WebClientAdapter webClientAdapter = WebClientAdapter.forClient(webClient);
		return HttpServiceProxyFactory.builder().clientAdapter(webClientAdapter)
				.build().createClient(this.type);
	}

	@Override
	public Class<?> getObjectType() {
		return type;
	}

	public Class<?> getType() {
		return type;
	}

	public void setType(Class<?> type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Class<?> getFallbackFactory() {
		return fallbackFactory;
	}

	public void setFallbackFactory(Class<?> fallbackFactory) {
		this.fallbackFactory = fallbackFactory;
	}
}
