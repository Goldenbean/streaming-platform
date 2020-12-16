package raptor.streaming.server.service.impl;

import raptor.streaming.server.entity.TableEntity;
import raptor.streaming.server.dao.TableDao;
import raptor.streaming.server.service.TableService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 数仓表 服务实现类
 * </p>
 *
 * @author azhe
 * @since 2020-12-08
 */
@Service
public class TableServiceImpl extends ServiceImpl<TableDao, TableEntity> implements TableService {

}
