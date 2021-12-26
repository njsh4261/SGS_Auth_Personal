package com.personalproject.integrated.logic.role;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Component
@NoArgsConstructor
public class UserRoleStore {
    private long userId;
    private UserRoleStore role;
}
