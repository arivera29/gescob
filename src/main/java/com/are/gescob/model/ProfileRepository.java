package com.are.gescob.model;

import org.springframework.data.repository.CrudRepository;

import com.are.gescob.entity.Profile;

public interface ProfileRepository extends CrudRepository<Profile, Long> {

}
