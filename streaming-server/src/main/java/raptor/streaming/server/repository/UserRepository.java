package raptor.streaming.server.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;
import raptor.streaming.dao.entity.User;
import raptor.streaming.dao.mapper.UserMapper;


@Repository
public class UserRepository extends ServiceImpl<UserMapper, User> implements IService<User> {

}
