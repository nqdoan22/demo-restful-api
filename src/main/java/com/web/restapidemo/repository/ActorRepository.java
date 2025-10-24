package com.web.restapidemo.repository;

import com.web.restapidemo.entity.Actor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActorRepository extends JpaRepository<Actor, Integer> { }
