package com.projects.flipkart.Controllers;

import com.projects.flipkart.CustomExceptions.ApiException;
import com.projects.flipkart.DTOs.ProductDTO;
import com.projects.flipkart.Entity.Product;
import com.projects.flipkart.Services.ProductService;
import io.springmcp.annotation.McpExpose;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

//    -----------------------Database Mapping-----------------------
@McpExpose(
        name = "browse_catalog",
        description = "Browse all items available in the catalog."
)
@GetMapping("/db")
public List<Product> getAllProductsfromDB() throws ApiException {
    return productService.getAllProductsFromDB();
}

    @McpExpose(
            name = "create_listing",
            description = "Create a new catalog listing."
    )
    @PostMapping("/db")
    public Product addProductToDB(@RequestBody Product product) throws ApiException {
        return productService.addProductToDB(product);
    }

    @McpExpose(
            name = "view_item_details",
            description = "Retrieve detailed information about a catalog item."
    )
    @GetMapping("/db/{id}")
    public Product getProductByIdFromDB(@PathVariable Integer id) throws ApiException {
        return productService.getProductByIdFromDB(id);
    }

    @McpExpose(
            name = "edit_listing",
            description = "Update an existing catalog listing."
    )
    @PutMapping("/db/{id}")
    public Product updateProductInDB(@PathVariable Integer id,
                                     @RequestBody Product product) throws ApiException {
        return productService.updateProductInDB(id, product);
    }

    @McpExpose(
            name = "delete_listing",
            description = "Remove a catalog listing."
    )
    @DeleteMapping("/db/{id}")
    public void deleteProductFromDB(@PathVariable Integer id) throws ApiException {
        productService.deleteProductFromDB(id);
    }
}
