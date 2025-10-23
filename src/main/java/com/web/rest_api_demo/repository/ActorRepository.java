package com.web.rest_api_demo.repository;

import com.web.rest_api_demo.entity.Actor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActorRepository extends JpaRepository<Actor, Integer> { }
