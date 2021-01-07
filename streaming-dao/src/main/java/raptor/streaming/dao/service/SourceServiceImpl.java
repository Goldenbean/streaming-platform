package raptor.streaming.dao.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import raptor.streaming.dao.SourceDao;
import raptor.streaming.dao.entity.SourceEntity;

/**
 * <p>
 * 数据源列表 服务实现类
 * </p>
 *
 * @author azhe
 * @since 2020-12-08
 */
@Service
public class SourceServiceImpl extends ServiceImpl<SourceDao, SourceEntity> implements
    SourceService {

}
