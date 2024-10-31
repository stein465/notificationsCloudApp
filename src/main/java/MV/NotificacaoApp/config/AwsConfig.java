package MV.NotificacaoApp.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration
public class AwsConfig {

    @Value("${s3.region}")
    private  String AWS_REGION;

    @Bean
    public SqsClient sqsClient() {
        return SqsClient.builder()
                .region(Region.of(AWS_REGION))
                .build();
    }

    @Bean
    public S3Client amazonS3() {
        return S3Client.builder()
                .region(Region.of(AWS_REGION))
                .build();
    }
}