package MV.NotificacaoApp.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
@Slf4j
public class S3Util {

    @Autowired
    private S3Client s3Client;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${s3.bucket-name}")
    private String  BUCKET_NAME;

    public void saveToS3(Map<String, Object> dataToStore) {
        try {
            log.info("Convertendo dados e metadados para String JSON");
            String jsonData = objectMapper.writeValueAsString(dataToStore);
            log.info("Dados convertidos para JSON: {}", jsonData);

            // Obter chave única (por exemplo, e-mail ou message_id dos metadados)
            String key = ((Map<String, Object>) dataToStore.get("metadata")).get("message_id").toString();

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(key)
                    .contentType("application/json")
                    .build();

            log.info("Enviando dados para o S3 - Bucket: {}, Key: {}", putObjectRequest.bucket(), putObjectRequest.key());

            s3Client.putObject(putObjectRequest, RequestBody.fromString(jsonData, StandardCharsets.UTF_8));
            log.info("Dados enviados com sucesso para o S3 - Bucket: {}, Key: {}", putObjectRequest.bucket(), putObjectRequest.key());

        } catch (JsonProcessingException e) {
            log.error("Erro ao converter dados para JSON: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao processar dados para S3", e);
        } catch (Exception e) {
            log.error("Erro inesperado ao salvar dados no S3: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao salvar dados no S3", e);
        }
    }
}
