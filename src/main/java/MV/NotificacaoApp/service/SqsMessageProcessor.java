package MV.NotificacaoApp.service;

import MV.NotificacaoApp.model.MessageForm;
import MV.NotificacaoApp.util.S3Util;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class SqsMessageProcessor {

    private static final int MAX_MESSAGES = 10;
    private static final int WAIT_TIME_SECONDS = 5;

    @Autowired
    private SqsClient sqs;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private S3Util s3Util;

    @Value("${sqs.queue.url}")
    private String QUEUE_URL;

    @Scheduled(fixedDelay = 5000)
    public void processMessages() {
        ReceiveMessageRequest receiveRequest = ReceiveMessageRequest.builder()
                .queueUrl(QUEUE_URL)
                .maxNumberOfMessages(MAX_MESSAGES)
                .waitTimeSeconds(WAIT_TIME_SECONDS)
                .build();

        List<Message> messages = sqs.receiveMessage(receiveRequest).messages();

        for (Message message : messages) {
            processMessage(message);
        }
    }

    private void processMessage(Message message) {
        try {
            log.info("Processando mensagem: {}", message.body());

            MessageForm messageForm = objectMapper.readValue(message.body(), MessageForm.class);
            log.info("Mensagem formatada: {}", messageForm);

            log.info("Adicionando metadados: {}", messageForm);
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("timestamp_received", Instant.now().toString());
            metadata.put("source", QUEUE_URL);
            metadata.put("message_id", message.messageId());

            Map<String, Object> dataToStore = new HashMap<>();
            dataToStore.put("metadata", metadata);
            dataToStore.put("message", messageForm);

            s3Util.saveToS3(dataToStore);
            deleteMessage(message);

        } catch (Exception e) {
            log.error("Erro ao processar mensagem ID {}: {}", message.messageId(), e.getMessage());
            log.debug("Mensagem: {}", message.body(), e);
            deleteMessage(message);
        }

    }

    private void deleteMessage(Message message) {
        DeleteMessageRequest deleteRequest = DeleteMessageRequest.builder()
                .queueUrl(QUEUE_URL)
                .receiptHandle(message.receiptHandle())
                .build();

        sqs.deleteMessage(deleteRequest);
        log.info("Mensagem removida da fila: {}", message.messageId());
    }
}
