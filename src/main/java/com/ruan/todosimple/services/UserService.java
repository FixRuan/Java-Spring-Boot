package com.ruan.todosimple.services;

import java.util.Optional;

import javax.management.RuntimeErrorException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruan.todosimple.models.User;

import com.ruan.todosimple.repositories.TaskRepository;
import com.ruan.todosimple.repositories.UserRepository;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private TaskRepository taskRepository;

  public User findById(Long id) {
    Optional<User> user = this.userRepository.findById(id);
    return user.orElseThrow(() -> new RuntimeException(
        "Usuário não encontrado! ID: " + id + ", Tipo> " + User.class.getName()));
  }

  @Transactional
  public User create(User user) {
    user.setId(null);
    user = this.userRepository.save(user);
    this.taskRepository.saveAll(user.getTasks());
    return user;
  }

  @Transactional
  public User Update(User user) {
    User newUser = findById(user.getId());
    newUser.setPassword(user.getPassword());
    return this.userRepository.save(newUser);
  }

  public void delete(Long id) {
    findById(id);

    try {
      this.userRepository.deleteById(id);
    } catch (Exception error) {
      // ! Tratar esse erro depois
      throw new RuntimeException("Não é possivel excluir pois há entidades relacionadas!");
    }
  }
}
