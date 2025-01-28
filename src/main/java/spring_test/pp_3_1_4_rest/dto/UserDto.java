package spring_test.pp_3_1_4_rest.dto;

import lombok.Value;

import java.util.HashSet;
import java.util.Set;


@Value
public class UserDto {
    String email;
    String password;
    String firstName;
    String lastName;
    int age;
    Set<RoleDto> role = new HashSet<>();;
}