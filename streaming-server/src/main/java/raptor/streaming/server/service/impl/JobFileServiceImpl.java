package raptor.streaming.server.service.impl;

import raptor.streaming.server.entity.JobFileEntity;
import raptor.streaming.server.dao.JobFileDao;
import raptor.streaming.server.service.JobFileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 作业文件表 服务实现类
 * </p>
 *
 * @author azhe
 * @since 2020-12-25
 */
@Service
public class JobFileServiceImpl extends ServiceImpl<JobFileDao, JobFileEntity> implements JobFileService {

}
