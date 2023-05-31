package Hospital.demo.Repository;

import Hospital.demo.Entity.Dean;
import org.springframework.data.jpa.repository.JpaRepository;
public interface DeanRepository extends JpaRepository<Dean,Integer> {

}
