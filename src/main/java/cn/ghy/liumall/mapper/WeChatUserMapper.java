package cn.ghy.liumall.mapper;

import cn.ghy.liumall.model.WeChatUser;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface WeChatUserMapper {

    @Select("select * from weChatUser where openId = #{openId}")
    WeChatUser findUserByOpenId(@Param("openId")String openId);

    @Insert("INSERT INTO weChatUser(openId,sessionKey,unionId) VALUES(#{openId}, #{sessionKey}, #{unionId})")
    @Options(useGeneratedKeys=true,keyProperty="userId",keyColumn = "userId")
    //keyProperty from model; keyColumn from database
    void insert(WeChatUser user);
}
