package com.example.coursemanagement.repository;

import com.example.coursemanagement.entity.FileServer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileServerRepository extends JpaRepository<FileServer, Integer> {
}