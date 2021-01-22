package dscatalogdesafiocrudclientes.dsClients.service;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dscatalogdesafiocrudclientes.dsClients.dto.ClientDTO;
import dscatalogdesafiocrudclientes.dsClients.entities.Client;
import dscatalogdesafiocrudclientes.dsClients.repository.ClientRepository;
import dscatalogdesafiocrudclientes.dsClients.service.exceptions.DatabaseException;
import dscatalogdesafiocrudclientes.dsClients.service.exceptions.ResourceNotFoundException;

@Service
public class ClientService {
	
	@Autowired
	ClientRepository repository;
	
	@Transactional(readOnly = true)
	public Page<ClientDTO> findAllPaged(PageRequest pageRequest) {
		Page<Client> list = repository.findAll(pageRequest);
		Page<ClientDTO> listDTO = list.map(x -> new ClientDTO(x));
		return listDTO;
	}
	
	@Transactional(readOnly = true)
	public ClientDTO findById(Long id) {
		Optional<Client> obj = repository.findById(id);
		// lança exceção quando objeto não encontrado
		Client entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));

		return new ClientDTO(entity);
	}
	
	@Transactional
	public ClientDTO insert(ClientDTO dto) {
		Client entity = new Client();
		entity.setName(dto.getName());
		entity.setCpf(dto.getCpf());
		entity.setBirthDate(dto.getBirthDate());
		entity.setIncome(dto.getIncome());
		entity.setChildren(dto.getChildren());
		
		entity = repository.save(entity);
		return new ClientDTO(entity);
	}
	
	@Transactional
	public ClientDTO update(Long id, ClientDTO dto) {
		try {
			Client entity = repository.getOne(id);
			entity.setName(dto.getName());
			entity.setCpf(dto.getCpf());
			entity.setBirthDate(dto.getBirthDate());
			entity.setIncome(dto.getIncome());
			entity.setChildren(dto.getChildren());
			
			entity = repository.save(entity);
			return new ClientDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		} 
	}

	public void delete(Long id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity violation");
		}
		
	}

}
