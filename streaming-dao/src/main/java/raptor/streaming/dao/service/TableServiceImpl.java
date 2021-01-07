package raptor.streaming.dao.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import raptor.streaming.dao.mapper.TableDao;
import raptor.streaming.dao.entity.TableEntity;

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
