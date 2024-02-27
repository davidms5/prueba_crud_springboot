package com.example.CRUD_API_test.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.CRUD_API_test.models.productoModel;
import com.example.CRUD_API_test.repository.productoRepository;

@RestController
@RequestMapping("/api/productos")
public class productoController {
    
    
    private final productoRepository productRepo;

   
    public productoController(productoRepository productRepo){
        this.productRepo = productRepo;
    };

    @GetMapping("/productsSortUp")
    public ResponseEntity<List<productoModel>> getAllProductSortedByPriceUp(){

        try {
            List <productoModel> productsList = new ArrayList<>();
            productRepo.findAllByOrderByPrecioAsc().forEach(productsList::add);
            
            if(productsList.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            };

            return new ResponseEntity<>(productsList, HttpStatus.OK);

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    };

    @GetMapping("/productsSortDown")
    public ResponseEntity<List<productoModel>> getAllProductSortedByPriceDown(){

        try {
            List <productoModel> productsList = new ArrayList<>();
            productRepo.findAllByOrderByPrecioDesc().forEach(productsList::add);
            
            if(productsList.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            };

            return new ResponseEntity<>(productsList, HttpStatus.OK);

        } catch (Exception e) {
            // TODO: handle exception
            //log.error("error procesando la solicitud", e); incluir en el maven spring-boot-starter-logging
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    };

    @GetMapping("/productById/{productId}")
    public ResponseEntity<productoModel> getProductById(@PathVariable long productId){

         productoModel producto = productRepo.findById(productId).orElse(null);

         return producto != null ? ResponseEntity.ok(producto) : ResponseEntity.notFound().build();

    };


    @GetMapping("/productByName/{productName}")
    public ResponseEntity<productoModel> getproductByName(@PathVariable String productName) {
        productoModel producto = productRepo.findByNombre(productName);

        return producto != null ? ResponseEntity.ok(producto) : ResponseEntity.notFound().build();
    };

    @PostMapping("/postProduct")
    public ResponseEntity<productoModel> postProduct(@RequestBody productoModel producto){

        try {
            if(producto == null){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);//.badRequest().body("producto data cannot be null");
            }

            if(producto.getPrecio()< 0 || producto.getCantidad() < 0){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);//.badRequest().body("precio y/o cantidad no pueden ser valores negativos");
            }

            productoModel productoObj = productRepo.save(producto);

            return new ResponseEntity<>(productoObj, HttpStatus.CREATED);

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    };

    @DeleteMapping("/deleteProduct/{productId}")
    public ResponseEntity<HttpStatus> deleteProduct(@PathVariable long productId){

        try {
            if(!productRepo.existsById(productId)){
                return ResponseEntity.noContent().build();
            };

            productRepo.deleteById(productId);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    };

    @PostMapping("/updateProducto/{productId}")
    public ResponseEntity<productoModel> updateProductById(@PathVariable long productId, @RequestBody productoModel producto){

        try {
            if(!productRepo.existsById(productId)){
                return ResponseEntity.notFound().build();
            };

            producto.setId(productId);
            productoModel productObj = productRepo.save(producto);

            return new ResponseEntity<>(productObj, HttpStatus.OK);
            
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
