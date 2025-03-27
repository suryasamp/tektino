package tektino.repository;

import java.util.Collection;
import java.util.List;

import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tektino.model.UserModel;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {

    UserModel findByUsername(String username);

    List<UserModel> findAllByOrderByIdDesc();

    boolean existsByUsername(String username);
}