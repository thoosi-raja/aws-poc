package com.appointment.app.repository;

import com.appointment.app.entity.EcommerceItem;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import java.util.List;

public interface EcommerceRepository {
    EcommerceItem save(EcommerceItem item);
    EcommerceItem findById(String pk, String sk);
    List<EcommerceItem> queryByPartitionKey(String pk);
    List<EcommerceItem> queryByGsi1(String gsi1pk, String gsi1skPrefix);
    void delete(String pk, String sk);
}
