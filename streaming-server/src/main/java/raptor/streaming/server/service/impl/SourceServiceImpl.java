package raptor.streaming.server.service.impl;

import raptor.streaming.server.entity.SourceEntity;
import raptor.streaming.server.dao.SourceDao;
import raptor.streaming.server.service.SourceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 数据源列表 服务实现类
 * </p>
 *
 * @author azhe
 * @since 2020-12-08
 */
@Service
public class SourceServiceImpl extends ServiceImpl<SourceDao, SourceEntity> implements SourceService {

}
