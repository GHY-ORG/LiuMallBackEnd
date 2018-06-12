package cn.ghy.liumall.mapper;

import cn.ghy.liumall.model.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface RoleMapper {

    @Select("select * from role where userId = #{userId}")
    List<Role> findRolesByUserId(@Param("userId")Long userId);

    @Select("INSERT INTO role(userId, role) VALUES(#{userId}, #{role})")
    void insert(@Param("userId")Long userId, @Param("role")String role);
}
