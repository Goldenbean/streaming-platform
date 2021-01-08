package raptor.streaming.server.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;
import raptor.streaming.dao.entity.Task;
import raptor.streaming.dao.mapper.TaskMapper;

@Repository
public class TaskRepository extends ServiceImpl<TaskMapper, Task> implements IService<Task> {

}
