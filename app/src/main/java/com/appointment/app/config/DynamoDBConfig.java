package com.appointment.app.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

@Configuration
public class DynamoDBConfig {

    private static final String TABLE_NAME = "EcommerceTable";

    @Value("${aws.region}")
    private String awsRegion;

    @Bean
    public DynamoDbClient dynamoDbClient() {
        return DynamoDbClient.builder()
                .credentialsProvider(DefaultCredentialsProvider.create())
                .region(Region.of(awsRegion))
                .build();
    }

    @Bean
    public DynamoDbEnhancedClient dynamoDbEnhancedClient(DynamoDbClient dynamoDbClient) {
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
    }

    @PostConstruct
    public void createTableIfNotExists() {
        DynamoDbClient client = dynamoDbClient();
        try {
            client.describeTable(DescribeTableRequest.builder()
                    .tableName(TABLE_NAME)
                    .build());
        } catch (ResourceNotFoundException e) {
            // Table doesn't exist, create it
            client.createTable(CreateTableRequest.builder()
                    .tableName(TABLE_NAME)
                    .attributeDefinitions(
                            AttributeDefinition.builder()
                                    .attributeName("PK")
                                    .attributeType(ScalarAttributeType.S)
                                    .build(),
                            AttributeDefinition.builder()
                                    .attributeName("SK")
                                    .attributeType(ScalarAttributeType.S)
                                    .build(),
                            AttributeDefinition.builder()
                                    .attributeName("GSI1PK")
                                    .attributeType(ScalarAttributeType.S)
                                    .build(),
                            AttributeDefinition.builder()
                                    .attributeName("GSI1SK")
                                    .attributeType(ScalarAttributeType.S)
                                    .build()
                    )
                    .keySchema(
                            KeySchemaElement.builder()
                                    .attributeName("PK")
                                    .keyType(KeyType.HASH)
                                    .build(),
                            KeySchemaElement.builder()
                                    .attributeName("SK")
                                    .keyType(KeyType.RANGE)
                                    .build()
                    )
                    .globalSecondaryIndexes(
                            GlobalSecondaryIndex.builder()
                                    .indexName("GSI1")
                                    .keySchema(
                                            KeySchemaElement.builder()
                                                    .attributeName("GSI1PK")
                                                    .keyType(KeyType.HASH)
                                                    .build(),
                                            KeySchemaElement.builder()
                                                    .attributeName("GSI1SK")
                                                    .keyType(KeyType.RANGE)
                                                    .build()
                                    )
                                    .projection(Projection.builder()
                                            .projectionType(ProjectionType.ALL)
                                            .build())
                                    .provisionedThroughput(ProvisionedThroughput.builder()
                                            .readCapacityUnits(5L)
                                            .writeCapacityUnits(5L)
                                            .build())
                                    .build()
                    )
                    .provisionedThroughput(ProvisionedThroughput.builder()
                            .readCapacityUnits(5L)
                            .writeCapacityUnits(5L)
                            .build())
                    .build());
        }
    }
}
