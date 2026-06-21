package com.projects.flipkart.Services;

import com.projects.flipkart.CustomExceptions.ApiException;
import com.projects.flipkart.DTOs.ProductDTO;
import com.projects.flipkart.Entity.Product;
import com.projects.flipkart.Gateways.IProductGateway;
import com.projects.flipkart.Repositories.IProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
        private final IProductGateway ProductGateway;
        private final IProductRepository ProductRepository;


        public ProductService(IProductGateway ProductGateway , IProductRepository ProductRepository) {
            this.ProductGateway = ProductGateway;
            this.ProductRepository = ProductRepository;
        }

        public List<Product> getAllProductsFromDB() throws ApiException {
            return ProductRepository.findAll();
        }
        public List<ProductDTO> getAllProducts() throws ApiException {
            return ProductGateway.getAllProducts();
        }

        public Product addProductToDB(Product product) throws ApiException  {
            return ProductRepository.save(product);
        }
        public ProductDTO addProduct(ProductDTO productDTO) throws ApiException  {
            return ProductGateway.addProduct(productDTO);
        }

        public Product getProductByIdFromDB(Integer id) throws ApiException  {
            return ProductRepository.findById(id).get();
        }
        public ProductDTO getProductById(Integer id) throws ApiException
        {
            return ProductGateway.getProductById(id);
        }

        public Product updateProductInDB(Integer id, Product product) throws ApiException  {
            Product existing = ProductRepository.findById(id)
                    .orElseThrow(() -> new ApiException("Product not found with id: " + id));

            // update fields
            existing.setTitle(product.getTitle());
            existing.setPrice(product.getPrice());
            existing.setDescription(product.getDescription());
            existing.setCategory(product.getCategory());
            existing.setImage(product.getImage());
            existing.setRating(product.getRating());

            return ProductRepository.save(existing);
        }
        public ProductDTO updateProduct(Integer id, ProductDTO productDTO) throws ApiException  {
            return ProductGateway.updateProduct(id, productDTO);
        }


        public void deleteProductFromDB(Integer id) throws ApiException  {
            ProductRepository.deleteById(id);
        }
        public void deleteProduct(Integer id) throws ApiException  {
            ProductGateway.deleteProduct(id);
        }
}
