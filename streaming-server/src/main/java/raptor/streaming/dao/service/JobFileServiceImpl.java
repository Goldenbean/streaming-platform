package raptor.streaming.dao.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import raptor.streaming.dao.JobFileDao;
import raptor.streaming.dao.entity.JobFileEntity;

/**
 * <p>
 * 作业文件表 服务实现类
 * </p>
 *
 * @author azhe
 * @since 2020-12-25
 */
@Service
public class JobFileServiceImpl extends ServiceImpl<JobFileDao, JobFileEntity> implements
    JobFileService {

}
