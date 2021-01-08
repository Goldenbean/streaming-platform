package raptor.streaming.server.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;
import raptor.streaming.dao.entity.Source;
import raptor.streaming.dao.mapper.SourceMapper;

@Repository
public class SourceRepository extends ServiceImpl<SourceMapper, Source> implements
    IService<Source> {

}
