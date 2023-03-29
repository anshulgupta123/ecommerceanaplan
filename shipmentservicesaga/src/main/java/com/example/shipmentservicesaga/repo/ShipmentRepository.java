package com.example.shipmentservicesaga.repo;

import com.example.shipmentservicesaga.data.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipmentRepository extends JpaRepository<Shipment, String> {
}
