package com.projects.EcommerceBackend.Controllers;

import com.projects.EcommerceBackend.CustomExceptions.ApiException;
import com.projects.EcommerceBackend.DTOs.ProductDTO;
import com.projects.EcommerceBackend.Entity.Product;
import com.projects.EcommerceBackend.Services.ProductService;
import org.springframework.web.bind.annotation.*;
import io.springmcp.annotation.McpTool;

import java.util.List;


@RestController
@RequestMapping("api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

//    -----------------------Database Mapping-----------------------
    @McpTool(
        name = "get_all_products_db",
        description = "Fetch all products"
    )
    @GetMapping("/db")
    public List<Product> getAllProductsfromDB() throws ApiException {
        return productService.getAllProductsFromDB();
    }

    @McpTool(
            name = "add_products_db",
            description = "add products to the database"
    )
    @PostMapping("/db")
    public Product addProductToDB(@RequestBody Product product) throws ApiException {
        return productService.addProductToDB(product);
    }

    @GetMapping("/db/{id}")
    public Product getProductByIdFromDB(@PathVariable Integer id) throws ApiException
    {
        return productService.getProductByIdFromDB(id);
    }


    @PutMapping("/db/{id}")
    public Product updateProductInDB(@PathVariable Integer id, @RequestBody Product product) throws ApiException {
        return productService.updateProductInDB(id, product);
    }


    @DeleteMapping("/db/{id}")
    public void deleteProductFromDB(@PathVariable Integer id) throws ApiException {
        productService.deleteProductFromDB(id);
    }




    //    -----------------------FakeStore Mapping-----------------------

    @GetMapping("/fakestore")
    public List<ProductDTO> getAllProducts() throws ApiException {
        return productService.getAllProducts();
    }

    @PostMapping("/fakestore")
    public ProductDTO addProduct(@RequestBody ProductDTO productDTO) throws ApiException {
        return productService.addProduct(productDTO);
    }


    @GetMapping("/fakestore/{id}")
    public ProductDTO getProductById(@PathVariable Integer id) throws ApiException
    {
        return productService.getProductById(id);
    }


    @PutMapping("/fakestore/{id}")
    public ProductDTO updateProduct(@PathVariable Integer id, @RequestBody ProductDTO productDTO) throws ApiException {
        return productService.updateProduct(id, productDTO);
    }

    @DeleteMapping("/fakestore/{id}")
    public void deleteProduct(@PathVariable Integer id) throws ApiException {
        productService.deleteProduct(id);
    }
}
