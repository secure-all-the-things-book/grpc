package com.example.grpc_client;

import com.example.client_grpc.MessageRequest;
import com.example.client_grpc.MessageServiceGrpc;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@ResponseBody
class MessageClientController {

	private final MessageServiceGrpc.MessageServiceBlockingStub client;

	MessageClientController(MessageServiceGrpc.MessageServiceBlockingStub client) {
		this.client = client;
	}

	@GetMapping("/grpc")
	Map<String, String> message() {
		var request = MessageRequest.newBuilder().setName("Spring fans").build();
		var msg = this.client.message(request); // <.>
		return Map.of("message", msg.getMessage());
	}

}
