package com.example.demo.service;

import com.example.demo.model.Notification;
import com.example.demo.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository repository;

    public List<Notification> getAll() {
        return repository.findAll();
    }

    public List<Notification> getByType(String type) {
        return repository.findByType(type);
    }

    public void save(Notification notification) {
        repository.save(notification);
    }

    public Notification getById(Long id) {
        Optional<Notification> optional = repository.findById(id);
        return optional.orElse(null); // hoặc ném exception nếu muốn xử lý kỹ
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
