//package cn.ghy.liumall.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//public class CustomUserDetailsService implements UserDetailsService {
////    @Autowired
////    UserRepository userRepository;
//
//    @Override
//    @Transactional
//    public UserDetails loadUserByUsername(String email)
//            throws UsernameNotFoundException {
//        // Let people login with email
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() ->
//                        new UsernameNotFoundException("User not found with email : " + email)
//                );
//
//        return JwtUser.create(user);
//    }
//
//    // This method is used by JwtAuthenticationFilter
//    @Transactional
//    public UserDetails loadUserById(Long id) {
//        User user = userRepository.findById(id).orElseThrow(
//                () -> new UsernameNotFoundException("User not found with id : " + id)
//        );
//
//        return JwtUser.create(user);
//    }
//
//}
