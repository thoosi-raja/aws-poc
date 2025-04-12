package com.appointment.app.repository;

import com.appointment.app.entity.EcommerceItem;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.ArrayList;
import java.util.List;

@Repository
public class EcommerceRepositoryImpl implements EcommerceRepository {

    private final DynamoDbTable<EcommerceItem> table;
    private final DynamoDbIndex<EcommerceItem> gsi1;

    private static final String TABLE_NAME = "EcommerceTable";
    private static final String GSI1_NAME = "GSI1";

    public EcommerceRepositoryImpl(DynamoDbEnhancedClient enhancedClient) {
        this.table = enhancedClient.table(TABLE_NAME, TableSchema.fromBean(EcommerceItem.class));
        this.gsi1 = this.table.index(GSI1_NAME);
    }

    @Override
    public EcommerceItem save(EcommerceItem item) {
        table.putItem(item);
        return item;
    }

    @Override
    public EcommerceItem findById(String pk, String sk) {
        Key key = Key.builder()
                .partitionValue(pk)
                .sortValue(sk)
                .build();
        return table.getItem(key);
    }

    @Override
    public List<EcommerceItem> queryByPartitionKey(String pk) {
        QueryConditional queryConditional = QueryConditional
                .keyEqualTo(Key.builder().partitionValue(pk).build());
        
        return table.query(queryConditional)
                .stream()
                .flatMap(page -> page.items().stream())
                .toList();
    }

    @Override
    public List<EcommerceItem> queryByGsi1(String gsi1pk, String gsi1skPrefix) {
        QueryConditional queryConditional = QueryConditional
                .keyEqualTo(Key.builder().partitionValue(gsi1pk).build());

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(queryConditional);

        if (gsi1skPrefix != null) {
            Expression filterExpression = Expression.builder()
                    .expression("begins_with(GSI1SK, :prefix)")
                    .putExpressionValue(":prefix", AttributeValue.fromS(gsi1skPrefix))
                    .build();
            requestBuilder.filterExpression(filterExpression);
        }

        return gsi1.query(requestBuilder.build())
                .stream()
                .flatMap(page -> page.items().stream())
                .toList();
    }

    @Override
    public void delete(String pk, String sk) {
        Key key = Key.builder()
                .partitionValue(pk)
                .sortValue(sk)
                .build();
        table.deleteItem(key);
    }
}
