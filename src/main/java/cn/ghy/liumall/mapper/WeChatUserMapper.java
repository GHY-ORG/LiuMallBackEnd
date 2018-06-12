package cn.ghy.liumall.mapper;

import cn.ghy.liumall.model.WeChatUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface WeChatUserMapper {

    @Select("select * from weChatUser where openId = #{openId}")
    WeChatUser findUserByOpenId(@Param("openId")String openId);

    @Insert("INSERT INTO weChatUser(userId,openId,sessionKey,unionId) VALUES(#{userId}, #{openId}, #{sessionKey}, #{unionId})")
    void insert(WeChatUser user);
}
