package com.epam.rd.autocode.assessment.appliances.service;

import com.epam.rd.autocode.assessment.appliances.model.CustomUser;

public interface UserService {
    CustomUser findUserByEmail(String email);

    boolean existsByEmail(String email);
}
