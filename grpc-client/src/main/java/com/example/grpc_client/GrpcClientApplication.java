package com.example.grpc_client;

import com.example.client_grpc.MessageServiceGrpc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.grpc.client.ChannelBuilderOptions;
import org.springframework.grpc.client.GrpcChannelFactory;
import org.springframework.grpc.client.interceptor.security.BearerTokenAuthenticationInterceptor;
import org.springframework.grpc.client.interceptor.security.TokenRelayTokenSupplier;
import org.springframework.grpc.client.interceptor.security.TokenSupplier;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;

import java.util.List;

@SpringBootApplication
public class GrpcClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(GrpcClientApplication.class, args);
	}

	@Bean // <.>
	BearerTokenAuthenticationInterceptor bearerTokenAuthenticationInterceptor(TokenSupplier supplier) {
		return new BearerTokenAuthenticationInterceptor(supplier);
	}

	@Bean // <.>
	TokenRelayTokenSupplier tokenRelayTokenSupplier(OAuth2AuthorizedClientManager auth2AuthorizedClientManager) {
		return new TokenRelayTokenSupplier(auth2AuthorizedClientManager);
	}

	@Bean
	@Lazy
	MessageServiceGrpc.MessageServiceBlockingStub messageServiceBlockingStub(GrpcChannelFactory factory,
			BearerTokenAuthenticationInterceptor tokenAuthenticationInterceptor) {
		var options = ChannelBuilderOptions.defaults().withInterceptors(List.of(tokenAuthenticationInterceptor)); // <.>
		var channel = factory.createChannel("localhost:8086", options); // <.>
		return MessageServiceGrpc.newBlockingStub(channel); // <.>
	}

}
