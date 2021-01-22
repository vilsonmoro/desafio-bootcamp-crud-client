package dscatalogdesafiocrudclientes.dsClients.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dscatalogdesafiocrudclientes.dsClients.entities.Client;

public interface ClientRepository extends JpaRepository<Client, Long>{

}
