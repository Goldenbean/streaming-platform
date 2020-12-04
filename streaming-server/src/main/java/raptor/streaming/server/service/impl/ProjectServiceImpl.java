package raptor.streaming.server.service.impl;

import raptor.streaming.server.entity.ProjectEntity;
import raptor.streaming.server.dao.ProjectDao;
import raptor.streaming.server.service.ProjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author azhe
 * @since 2020-12-03
 */
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectDao, ProjectEntity> implements ProjectService {

}
