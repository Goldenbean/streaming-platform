package raptor.streaming.server.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;
import raptor.streaming.dao.entity.Cluster;
import raptor.streaming.dao.mapper.ClusterMapper;

@Repository
public class ClusterRepository
    extends ServiceImpl<ClusterMapper, Cluster>
    implements IService<Cluster> {

}
