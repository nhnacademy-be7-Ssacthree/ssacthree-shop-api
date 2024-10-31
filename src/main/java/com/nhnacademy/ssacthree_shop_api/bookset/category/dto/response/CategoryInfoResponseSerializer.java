package com.nhnacademy.ssacthree_shop_api.bookset.category.dto.response;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class CategoryInfoResponseSerializer extends JsonSerializer<CategoryInfoResponse> {

    @Override
    public void serialize(CategoryInfoResponse value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("categoryName", value.getCategoryName());
        gen.writeBooleanField("categoryIsUsed", value.isCategoryIsUsed());

        // children 필드가 비어 있지 않은 경우에만 직렬화합니다.
        if (value.getChildren() != null && !value.getChildren().isEmpty()) {
            gen.writeArrayFieldStart("children");
            for (CategoryInfoResponse child : value.getChildren()) {
                gen.writeObject(child);
            }
            gen.writeEndArray();
        }

        gen.writeEndObject();
    }
}