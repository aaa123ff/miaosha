package com.imooc.miaosha.dao;


import com.imooc.miaosha.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

//  mybatis中的一个注解, 作用如下
// 使用@Mapper将UserDao接口交给Spring进行管理
// 不用写Mapper映射文件（XML）
// 为这个UserDao接口生成一个实现类，让别的类进行引用
@Mapper
public interface UserDao {

    // 选择语句
    @Select("select * from user where id = #{id}")
    // @Param 声明参数
    // 使用@Param注解来声明参数时，如果使用 #{} 或 ${} 的方式都可以
    public User getById(@Param("id")int id);

    // 插入语句
    @Insert("insert into user(id, name)values(#{id}, #{name})")
    public int insert(User user);
}
