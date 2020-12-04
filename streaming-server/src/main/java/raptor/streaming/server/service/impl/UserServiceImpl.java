package raptor.streaming.server.service.impl;

import raptor.streaming.server.entity.UserEntity;
import raptor.streaming.server.dao.UserDao;
import raptor.streaming.server.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author azhe
 * @since 2020-12-04
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {

}
