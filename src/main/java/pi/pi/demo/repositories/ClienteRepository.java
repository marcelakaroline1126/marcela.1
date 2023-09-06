package pi.pi.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pi.pi.demo.models.Cliente;
import pi.pi.demo.models.Sorvete;

public interface ClienteRepository extends JpaRepository<Cliente, Long>{

	List<Cliente> findBySorvete (Sorvete sorvete);
	
	
}
