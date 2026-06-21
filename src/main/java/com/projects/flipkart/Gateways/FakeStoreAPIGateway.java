package com.projects.flipkart.Gateways;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projects.flipkart.CustomExceptions.ApiException;
import com.projects.flipkart.DTOs.ProductDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import okhttp3.MediaType;
import org.springframework.beans.factory.annotation.Value;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Component;
import okhttp3.RequestBody;
import java.io.IOException;
import java.util.List;

@Component
public class FakeStoreAPIGateway implements IProductGateway {

    private final String baseUrl;
    private final OkHttpClient client;
    private final ObjectMapper objectMapper;

    // Constructor injection using your AppConfig beans
    public FakeStoreAPIGateway(OkHttpClient client, ObjectMapper objectMapper, @Value("${fakeStore.baseUrl}") String baseUrl) {
        this.client = client;
        this.objectMapper = objectMapper;
        this.baseUrl = baseUrl;
    }

    @Override
    public List<ProductDTO> getAllProducts() throws ApiException {
        Request request = new Request.Builder()
                .url(baseUrl)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new ApiException("FakeStore API error: " + response.code());
            }

            String responseBody = response.body() != null ? response.body().string() : "";
            if (responseBody.isEmpty()) {
                throw new ApiException("Empty response from FakeStore API");
            }

            // Directly deserialize to a List using TypeReference
            List<ProductDTO> productList = objectMapper.readValue(responseBody,
                    new TypeReference<List<ProductDTO>>() {});

            if (productList.isEmpty()) {
                throw new ApiException("FakeStore API returned empty product list");
            }

            return productList;

        } catch (JsonProcessingException e) {
            throw new ApiException("Failed to parse response from FakeStore API", e);
        } catch (IOException e) {
            throw new ApiException("Network error when calling FakeStore API", e);
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public ProductDTO addProduct(ProductDTO productDTO) throws ApiException {
        try {
            // 1. Serialize the product to JSON
            String json = objectMapper.writeValueAsString(productDTO);
            RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));

            // 2. Build the POST request
            Request request = new Request.Builder()
                    .url(baseUrl)
                    .post(body)
                    .build();

            // 3. Execute and handle the response
            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new ApiException("Failed to add product, API error: " + response.code());
                }

                String responseBody = response.body() != null ? response.body().string() : "";
                if (responseBody.isEmpty()) {
                    throw new ApiException("Empty response from FakeStore API after adding product");
                }

                // 4. Parse and return the new product (often includes the new ID)
                return objectMapper.readValue(responseBody, ProductDTO.class);
            }
        } catch (JsonProcessingException e) {
            // Can be thrown by writeValueAsString or readValue
            throw new ApiException("Failed to process product JSON", e);
        } catch (IOException e) {
            throw new ApiException("Network error when adding product to FakeStore API", e);
        }
    }

    @Override
    public ProductDTO getProductById(Integer id) throws ApiException {
        String url = baseUrl + "/" + id;
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                // Provide a specific error for 404
                if (response.code() == 404) {
                    throw new ApiException("Product not found with ID: " + id);
                }
                throw new ApiException("FakeStore API error: " + response.code());
            }

            String responseBody = response.body() != null ? response.body().string() : "";
            if (responseBody.isEmpty()) {
                throw new ApiException("Empty response from FakeStore API for product ID: " + id);
            }

            // The FakeStore API returns the string "null" for an ID that doesn't exist
            if (responseBody.equalsIgnoreCase("null")) {
                throw new ApiException("Product not found with ID: " + id);
            }

            return objectMapper.readValue(responseBody, ProductDTO.class);

        } catch (JsonProcessingException e) {
            throw new ApiException("Failed to parse response for product ID: " + id, e);
        } catch (IOException e) {
            throw new ApiException("Network error when fetching product with ID " + id, e);
        }
    }


    @Override
    public ProductDTO updateProduct(Integer id, ProductDTO productDTO) throws ApiException {
        try {
            String json = objectMapper.writeValueAsString(productDTO);
            RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
            String url = baseUrl + "/" + id;

            Request request = new Request.Builder()
                    .url(url)
                    .put(body) // Use PUT for updates
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    if (response.code() == 404) {
                        throw new ApiException("Product not found to update with ID: " + id);
                    }
                    throw new ApiException("Failed to update product, API error: " + response.code());
                }

                String responseBody = response.body() != null ? response.body().string() : "";
                if (responseBody.isEmpty()) {
                    throw new ApiException("Empty response from FakeStore API after updating product");
                }

                return objectMapper.readValue(responseBody, ProductDTO.class);
            }
        } catch (JsonProcessingException e) {
            throw new ApiException("Failed to process product JSON for update", e);
        } catch (IOException e) {
            throw new ApiException("Network error when updating product with ID " + id, e);
        }
    }

    @Override
    public void deleteProduct(Integer id) throws ApiException {
        String url = baseUrl + "/" + id;
        Request request = new Request.Builder()
                .url(url)
                .delete()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                if (response.code() == 404) {
                    throw new ApiException("Product not found to delete with ID: " + id);
                }
                throw new ApiException("Failed to delete product, API error: " + response.code());
            }

            // A successful DELETE often returns no body, or just a confirmation.
            // We don't need to parse anything, so we just return void.

        } catch (IOException e) {
            // No JsonProcessingException needed here since we don't parse the response
            throw new ApiException("Network error when deleting product with ID " + id, e);
        }
    }
}