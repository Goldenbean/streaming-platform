package raptor.streaming.server.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;
import raptor.streaming.dao.entity.JobFile;
import raptor.streaming.dao.mapper.JobFileMapper;


@Repository
public class JobFileRepository extends ServiceImpl<JobFileMapper, JobFile> implements
    IService<JobFile> {

}
