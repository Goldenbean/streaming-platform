package raptor.streaming.server.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import raptor.streaming.dao.mapper.TableMapper;
import raptor.streaming.dao.entity.Table;


@Repository
public class TableRepository extends ServiceImpl<TableMapper, Table> implements IService<Table> {

}
