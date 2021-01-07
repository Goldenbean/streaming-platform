package raptor.streaming.server.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;
import raptor.streaming.dao.entity.ClusterEntity;
import raptor.streaming.common.http.RestResult;

/**
 * <p>
 * 集群列表 服务类
 * </p>
 *
 * @author azhe
 * @since 2020-12-02
 */
public interface ClusterService extends IService<ClusterEntity> {

  RestResult addCluster(String name, int type,
      String description,
      String spuConf,
      MultipartFile file) throws IOException;

  RestResult updateCluster(String name, int type, String description,
      String spuConf,
      MultipartFile file) throws IOException;


}
