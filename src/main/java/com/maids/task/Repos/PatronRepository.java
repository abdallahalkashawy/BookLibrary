package com.maids.task.Repos;

import com.maids.task.Models.Patron;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatronRepository extends JpaRepository<Patron, Integer>{
}
