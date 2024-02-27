package com.example.CRUD_API_test.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.CRUD_API_test.models.productoModel;

@Repository
public interface productoRepository extends JpaRepository<productoModel, Long>{

    //enfasis en "precio, que es una propiedad del modelo productoModel"
    List<productoModel> findAllByOrderByPrecioAsc();

    List<productoModel> findAllByOrderByPrecioDesc();

    @Query("SELECT p FROM productoModel p WHERE p.nombre = :nombre")
    productoModel findByNombre(@Param("nombre") String nombre);
}