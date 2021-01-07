package raptor.streaming.dao.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import raptor.streaming.dao.mapper.UserDao;
import raptor.streaming.dao.entity.UserEntity;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author azhe
 * @since 2020-12-04
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {

}
