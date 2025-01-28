package spring_test.pp_3_1_4_rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring_test.pp_3_1_4_rest.entity.Role;


public interface RoleRepository extends JpaRepository<Role, Long> {

}
