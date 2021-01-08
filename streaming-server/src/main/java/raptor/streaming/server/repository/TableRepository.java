package raptor.streaming.server.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;
import raptor.streaming.dao.entity.Table;
import raptor.streaming.dao.mapper.TableMapper;


@Repository
public class TableRepository extends ServiceImpl<TableMapper, Table> implements IService<Table> {

}
