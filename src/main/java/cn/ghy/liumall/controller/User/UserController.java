package cn.ghy.liumall.controller.User;

import cn.ghy.liumall.service.ApiResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value="/user")
@PreAuthorize("hasRole('USER')")
public class UserController {
    @RequestMapping(value="", method= RequestMethod.GET)
    @ResponseBody
    public ApiResponse authenticateUser(){
        return new ApiResponse(true,"you have user role");
    }
}
