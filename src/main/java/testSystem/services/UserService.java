package testSystem.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import testSystem.Enum.Role;
import testSystem.entity.Admin;
import testSystem.entity.Student;
import testSystem.entity.User;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserDetailsService {


    private final StudetsService studetsService;
    private final AdminService adminService;

    @Autowired
    public UserService(StudetsService studetsService, AdminService adminService) {
        this.studetsService = studetsService;
        this.adminService = adminService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //change this place!!!!
        List<Student> student = studetsService.findStudentByEmail(username);
        List<Admin> admin = adminService.findAdminByEmail(username);

        System.out.println(student.toString());
        System.out.println(admin.toString());
        ArrayList<Role> roles = new ArrayList<Role>();


        if (student.size() > 0) {
            roles.add(Role.ROLE_USER);
            return new User(student.get(0).getId(),
                    roles,
                    student.get(0).getPassword(),
                    username,
                    true,
                    true,
                    true,
                    true);
        } else if (admin.size() > 0) {
            roles.add(Role.ROLE_ADMIN);
            return new User(admin.get(0).getId(),
                    roles,
                    admin.get(0).getPassword(),
                    username,
                    true,
                    true,
                    true,
                    true);
        }
        return null;
    }
}
